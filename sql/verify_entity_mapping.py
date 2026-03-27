#!/usr/bin/env python3
"""验证 xobject(entity) 的 p_meta_item 字段定义能否完整映射到 Entity.java + 大宽表"""
import psycopg2

PG = dict(host='10.65.2.6', port=5432, dbname='crm_cd_data',
          user='xsy_metarepo', password='sk29XGLI%iu88pF*', connect_timeout=10)

# Entity.java 业务字段（不含基类固定列）
ENTITY_BIZ_FIELDS = {
    'entityType', 'svgApiKey', 'svgColor', 'description', 'descriptionKey',
    'customEntitySeq', 'enableFlg', 'customFlg', 'businessCategory',
    'typeProperty', 'dbTable', 'detailFlg', 'enableTeam', 'enableSocial',
    'enableConfig', 'hiddenFlg', 'searchable', 'enableSharing',
    'enableScriptTrigger', 'enableActivity', 'enableHistoryLog',
    'enableReport', 'enableRefer', 'enableApi', 'enableFlow', 'enablePackage',
    'extendProperty',
    'enableDynamicFeed', 'enableGroupMember', 'isArchived',
    'enableScriptExecutor', 'enableDuplicaterule', 'enableCheckrule', 'enableBusitype',
}

# 同步脚本 FIELD_MAP: JSON key → 新 dbc 列
FIELD_MAP = {
    'objectType': 'dbc_int_1',
    'iconId': 'dbc_varchar_1',
    'businessCategory': 'dbc_int_2',
    'customEntitySeq': 'dbc_bigint_1',
    'isActive': 'dbc_smallint_1',
    'isDetail': 'dbc_smallint_2',
    'enableHistoryLog': 'dbc_smallint_3',
    'dbTable': 'dbc_varchar_2',
    'enableDynamicFeed': 'dbc_smallint_4',
    'enableGroupMember': 'dbc_smallint_5',
    'isArchived': 'dbc_smallint_6',
    'enableScriptExecutor': 'dbc_smallint_7',
    'enableDuplicaterule': 'dbc_smallint_8',
    'enableCheckrule': 'dbc_smallint_9',
    'enableBusitype': 'dbc_smallint_10',
}

# JSON key → Entity 字段名映射（当 JSON key 和 Entity 字段名不同时）
JSON_TO_ENTITY = {
    'objectType': 'entityType',
    'iconId': 'svgApiKey',
    'isActive': 'enableFlg',
    'isDetail': 'detailFlg',
    'isCustom': 'customFlg',
}

# 固定列映射
FIXED = {'apiKey', 'label', 'labelKey', 'namespace', 'description',
         'isCustom', 'isDelete', 'createdAt', 'createdBy', 'updatedAt', 'updatedBy'}

# 丢弃
DROP = {'name', 'objectId', 'tenantId', 'metaModelId', 'id', 'deleteFlg'}

pg = psycopg2.connect(**PG)
c = pg.cursor()
c.execute("""
    SELECT i.api_key, i.db_column, i.data_type
    FROM p_meta_item i JOIN p_meta_model m ON m.id = i.metamodel_id
    WHERE m.api_key = 'xobject' AND i.tenant_id <= 0
          AND (i.delete_flg IS NULL OR i.delete_flg = 0)
    ORDER BY i.api_key
""")
items = c.fetchall()
pg.close()

print(f"{'meta_item.api_key':30s} {'old db_column':20s} {'new dbc':20s} {'Entity field':25s} {'状态'}")
print('-' * 120)

covered_entity_fields = set()
issues = []

for ak, dc, dt in items:
    entity_field = JSON_TO_ENTITY.get(ak, ak)
    new_dbc = FIELD_MAP.get(ak, '')
    
    if ak in DROP:
        status = '丢弃'
    elif ak in FIXED:
        status = '→ 固定列'
        if entity_field in ENTITY_BIZ_FIELDS:
            covered_entity_fields.add(entity_field)
    elif new_dbc:
        if entity_field in ENTITY_BIZ_FIELDS:
            status = f'✓ → {new_dbc}'
            covered_entity_fields.add(entity_field)
        else:
            status = f'⚠ dbc={new_dbc} 但 Entity 无 {entity_field}'
            issues.append(f'{ak}: 有 FIELD_MAP 但 Entity 无字段 {entity_field}')
    elif dc:
        status = f'⚠ 有 old db_column={dc} 但 FIELD_MAP 未覆盖'
        issues.append(f'{ak}: old db_column={dc} 未在 FIELD_MAP 中')
    else:
        if entity_field in ENTITY_BIZ_FIELDS:
            status = f'⚠ Entity 有字段但无 dbc 映射'
            issues.append(f'{ak}: Entity 有 {entity_field} 但无 FIELD_MAP')
        else:
            status = '无 db_column，无 Entity 字段（可忽略）'
    
    print(f'{str(ak):30s} {str(dc):20s} {str(new_dbc):20s} {str(entity_field):25s} {status}')

# Entity 中有但 meta_item 中没有的字段
meta_item_keys = {ak for ak, _, _ in items}
mapped_entity = set()
for ak in meta_item_keys:
    ef = JSON_TO_ENTITY.get(ak, ak)
    mapped_entity.add(ef)
# 加上固定列覆盖的
for ak in FIXED:
    ef = JSON_TO_ENTITY.get(ak, ak)
    mapped_entity.add(ef)

uncovered = ENTITY_BIZ_FIELDS - covered_entity_fields
# 检查 FIELD_MAP 中直接用 Entity 字段名的
for jk, dbc in FIELD_MAP.items():
    ef = JSON_TO_ENTITY.get(jk, jk)
    if ef in uncovered:
        uncovered.discard(ef)

print(f'\n=== Entity 字段覆盖检查 ===')
print(f'Entity 业务字段总数: {len(ENTITY_BIZ_FIELDS)}')
print(f'已覆盖: {len(ENTITY_BIZ_FIELDS - uncovered)}')
if uncovered:
    print(f'未覆盖 ({len(uncovered)}):')
    for f in sorted(uncovered):
        print(f'  ⚠ {f}')

if issues:
    print(f'\n=== 问题 ({len(issues)}) ===')
    for i in issues:
        print(f'  {i}')
else:
    print('\n✓ 无问题')
