package com.hongyang.platform.metarepo.service.service.metadata;

import com.hongyang.framework.dao.entity.BaseMetaTenantEntity;
import com.hongyang.framework.dao.query.PageResult;
import com.hongyang.platform.metarepo.service.common.annotation.CommonTenantSplit;
import com.hongyang.platform.metarepo.service.common.converter.CommonMetadataConverter;
import com.hongyang.platform.metarepo.service.entity.metamodel.CommonMetadata;
import com.hongyang.platform.metarepo.service.entity.metamodel.MetaItem;
import com.hongyang.platform.metarepo.service.entity.metamodel.TenantMetadata;
import com.hongyang.platform.metarepo.service.service.metamodel.ICommonMetadataService;
import com.hongyang.platform.metarepo.service.service.metamodel.IMetaItemService;
import com.hongyang.platform.metarepo.service.service.metamodel.ITenantMetadataService;
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
 * - Common 数据：查 p_common_metadata 大宽表 → 列映射转换为业务 Entity
 * - Tenant 数据：查 Tenant 快捷表（如 p_tenant_entity，结构与大宽表一致）→ 列映射转换为业务 Entity
 * - Tenant 快捷表名由 @TableName 注解指定，结构与 p_tenant_metadata 一致（dbc_xxx 列）
 * - 合并算法：Common 有 Tenant 无 → Common；同 apiKey → Tenant 覆盖；delete_flg=1 → 隐藏
 */
@Slf4j
public abstract class AbstractMetadataServiceImpl<T extends BaseMetaTenantEntity> {

    @Autowired
    private ICommonMetadataService commonMetadataService;

    @Autowired
    private ITenantMetadataService tenantMetadataService;

    @Autowired
    private IMetaItemService metaItemService;

    private String metamodelApiKey;
    private String tenantTableName;
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

    /**
     * 获取 Tenant 快捷表名（从 @TableName 注解读取）
     */
    protected String getTenantTableName() {
        if (tenantTableName == null) {
            com.baomidou.mybatisplus.annotation.TableName tn =
                    getEntityClass().getAnnotation(com.baomidou.mybatisplus.annotation.TableName.class);
            if (tn != null) {
                tenantTableName = tn.value();
            }
        }
        return tenantTableName;
    }

    @SuppressWarnings("unchecked")
    public Class<T> getEntityClass() {
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];
    }

    // ==================== 列映射（走 IMetaItemService） ====================

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

    // ==================== Common 数据查询 ====================

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

    // ==================== Tenant 数据查询（查快捷表，结构与大宽表一致） ====================

    /**
     * 查询 Tenant 级数据。
     * 通过 DynamicTableNameHolder 将 ITenantMetadataService 的查询切换到 Tenant 快捷表
     * （如 p_tenant_entity），查出 TenantMetadata 行后通过列映射转换为业务 Entity。
     */
    protected List<T> listTenant() {
        String tableName = getTenantTableName();
        if (tableName == null) {
            return Collections.emptyList();
        }
        List<TenantMetadata> rows = tenantMetadataService.listByTable(tableName);
        return CommonMetadataConverter.convertFromTenant(rows, getEntityClass(), getColumnMapping());
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
