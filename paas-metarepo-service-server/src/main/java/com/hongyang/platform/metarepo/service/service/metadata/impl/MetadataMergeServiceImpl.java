package com.hongyang.platform.metarepo.service.service.metadata.impl;

import com.hongyang.framework.dao.entity.BaseMetaCommonEntity;
import com.hongyang.framework.dao.entity.BaseMetaTenantEntity;
import com.hongyang.framework.dao.query.PageResult;
import com.hongyang.platform.metarepo.service.common.constants.MetamodelApiKeyEnum;
import com.hongyang.platform.metarepo.service.common.converter.CommonMetadataConverter;
import com.hongyang.platform.metarepo.service.entity.metadata.GenericMetadata;
import com.hongyang.platform.metarepo.service.entity.metamodel.CommonMetadata;
import com.hongyang.platform.metarepo.service.entity.metamodel.MetaItem;
import com.hongyang.platform.metarepo.service.entity.metamodel.TenantMetadata;
import com.hongyang.platform.metarepo.service.service.metadata.IMetadataMergeService;
import com.hongyang.platform.metarepo.service.service.metamodel.ICommonMetadataService;
import com.hongyang.platform.metarepo.service.service.metamodel.IMetaItemService;
import com.hongyang.platform.metarepo.service.service.metamodel.ITenantMetadataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 元数据合并查询 Service 实现。
 * <p>
 * 统一处理所有元模型类型的 Common + Tenant 合并逻辑：
 * 1. 从 ICommonMetadataService 查 Common 数据（走缓存）
 * 2. 从 ITenantMetadataService 查 Tenant 数据（TenantInterceptor 自动注入 tenant_id）
 * 3. 通过 IMetaItemService 获取列映射（走缓存）
 * 4. 通过 CommonMetadataConverter 将大宽表行转换为业务 Entity
 * 5. 按 apiKey 合并：Common 有 Tenant 无 → Common；同 apiKey → Tenant 覆盖；delete_flg=1 → 隐藏
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MetadataMergeServiceImpl implements IMetadataMergeService {

    private final ICommonMetadataService commonMetadataService;
    private final ITenantMetadataService tenantMetadataService;
    private final IMetaItemService metaItemService;

    /** 列映射缓存：metamodelApiKey → {db_column → entityFieldName} */
    private final ConcurrentHashMap<String, Map<String, String>> columnMappingCache = new ConcurrentHashMap<>();

    @Override
    public <T extends BaseMetaTenantEntity> List<T> listMerged(String metamodelApiKey, Class<T> entityClass) {
        List<T> commonList = listCommon(metamodelApiKey, entityClass);
        List<T> tenantList = listTenant(metamodelApiKey, entityClass);
        return merge(commonList, tenantList);
    }

    @Override
    public <T extends BaseMetaTenantEntity> T getByApiKeyMerged(String metamodelApiKey, String apiKey, Class<T> entityClass) {
        // 先查 Tenant
        List<T> tenantList = listTenant(metamodelApiKey, entityClass);
        T tenant = tenantList.stream()
                .filter(e -> apiKey.equals(e.getApiKey()))
                .findFirst().orElse(null);
        if (tenant != null) {
            if (tenant.getDeleteFlg() != null && tenant.getDeleteFlg() == 1) return null;
            return tenant;
        }
        // 再查 Common
        CommonMetadata row = commonMetadataService.getByMetamodelApiKeyAndApiKey(metamodelApiKey, apiKey);
        return CommonMetadataConverter.convertOne(row, entityClass, getColumnMapping(metamodelApiKey));
    }

    @Override
    public <T extends BaseMetaTenantEntity> PageResult<T> pageMerged(String metamodelApiKey, Class<T> entityClass, int page, int size) {
        List<T> all = listMerged(metamodelApiKey, entityClass);
        int total = all.size();
        int from = Math.min((page - 1) * size, total);
        int to = Math.min(from + size, total);
        PageResult<T> result = new PageResult<>();
        result.setPage(page);
        result.setSize(size);
        result.setTotal(total);
        result.setRecords(all.subList(from, to));
        return result;
    }

    // ==================== 内部方法 ====================

    @Override
    public List<GenericMetadata> listMergedGeneric(String metamodelApiKey) {
        return listMerged(metamodelApiKey, GenericMetadata.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<?> listMergedAuto(String metamodelApiKey) {
        Class<? extends BaseMetaTenantEntity> entityClass = MetamodelApiKeyEnum.getEntityClassByKey(metamodelApiKey);
        if (entityClass != null) {
            return listMerged(metamodelApiKey, entityClass);
        }
        return listMergedGeneric(metamodelApiKey);
    }

    private <T extends BaseMetaTenantEntity> List<T> listCommon(String metamodelApiKey, Class<T> entityClass) {
        List<CommonMetadata> rows = commonMetadataService.listByMetamodelApiKey(metamodelApiKey);
        return CommonMetadataConverter.convert(rows, entityClass, getColumnMapping(metamodelApiKey));
    }

    private <T extends BaseMetaTenantEntity> List<T> listTenant(String metamodelApiKey, Class<T> entityClass) {
        List<TenantMetadata> rows = tenantMetadataService.listByMetamodelApiKey(metamodelApiKey);
        return CommonMetadataConverter.convertFromTenant(rows, entityClass, getColumnMapping(metamodelApiKey));
    }

    private Map<String, String> getColumnMapping(String metamodelApiKey) {
        return columnMappingCache.computeIfAbsent(metamodelApiKey, key -> {
            List<MetaItem> items = metaItemService.listByMetamodelApiKey(key);
            Map<String, String> map = new HashMap<>();
            for (MetaItem item : items) {
                if (item.getDbColumn() != null && item.getApiKey() != null) {
                    map.put(item.getDbColumn(), snakeToCamel(item.getApiKey()));
                }
            }
            return Collections.unmodifiableMap(map);
        });
    }

    /**
     * 合并算法：Common 有 Tenant 无 → Common；同 apiKey → Tenant 覆盖；delete_flg=1 → 隐藏
     */
    private <T extends BaseMetaCommonEntity> List<T> merge(List<T> commonList, List<T> tenantList) {
        Map<String, T> tenantMap = tenantList.stream()
                .filter(e -> e.getApiKey() != null)
                .collect(Collectors.toMap(BaseMetaCommonEntity::getApiKey, e -> e, (a, b) -> b));

        List<T> result = new ArrayList<>();
        for (T common : commonList) {
            String apiKey = common.getApiKey();
            T tenant = tenantMap.remove(apiKey);
            if (tenant != null) {
                if (tenant.getDeleteFlg() != null && tenant.getDeleteFlg() == 1) continue;
                result.add(tenant);
            } else {
                result.add(common);
            }
        }
        for (T tenant : tenantMap.values()) {
            if (tenant.getDeleteFlg() == null || tenant.getDeleteFlg() != 1) {
                result.add(tenant);
            }
        }
        return result;
    }

    private static String snakeToCamel(String snake) {
        if (snake == null || !snake.contains("_")) return snake;
        StringBuilder sb = new StringBuilder();
        boolean upper = false;
        for (char c : snake.toCharArray()) {
            if (c == '_') {
                upper = true;
            } else {
                sb.append(upper ? Character.toUpperCase(c) : c);
                upper = false;
            }
        }
        return sb.toString();
    }
}
