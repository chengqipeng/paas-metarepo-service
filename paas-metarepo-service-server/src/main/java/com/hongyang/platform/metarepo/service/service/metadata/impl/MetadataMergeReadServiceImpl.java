package com.hongyang.platform.metarepo.service.service.metadata.impl;

import com.hongyang.framework.base.exception.BaseKnownException;
import com.hongyang.framework.common.enums.paas.MetaRepoErrorCodeEnum;
import com.hongyang.framework.dao.entity.BaseMetaCommonEntity;
import com.hongyang.framework.dao.entity.BaseMetaTenantEntity;
import com.hongyang.framework.dao.query.PageResult;
import com.hongyang.platform.metarepo.service.common.constants.MetamodelApiKeyEnum;
import com.hongyang.platform.metarepo.service.common.converter.CommonMetadataConverter;
import com.hongyang.platform.metarepo.service.entity.metadata.GenericMetadata;
import com.hongyang.platform.metarepo.service.entity.metamodel.CommonMetadata;
import com.hongyang.platform.metarepo.service.entity.metamodel.MetaItem;
import com.hongyang.platform.metarepo.service.entity.metamodel.MetaModel;
import com.hongyang.platform.metarepo.service.entity.metamodel.TenantMetadata;
import com.hongyang.platform.metarepo.service.service.metadata.IMetadataMergeReadService;
import com.hongyang.platform.metarepo.service.service.metamodel.ICommonMetadataService;
import com.hongyang.platform.metarepo.service.service.metamodel.IMetaItemService;
import com.hongyang.platform.metarepo.service.service.metamodel.IMetaModelService;
import com.hongyang.platform.metarepo.service.service.metamodel.ITenantMetadataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class MetadataMergeReadServiceImpl implements IMetadataMergeReadService {

    private final ICommonMetadataService commonMetadataService;
    private final ITenantMetadataService tenantMetadataService;
    private final IMetaItemService metaItemService;
    private final IMetaModelService metaModelService;

    @Override
    @SuppressWarnings("unchecked")
    public <T extends BaseMetaTenantEntity> List<T> listMerged(String metamodelApiKey) {
        Class<T> entityClass = (Class<T>) resolveEntityClass(metamodelApiKey);
        MetaModel metaModel = getMetaModel(metamodelApiKey);
        boolean queryCommon = isEnabled(metaModel, true);
        boolean queryTenant = isEnabled(metaModel, false);

        List<T> commonList = queryCommon ? listCommon(metamodelApiKey, entityClass) : Collections.emptyList();
        List<T> tenantList = queryTenant ? listTenant(metamodelApiKey, entityClass) : Collections.emptyList();

        if (queryCommon && queryTenant) {
            return merge(commonList, tenantList);
        }
        return queryCommon ? commonList : tenantList;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends BaseMetaTenantEntity> T getByApiKeyMerged(String metamodelApiKey, String apiKey) {
        Class<T> entityClass = (Class<T>) resolveEntityClass(metamodelApiKey);
        MetaModel metaModel = getMetaModel(metamodelApiKey);
        boolean queryCommon = isEnabled(metaModel, true);
        boolean queryTenant = isEnabled(metaModel, false);

        // 先查 Tenant
        if (queryTenant) {
            List<T> tenantList = listTenant(metamodelApiKey, entityClass);
            T tenant = tenantList.stream()
                    .filter(e -> apiKey.equals(e.getApiKey()))
                    .findFirst().orElse(null);
            if (tenant != null) {
                if (tenant.getDeleteFlg() != null && tenant.getDeleteFlg() == 1) return null;
                return tenant;
            }
        }
        // 再查 Common
        if (queryCommon) {
            CommonMetadata row = commonMetadataService.getByMetamodelApiKeyAndApiKey(metamodelApiKey, apiKey);
            return CommonMetadataConverter.convertOne(row, entityClass, getColumnMapping(metamodelApiKey));
        }
        return null;
    }

    @Override
    public <T extends BaseMetaTenantEntity> PageResult<T> pageMerged(String metamodelApiKey, int page, int size) {
        List<T> all = listMerged(metamodelApiKey);
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

    @Override
    public List<GenericMetadata> listMergedGeneric(String metamodelApiKey) {
        // GenericMetadata 不走枚举映射，直接指定 class
        return listMergedWithClass(metamodelApiKey, GenericMetadata.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<?> listMergedAuto(String metamodelApiKey) {
        Class<? extends BaseMetaTenantEntity> entityClass = MetamodelApiKeyEnum.getEntityClassByKey(metamodelApiKey);
        if (entityClass != null) {
            return listMergedWithClass(metamodelApiKey, entityClass);
        }
        return listMergedGeneric(metamodelApiKey);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends BaseMetaTenantEntity> List<T> listMergedByEntityApiKey(
            String metamodelApiKey, String entityApiKey) {
        Class<T> entityClass = (Class<T>) resolveEntityClass(metamodelApiKey);
        MetaModel metaModel = getMetaModel(metamodelApiKey);
        boolean queryCommon = isEnabled(metaModel, true);
        boolean queryTenant = isEnabled(metaModel, false);

        List<T> commonList = queryCommon ? listCommonByEntityApiKey(metamodelApiKey, entityApiKey, entityClass) : Collections.emptyList();
        List<T> tenantList = queryTenant ? listTenantByEntityApiKey(metamodelApiKey, entityApiKey, entityClass) : Collections.emptyList();

        if (queryCommon && queryTenant) {
            return merge(commonList, tenantList);
        }
        return queryCommon ? commonList : tenantList;
    }

    // ==================== 内部方法 ====================

    /**
     * 通过 MetamodelApiKeyEnum 解析 entityClass，未注册则回退到 GenericMetadata。
     */
    private Class<? extends BaseMetaTenantEntity> resolveEntityClass(String metamodelApiKey) {
        Class<? extends BaseMetaTenantEntity> clazz = MetamodelApiKeyEnum.getEntityClassByKey(metamodelApiKey);
        if (clazz == null) {
            return GenericMetadata.class;
        }
        return clazz;
    }

    /** 内部带 entityClass 参数的 listMerged，供 listMergedGeneric/listMergedAuto 使用 */
    private <T extends BaseMetaTenantEntity> List<T> listMergedWithClass(String metamodelApiKey, Class<T> entityClass) {
        MetaModel metaModel = getMetaModel(metamodelApiKey);
        boolean queryCommon = isEnabled(metaModel, true);
        boolean queryTenant = isEnabled(metaModel, false);

        List<T> commonList = queryCommon ? listCommon(metamodelApiKey, entityClass) : Collections.emptyList();
        List<T> tenantList = queryTenant ? listTenant(metamodelApiKey, entityClass) : Collections.emptyList();

        if (queryCommon && queryTenant) {
            return merge(commonList, tenantList);
        }
        return queryCommon ? commonList : tenantList;
    }

    private <T extends BaseMetaTenantEntity> List<T> listCommon(String metamodelApiKey, Class<T> entityClass) {
        List<CommonMetadata> rows = commonMetadataService.listByMetamodelApiKey(metamodelApiKey);
        return CommonMetadataConverter.convert(rows, entityClass, getColumnMapping(metamodelApiKey));
    }

    private <T extends BaseMetaTenantEntity> List<T> listTenant(String metamodelApiKey, Class<T> entityClass) {
        List<TenantMetadata> rows = tenantMetadataService.listByMetamodelApiKey(metamodelApiKey);
        return CommonMetadataConverter.convertFromTenant(rows, entityClass, getColumnMapping(metamodelApiKey));
    }

    private <T extends BaseMetaTenantEntity> List<T> listCommonByEntityApiKey(String metamodelApiKey, String entityApiKey, Class<T> entityClass) {
        List<CommonMetadata> rows = commonMetadataService.listByMetamodelApiKeyAndEntityApiKey(metamodelApiKey, entityApiKey);
        return CommonMetadataConverter.convert(rows, entityClass, getColumnMapping(metamodelApiKey));
    }

    private <T extends BaseMetaTenantEntity> List<T> listTenantByEntityApiKey(String metamodelApiKey, String entityApiKey, Class<T> entityClass) {
        List<TenantMetadata> rows = tenantMetadataService.listByMetamodelApiKeyAndEntityApiKey(metamodelApiKey, entityApiKey);
        return CommonMetadataConverter.convertFromTenant(rows, entityClass, getColumnMapping(metamodelApiKey));
    }

    private Map<String, String> getColumnMapping(String metamodelApiKey) {
        List<MetaItem> items = metaItemService.listByMetamodelApiKey(metamodelApiKey);
        Map<String, String> map = new HashMap<>();
        for (MetaItem item : items) {
            if (item.getDbColumn() != null && item.getApiKey() != null) {
                map.put(item.getDbColumn(), snakeToCamel(item.getApiKey()));
            }
        }
        return map;
    }

    /** 获取元模型定义（走缓存），不存在则抛异常 */
    private MetaModel getMetaModel(String metamodelApiKey) {
        MetaModel metaModel = metaModelService.getByApiKey(metamodelApiKey);
        if (metaModel == null) {
            throw new BaseKnownException(MetaRepoErrorCodeEnum.META_NOT_FOUND, metamodelApiKey);
        }
        return metaModel;
    }

    /**
     * 判断是否启用 Common 或 Tenant 查询。
     * @param isCommon true=检查 enableCommon，false=检查 enableTenant
     * @return 字段为 null 时默认启用
     */
    private boolean isEnabled(MetaModel metaModel, boolean isCommon) {
        Integer flag = isCommon ? metaModel.getEnableCommon() : metaModel.getEnableTenant();
        return flag == null || flag != 0;
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
