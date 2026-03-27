#!/usr/bin/env python3
"""
从老 PG (xsy_metarepo) 的 p_common_metadata 同步到新 MySQL p_common_metadata。

核心转换：
- id/tenant_id → 丢弃
- metamodel_id → metamodel_api_key（查 p_meta_model id→api_key）
- parent_object_id → parent_entity_api_key（查 p_custom_entity id→api_key）
- dbc_xxx_N 中的 ID 值 → api_key（按 p_meta_item 定义逐列识别转换）
- 被删除的模型字段（tenantId, metaModelId, name, nameKey 等）→ 置 NULL
"""
import psycopg2
import mysql.connector
import sys
import json
from collections import defaultdict

PG = dict(host='10.65.2.6', port=5432, dbname='crm_cd_data',
          user='xsy_metarepo', password='sk29XGLI%iu88pF*', connect_timeout=15)
MY = dict(host='106.14.194.144', port=3306, user='root', password='MySql@888888')


def get_pg():
    return psycopg2.connect(**PG)


def get_my(db):
    return mysql.connector.connect(**MY, database=db)


# ==================== ID→api_key 映射构建 ====================

def build_id_map(pg, table, where="tenant_id <= 0 AND (delete_flg IS NULL OR delete_flg=0)"):
    cur = pg.cursor()
    cur.execute(f"SELECT id, api_key FROM {table} WHERE {where} AND api_key IS NOT NULL AND api_key != ''")
    return {r[0]: r[1] for r in cur.fetchall()}

def build_all_id_maps(pg):
    """构建所有需要的 id→api_key 映射"""
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

# ==================== p_meta_item 列映射分析 ====================

# 需要 ID→api_key 转换的老字段名 → 查哪个映射表
ID_CONVERT_FIELDS = {
    'entityId': 'entity', 'objectId': 'entity',
    'parentEntityId': 'entity', 'childEntityId': 'entity',
    'referEntityId': 'entity',
    'itemId': 'item',
    'checkErrorItemId': 'item',
    'referLinkId': 'link', 'linkId': 'link',
    'svgId': 'entity',  # svgId 直接转字符串
}

# 需要丢弃的老字段名（不写入新库的 dbc 列）
DROP_FIELDS = {'tenantId', 'metaModelId', 'name', 'nameKey', 'optionCode', 'optionLabel'}

# entityId/objectId 类字段 → 转到固定列 parent_entity_api_key（不写 dbc）
PARENT_ENTITY_FIELDS = {'entityId', 'objectId'}

def load_meta_items(pg):
    """
    从老 PG 加载 p_meta_item，按 metamodel_id 分组。
    返回 {metamodel_id: [{api_key, db_column, ...}, ...]}
    """
    cur = pg.cursor()
    cur.execute("""
        SELECT metamodel_id, api_key, db_column
        FROM p_meta_item
        WHERE tenant_id <= 0 AND (delete_flg IS NULL OR delete_flg = 0)
              AND api_key IS NOT NULL AND db_column IS NOT NULL
    """)
    result = defaultdict(list)
    for mm_id, api_key, db_col in cur.fetchall():
        result[mm_id].append({'api_key': api_key, 'db_column': db_col})
    return dict(result)


# ==================== dbc 列名列表 ====================

DBC_COLUMNS = []
for prefix, count in [('dbc_varchar_', 20), ('dbc_textarea_', 10), ('dbc_select_', 10),
                       ('dbc_integer_', 10), ('dbc_real_', 10), ('dbc_date_', 10),
                       ('dbc_relation_', 10), ('dbc_tinyint_', 10)]:
    for i in range(1, count + 1):
        DBC_COLUMNS.append(f"{prefix}{i}")

# 新库 p_common_metadata 的固定列
FIXED_COLUMNS = [
    'metamodel_api_key', 'api_key', 'namespace', 'parent_entity_api_key',
    'label', 'label_key', 'custom_flg', 'metadata_order', 'owner_api_key',
    'description', 'delete_flg',
    'created_at', 'created_by', 'updated_at', 'updated_by',
]

ALL_NEW_COLUMNS = FIXED_COLUMNS + DBC_COLUMNS


# ==================== 行转换逻辑 ====================

def convert_row(old_row, old_cols, metamodel_id, meta_items, id_maps):
    """
    将老库一行 p_common_metadata 转换为新库格式。
    
    old_row: tuple，老库查询结果
    old_cols: list[str]，老库列名
    metamodel_id: int，老库 metamodel_id
    meta_items: list[dict]，该 metamodel 的 p_meta_item 定义
    id_maps: dict，所有 id→api_key 映射
    
    返回 dict（新库列名→值），或 None（跳过）
    """
    old = dict(zip(old_cols, old_row))
    
    # metamodel_id → metamodel_api_key
    mm_ak = id_maps['meta_model'].get(metamodel_id)
    if not mm_ak:
        return None  # 未知 metamodel，跳过
    
    # 基础校验
    api_key = old.get('api_key')
    label = old.get('label')
    if not api_key or not label:
        return None
    
    # parent_object_id → parent_entity_api_key
    parent_obj_id = old.get('parent_object_id')
    parent_entity_ak = None
    if parent_obj_id:
        parent_entity_ak = id_maps['entity'].get(int(parent_obj_id)) if parent_obj_id else None
    
    # owner_id → owner_api_key（暂时直接转字符串，如有 module 表可查映射）
    owner_id = old.get('owner_id')
    owner_ak = str(owner_id) if owner_id else None
    
    new_row = {
        'metamodel_api_key': mm_ak,
        'api_key': api_key,
        'namespace': old.get('namespace') or 'system',
        'parent_entity_api_key': parent_entity_ak,
        'label': label,
        'label_key': old.get('label_key'),
        'custom_flg': old.get('custom_flg'),
        'metadata_order': old.get('metadata_order'),
        'owner_api_key': owner_ak,
        'description': old.get('description'),
        'delete_flg': 0,
        'created_at': old.get('created_at'),
        'created_by': old.get('created_by'),
        'updated_at': old.get('updated_at'),
        'updated_by': old.get('updated_by'),
    }
    
    # 构建 db_column → meta_item 映射
    col_to_item = {item['db_column']: item for item in meta_items}
    
    # 处理每个 dbc_xxx_N 列
    for dbc_col in DBC_COLUMNS:
        old_val = old.get(dbc_col)
        item = col_to_item.get(dbc_col)
        
        if item is None:
            # 该列在 p_meta_item 中无定义，直接复制
            new_row[dbc_col] = old_val
            continue
        
        field_api_key = item['api_key']
        
        # 丢弃字段 → 置 NULL
        if field_api_key in DROP_FIELDS:
            new_row[dbc_col] = None
            continue
        
        # entityId/objectId → 已转到固定列 parent_entity_api_key，dbc 列也做转换
        if field_api_key in PARENT_ENTITY_FIELDS:
            if old_val:
                new_row[dbc_col] = id_maps['entity'].get(int(old_val))
            else:
                new_row[dbc_col] = None
            continue
        
        # ID 转换字段
        if field_api_key in ID_CONVERT_FIELDS:
            if old_val:
                map_name = ID_CONVERT_FIELDS[field_api_key]
                if field_api_key == 'svgId':
                    new_row[dbc_col] = str(int(old_val)) if old_val else None
                else:
                    new_row[dbc_col] = id_maps[map_name].get(int(old_val))
            else:
                new_row[dbc_col] = None
            continue
        
        # 普通字段 → 直接复制
        new_row[dbc_col] = old_val
    
    return new_row


# ==================== 数据验证 ====================

def validate_row(new_row, mm_ak, warnings):
    """验证转换后的行数据，收集警告"""
    if not new_row.get('api_key'):
        warnings.append(f"[{mm_ak}] api_key 为空")
        return False
    if not new_row.get('label'):
        warnings.append(f"[{mm_ak}] api_key={new_row['api_key']} label 为空")
        return False
    if not new_row.get('metamodel_api_key'):
        warnings.append(f"[{mm_ak}] metamodel_api_key 为空")
        return False
    return True


def validate_id_conversions(new_row, old_row, old_cols, meta_items, id_maps, warnings):
    """验证 ID→api_key 转换是否成功（有值但转换后为 NULL 说明映射缺失）"""
    old = dict(zip(old_cols, old_row))
    col_to_item = {item['db_column']: item for item in meta_items}
    mm_ak = new_row['metamodel_api_key']
    ak = new_row['api_key']
    
    for dbc_col in DBC_COLUMNS:
        item = col_to_item.get(dbc_col)
        if not item:
            continue
        field_api_key = item['api_key']
        if field_api_key in ID_CONVERT_FIELDS and field_api_key not in DROP_FIELDS:
            old_val = old.get(dbc_col)
            new_val = new_row.get(dbc_col)
            if old_val and not new_val:
                warnings.append(
                    f"[{mm_ak}] api_key={ak} 字段 {field_api_key}({dbc_col}) "
                    f"ID={old_val} 转换失败（映射缺失）"
                )
    
    # parent_object_id 转换验证
    parent_obj_id = old.get('parent_object_id')
    if parent_obj_id and not new_row.get('parent_entity_api_key'):
        warnings.append(
            f"[{mm_ak}] api_key={ak} parent_object_id={parent_obj_id} 转换失败"
        )


# ==================== 同步主逻辑 ====================

def sync_common_metadata(pg, my):
    id_maps = build_all_id_maps(pg)
    meta_items_by_mm = load_meta_items(pg)
    
    print(f"\n=== p_meta_item 元模型数: {len(meta_items_by_mm)} ===", flush=True)
    for mm_id, items in meta_items_by_mm.items():
        mm_ak = id_maps['meta_model'].get(mm_id, f'?{mm_id}')
        print(f"  {mm_ak}: {len(items)} 个字段", flush=True)
    
    # 查老库 p_common_metadata 的列名
    pc = pg.cursor()
    pc.execute("""
        SELECT column_name FROM information_schema.columns
        WHERE table_schema = 'xsy_metarepo' AND table_name = 'p_common_metadata'
        ORDER BY ordinal_position
    """)
    old_cols = [r[0] for r in pc.fetchall()]
    # 如果老库表名不是 p_common_metadata，尝试 p_meta_metamodel_data
    if not old_cols:
        pc.execute("""
            SELECT column_name FROM information_schema.columns
            WHERE table_schema = 'xsy_metarepo' AND table_name = 'p_meta_metamodel_data'
            ORDER BY ordinal_position
        """)
        old_cols = [r[0] for r in pc.fetchall()]
        old_table = 'p_meta_metamodel_data'
    else:
        old_table = 'p_common_metadata'
    
    print(f"\n=== 老库表: {old_table}, 列数: {len(old_cols)} ===", flush=True)
    
    # 查老库数据（只同步 tenant_id<=0, delete_flg=0）
    col_str = ', '.join(old_cols)
    pc.execute(f"""
        SELECT {col_str} FROM {old_table}
        WHERE tenant_id <= 0 AND (delete_flg IS NULL OR delete_flg = 0)
              AND api_key IS NOT NULL AND api_key != ''
    """)
    old_rows = pc.fetchall()
    print(f"  老库数据量: {len(old_rows)} 行", flush=True)
    
    # 按 metamodel_id 分组转换
    mm_id_idx = old_cols.index('metamodel_id')
    stats = defaultdict(lambda: {'total': 0, 'ok': 0, 'skip': 0, 'warn': 0})
    all_warnings = []
    all_new_rows = []
    
    for old_row in old_rows:
        mm_id = old_row[mm_id_idx]
        mm_ak = id_maps['meta_model'].get(mm_id, f'unknown_{mm_id}')
        meta_items = meta_items_by_mm.get(mm_id, [])
        stats[mm_ak]['total'] += 1
        
        new_row = convert_row(old_row, old_cols, mm_id, meta_items, id_maps)
        if new_row is None:
            stats[mm_ak]['skip'] += 1
            continue
        
        row_warnings = []
        valid = validate_row(new_row, mm_ak, row_warnings)
        validate_id_conversions(new_row, old_row, old_cols, meta_items, id_maps, row_warnings)
        
        if row_warnings:
            stats[mm_ak]['warn'] += len(row_warnings)
            all_warnings.extend(row_warnings)
        
        if valid:
            all_new_rows.append(new_row)
            stats[mm_ak]['ok'] += 1
        else:
            stats[mm_ak]['skip'] += 1
    
    # 打印统计
    print(f"\n=== 转换统计 ===", flush=True)
    for mm_ak in sorted(stats.keys()):
        s = stats[mm_ak]
        print(f"  {mm_ak}: 总计={s['total']}, 成功={s['ok']}, 跳过={s['skip']}, 警告={s['warn']}", flush=True)
    
    if all_warnings:
        print(f"\n=== 警告 ({len(all_warnings)} 条) ===", flush=True)
        for w in all_warnings[:50]:
            print(f"  {w}", flush=True)
        if len(all_warnings) > 50:
            print(f"  ... 还有 {len(all_warnings) - 50} 条警告", flush=True)
    
    # 写入新库
    if not all_new_rows:
        print("\n无数据可写入", flush=True)
        return
    
    print(f"\n=== 写入新库 ({len(all_new_rows)} 行) ===", flush=True)
    mc = my.cursor()
    
    col_list = ALL_NEW_COLUMNS
    placeholders = ', '.join(['%s'] * len(col_list))
    col_str = ', '.join(col_list)
    sql = f"INSERT IGNORE INTO p_common_metadata ({col_str}) VALUES ({placeholders})"
    
    batch = []
    for row in all_new_rows:
        batch.append(tuple(row.get(c) for c in col_list))
    
    # 分批写入（每 500 条一批）
    batch_size = 500
    inserted = 0
    for i in range(0, len(batch), batch_size):
        chunk = batch[i:i + batch_size]
        mc.executemany(sql, chunk)
        my.commit()
        inserted += len(chunk)
        print(f"  已写入: {inserted}/{len(batch)}", flush=True)
    
    # 验证写入结果
    print(f"\n=== 写入验证 ===", flush=True)
    mc.execute("SELECT metamodel_api_key, COUNT(*) FROM p_common_metadata GROUP BY metamodel_api_key ORDER BY metamodel_api_key")
    for mm_ak, cnt in mc.fetchall():
        old_cnt = stats.get(mm_ak, {}).get('ok', 0)
        match = "✓" if cnt == old_cnt else f"✗ (期望 {old_cnt})"
        print(f"  {mm_ak}: {cnt} {match}", flush=True)
    
    mc.execute("SELECT COUNT(*) FROM p_common_metadata")
    total = mc.fetchone()[0]
    print(f"  总计: {total} (期望 {len(all_new_rows)})", flush=True)


def main():
    print("=" * 60, flush=True)
    print("p_common_metadata 数据同步: PG → MySQL", flush=True)
    print("=" * 60, flush=True)
    
    pg = get_pg()
    my = get_my('paas_metarepo_common')
    
    try:
        sync_common_metadata(pg, my)
    finally:
        pg.close()
        my.close()
    
    print("\nDONE", flush=True)


if __name__ == '__main__':
    main()
