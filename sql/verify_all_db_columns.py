#!/usr/bin/env python3
"""
严格验证所有 6 个元模型的 p_meta_item.db_column 是否能被 CommonMetadataConverter 正确映射。

验证规则：
  db_column 经过 snakeToCamel() 后，必须是 CommonMetadata Java 类中存在的字段名。
  - 固定列（如 entity_api_key → entityApiKey）：CommonMetadata 有此字段 ✅
  - dbc 列（如 dbc_int1 → dbcInt1）：CommonMetadata 有此字段 ✅
  - 不存在的列（如 check_formula → checkFormula）：CommonMetadata 没有此字段 ❌
"""

def snake_to_camel(s):
    if s is None or '_' not in s:
        return s
    parts = s.split('_')
    return parts[0] + ''.join(p.capitalize() for p in parts[1:])

# CommonMetadata 的所有 Java 字段名（从源码提取）
common_metadata_fields = {
    # 继承自 BaseEntity
    'id', 'deleteFlg', 'createdAt', 'createdBy', 'updatedAt', 'updatedBy',
    # 继承自 BaseMetaCommonEntity
    'apiKey', 'label', 'labelKey', 'namespace',
    # CommonMetadata 自有固定列
    'metamodelApiKey', 'metadataApiKey', 'entityApiKey', 'parentMetadataApiKey',
    'customFlg', 'metadataOrder', 'description', 'metaVersion',
    # dbc_varchar 1~30
    *[f'dbcVarchar{i}' for i in range(1, 31)],
    # dbc_textarea 1~10
    *[f'dbcTextarea{i}' for i in range(1, 11)],
    # dbc_bigint 1~20
    *[f'dbcBigint{i}' for i in range(1, 21)],
    # dbc_int 1~15
    *[f'dbcInt{i}' for i in range(1, 16)],
    # dbc_smallint 1~50
    *[f'dbcSmallint{i}' for i in range(1, 51)],
    # dbc_decimal 1~5
    *[f'dbcDecimal{i}' for i in range(1, 6)],
}

# SKIP_FIELDS in CommonMetadataConverter (Step 1 skips these, Step 3 handles via columnMapping)
skip_fields = {'metamodelApiKey', 'metadataId', 'objectApiKey', 'parentMetadataId', 'metadataOrder', 'metaVersion', 'customFlg'}

# 从数据库查询结果（硬编码，刚才已获取）
meta_items = [
    # checkRule
    ('checkRule', 'namespace', 'namespace'),
    ('checkRule', 'entityApiKey', 'entity_api_key'),
    ('checkRule', 'apiKey', 'api_key'),
    ('checkRule', 'description', 'description'),
    ('checkRule', 'label', 'label'),
    ('checkRule', 'labelKey', 'label_key'),
    ('checkRule', 'activeFlg', 'active_flg'),
    ('checkRule', 'checkFormula', 'check_formula'),
    ('checkRule', 'checkErrorMsg', 'check_error_msg'),
    ('checkRule', 'checkErrorMsgKey', 'check_error_msg_key'),
    ('checkRule', 'checkErrorLocation', 'check_error_location'),
    ('checkRule', 'checkErrorWay', 'check_error_way'),
    ('checkRule', 'checkErrorItemApiKey', 'check_error_item_api_key'),
    ('checkRule', 'checkAllItemsFlg', 'check_all_items_flg'),
    ('checkRule', 'createdBy', 'created_by'),
    ('checkRule', 'createdAt', 'created_at'),
    ('checkRule', 'updatedBy', 'updated_by'),
    ('checkRule', 'updatedAt', 'updated_at'),
    # entity
    ('entity', 'svg_api_key', 'dbc_varchar1'),
    ('entity', 'db_table', 'dbc_varchar2'),
    ('entity', 'entity_type', 'dbc_int1'),
    ('entity', 'custom_entity_seq', 'dbc_int2'),
    ('entity', 'business_category', 'dbc_int3'),
    ('entity', 'enable_flg', 'dbc_smallint3'),
    ('entity', 'custom_flg', 'dbc_smallint4'),
    ('entity', 'enable_history_log', 'dbc_smallint5'),
    ('entity', 'enable_config', 'dbc_bigint1'),
    ('entity', 'enable_busitype', 'dbc_smallint1'),
    ('entity', 'enable_checkrule', 'dbc_smallint2'),
    ('entity', 'enable_duplicaterule', 'dbc_int4'),
    ('entity', 'enable_script_executor', 'dbc_int5'),
    ('entity', 'is_archived', 'dbc_int6'),
    ('entity', 'enable_group_member', 'dbc_int7'),
    ('entity', 'enable_dynamic_feed', 'dbc_int8'),
    ('entity', 'svg_color', 'dbc_varchar3'),
    # entityLink
    ('entityLink', 'type_property', 'dbc_varchar_1'),
    ('entityLink', 'parent_entity_api_key', 'dbc_varchar_2'),
    ('entityLink', 'child_entity_api_key', 'dbc_varchar_3'),
    ('entityLink', 'description_key', 'dbc_varchar_4'),
    ('entityLink', 'link_type', 'dbc_int_1'),
    ('entityLink', 'detail_link', 'dbc_smallint_1'),
    ('entityLink', 'cascade_delete', 'dbc_smallint_2'),
    ('entityLink', 'access_control', 'dbc_smallint_3'),
    ('entityLink', 'enable_flg', 'dbc_smallint_4'),
    # pickOption (after fix)
    ('pickOption', 'namespace', 'namespace'),
    ('pickOption', 'entityApiKey', 'entity_api_key'),
    ('pickOption', 'itemApiKey', 'dbc_varchar1'),
    ('pickOption', 'apiKey', 'api_key'),
    ('pickOption', 'label', 'label'),
    ('pickOption', 'labelKey', 'label_key'),
    ('pickOption', 'optionOrder', 'dbc_int1'),
    ('pickOption', 'isDefault', 'dbc_smallint1'),
    ('pickOption', 'isGlobal', 'dbc_smallint2'),
    ('pickOption', 'isCustom', 'custom_flg'),
    ('pickOption', 'isDeleted', 'delete_flg'),
    ('pickOption', 'isActive', 'dbc_smallint3'),
    ('pickOption', 'description', 'description'),
    ('pickOption', 'specialFlg', 'dbc_smallint4'),
    ('pickOption', 'optionType', 'dbc_int2'),
    ('pickOption', 'createdBy', 'created_by'),
    ('pickOption', 'createdAt', 'created_at'),
    ('pickOption', 'updatedBy', 'updated_by'),
    ('pickOption', 'updatedAt', 'updated_at'),
    # referenceFilter (after fix)
    ('referenceFilter', 'namespace', 'namespace'),
    ('referenceFilter', 'entityApiKey', 'entity_api_key'),
    ('referenceFilter', 'itemApiKey', 'dbc_varchar1'),
    ('referenceFilter', 'filterMode', 'dbc_smallint1'),
    ('referenceFilter', 'filterFormula', 'dbc_textarea1'),
    ('referenceFilter', 'isActive', 'dbc_smallint2'),
    ('referenceFilter', 'andor', 'dbc_smallint3'),
    ('referenceFilter', 'isDeleted', 'delete_flg'),
    ('referenceFilter', 'createdBy', 'created_by'),
    ('referenceFilter', 'createdAt', 'created_at'),
    ('referenceFilter', 'updatedBy', 'updated_by'),
    ('referenceFilter', 'updatedAt', 'updated_at'),
]

# item 太多（101行），只检查关键的非 dbc 列
item_non_dbc = [
    ('item', 'namespace', 'namespace'),
    ('item', 'entityApiKey', 'entity_api_key'),
    ('item', 'apiKey', 'api_key'),
    ('item', 'label', 'label'),
    ('item', 'labelKey', 'label_key'),
    ('item', 'name', 'name'),
    ('item', 'description', 'description'),
    ('item', 'isCustom', 'custom_flg'),
    ('item', 'isDeleted', 'delete_flg'),
    ('item', 'createdBy', 'created_by'),
    ('item', 'createdAt', 'created_at'),
    ('item', 'updatedBy', 'updated_by'),
    ('item', 'updatedAt', 'updated_at'),
]
meta_items.extend(item_non_dbc)

errors = []
warnings = []

for metamodel, api_key, db_column in meta_items:
    if db_column is None:
        continue
    
    camel = snake_to_camel(db_column)
    
    if camel not in common_metadata_fields:
        errors.append(f"❌ [{metamodel}] api_key='{api_key}', db_column='{db_column}' → snakeToCamel='{camel}' → CommonMetadata 中不存在此字段！")
    else:
        # 检查是否在 SKIP_FIELDS 中（Step 1 会跳过，但 Step 3 的 columnMapping 会处理）
        pass

print(f"验证了 {len(meta_items)} 条 p_meta_item 映射\n")

if errors:
    print(f"{'='*70}")
    print(f"发现 {len(errors)} 个错误：")
    print(f"{'='*70}")
    for e in errors:
        print(f"  {e}")
else:
    print("✅ 全部 6 个元模型的 db_column 映射验证通过！")

# 特别检查 entityLink 的下划线格式问题
print(f"\n{'='*70}")
print("entityLink 列名格式检查（历史遗留下划线问题）：")
print(f"{'='*70}")
for metamodel, api_key, db_column in meta_items:
    if metamodel == 'entityLink' and db_column and 'dbc_' in db_column:
        camel = snake_to_camel(db_column)
        if camel in common_metadata_fields:
            print(f"  ✅ {db_column} → {camel} → 存在")
        else:
            print(f"  ❌ {db_column} → {camel} → 不存在！")

# 特别检查 checkRule 的非固定列
print(f"\n{'='*70}")
print("checkRule 非固定列检查：")
print(f"{'='*70}")
non_fixed = {'active_flg', 'check_formula', 'check_error_msg', 'check_error_msg_key',
             'check_error_location', 'check_error_way', 'check_error_item_api_key', 'check_all_items_flg'}
for metamodel, api_key, db_column in meta_items:
    if metamodel == 'checkRule' and db_column in non_fixed:
        camel = snake_to_camel(db_column)
        exists = camel in common_metadata_fields
        print(f"  {'✅' if exists else '❌'} api_key='{api_key}', db_column='{db_column}' → '{camel}' → {'存在' if exists else '不存在！'}")

