#!/usr/bin/env python3
"""同步 xobject（entity）的 Common 级元数据到 p_common_metadata"""
import psycopg2, mysql.connector, json

PG = dict(host='10.65.2.6', port=5432, dbname='crm_cd_data',
          user='xsy_metarepo', password='sk29XGLI%iu88pF*', connect_timeout=10)
MY = dict(host='106.14.194.144', port=3306, user='root', password='MySql@888888',
          database='paas_metarepo_common')

# xobject JSON key → 新 dbc 列
# key = 老库 JSON 中的字段名
# value = (新 dbc 列, 是否需要 ID→str 转换, 新 p_meta_item.api_key)
FIELD_MAP = {
    'objectType':          ('dbc_int_1',       False, 'entityType'),
    'iconId':              ('dbc_varchar_1',    True,  'svgApiKey'),
    'businessCategory':    ('dbc_int_2',       False, 'businessCategory'),
    'customEntitySeq':     ('dbc_bigint_1',    False, 'customEntitySeq'),
    'isActive':            ('dbc_smallint_1',   False, 'enableFlg'),
    'isDetail':            ('dbc_smallint_2',   False, 'detailFlg'),
    'enableHistoryLog':    ('dbc_smallint_3',   False, 'enableHistoryLog'),
    'dbTable':             ('dbc_varchar_2',    False, 'dbTable'),
    'enableDynamicFeed':   ('dbc_smallint_4',   False, 'enableDynamicFeed'),
    'enableGroupMember':   ('dbc_smallint_5',   False, 'enableGroupMember'),
    'isArchived':          ('dbc_smallint_6',   False, 'isArchived'),
    'enableScriptExecutor':('dbc_smallint_7',   False, 'enableScriptExecutor'),
    'enableDuplicaterule': ('dbc_smallint_8',   False, 'enableDuplicaterule'),
    'enableCheckrule':     ('dbc_smallint_9',   False, 'enableCheckrule'),
    'enableBusitype':      ('dbc_smallint_10',  False, 'enableBusitype'),
}

DBC_COLS = sorted(set(col for col, _, _ in FIELD_MAP.values()))

# namespace 转换规则（老值 → 新枚举值）
# 合法值：system, product, custom
NAMESPACE_MAP = {
    'system': 'system',
    'xsy': 'product',       # 老库产品级数据 → product
}

def convert_namespace(old_ns):
    """将老库 namespace 转换为新枚举值，不在映射中的默认为 system"""
    if not old_ns:
        return 'system'
    mapped = NAMESPACE_MAP.get(old_ns)
    if mapped:
        return mapped
    # 未知 namespace 默认归为 system，打印警告
    print(f'  WARN: 未知 namespace "{old_ns}" → 默认 system')
    return 'system'

FIXED = ['metamodel_api_key','api_key','metadata_api_key','namespace',
         'label','label_key','entity_api_key','parent_metadata_api_key',
         'custom_flg','metadata_order','description','meta_version',
         'delete_flg','created_at','created_by','updated_at','updated_by']
ALL_COLS = FIXED + DBC_COLS

pg = psycopg2.connect(**PG)
my = mysql.connector.connect(**MY)
pc = pg.cursor()
mc = my.cursor()

# 找 xobject metamodel_id
pc.execute("SELECT id FROM p_meta_model WHERE api_key='xobject' AND tenant_id<=0 LIMIT 1")
mm_id = pc.fetchone()[0]
print(f'xobject metamodel_id: {mm_id}')

# 构建 svg id → svg_class 映射
pc.execute("SELECT id, svg_class FROM p_custom_svg WHERE (delete_flg IS NULL OR delete_flg=0)")
svg_map = {r[0]: r[1] for r in pc.fetchall()}
print(f'svg 映射: {len(svg_map)} 条')

# 查数据
pc.execute("""SELECT api_key, label, label_key, namespace, metadata_id,
    object_id, parent_metadata_id, metadata_json, meta_version, description,
    created_by, created_at, updated_by, updated_at
    FROM p_meta_common_metadata
    WHERE metamodel_id=%s AND tenant_id<=0 AND (delete_flg IS NULL OR delete_flg=0)""", (mm_id,))
rows = pc.fetchall()
print(f'查到 {len(rows)} 行')

batch = []
for ak, lb, lk, ns, mid, oid, pmid, mj, mv, desc, cb, ca, ub, ua in rows:
    if not mj:
        continue
    if ak and '未使用' in ak:
        continue
    jd = {}
    try:
        jd = json.loads(mj)
    except json.JSONDecodeError:
        import re
        cleaned = re.sub(r'[\x00-\x1f\x7f]', '', mj)
        try:
            jd = json.loads(cleaned)
        except:
            print(f'  WARN: JSON parse failed api_key={ak}')
            continue
    rd = {c: None for c in ALL_COLS}
    rd['metamodel_api_key'] = 'entity'
    rd['api_key'] = ak or ''
    rd['metadata_api_key'] = ak or ''
    rd['namespace'] = convert_namespace(ns)
    rd['label'] = lb or jd.get('label', '')
    rd['label_key'] = f"XdMDObj.{ak}" if ak else lk
    rd['entity_api_key'] = None
    rd['parent_metadata_api_key'] = str(pmid) if pmid else None
    rd['custom_flg'] = jd.get('isCustom')
    rd['metadata_order'] = jd.get('metadataOrder')
    rd['description'] = desc or jd.get('description')
    rd['meta_version'] = mv
    rd['delete_flg'] = 0
    rd['created_at'] = ca; rd['created_by'] = cb
    rd['updated_at'] = ua; rd['updated_by'] = ub
    for jk, (dc, id_conv, new_ak) in FIELD_MAP.items():
        v = jd.get(jk)
        if v is not None:
            if jk == 'iconId' and isinstance(v, (int, float)):
                rd[dc] = svg_map.get(int(v), str(int(v)))
            elif id_conv and isinstance(v, (int, float)):
                rd[dc] = str(int(v))
            else:
                rd[dc] = v
    batch.append(tuple(rd[c] for c in ALL_COLS))

ph = ','.join(['%s']*len(ALL_COLS))
sql = f"INSERT IGNORE INTO p_common_metadata ({','.join(ALL_COLS)}) VALUES ({ph})"
mc.executemany(sql, batch)
my.commit()

mc.execute("SELECT COUNT(*) FROM p_common_metadata WHERE metamodel_api_key='entity'")
print(f'写入: {len(batch)}, DB: {mc.fetchone()[0]}')

mc.execute("SELECT api_key, label, dbc_varchar_2, dbc_int_1, dbc_smallint_1 FROM p_common_metadata WHERE metamodel_api_key='entity' LIMIT 5")
print('\n抽样 (api_key, label, dbTable, objectType, enableFlg):')
for r in mc.fetchall(): print(f'  {r}')

pg.close(); my.close()
print('\nDONE')
