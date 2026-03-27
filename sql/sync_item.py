#!/usr/bin/env python3
"""同步 item（EntityItem）的 Common 级元数据到 p_common_metadata

老库: PG p_meta_common_metadata (metamodel_id → item 的 id)
新库: MySQL p_common_metadata (metamodel_api_key='item')

特殊转换:
- objectId (ID) → entity_api_key (通过 p_meta_common_metadata 查 entity 的 api_key)
- referObjectId (ID) → referEntityApiKey (同上)
- referLinkId (ID) → referLinkApiKey (通过 p_meta_common_metadata 查 entityLink 的 api_key)
- namespace: xsy→product, zly01→system
- labelKey: XdMDItem.{apiKey}
- 13 个 JSON-only 字段分配 dbc 列
"""
import psycopg2
import mysql.connector
import json
import re

PG = dict(host='10.65.2.6', port=5432, dbname='crm_cd_data',
          user='xsy_metarepo', password='sk29XGLI%iu88pF*', connect_timeout=10)
MY = dict(host='106.14.194.144', port=3306, user='root', password='MySql@888888',
          database='paas_metarepo_common')

# 老库 JSON key → (新 dbc 列, 转换类型, 新 api_key)
# 转换类型: 'none'=直接映射, 'id2entity'=ID→entity apiKey, 'id2link'=ID→link apiKey, 'id2str'=ID→字符串
FIELD_MAP = {
    # --- 有 db_column 的字段 ---
    'itemType':           ('dbc_int1',      'none',      'itemType'),
    'dataType':           ('dbc_int2',      'none',      'dataType'),
    'itemOrder':          ('dbc_int3',      'none',      'itemOrder'),
    'readonlyStatus':     ('dbc_int4',      'none',      'readonlyStatus'),
    'visibleStatus':      ('dbc_int5',      'none',      'visibleStatus'),
    'sortable':           ('dbc_int6',      'none',      'sortFlg'),
    'isRequired':         ('dbc_smallint1', 'none',      'requireFlg'),
    'isActive':           ('dbc_smallint2', 'none',      'enableFlg'),
    'isHidden':           ('dbc_smallint3', 'none',      'hiddenFlg'),
    'isUniqueKey':        ('dbc_smallint4', 'none',      'uniqueKeyFlg'),
    'creatable':          ('dbc_smallint5', 'none',      'creatable'),
    'updatable':          ('dbc_smallint6', 'none',      'updatable'),
    'enableHistoryLog':   ('dbc_smallint7', 'none',      'enableHistoryLog'),
    'referObjectId':      ('dbc_varchar1',  'id2entity', 'referEntityApiKey'),
    'referLinkId':        ('dbc_varchar2',  'id2link',   'referLinkApiKey'),
    'dbColumn':           ('dbc_varchar3',  'none',      'dbColumn'),
    'helpText':           ('dbc_varchar4',  'none',      'helpText'),
    'helpTextKey':        ('dbc_varchar5',  'none',      'helpTextKey'),
    'defaultValue':       ('dbc_textarea2', 'none',      'defaultValue'),
    # --- 有 db_column 的字段（enableDeactive 在老库有 db_column） ---
    'enableDeactive':     ('dbc_smallint8', 'none',      'enableDeactive'),
    # --- JSON-only 字段（老库无 db_column，新库分配 dbc 列） ---
    'compound':                    ('dbc_smallint9',  'none', 'compound'),
    'maskPrefix':                  ('dbc_int7',       'none', 'maskPrefix'),
    'maskSuffix':                  ('dbc_int8',       'none', 'maskSuffix'),
    'encrypt':                     ('dbc_smallint10', 'none', 'encrypt'),
    'indexOrder':                   ('dbc_int9',       'none', 'indexOrder'),
    'indexType':                    ('dbc_int10',      'none', 'indexType'),
    'markdown':                     ('dbc_smallint11', 'none', 'markdown'),
    'maskSymbolType':               ('dbc_int11',      'none', 'maskSymbolType'),
    'incrementStrategy':            ('dbc_int12',      'none', 'incrementStrategy'),
    'referItemFilterEnable':        ('dbc_smallint12', 'none', 'referItemFilterEnable'),
    'isComputeMultiCurrencyUnit':   ('dbc_varchar8',   'none', 'isComputeMultiCurrencyUnit'),
    'format':                       ('dbc_varchar9',   'none', 'format'),
}

DBC_COLS = sorted(set(col for col, _, _ in FIELD_MAP.values()))

NAMESPACE_MAP = {
    'system': 'system',
    'xsy': 'product',
    'zly01': 'system',
}

def convert_namespace(old_ns):
    if not old_ns:
        return 'system'
    mapped = NAMESPACE_MAP.get(old_ns)
    if mapped:
        return mapped
    print(f'  WARN: 未知 namespace "{old_ns}" → 默认 system')
    return 'system'

def parse_json(mj):
    """解析 metadata_json，处理控制字符"""
    if not mj:
        return None
    try:
        return json.loads(mj)
    except json.JSONDecodeError:
        cleaned = re.sub(r'[\x00-\x1f\x7f]', '', mj)
        try:
            return json.loads(cleaned)
        except Exception:
            return None

FIXED = ['id', 'metamodel_api_key', 'api_key', 'metadata_api_key', 'namespace',
         'label', 'label_key', 'entity_api_key', 'parent_metadata_api_key',
         'custom_flg', 'metadata_order', 'description', 'meta_version',
         'delete_flg', 'created_at', 'created_by', 'updated_at', 'updated_by']
ALL_COLS = FIXED + DBC_COLS

# ==================== 连接数据库 ====================
pg = psycopg2.connect(**PG)
my = mysql.connector.connect(**MY)
pc = pg.cursor()
mc = my.cursor()

# ==================== 构建 ID → apiKey 映射 ====================

# 1. 找 item 的 metamodel_id
pc.execute("SELECT id FROM p_meta_model WHERE api_key='item' AND tenant_id<=0 LIMIT 1")
row = pc.fetchone()
if not row:
    print('ERROR: 找不到 item metamodel')
    exit(1)
item_mm_id = row[0]
print(f'item metamodel_id: {item_mm_id}')

# 2. 找 xobject 的 metamodel_id（用于 objectId → entity apiKey）
pc.execute("SELECT id FROM p_meta_model WHERE api_key='xobject' AND tenant_id<=0 LIMIT 1")
entity_mm_id = pc.fetchone()[0]
print(f'xobject metamodel_id: {entity_mm_id}')

# 3. 找 xobjectLink 的 metamodel_id（用于 referLinkId → link apiKey）
pc.execute("SELECT id FROM p_meta_model WHERE api_key='xobjectLink' AND tenant_id<=0 LIMIT 1")
link_row = pc.fetchone()
link_mm_id = link_row[0] if link_row else None
print(f'xobjectLink metamodel_id: {link_mm_id}')

# 4. 构建 entity metadata_id → api_key 映射
pc.execute("""SELECT metadata_id, api_key FROM p_meta_common_metadata
    WHERE metamodel_id=%s AND tenant_id<=0 AND (delete_flg IS NULL OR delete_flg=0)""",
    (entity_mm_id,))
entity_id_map = {r[0]: r[1] for r in pc.fetchall()}
print(f'entity ID→apiKey 映射: {len(entity_id_map)} 条')

# 5. 构建 link metadata_id → api_key 映射
link_id_map = {}
if link_mm_id:
    pc.execute("""SELECT metadata_id, api_key FROM p_meta_common_metadata
        WHERE metamodel_id=%s AND tenant_id<=0 AND (delete_flg IS NULL OR delete_flg=0)""",
        (link_mm_id,))
    link_id_map = {r[0]: r[1] for r in pc.fetchall()}
print(f'link ID→apiKey 映射: {len(link_id_map)} 条')

def resolve_id(val, conv_type):
    """将老库 ID 转换为 apiKey"""
    if val is None:
        return None
    mid = int(val) if isinstance(val, (int, float)) else None
    if mid is None:
        try:
            mid = int(val)
        except (ValueError, TypeError):
            return str(val)
    if conv_type == 'id2entity':
        return entity_id_map.get(mid, str(mid))
    elif conv_type == 'id2link':
        return link_id_map.get(mid, str(mid))
    elif conv_type == 'id2str':
        return str(mid)
    return val

# ==================== 查询 item 数据 ====================
pc.execute("""SELECT api_key, label, label_key, namespace, metadata_id,
    object_id, parent_metadata_id, metadata_json, meta_version, description,
    created_by, created_at, updated_by, updated_at
    FROM p_meta_common_metadata
    WHERE metamodel_id=%s AND tenant_id<=0 AND (delete_flg IS NULL OR delete_flg=0)""",
    (item_mm_id,))
rows = pc.fetchall()
print(f'\n查到 item 数据: {len(rows)} 行')

# ==================== 转换数据 ====================
batch = []
skip_count = 0
warn_count = 0
# 简易 ID 生成器（基于时间戳 + 序号）
import time
id_base = int(time.time() * 1000) << 20
id_seq = 0

for ak, lb, lk, ns, mid, oid, pmid, mj, mv, desc, cb, ca, ub, ua in rows:
    # 跳过"未使用"
    if ak and '未使用' in ak:
        skip_count += 1
        continue

    jd = parse_json(mj)
    if jd is None:
        if mj:
            print(f'  WARN: JSON parse failed api_key={ak}')
            warn_count += 1
        continue

    rd = {c: None for c in ALL_COLS}
    id_seq += 1
    rd['id'] = id_base + id_seq
    rd['metamodel_api_key'] = 'item'
    rd['api_key'] = ak or ''
    # item 的 metadata_api_key 需要包含 entity 信息以保证唯一性
    # 因为不同 entity 下可以有相同 apiKey 的 item（如 updatedAt）
    entity_ak_for_key = None
    rd['namespace'] = convert_namespace(ns)
    rd['label'] = lb or jd.get('label', '')
    rd['label_key'] = f"XdMDItem.{ak}" if ak else lk
    rd['description'] = desc or jd.get('description')

    # objectId → entity_api_key（固定列）
    obj_id = jd.get('objectId') or oid
    if obj_id is not None:
        eid = int(obj_id) if isinstance(obj_id, (int, float)) else None
        if eid is None:
            try:
                eid = int(obj_id)
            except (ValueError, TypeError):
                pass
        if eid is not None:
            rd['entity_api_key'] = entity_id_map.get(eid, str(eid))
            entity_ak_for_key = rd['entity_api_key']
        else:
            rd['entity_api_key'] = str(obj_id)
            entity_ak_for_key = rd['entity_api_key']

    # metadata_api_key = entity_api_key.api_key（保证跨 entity 唯一）
    if entity_ak_for_key:
        rd['metadata_api_key'] = f"{entity_ak_for_key}.{ak}" if ak else str(mid)
    else:
        rd['metadata_api_key'] = str(mid) if mid else (ak or '')

    rd['parent_metadata_api_key'] = str(pmid) if pmid else None
    rd['custom_flg'] = jd.get('isCustom')
    rd['metadata_order'] = jd.get('metadataOrder') or jd.get('itemOrder')
    rd['meta_version'] = mv
    rd['delete_flg'] = 0
    rd['created_at'] = ca
    rd['created_by'] = cb
    rd['updated_at'] = ua
    rd['updated_by'] = ub

    # dbc 列映射
    for jk, (dc, conv_type, new_ak) in FIELD_MAP.items():
        v = jd.get(jk)
        if v is not None:
            if conv_type != 'none':
                rd[dc] = resolve_id(v, conv_type)
            else:
                rd[dc] = v

    batch.append(tuple(rd[c] for c in ALL_COLS))

print(f'跳过: {skip_count}, 警告: {warn_count}, 待写入: {len(batch)}')

# ==================== 写入新库 ====================
if batch:
    # 先清除旧数据
    mc.execute("DELETE FROM p_common_metadata WHERE metamodel_api_key='item'")
    my.commit()
    print(f'已清除旧 item 数据')

    ph = ','.join(['%s'] * len(ALL_COLS))
    sql = f"INSERT IGNORE INTO p_common_metadata ({','.join(ALL_COLS)}) VALUES ({ph})"
    mc.executemany(sql, batch)
    my.commit()

mc.execute("SELECT COUNT(*) FROM p_common_metadata WHERE metamodel_api_key='item'")
total = mc.fetchone()[0]
print(f'写入: {len(batch)}, DB 总数: {total}')

# ==================== 验证 ====================
print('\n--- 验证 ---')

# 抽样
mc.execute("""SELECT api_key, label, entity_api_key, dbc_int1, dbc_int2, dbc_smallint2, dbc_varchar1
    FROM p_common_metadata WHERE metamodel_api_key='item' LIMIT 5""")
print('抽样 (apiKey, label, entityApiKey, itemType, dataType, enableFlg, referEntityApiKey):')
for r in mc.fetchall():
    print(f'  {r}')

# entity_api_key 统计
mc.execute("""SELECT entity_api_key, COUNT(*) as cnt
    FROM p_common_metadata WHERE metamodel_api_key='item'
    GROUP BY entity_api_key ORDER BY cnt DESC LIMIT 10""")
print('\nentity_api_key 分布 (top 10):')
for r in mc.fetchall():
    print(f'  {r}')

# entity_api_key 为数字的（未解析成功的）
mc.execute("""SELECT COUNT(*) FROM p_common_metadata
    WHERE metamodel_api_key='item' AND entity_api_key REGEXP '^[0-9]+$'""")
num_unresolved = mc.fetchone()[0]
print(f'\nentity_api_key 未解析（仍为数字ID）: {num_unresolved} 条')

# namespace 分布
mc.execute("""SELECT namespace, COUNT(*) FROM p_common_metadata
    WHERE metamodel_api_key='item' GROUP BY namespace""")
print('\nnamespace 分布:')
for r in mc.fetchall():
    print(f'  {r}')

# referEntityApiKey 非空统计
mc.execute("""SELECT COUNT(*) FROM p_common_metadata
    WHERE metamodel_api_key='item' AND dbc_varchar1 IS NOT NULL""")
print(f'\nreferEntityApiKey 非空: {mc.fetchone()[0]} 条')

pg.close()
my.close()
print('\nDONE')
