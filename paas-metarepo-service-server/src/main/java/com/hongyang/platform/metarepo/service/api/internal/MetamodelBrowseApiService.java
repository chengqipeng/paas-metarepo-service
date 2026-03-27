package com.hongyang.platform.metarepo.service.api.internal;

import com.hongyang.platform.metarepo.service.common.converter.CommonMetadataConverter;
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
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 元模型浏览接口（供前端管理界面使用）。
 * <p>
 * 对外只提供 merge 后的数据接口，不区分 Common/Tenant。
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
     * 查询指定元模型的列映射摘要（dbColumn → fieldName）
     */
    @GetMapping("/column-mapping")
    public Map<String, String> getColumnMapping(
            @RequestParam("metamodelApiKey") String metamodelApiKey) {
        return buildColumnMapping(metamodelApiKey);
    }

    /**
     * 查询指定元模型的合并后元数据（Common + Tenant merge）。
     * 返回结构化数据：固定列 + dbc 列按字段映射展开为 fieldName→value。
     * 合并规则：Common 有 Tenant 无 → Common；同 apiKey → Tenant 覆盖；delete_flg=1 → 隐藏。
     */
    @GetMapping("/metadata")
    public List<Map<String, Object>> listMergedMetadata(
            @RequestParam("metamodelApiKey") String metamodelApiKey) {
        List<CommonMetadata> commonRows = commonMetadataService.listByMetamodelApiKey(metamodelApiKey);
        List<TenantMetadata> tenantRows = tenantMetadataService.listByMetamodelApiKey(metamodelApiKey);
        Map<String, String> columnMapping = buildColumnMapping(metamodelApiKey);

        // 转为统一的 Map 格式
        List<Map<String, Object>> commonMaps = toReadableRows(commonRows, columnMapping);
        List<Map<String, Object>> tenantMaps = toReadableRows(tenantRows, columnMapping);

        // 合并：按 apiKey
        return merge(commonMaps, tenantMaps);
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
     * 合并 Common 和 Tenant 数据。
     * 按 apiKey 合并：Common 有 Tenant 无 → Common；同 apiKey → Tenant 覆盖；delete_flg=1 → 隐藏。
     */
    private List<Map<String, Object>> merge(List<Map<String, Object>> commonList,
                                             List<Map<String, Object>> tenantList) {
        Map<String, Map<String, Object>> tenantMap = tenantList.stream()
                .filter(m -> m.get("apiKey") != null)
                .collect(Collectors.toMap(
                        m -> (String) m.get("apiKey"), m -> m, (a, b) -> b, LinkedHashMap::new));

        List<Map<String, Object>> result = new ArrayList<>();
        for (Map<String, Object> common : commonList) {
            String apiKey = (String) common.get("apiKey");
            Map<String, Object> tenant = tenantMap.remove(apiKey);
            if (tenant != null) {
                Object deleteFlg = tenant.get("deleteFlg");
                if (deleteFlg != null && (deleteFlg.equals(1) || deleteFlg.equals("1"))) continue;
                tenant.put("_source", "tenant");
                result.add(tenant);
            } else {
                common.put("_source", "common");
                result.add(common);
            }
        }
        for (Map<String, Object> tenant : tenantMap.values()) {
            Object deleteFlg = tenant.get("deleteFlg");
            if (deleteFlg == null || (!deleteFlg.equals(1) && !deleteFlg.equals("1"))) {
                tenant.put("_source", "tenant");
                result.add(tenant);
            }
        }
        return result;
    }

    private <R extends Serializable> List<Map<String, Object>> toReadableRows(
            List<R> rows, Map<String, String> columnMapping) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (R row : rows) {
            Map<String, Object> map = new LinkedHashMap<>();
            try {
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
                putIfPresent(map, "description", allFields.get("description"));
                putIfPresent(map, "deleteFlg", allFields.get("deleteFlg"));

                // dbc 列 → 业务字段名
                for (Map.Entry<String, String> entry : columnMapping.entrySet()) {
                    String camelColumn = snakeToCamel(entry.getKey());
                    Object val = allFields.get(camelColumn);
                    if (val != null) {
                        map.put(entry.getValue(), val);
                    }
                }
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
