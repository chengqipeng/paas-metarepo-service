package com.hongyang.platform.metarepo.service.service.metadata;

import com.hongyang.framework.dao.entity.BaseMetaTenantEntity;
import com.hongyang.framework.dao.query.PageResult;
import com.hongyang.framework.dao.service.DataBaseServiceImpl;
import com.hongyang.platform.metarepo.service.common.annotation.CommonTenantSplit;
import com.hongyang.platform.metarepo.service.common.converter.CommonMetadataConverter;
import com.hongyang.platform.metarepo.service.entity.metamodel.CommonMetadata;
import com.hongyang.platform.metarepo.service.entity.metamodel.MetaItem;
import com.hongyang.platform.metarepo.service.service.metamodel.ICommonMetadataService;
import com.hongyang.platform.metarepo.service.service.metamodel.IMetaItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 元数据 Service 基类（metarepo 层）。
 * <p>
 * 提供 Common/Tenant 合并查询能力：
 * - Common 数据：通过 ICommonMetadataService（走 MetaServiceImpl 两阶段缓存）查询，
 *   CommonMetadata 的固定列和 dbc_xxx_N 列映射到 Entity 字段
 * - Tenant 数据：查各自的快捷表（@TableName 指定）
 * - 合并算法：Common 有 Tenant 无 → Common；同 apiKey → Tenant 覆盖；delete_flg=1 → 隐藏
 */
@Slf4j
public abstract class AbstractMetadataServiceImpl<T extends BaseMetaTenantEntity>
        extends DataBaseServiceImpl<T> {

    @Autowired
    private ICommonMetadataService commonMetadataService;

    @Autowired
    private IMetaItemService metaItemService;

    private String metamodelApiKey;
    private volatile Map<String, String> columnMappingCache;

    protected String getMetamodelApiKey() {
        if (metamodelApiKey == null) {
            CommonTenantSplit split = getEntityClass().getAnnotation(CommonTenantSplit.class);
            if (split == null) {
                throw new IllegalStateException(
                    getEntityClass().getSimpleName() + " 未标注 @CommonTenantSplit");
            }
            metamodelApiKey = split.metamodelApiKey();
        }
        return metamodelApiKey;
    }

    @SuppressWarnings("unchecked")
    public Class<T> getEntityClass() {
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];
    }

    // ==================== 列映射（走 IMetaItemService 缓存） ====================

    protected Map<String, String> getColumnMapping() {
        if (columnMappingCache == null) {
            synchronized (this) {
                if (columnMappingCache == null) {
                    String key = getMetamodelApiKey();
                    List<MetaItem> items = metaItemService.listByMetamodelApiKey(key);
                    Map<String, String> map = new HashMap<>();
                    for (MetaItem item : items) {
                        if (item.getDbColumn() != null && item.getApiKey() != null) {
                            map.put(item.getDbColumn(), snakeToCamel(item.getApiKey()));
                        }
                    }
                    columnMappingCache = Collections.unmodifiableMap(map);
                }
            }
        }
        return columnMappingCache;
    }

    // ==================== Common 数据查询（走 ICommonMetadataService 缓存） ====================

    protected List<T> listCommon() {
        List<CommonMetadata> rows = commonMetadataService
                .listByMetamodelApiKey(getMetamodelApiKey());
        return CommonMetadataConverter.convert(rows, getEntityClass(), getColumnMapping());
    }

    protected T getCommonByApiKey(String apiKey) {
        CommonMetadata row = commonMetadataService
                .getByMetamodelApiKeyAndApiKey(getMetamodelApiKey(), apiKey);
        return CommonMetadataConverter.convertOne(row, getEntityClass(), getColumnMapping());
    }

    // ==================== Tenant 数据查询 ====================

    public List<T> listTenant() {
        return lambdaQuery().list();
    }

    // ==================== 合并查询 ====================

    public List<T> listMerged(Function<T, String> apiKeyExtractor) {
        return merge(listCommon(), listTenant(), apiKeyExtractor);
    }

    public PageResult<T> pageMerged(int page, int size, Function<T, String> apiKeyExtractor) {
        List<T> all = listMerged(apiKeyExtractor);
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

    public T getByApiKeyMerged(String apiKey, Function<T, String> apiKeyExtractor) {
        T tenant = listTenant().stream()
                .filter(e -> apiKey.equals(apiKeyExtractor.apply(e)))
                .findFirst().orElse(null);
        if (tenant != null) {
            if (tenant.getDeleteFlg() != null && tenant.getDeleteFlg() == 1) return null;
            return tenant;
        }
        return getCommonByApiKey(apiKey);
    }

    protected List<T> merge(List<T> commonList, List<T> tenantList,
                             Function<T, String> apiKeyExtractor) {
        Map<String, T> tenantMap = tenantList.stream()
                .filter(e -> apiKeyExtractor.apply(e) != null)
                .collect(Collectors.toMap(apiKeyExtractor, e -> e, (a, b) -> b));

        List<T> result = new ArrayList<>();
        for (T common : commonList) {
            String apiKey = apiKeyExtractor.apply(common);
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
