package com.hongyang.platform.metarepo.service.api.internal;

import com.hongyang.platform.metarepo.service.entity.metamodel.CommonMetadata;
import com.hongyang.platform.metarepo.service.entity.metamodel.MetaItem;
import com.hongyang.platform.metarepo.service.entity.metamodel.MetaModel;
import com.hongyang.platform.metarepo.service.entity.metamodel.TenantMetadata;
import com.hongyang.platform.metarepo.service.service.metamodel.ICommonMetadataService;
import com.hongyang.platform.metarepo.service.service.metamodel.IMetaItemService;
import com.hongyang.platform.metarepo.service.service.metamodel.IMetaModelService;
import com.hongyang.platform.metarepo.service.service.metamodel.ITenantMetadataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 元模型浏览内部接口（供前端管理界面使用）。
 * <p>
 * 提供元模型列表、字段映射、Common/Tenant 原始数据查看能力。
 */
@Slf4j
@RestController
@RequestMapping("/metarepo/internal")
@RequiredArgsConstructor
public class MetamodelBrowseApiService {

    private final IMetaModelService metaModelService;
    private final IMetaItemService metaItemService;
    private final ICommonMetadataService commonMetadataService;
    private final ITenantMetadataService tenantMetadataService;

    /**
     * 查询所有元模型定义
     */
    @GetMapping("/metamodels")
    public List<MetaModel> listMetaModels() {
        return metaModelService.listAll();
    }

    /**
     * 查询指定元模型的字段映射（p_meta_item）
     */
    @GetMapping("/meta-items")
    public List<MetaItem> listMetaItems(@RequestParam("metamodelApiKey") String metamodelApiKey) {
        return metaItemService.listByMetamodelApiKey(metamodelApiKey);
    }

    /**
     * 查询指定元模型的 Common 级原始数据（p_common_metadata 大宽表）。
     * 返回结构化数据：固定列 + dbc 列按字段映射展开为 fieldName→value。
     */
    @GetMapping("/common-metadata")
    public List<Map<String, Object>> listCommonMetadata(
            @RequestParam("metamodelApiKey") String metamodelApiKey) {
        List<CommonMetadata> rows = commonMetadataService.listByMetamodelApiKey(metamodelApiKey);
        Map<String, String> columnMapping = buildColumnMapping(metamodelApiKey);
        return toReadableRows(rows, columnMapping);
    }

    /**
     * 查询指定元模型的 Tenant 级原始数据（p_tenant_metadata 大宽表）。
     */
    @GetMapping("/tenant-metadata")
    public List<Map<String, Object>> listTenantMetadata(
            @RequestParam("metamodelApiKey") String metamodelApiKey) {
        List<TenantMetadata> rows = tenantMetadataService.listByMetamodelApiKey(metamodelApiKey);
        Map<String, String> columnMapping = buildColumnMapping(metamodelApiKey);
        return toReadableRows(rows, columnMapping);
    }

    /**
     * 查询指定元模型的列映射摘要（dbColumn → fieldName）
     */
    @GetMapping("/column-mapping")
    public Map<String, String> getColumnMapping(
            @RequestParam("metamodelApiKey") String metamodelApiKey) {
        return buildColumnMapping(metamodelApiKey);
    }

    // ==================== 内部方法 ====================

    private Map<String, String> buildColumnMapping(String metamodelApiKey) {
        List<MetaItem> items = metaItemService.listByMetamodelApiKey(metamodelApiKey);
        Map<String, String> map = new LinkedHashMap<>();
        for (MetaItem item : items) {
            if (item.getDbColumn() != null && item.getApiKey() != null) {
                map.put(item.getDbColumn(), item.getApiKey());
            }
        }
        return map;
    }

    /**
     * 将大宽表行转换为可读的 Map 列表。
     * 固定列直接输出，dbc_xxx_N 列通过 columnMapping 转换为业务字段名。
     */
    private <R extends Serializable> List<Map<String, Object>> toReadableRows(
            List<R> rows, Map<String, String> columnMapping) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (R row : rows) {
            Map<String, Object> map = new LinkedHashMap<>();
            try {
                // 通过反射读取所有字段
                Map<String, Object> allFields = new HashMap<>();
                for (Class<?> c = row.getClass(); c != null && c != Object.class; c = c.getSuperclass()) {
                    for (java.lang.reflect.Field f : c.getDeclaredFields()) {
                        f.setAccessible(true);
                        Object val = f.get(row);
                        if (val != null) {
                            allFields.put(f.getName(), val);
                        }
                    }
                }

                // 固定列
                putIfPresent(map, "apiKey", allFields.get("apiKey"));
                putIfPresent(map, "label", allFields.get("label"));
                putIfPresent(map, "labelKey", allFields.get("labelKey"));
                putIfPresent(map, "namespace", allFields.get("namespace"));
                putIfPresent(map, "metamodelApiKey", allFields.get("metamodelApiKey"));
                putIfPresent(map, "metadataApiKey", allFields.get("metadataApiKey"));
                putIfPresent(map, "entityApiKey", allFields.get("entityApiKey"));
                putIfPresent(map, "parentMetadataApiKey", allFields.get("parentMetadataApiKey"));
                putIfPresent(map, "customFlg", allFields.get("customFlg"));
                putIfPresent(map, "metadataOrder", allFields.get("metadataOrder"));
                putIfPresent(map, "description", allFields.get("description"));
                putIfPresent(map, "deleteFlg", allFields.get("deleteFlg"));
                putIfPresent(map, "tenantId", allFields.get("tenantId"));

                // dbc 列 → 业务字段名
                Map<String, Object> mappedFields = new LinkedHashMap<>();
                for (Map.Entry<String, String> entry : columnMapping.entrySet()) {
                    String dbColumn = entry.getKey();
                    String fieldName = entry.getValue();
                    String camelColumn = snakeToCamel(dbColumn);
                    Object val = allFields.get(camelColumn);
                    if (val != null) {
                        mappedFields.put(fieldName + " (" + dbColumn + ")", val);
                    }
                }
                map.put("_mappedFields", mappedFields);

            } catch (Exception e) {
                log.warn("转换行数据失败: {}", e.getMessage());
            }
            result.add(map);
        }
        return result;
    }

    private void putIfPresent(Map<String, Object> map, String key, Object value) {
        if (value != null) {
            map.put(key, value);
        }
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
