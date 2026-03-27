package com.hongyang.platform.metarepo.service.common.converter;

import com.hongyang.platform.metarepo.service.entity.metamodel.CommonMetadata;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 将 CommonMetadata（p_common_metadata 大宽表行）转换为业务 Entity 对象。
 * <p>
 * 转换分两步：
 * 1. 自动映射：CommonMetadata 的固定列（api_key, label, namespace, description, customFlg 等）
 *    与目标 Entity 同名字段自动赋值，无需硬编码。特殊映射：parentEntityApiKey → entityApiKey。
 * 2. 列映射：dbc_xxx_N 列通过 columnMapping（来自 p_meta_item）映射到业务字段。
 *    如果 dbc_xxx_N 映射的目标字段已在步骤 1 中设置过，以步骤 2 为准（覆盖）。
 */
@Slf4j
public final class CommonMetadataConverter {

    private CommonMetadataConverter() {}

    /** dbc_ 开头的字段名集合，用于区分固定列和扩展列 */
    private static final String DBC_PREFIX = "dbc";

    /** 特殊字段映射：CommonMetadata 字段名 → Entity 字段名 */
    private static final Map<String, String> SPECIAL_FIELD_MAPPING;
    static {
        Map<String, String> m = new HashMap<>();
        m.put("parentEntityApiKey", "entityApiKey");
        SPECIAL_FIELD_MAPPING = Collections.unmodifiableMap(m);
    }

    /** 不应映射到目标 Entity 的 CommonMetadata 字段 */
    private static final Set<String> SKIP_FIELDS = Set.of(
            "metamodelApiKey", "ownerApiKey", "metadataOrder", "parentEntityApiKey"
    );

    public static <T> List<T> convert(List<CommonMetadata> rows, Class<T> entityClass,
                                       Map<String, String> columnMapping) {
        if (rows == null || rows.isEmpty()) return Collections.emptyList();
        Map<String, Field> entityFields = buildFieldCache(entityClass);
        Map<String, Field> sourceFields = buildFieldCache(CommonMetadata.class);
        List<T> result = new ArrayList<>(rows.size());
        for (CommonMetadata row : rows) {
            T entity = convertRow(row, entityClass, columnMapping, entityFields, sourceFields);
            if (entity != null) result.add(entity);
        }
        return result;
    }

    public static <T> T convertOne(CommonMetadata row, Class<T> entityClass,
                                    Map<String, String> columnMapping) {
        if (row == null) return null;
        return convertRow(row, entityClass, columnMapping,
                buildFieldCache(entityClass), buildFieldCache(CommonMetadata.class));
    }

    private static <T> T convertRow(CommonMetadata row, Class<T> entityClass,
                                     Map<String, String> columnMapping,
                                     Map<String, Field> entityFields,
                                     Map<String, Field> sourceFields) {
        try {
            T entity = entityClass.getDeclaredConstructor().newInstance();

            // Step 1: 自动映射固定列（非 dbc_ 开头的 CommonMetadata 字段）
            for (Map.Entry<String, Field> entry : sourceFields.entrySet()) {
                String srcFieldName = entry.getKey();

                // 跳过 dbc_xxx_N 扩展列（由 Step 2 处理）
                if (srcFieldName.startsWith(DBC_PREFIX)) continue;

                // 跳过不应映射的字段
                if (SKIP_FIELDS.contains(srcFieldName)) continue;

                Object value = entry.getValue().get(row);
                if (value == null) continue;

                // 同名映射
                Field tgtField = entityFields.get(srcFieldName);
                if (tgtField != null) {
                    tgtField.set(entity, convertType(value, tgtField.getType()));
                }
            }

            // Step 1.5: 特殊字段映射（parentEntityApiKey → entityApiKey）
            for (Map.Entry<String, String> special : SPECIAL_FIELD_MAPPING.entrySet()) {
                Field srcField = sourceFields.get(special.getKey());
                Field tgtField = entityFields.get(special.getValue());
                if (srcField != null && tgtField != null) {
                    Object value = srcField.get(row);
                    if (value != null) {
                        tgtField.set(entity, convertType(value, tgtField.getType()));
                    }
                }
            }

            // Step 2: dbc_xxx_N → 业务字段（通过 p_meta_item 列映射）
            for (Map.Entry<String, String> m : columnMapping.entrySet()) {
                String dbColumn = m.getKey();           // e.g. "dbc_varchar_1"
                String entityFieldName = m.getValue();  // e.g. "entityType"

                // 从 CommonMetadata 读取 dbc_xxx_N 的值
                String sourceFieldName = snakeToCamel(dbColumn);
                Field srcField = sourceFields.get(sourceFieldName);
                if (srcField == null) continue;
                Object value = srcField.get(row);
                if (value == null) continue;

                Field tgtField = entityFields.get(entityFieldName);
                if (tgtField != null) {
                    tgtField.set(entity, convertType(value, tgtField.getType()));
                }
            }

            return entity;
        } catch (Exception e) {
            log.error("转换 CommonMetadata → {} 失败: {}", entityClass.getSimpleName(), e.getMessage());
            return null;
        }
    }

    private static Map<String, Field> buildFieldCache(Class<?> clazz) {
        Map<String, Field> cache = new HashMap<>();
        for (Class<?> c = clazz; c != null && c != Object.class; c = c.getSuperclass()) {
            for (Field f : c.getDeclaredFields()) {
                f.setAccessible(true);
                cache.putIfAbsent(f.getName(), f);
            }
        }
        return cache;
    }

    private static Object convertType(Object value, Class<?> target) {
        if (value == null) return null;
        if (target.isAssignableFrom(value.getClass())) return value;
        String s = value.toString();
        try {
            if (target == String.class) return s;
            if (target == Integer.class || target == int.class) return Integer.parseInt(s);
            if (target == Long.class || target == long.class) return Long.parseLong(s);
            if (target == Double.class || target == double.class) return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            // ignore
        }
        return value;
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
