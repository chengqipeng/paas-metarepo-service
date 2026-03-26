package com.hongyang.platform.metarepo.service.service.impl;

import com.hongyang.framework.dao.dynamic.DynamicTableNameHolder;
import com.hongyang.framework.dao.service.SimpleBaseServiceImpl;
import com.hongyang.platform.metarepo.service.entity.metamodel.tenant.MetaMetamodelData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Common/Tenant/Delta 三层元数据合并服务
 * 实现 DESIGN.md 3.4 节的核心合并算法：最终值 = Delta(Tenant(Common))
 */
@Slf4j
@Service
public class MetamodelDataMergeService
        extends SimpleBaseServiceImpl<MetaMetamodelData> {

    private static final String TABLE_COMMON = "p_meta_metamodel_data";
    private static final String TABLE_TENANT = "p_meta_tenant_metadata";
    private static final String TABLE_DELTA = "p_meta_metadata_delta";

    /**
     * 查询单条元数据（Common + Tenant + Delta 合并）
     */
    public MetaMetamodelData getMerged(Long tenantId, Long metamodelId, String apiKey) {
        // Step 1: 查 Common 层（tenant_id=0）
        MetaMetamodelData common = DynamicTableNameHolder.executeWith(TABLE_COMMON, () ->
                lambdaQuery()
                        .eq(MetaMetamodelData::getTenantId, 0L)
                        .eq(MetaMetamodelData::getMetamodelId, metamodelId)
                        .eq(MetaMetamodelData::getApiKey, apiKey)
                        .one()
        );

        // Step 2: 查 Tenant 层
        MetaMetamodelData tenant = DynamicTableNameHolder.executeWith(TABLE_TENANT, () ->
                lambdaQuery()
                        .eq(MetaMetamodelData::getTenantId, tenantId)
                        .eq(MetaMetamodelData::getMetamodelId, metamodelId)
                        .eq(MetaMetamodelData::getApiKey, apiKey)
                        .one()
        );

        // 合并 Common + Tenant
        MetaMetamodelData merged = mergeFields(common, tenant);
        if (merged == null) return null;

        // Step 3: 查 Delta 层
        MetaMetamodelData delta = DynamicTableNameHolder.executeWith(TABLE_DELTA, () ->
                lambdaQuery()
                        .eq(MetaMetamodelData::getTenantId, tenantId)
                        .eq(MetaMetamodelData::getMetamodelId, metamodelId)
                        .eq(MetaMetamodelData::getApiKey, apiKey)
                        .one()
        );

        return mergeFields(merged, delta);
    }

    /**
     * 查询某对象下所有子元数据（如字段列表），合并 Common + Tenant
     */
    public List<MetaMetamodelData> listMergedChildren(Long tenantId, Long metamodelId, Long parentEntityId) {
        // Common 层
        List<MetaMetamodelData> commonList = DynamicTableNameHolder.executeWith(TABLE_COMMON, () ->
                lambdaQuery()
                        .eq(MetaMetamodelData::getTenantId, 0L)
                        .eq(MetaMetamodelData::getMetamodelId, metamodelId)
                        .eq(MetaMetamodelData::getParentEntityId, parentEntityId)
                        .orderByAsc(MetaMetamodelData::getMetadataOrder)
                        .list()
        );

        // Tenant 层
        List<MetaMetamodelData> tenantList = DynamicTableNameHolder.executeWith(TABLE_TENANT, () ->
                lambdaQuery()
                        .eq(MetaMetamodelData::getTenantId, tenantId)
                        .eq(MetaMetamodelData::getMetamodelId, metamodelId)
                        .eq(MetaMetamodelData::getParentEntityId, parentEntityId)
                        .orderByAsc(MetaMetamodelData::getMetadataOrder)
                        .list()
        );

        return mergeChildLists(commonList, tenantList);
    }

    /**
     * 字段级合并：overlay 中非 null 的字段覆盖 base
     */
    private MetaMetamodelData mergeFields(MetaMetamodelData base, MetaMetamodelData overlay) {
        if (base == null && overlay == null) return null;
        if (base == null) return overlay;
        if (overlay == null) return base;

        try {
            for (Field field : MetaMetamodelData.class.getDeclaredFields()) {
                field.setAccessible(true);
                Object overlayVal = field.get(overlay);
                if (overlayVal != null) {
                    field.set(base, overlayVal);
                }
            }
        } catch (IllegalAccessException e) {
            log.error("元数据字段合并失败", e);
        }
        return base;
    }

    /**
     * 子元数据列表合并（DESIGN.md 3.4 节子元数据合并规则）
     * - Common 有、Tenant 无 → 使用 Common
     * - Common 有、Tenant 有（同 apiKey）→ Tenant 覆盖 Common
     * - Common 无、Tenant 有 → 纯租户自定义
     * - Tenant 中 deleteFlg=1 → 对此租户隐藏
     */
    private List<MetaMetamodelData> mergeChildLists(List<MetaMetamodelData> commonList,
                                                           List<MetaMetamodelData> tenantList) {
        Map<String, MetaMetamodelData> tenantMap = tenantList.stream()
                .filter(e -> e.getApiKey() != null)
                .collect(Collectors.toMap(MetaMetamodelData::getApiKey, e -> e, (a, b) -> b));

        List<MetaMetamodelData> result = new ArrayList<>();

        // 处理 Common 列表
        for (MetaMetamodelData common : commonList) {
            MetaMetamodelData tenant = tenantMap.remove(common.getApiKey());
            if (tenant != null) {
                // Tenant 中 deleteFlg=1 表示隐藏
                if (tenant.getDeleteFlg() != null && tenant.getDeleteFlg() == 1) {
                    continue;
                }
                // Tenant 覆盖 Common
                result.add(mergeFields(common, tenant));
            } else {
                result.add(common);
            }
        }

        // 剩余的 Tenant 独有记录（纯租户自定义）
        for (MetaMetamodelData tenant : tenantMap.values()) {
            if (tenant.getDeleteFlg() == null || tenant.getDeleteFlg() != 1) {
                result.add(tenant);
            }
        }

        // 按 metadataOrder 排序
        result.sort(Comparator.comparingInt(e ->
                e.getMetadataOrder() != null ? e.getMetadataOrder() : Integer.MAX_VALUE));

        return result;
    }
}
