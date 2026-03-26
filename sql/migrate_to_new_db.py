#!/usr/bin/env python3
"""
从老 PG (xsy_metarepo) 迁移 Common 级数据到新 MySQL 双库
规则：
- 去掉 id 列（新表用 api_key 联合主键）
- 去掉 tenant_id 列（Common 表无 tenant_id）
- ID 关联转为 api_key 关联
- delete_flg=1 的不同步
- api_key 和 label 不允许为空
- p_meta_item 中 metaModelId->metamodelApiKey, entityId/objectId->entityApiKey,
  tenantId 删除, itemId->itemApiKey
"""
import psycopg2
import mysql.connector
import sys

PG = dict(host='10.65.2.6', port=5432, dbname='crm_cd_data',
          user='xsy_metarepo', password='sk29XGLI%iu88pF*', connect_timeout=15)
MY = dict(host='106.14.194.144', port=3306, user='root', password='MySql@888888')

def get_pg():
    return psycopg2.connect(**PG)

def get_my(db):
    return mysql.connector.connect(**MY, database=db)

def build_map(pg, table, where="tenant_id <= 0 AND (delete_flg IS NULL OR delete_flg=0)"):
    """构建 id -> api_key 映射"""
    cur = pg.cursor()
    cur.execute(f"SELECT id, api_key FROM {table} WHERE {where} AND api_key IS NOT NULL AND api_key != ''")
    return {r[0]: r[1] for r in cur.fetchall()}

def migrate_meta_model(pg, my):
    print("\n=== p_meta_model ===", flush=True)
    pc = pg.cursor()
    mc = my.cursor()
    pc.execute("SELECT api_key, label, label_key, metamodel_type, "
               "enable_package, enable_app, enable_deprecation, enable_deactivation, "
               "enable_delta, enable_log, delta_scope, delta_mode, "
               "enable_module_control, db_table, description, "
               "delete_flg, xobject_dependency, visible, "
               "created_by, created_at, updated_by, updated_at "
               "FROM p_meta_model WHERE tenant_id<=0 AND (delete_flg IS NULL OR delete_flg=0) "
               "AND api_key IS NOT NULL AND api_key!='' AND label IS NOT NULL AND label!=''")
    rows = pc.fetchall()
    batch = [('system',) + r for r in rows]
    mc.executemany("INSERT IGNORE INTO p_meta_model "
                   "(namespace, api_key, label, label_key, metamodel_type, "
                   "enable_package, enable_app, enable_deprecation, enable_deactivation, "
                   "enable_delta, enable_log, delta_scope, delta_mode, "
                   "enable_module_control, db_table, description, "
                   "delete_flg, xobject_dependency, visible, "
                   "created_by, created_at, updated_by, updated_at) "
                   "VALUES (%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s)", batch)
    my.commit()
    print(f"  inserted: {len(batch)}", flush=True)

def migrate_meta_item(pg, my):
    """转换规则：metamodel_id->metamodel_api_key, 跳过 delete_flg=1,
    后处理：metaModelId->metamodelApiKey, entityId/objectId->entityApiKey, 删tenantId, itemId->itemApiKey"""
    print("\n=== p_meta_item ===", flush=True)
    mm_map = build_map(pg, 'p_meta_model')
    pc = pg.cursor()
    mc = my.cursor()
    pc.execute("SELECT metamodel_id, api_key, label, label_key, item_type, data_type, "
               "item_order, require_flg, unique_key_flg, creatable, updatable, "
               "enable_package, enable_delta, enable_log, db_column, description, "
               "min_length, max_length, text_format, json_schema, name_field, delete_flg, "
               "created_by, created_at, updated_by, updated_at "
               "FROM p_meta_item WHERE tenant_id<=0 AND (delete_flg IS NULL OR delete_flg=0) "
               "AND api_key IS NOT NULL AND api_key!='' AND label IS NOT NULL AND label!=''")
    rows = pc.fetchall()
    batch = []
    skipped = 0
    for r in rows:
        mm_ak = mm_map.get(r[0])
        api_key = r[1]
        db_col = r[14]
        if not mm_ak:
            skipped += 1
            continue
        # 转换 ID 类字段名
        if api_key == 'metaModelId' and db_col == 'metamodel_id':
            api_key = 'metamodelApiKey'
        elif api_key in ('entityId','objectId') and db_col in ('entity_id','object_id'):
            api_key = 'entityApiKey'
        elif api_key == 'tenantId' and db_col == 'tenant_id':
            skipped += 1
            continue  # 跳过 tenantId
        elif api_key == 'itemId' and db_col == 'item_id':
            api_key = 'itemApiKey'
        batch.append((mm_ak, api_key) + r[2:])
    mc.executemany("INSERT IGNORE INTO p_meta_item "
                   "(metamodel_api_key, api_key, label, label_key, item_type, data_type, "
                   "item_order, require_flg, unique_key_flg, creatable, updatable, "
                   "enable_package, enable_delta, enable_log, db_column, description, "
                   "min_length, max_length, text_format, json_schema, name_field, delete_flg, "
                   "created_by, created_at, updated_by, updated_at) "
                   "VALUES (%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s)", batch)
    my.commit()
    print(f"  inserted: {len(batch)}, skipped: {skipped}", flush=True)

def migrate_meta_option(pg, my):
    print("\n=== p_meta_option ===", flush=True)
    mm_map = build_map(pg, 'p_meta_model')
    pc = pg.cursor()
    # item_id -> item_api_key, derive metamodel_api_key via item->metamodel
    pc.execute("SELECT id, api_key, metamodel_id FROM p_meta_item WHERE tenant_id<=0 AND api_key IS NOT NULL")
    mi_rows = pc.fetchall()
    mi_map = {r[0]: r[1] for r in mi_rows}
    mi_to_mm = {r[0]: r[2] for r in mi_rows}
    mc = my.cursor()
    pc.execute("SELECT item_id, option_code, option_label, label_key, option_order, default_flg, "
               "description, created_at, created_by, updated_at, updated_by, delete_flg "
               "FROM p_meta_option WHERE tenant_id<=0 AND (delete_flg IS NULL OR delete_flg=0)")
    rows = pc.fetchall()
    batch = []
    for r in rows:
        mi_ak = mi_map.get(r[0])
        mm_id = mi_to_mm.get(r[0])
        mm_ak = mm_map.get(mm_id) if mm_id else None
        if mm_ak and mi_ak and r[1] is not None:
            batch.append((mm_ak, mi_ak) + r[1:])
    mc.executemany("INSERT IGNORE INTO p_meta_option "
                   "(metamodel_api_key, item_api_key, option_code, option_label, option_label_key, "
                   "option_order, default_flg, description, created_at, created_by, updated_at, updated_by, delete_flg) "
                   "VALUES (%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s)", batch)
    my.commit()
    print(f"  inserted: {len(batch)}", flush=True)

def migrate_common_entity(pg, my):
    print("\n=== p_common_entity ===", flush=True)
    pc = pg.cursor()
    mc = my.cursor()
    pc.execute("SELECT api_key, label, label_key, name, object_type, svg_id, svg_color, "
               "description, custom_entityseq, delete_flg, enable_flg, custom_flg, "
               "business_category, type_property, db_table, detail_flg, "
               "enable_team, enable_social, enable_config, hidden_flg, searchable, "
               "enable_sharing, enable_script_trigger, enable_activity, "
               "enable_history_log, enable_report, enable_refer, enable_api, "
               "enable_flow, enable_package, extend_property, "
               "created_at, created_by, updated_at, updated_by "
               "FROM p_custom_entity WHERE tenant_id<=0 AND (delete_flg IS NULL OR delete_flg=0) "
               "AND api_key IS NOT NULL AND api_key!='' AND label IS NOT NULL AND label!=''")
    rows = pc.fetchall()
    batch = [(r[0], 'system') + r[1:] for r in rows]
    mc.executemany("INSERT IGNORE INTO p_common_entity "
                   "(api_key, namespace, label, label_key, name, object_type, svg_id, svg_color, "
                   "description, custom_entityseq, delete_flg, enable_flg, custom_flg, "
                   "business_category, type_property, db_table, detail_flg, "
                   "enable_team, enable_social, enable_config, hidden_flg, searchable, "
                   "enable_sharing, enable_script_trigger, enable_activity, "
                   "enable_history_log, enable_report, enable_refer, enable_api, "
                   "enable_flow, enable_package, extend_property, "
                   "created_at, created_by, updated_at, updated_by) "
                   "VALUES (%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s)", batch)
    my.commit()
    print(f"  inserted: {len(batch)}", flush=True)

def migrate_common_item(pg, my):
    print("\n=== p_common_item ===", flush=True)
    ce_map = build_map(pg, 'p_custom_entity')
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
               "FROM p_custom_item WHERE tenant_id<=0 AND (delete_flg IS NULL OR delete_flg=0) "
               "AND api_key IS NOT NULL AND api_key!='' AND label IS NOT NULL AND label!=''")
    rows = pc.fetchall()
    batch = []
    for r in rows:
        ent_ak = ce_map.get(r[0])
        ref_ak = ce_map.get(r[26]) if r[26] else None
        if not ent_ak:
            continue
        batch.append((ent_ak, r[1], 'system') + r[2:26] + (ref_ak,) + r[27:])
    mc.executemany("INSERT IGNORE INTO p_common_item "
                   "(entity_api_key, api_key, namespace, label, label_key, name, "
                   "item_type, data_type, type_property, help_text, help_text_key, "
                   "description, custom_itemseq, default_value, "
                   "require_flg, delete_flg, custom_flg, enable_flg, "
                   "creatable, updatable, unique_key_flg, "
                   "enable_history_log, enable_config, enable_package, "
                   "readonly_status, visible_status, hidden_flg, "
                   "refer_entity_api_key, db_column, item_order, sort_flg, column_name, "
                   "created_at, created_by, updated_at, updated_by) "
                   "VALUES (%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s)", batch)
    my.commit()
    print(f"  inserted: {len(batch)}", flush=True)

def main():
    pg = get_pg()
    my = get_my('paas_metarepo_common')
    print("=== Migration: xsy_metarepo -> paas_metarepo_common ===", flush=True)
    migrate_meta_model(pg, my)
    migrate_meta_item(pg, my)
    migrate_meta_option(pg, my)
    migrate_common_entity(pg, my)
    migrate_common_item(pg, my)
    mc = my.cursor()
    print("\nFinal counts:", flush=True)
    for t in ['p_meta_model','p_meta_item','p_meta_option','p_common_entity','p_common_item']:
        mc.execute(f"SELECT COUNT(*) FROM {t}")
        print(f"  {t}: {mc.fetchone()[0]}", flush=True)
    pg.close()
    my.close()
    print("DONE", flush=True)

if __name__ == '__main__':
    main()
