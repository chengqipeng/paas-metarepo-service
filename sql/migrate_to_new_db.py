#!/usr/bin/env python3
"""
从老 PG (xsy_metarepo) 迁移 Common 级数据到新 MySQL 双库
转换规则：
- 去掉 id 列（新表用 api_key 做主键）
- 去掉 tenant_id 列（Common 表无 tenant_id）
- ID 关联转为 api_key 关联
- 校验 api_key 和 label 不为空
"""
import psycopg2
import mysql.connector
import sys

PG = dict(host='10.65.2.6', port=5432, dbname='crm_cd_data',
          user='xsy_metarepo', password='sk29XGLI%iu88pF*', connect_timeout=15)
MY = dict(host='106.14.194.144', port=3306, user='root',
          password='MySql@888888')

def get_pg():
    return psycopg2.connect(**PG)

def get_my(db):
    return mysql.connector.connect(**MY, database=db)

def migrate_meta_model(pg, my):
    """p_meta_model -> paas_metarepo_common.p_meta_model"""
    print("\n=== p_meta_model ===")
    pc = pg.cursor()
    mc = my.cursor()
    pc.execute("SELECT api_key, label, label_key, metamodel_type, "
               "enable_package, enable_app, enable_deprecation, enable_deactivation, "
               "enable_delta, enable_log, delta_scope, delta_mode, "
               "enable_module_control, db_table, description, "
               "delete_flg, xobject_dependency, visible, "
               "created_by, created_at, updated_by, updated_at "
               "FROM p_meta_model WHERE tenant_id <= 0 AND api_key IS NOT NULL AND api_key != ''")
    rows = pc.fetchall()
    skipped = 0
    inserted = 0
    for r in rows:
        api_key, label = r[0], r[1]
        if not api_key or not label:
            skipped += 1
            continue
        try:
            mc.execute("INSERT IGNORE INTO p_meta_model "
                       "(api_key, namespace, label, label_key, metamodel_type, "
                       "enable_package, enable_app, enable_deprecation, enable_deactivation, "
                       "enable_delta, enable_log, delta_scope, delta_mode, "
                       "enable_module_control, db_table, description, "
                       "delete_flg, xobject_dependency, visible, "
                       "created_by, created_at, updated_by, updated_at) "
                       "VALUES (%s,'system',%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s)", r)
            inserted += 1
        except Exception as e:
            skipped += 1
    my.commit()
    print(f"  inserted={inserted}, skipped={skipped}")

def build_id_to_apikey_map(pg, table, where="tenant_id <= 0"):
    """构建 id -> api_key 映射表"""
    pc = pg.cursor()
    pc.execute(f"SELECT id, api_key FROM {table} WHERE {where} AND api_key IS NOT NULL")
    return {r[0]: r[1] for r in pc.fetchall()}

def migrate_meta_item(pg, my):
    """p_meta_item -> paas_metarepo_common.p_meta_item (metamodel_id -> metamodel_api_key)"""
    print("\n=== p_meta_item ===")
    mm_map = build_id_to_apikey_map(pg, 'p_meta_model')
    pc = pg.cursor()
    mc = my.cursor()
    pc.execute("SELECT metamodel_id, api_key, label, label_key, item_type, data_type, "
               "item_order, require_flg, unique_key_flg, creatable, updatable, "
               "enable_package, enable_delta, enable_log, db_column, description, "
               "min_length, max_length, text_format, json_schema, name_field, delete_flg, "
               "created_by, created_at, updated_by, updated_at "
               "FROM p_meta_item WHERE tenant_id <= 0 AND api_key IS NOT NULL")
    rows = pc.fetchall()
    inserted = skipped = 0
    for r in rows:
        mm_id, api_key, label = r[0], r[1], r[2]
        mm_ak = mm_map.get(mm_id)
        if not mm_ak or not api_key or not label:
            skipped += 1
            continue
        vals = (mm_ak,) + r[1:]
        try:
            mc.execute("INSERT IGNORE INTO p_meta_item "
                       "(metamodel_api_key, api_key, label, label_key, item_type, data_type, "
                       "item_order, require_flg, unique_key_flg, creatable, updatable, "
                       "enable_package, enable_delta, enable_log, db_column, description, "
                       "min_length, max_length, text_format, json_schema, name_field, delete_flg, "
                       "created_by, created_at, updated_by, updated_at) "
                       "VALUES (%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s)", vals)
            inserted += 1
        except: skipped += 1
    my.commit()
    print(f"  inserted={inserted}, skipped={skipped}")

def migrate_meta_option(pg, my):
    """p_meta_option -> paas_metarepo_common.p_meta_option"""
    print("\n=== p_meta_option ===")
    mm_map = build_id_to_apikey_map(pg, 'p_meta_model')
    mi_map = build_id_to_apikey_map(pg, 'p_meta_item')
    pc = pg.cursor()
    mc = my.cursor()
    pc.execute("SELECT metamodel_id, item_id, option_code, option_label, "
               "label_key, option_order, default_flg, description, "
               "created_at, created_by, updated_at, updated_by, delete_flg, api_key "
               "FROM p_meta_option WHERE tenant_id <= 0")
    rows = pc.fetchall()
    inserted = skipped = 0
    for r in rows:
        mm_ak = mm_map.get(r[0])
        mi_ak = mi_map.get(r[1])
        if not mm_ak or not mi_ak or r[2] is None:
            skipped += 1
            continue
        try:
            mc.execute("INSERT IGNORE INTO p_meta_option "
                       "(metamodel_api_key, item_api_key, option_code, option_label, "
                       "option_label_key, option_order, default_flg, description, "
                       "created_at, created_by, updated_at, updated_by, delete_flg) "
                       "VALUES (%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s)",
                       (mm_ak, mi_ak, r[2], r[3], r[4], r[5], r[6], r[7], r[8], r[9], r[10], r[11], r[12]))
            inserted += 1
        except: skipped += 1
    my.commit()
    print(f"  inserted={inserted}, skipped={skipped}")

def migrate_common_entity(pg, my):
    """p_custom_entity (common) -> paas_metarepo_common.p_common_entity"""
    print("\n=== p_common_entity ===")
    pc = pg.cursor()
    mc = my.cursor()
    pc.execute("SELECT api_key, label, label_key, name, object_type, "
               "svg_id, svg_color, description, custom_entityseq, "
               "delete_flg, enable_flg, custom_flg, business_category, "
               "type_property, db_table, detail_flg, "
               "enable_team, enable_social, enable_config, hidden_flg, "
               "searchable, enable_sharing, enable_script_trigger, enable_activity, "
               "enable_history_log, enable_report, enable_refer, enable_api, "
               "enable_flow, enable_package, extend_property, "
               "created_at, created_by, updated_at, updated_by, name_space "
               "FROM p_custom_entity WHERE tenant_id <= 0 AND api_key IS NOT NULL AND label IS NOT NULL")
    rows = pc.fetchall()
    inserted = skipped = 0
    for r in rows:
        api_key, label = r[0], r[1]
        if not api_key or not label:
            skipped += 1
            continue
        ns = r[35] if r[35] else 'system'
        try:
            mc.execute("INSERT IGNORE INTO p_common_entity "
                       "(api_key, namespace, label, label_key, name, object_type, "
                       "svg_id, svg_color, description, custom_entityseq, "
                       "delete_flg, enable_flg, custom_flg, business_category, "
                       "type_property, db_table, detail_flg, "
                       "enable_team, enable_social, enable_config, hidden_flg, "
                       "searchable, enable_sharing, enable_script_trigger, enable_activity, "
                       "enable_history_log, enable_report, enable_refer, enable_api, "
                       "enable_flow, enable_package, extend_property, "
                       "created_at, created_by, updated_at, updated_by) "
                       "VALUES (%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s)",
                       (api_key, ns) + r[1:35])
            inserted += 1
        except: skipped += 1
    my.commit()
    print(f"  inserted={inserted}, skipped={skipped}")

def migrate_common_item(pg, my):
    """p_custom_item (common) -> paas_metarepo_common.p_common_item (entity_id -> entity_api_key)"""
    print("\n=== p_common_item ===")
    entity_map = build_id_to_apikey_map(pg, 'p_custom_entity')
    pc = pg.cursor()
    mc = my.cursor()
    pc.execute("SELECT entity_id, api_key, label, label_key, name, "
               "item_type, data_type, type_property, help_text, help_text_key, "
               "description, custom_itemseq, default_value, "
               "require_flg, delete_flg, custom_flg, enable_flg, "
               "creatable, updatable, unique_key_flg, "
               "enable_history_log, enable_config, enable_package, "
               "readonly_status, visible_status, hidden_flg, "
               "refer_entity_id, db_column, item_order, sort_flg, column_name, "
               "created_at, created_by, updated_at, updated_by "
               "FROM p_custom_item WHERE tenant_id <= 0 AND api_key IS NOT NULL AND label IS NOT NULL")
    rows = pc.fetchall()
    inserted = skipped = 0
    for r in rows:
        ent_ak = entity_map.get(r[0])
        ref_ent_ak = entity_map.get(r[26]) if r[26] else None
        if not ent_ak or not r[1] or not r[2]:
            skipped += 1
            continue
        try:
            mc.execute("INSERT IGNORE INTO p_common_item "
                       "(entity_api_key, api_key, namespace, label, label_key, name, "
                       "item_type, data_type, type_property, help_text, help_text_key, "
                       "description, custom_itemseq, default_value, "
                       "require_flg, delete_flg, custom_flg, enable_flg, "
                       "creatable, updatable, unique_key_flg, "
                       "enable_history_log, enable_config, enable_package, "
                       "readonly_status, visible_status, hidden_flg, "
                       "refer_entity_api_key, db_column, item_order, sort_flg, column_name, "
                       "created_at, created_by, updated_at, updated_by) "
                       "VALUES (%s,%s,'system',%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s)",
                       (ent_ak, r[1], r[2], r[3], r[4], r[5], r[6], r[7], r[8], r[9],
                        r[10], r[11], r[12], r[13], r[14], r[15], r[16], r[17], r[18], r[19],
                        r[20], r[21], r[22], r[23], r[24], r[25],
                        ref_ent_ak, r[27], r[28], r[29], r[30], r[31], r[32], r[33], r[34]))
            inserted += 1
        except: skipped += 1
    my.commit()
    print(f"  inserted={inserted}, skipped={skipped}")

def main():
    pg = get_pg()
    my_common = get_my('paas_metarepo_common')
    print("Starting migration from xsy_metarepo to new MySQL...")
    migrate_meta_model(pg, my_common)
    migrate_meta_item(pg, my_common)
    migrate_meta_option(pg, my_common)
    migrate_common_entity(pg, my_common)
    migrate_common_item(pg, my_common)
    # Verify
    mc = my_common.cursor()
    for t in ['p_meta_model','p_meta_item','p_meta_option','p_common_entity','p_common_item']:
        mc.execute(f"SELECT COUNT(*) FROM {t}")
        print(f"  {t}: {mc.fetchone()[0]}")
    pg.close()
    my_common.close()
    print("\nDone!")

if __name__ == '__main__':
    main()
