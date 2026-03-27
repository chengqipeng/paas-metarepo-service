package com.hongyang.platform.metarepo.service.common.converter;

import com.hongyang.platform.metarepo.service.entity.metamodel.CommonMetadata;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 将 CommonMetadata（p_meta_common_metadata 大宽表行）转换为业务 Entity 对象。
 * <p>
 * 转换分三步：
 * 1. 自动映射：CommonMetadata 的固定列与目标 Entity 同名字段自动赋值
 * 2. 特殊映射：objectApiKey → entityApiKey（子表需要）
 * 3. 列映射：dbc_xxx_N 列通过 columnMapping（来自 p_meta_item）映射到业务字段
 */
@Slf4j
public final class CommonMetadataConverter {

    private CommonMetadataConverter() {}

    private static final String DBC_PREFIX = "dbc";

    /** 特殊字段映射：CommonMetadata 字段名 → Entity 字段名 */
    private static final Map<String, String> SPECIAL_MAPPING = Map.of(
            "objectApiKey", "entityApiKey"
    );

    /** 不应自动映射到目标 Entity 的 CommonMetadata 字段（通过特殊映射或无对应字段） */
    private static final Set<String> SKIP_FIELDS = Set.of(
            "metamodelApiKey", "metadataId", "objectApiKey",
            "parentMetadataId", "metadataOrder", "metaVersion", "customFlg"
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

            // Step 1: 自动映射固定列（非 dbc_ 开头、非 SKIP 的同名字段）
            for (Map.Entry<String, Field> entry : sourceFields.entrySet()) {
                String srcName = entry.getKey();
                if (srcName.startsWith(DBC_PREFIX)) continue;
                if (SKIP_FIELDS.contains(srcName)) continue;

                Object value = entry.getValue().get(row);
                if (value == null) continue;

                Field tgt = entityFields.get(srcName);
                if (tgt != null) {
                    tgt.set(entity, convertType(value, tgt.getType()));
                }
            }

            // Step 2: 特殊映射（objectApiKey → entityApiKey）
            for (Map.Entry<String, String> sp : SPECIAL_MAPPING.entrySet()) {
                Field src = sourceFields.get(sp.getKey());
                Field tgt = entityFields.get(sp.getValue());
                if (src != null && tgt != null) {
                    Object value = src.get(row);
                    if (value != null) {
                        tgt.set(entity, convertType(value, tgt.getType()));
                    }
                }
            }

            // Step 3: dbc_xxx_N → 业务字段（通过 p_meta_item 列映射）
            for (Map.Entry<String, String> m : columnMapping.entrySet()) {
                String dbColumn = m.getKey();           // e.g. "dbc_bigint_1"
                String entityFieldName = m.getValue();  // e.g. "entityType"

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
            log.error("转换 CommonMetadata → {} 失败: api_key={}, error={}",
                    entityClass.getSimpleName(), row.getApiKey(), e.getMessage());
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
            if (target == BigDecimal.class) return new BigDecimal(s);
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
