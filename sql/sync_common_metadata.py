#!/usr/bin/env python3
"""
从老 PG (xsy_metarepo) 的 p_meta_common_metadata 同步到新 MySQL p_meta_common_metadata。

老库结构：id, tenant_id, namespace, api_key, label, label_key,
          metamodel_id(BIGINT), metadata_id, object_id(BIGINT),
          parent_metadata_id, metadata_json, meta_version, description,
          created_by, created_at, updated_by, updated_at, delete_flg

新库结构：api_key(PK), namespace, label, label_key,
          metamodel_api_key(VARCHAR), metadata_id, object_api_key(VARCHAR),
          parent_metadata_id, metadata_json, meta_version, description,
          created_by, created_at, updated_by, updated_at, delete_flg

转换规则：
- id, tenant_id → 丢弃
- metamodel_id → metamodel_api_key（查 p_meta_model id→api_key）
- object_id → object_api_key（查 p_custom_entity id→api_key）
- metadata_json 中的 ID 值 → api_key（按字段名识别转换）
- delete_flg=1 的不同步
"""
import psycopg2
import mysql.connector
import json
import csv
import os
from collections import defaultdict

PG = dict(host='10.65.2.6', port=5432, dbname='crm_cd_data',
          user='xsy_metarepo', password='sk29XGLI%iu88pF*', connect_timeout=15)
MY = dict(host='106.14.194.144', port=3306, user='root', password='MySql@888888')
OUTDIR = os.path.dirname(os.path.abspath(__file__))


def get_pg():
    return psycopg2.connect(**PG)


def get_my(db):
    return mysql.connector.connect(**MY, database=db)


# ==================== ID→api_key 映射 ====================

def build_id_map(pg, table, where="tenant_id <= 0 AND (delete_flg IS NULL OR delete_flg=0)"):
    cur = pg.cursor()
    cur.execute(f"SELECT id, api_key FROM {table} WHERE {where} AND api_key IS NOT NULL AND api_key != ''")
    return {r[0]: r[1] for r in cur.fetchall()}


def build_all_id_maps(pg):
    print("=== 构建 ID→api_key 映射 ===", flush=True)
    maps = {
        'meta_model': build_id_map(pg, 'p_meta_model'),
        'entity': build_id_map(pg, 'p_custom_entity'),
        'item': build_id_map(pg, 'p_custom_item'),
        'link': build_id_map(pg, 'p_custom_entity_link'),
    }
    for k, v in maps.items():
        print(f"  {k}: {len(v)} 条映射", flush=True)
    return maps


# ==================== metadata_json 中的 ID 字段转换规则 ====================

# JSON key → 查哪个映射表（值是 ID，需要转为 api_key）
JSON_ID_FIELDS = {
    'entityId': 'entity', 'objectId': 'entity',
    'parentEntityId': 'entity', 'childEntityId': 'entity',
    'referEntityId': 'entity', 'aggregateObjectId': 'entity',
    'itemId': 'item', 'checkErrorItemId': 'item',
    'aggregateItemId': 'item',
    'referLinkId': 'link', 'linkId': 'link',
    'aggregateLinkId': 'link',
    'svgId': None,  # 直接转字符串
}

# JSON key 需要丢弃（不写入新库的 metadata_json）
JSON_DROP_FIELDS = {'tenantId', 'metaModelId', 'id', 'deleteFlg'}


def convert_metadata_json(json_str, id_maps, mm_ak, api_key, warnings):
    """
    转换 metadata_json 中的 ID 值为 api_key。
    返回转换后的 JSON 字符串。
    """
    if not json_str:
        return json_str
    try:
        data = json.loads(json_str)
    except json.JSONDecodeError:
        warnings.append(f"[{mm_ak}] api_key={api_key} metadata_json 解析失败")
        return json_str

    new_data = {}
    for key, value in data.items():
        # 丢弃字段
        if key in JSON_DROP_FIELDS:
            continue

        # ID 转换字段
        if key in JSON_ID_FIELDS:
            map_name = JSON_ID_FIELDS[key]
            if value is not None and value != '' and value != 0:
                if map_name is None:
                    # svgId → 直接转字符串
                    new_data[key] = str(int(value)) if isinstance(value, (int, float)) else str(value)
                else:
                    converted = id_maps[map_name].get(int(value)) if isinstance(value, (int, float)) else None
                    if converted:
                        # 重命名：entityId→entityApiKey, itemId→itemApiKey 等
                        new_key = key.replace('Id', 'ApiKey')
                        new_data[new_key] = converted
                    else:
                        warnings.append(f"[{mm_ak}] api_key={api_key} JSON字段 {key}={value} 转换失败")
                        new_data[key] = value  # 保留原值
            continue

        # 普通字段 → 直接保留
        new_data[key] = value

    return json.dumps(new_data, ensure_ascii=False)


def convert_row(old_row, id_maps, warnings):
    """
    转换一行 p_meta_common_metadata 数据。
    old_row: dict（老库列名→值）
    返回 dict（新库列名→值），或 None（跳过）
    """
    # metamodel_id → metamodel_api_key
    mm_id = old_row.get('metamodel_id')
    mm_ak = id_maps['meta_model'].get(mm_id)
    if not mm_ak:
        return None

    api_key = old_row.get('api_key')
    label = old_row.get('label')

    # object_id → object_api_key
    obj_id = old_row.get('object_id')
    obj_ak = id_maps['entity'].get(obj_id) if obj_id else None
    if obj_id and not obj_ak:
        warnings.append(f"[{mm_ak}] api_key={api_key} object_id={obj_id} 转换失败")

    # 转换 metadata_json
    new_json = convert_metadata_json(
        old_row.get('metadata_json'), id_maps, mm_ak, api_key, warnings)

    return {
        'metamodel_api_key': mm_ak,
        'api_key': api_key or '',
        'namespace': old_row.get('namespace') or 'system',
        'label': label or '',
        'label_key': old_row.get('label_key'),
        'metadata_id': old_row.get('metadata_id'),
        'object_api_key': obj_ak,
        'parent_metadata_id': old_row.get('parent_metadata_id'),
        'metadata_json': new_json,
        'meta_version': old_row.get('meta_version'),
        'description': old_row.get('description'),
        'delete_flg': 0,
        'created_at': old_row.get('created_at'),
        'created_by': old_row.get('created_by'),
        'updated_at': old_row.get('updated_at'),
        'updated_by': old_row.get('updated_by'),
    }


# ==================== 同步主逻辑 ====================

NEW_COLUMNS = [
    'metamodel_api_key', 'api_key', 'namespace', 'label', 'label_key',
    'metadata_id', 'object_api_key', 'parent_metadata_id',
    'metadata_json', 'meta_version', 'description', 'delete_flg',
    'created_at', 'created_by', 'updated_at', 'updated_by',
]


def sync(pg, my):
    id_maps = build_all_id_maps(pg)

    # 查老库数据
    print("\n=== 查询老库 p_meta_common_metadata ===", flush=True)
    pc = pg.cursor()
    pc.execute("""
        SELECT id, tenant_id, namespace, api_key, label, label_key,
               metamodel_id, metadata_id, object_id, parent_metadata_id,
               metadata_json, meta_version, description,
               created_by, created_at, updated_by, updated_at, delete_flg
        FROM p_meta_common_metadata
        WHERE tenant_id <= 0 AND (delete_flg IS NULL OR delete_flg = 0)
        ORDER BY metamodel_id, api_key
    """)
    old_cols = [desc[0] for desc in pc.description]
    old_rows = pc.fetchall()
    print(f"  老库数据量: {len(old_rows)} 行", flush=True)

    # 转换
    stats = defaultdict(lambda: {'total': 0, 'ok': 0, 'skip': 0, 'warn': 0})
    all_warnings = []
    new_rows = []

    for old_tuple in old_rows:
        old_dict = dict(zip(old_cols, old_tuple))
        mm_id = old_dict.get('metamodel_id')
        mm_ak = id_maps['meta_model'].get(mm_id, f'unknown_{mm_id}')
        stats[mm_ak]['total'] += 1

        row_warnings = []
        new_row = convert_row(old_dict, id_maps, row_warnings)

        if new_row is None:
            stats[mm_ak]['skip'] += 1
            continue

        if row_warnings:
            stats[mm_ak]['warn'] += len(row_warnings)
            all_warnings.extend(row_warnings)

        new_rows.append(new_row)
        stats[mm_ak]['ok'] += 1

    # 统计
    print(f"\n=== 转换统计 ===", flush=True)
    total_ok = 0
    for mm_ak in sorted(stats.keys()):
        s = stats[mm_ak]
        total_ok += s['ok']
        print(f"  {mm_ak}: 总计={s['total']}, 成功={s['ok']}, 跳过={s['skip']}, 警告={s['warn']}", flush=True)
    print(f"  合计成功: {total_ok}", flush=True)

    # 输出警告到文件
    if all_warnings:
        warn_file = os.path.join(OUTDIR, 'sync_warnings.txt')
        with open(warn_file, 'w') as f:
            for w in all_warnings:
                f.write(w + '\n')
        print(f"\n  警告 {len(all_warnings)} 条，已输出到 {warn_file}", flush=True)

    # 写入新库
    if not new_rows:
        print("\n无数据可写入", flush=True)
        return

    print(f"\n=== 写入新库 ({len(new_rows)} 行) ===", flush=True)
    mc = my.cursor()
    placeholders = ', '.join(['%s'] * len(NEW_COLUMNS))
    col_str = ', '.join(NEW_COLUMNS)
    sql = f"INSERT IGNORE INTO p_meta_common_metadata ({col_str}) VALUES ({placeholders})"

    batch_size = 500
    inserted = 0
    for i in range(0, len(new_rows), batch_size):
        chunk = [tuple(r.get(c) for c in NEW_COLUMNS) for r in new_rows[i:i + batch_size]]
        mc.executemany(sql, chunk)
        my.commit()
        inserted += len(chunk)
        if inserted % 5000 == 0 or inserted == len(new_rows):
            print(f"  已写入: {inserted}/{len(new_rows)}", flush=True)

    # 验证
    print(f"\n=== 写入验证 ===", flush=True)
    mc.execute("""
        SELECT metamodel_api_key, COUNT(*)
        FROM p_meta_common_metadata
        GROUP BY metamodel_api_key
        ORDER BY COUNT(*) DESC
    """)
    db_counts = {r[0]: r[1] for r in mc.fetchall()}
    mismatch = 0
    for mm_ak in sorted(stats.keys()):
        expected = stats[mm_ak]['ok']
        actual = db_counts.get(mm_ak, 0)
        mark = "✓" if actual == expected else f"✗ (期望{expected})"
        if actual != expected:
            mismatch += 1
        print(f"  {mm_ak}: {actual} {mark}", flush=True)

    mc.execute("SELECT COUNT(*) FROM p_meta_common_metadata")
    total_db = mc.fetchone()[0]
    print(f"  总计: {total_db} (期望 {total_ok})", flush=True)

    if mismatch > 0:
        print(f"\n  ⚠ {mismatch} 个 metamodel 数量不匹配（可能有主键冲突去重）", flush=True)

    # 输出结果到 CSV
    result_file = os.path.join(OUTDIR, 'sync_result.csv')
    with open(result_file, 'w', newline='') as f:
        w = csv.writer(f)
        w.writerow(['metamodel_api_key', 'old_total', 'converted', 'skipped', 'warnings', 'db_actual', 'match'])
        for mm_ak in sorted(stats.keys()):
            s = stats[mm_ak]
            actual = db_counts.get(mm_ak, 0)
            w.writerow([mm_ak, s['total'], s['ok'], s['skip'], s['warn'], actual,
                        'Y' if actual == s['ok'] else 'N'])
    print(f"  结果已输出到 {result_file}", flush=True)


def main():
    print("=" * 60, flush=True)
    print("p_meta_common_metadata 数据同步: PG → MySQL", flush=True)
    print("=" * 60, flush=True)

    pg = get_pg()
    my = get_my('paas_metarepo_common')

    try:
        sync(pg, my)
    finally:
        pg.close()
        my.close()

    print("\nDONE", flush=True)


if __name__ == '__main__':
    main()
