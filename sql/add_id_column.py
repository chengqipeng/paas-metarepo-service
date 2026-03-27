#!/usr/bin/env python3
"""
为元模型/元数据表补加 id 列（雪花算法生成唯一值）。
步骤：加列(允许NULL) → 填充雪花ID → 改NOT NULL → 改主键
"""
import mysql.connector
import time
import random

# 简易雪花ID生成器
_seq = 0
_last_ts = 0
_worker = random.randint(0, 31)

def snowflake_id():
    global _seq, _last_ts
    ts = int(time.time() * 1000)
    if ts == _last_ts:
        _seq = (_seq + 1) & 0xFFF
        if _seq == 0:
            while ts <= _last_ts:
                ts = int(time.time() * 1000)
    else:
        _seq = 0
    _last_ts = ts
    epoch = 1704067200000  # 2024-01-01
    return ((ts - epoch) << 22) | (_worker << 12) | _seq

DB_CONFIG = {
    'host': '106.14.194.144',
    'user': 'root',
    'password': 'MySql@888888',
}

TABLES = [
    {
        'db': 'paas_metarepo_common',
        'table': 'p_meta_model',
        'old_pk': ['api_key'],
        'uk_name': 'uk_meta_model_apikey',
        'uk_cols': ['api_key'],
    },
    {
        'db': 'paas_metarepo_common',
        'table': 'p_meta_item',
        'old_pk': ['metamodel_api_key', 'api_key'],
        'uk_name': 'uk_meta_item_mm_ak',
        'uk_cols': ['metamodel_api_key', 'api_key'],
    },
    {
        'db': 'paas_metarepo_common',
        'table': 'p_meta_option',
        'old_pk': ['metamodel_api_key', 'item_api_key', 'api_key'],
        'uk_name': 'uk_meta_option_mm_item_ak',
        'uk_cols': ['metamodel_api_key', 'item_api_key', 'api_key'],
    },
    {
        'db': 'paas_metarepo_common',
        'table': 'p_meta_link',
        'old_pk': ['api_key'],
        'uk_name': 'uk_meta_link_apikey',
        'uk_cols': ['api_key'],
    },
    {
        'db': 'paas_metarepo_common',
        'table': 'p_common_metadata',
        'old_pk': ['metamodel_api_key', 'api_key', 'metadata_api_key'],
        'uk_name': 'uk_common_metadata_mm_ak_mk',
        'uk_cols': ['metamodel_api_key', 'api_key', 'metadata_api_key'],
    },
]

def has_column(cursor, db, table, column):
    cursor.execute(f"SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA=%s AND TABLE_NAME=%s AND COLUMN_NAME=%s", (db, table, column))
    return cursor.fetchone()[0] > 0

def migrate_table(cursor, t):
    full = f"{t['db']}.{t['table']}"
    print(f"\n--- {full} ---")

    if has_column(cursor, t['db'], t['table'], 'id'):
        print(f"  已有 id 列，跳过")
        return

    # 1. 加列（允许 NULL）
    print(f"  添加 id 列...")
    cursor.execute(f"ALTER TABLE {full} ADD COLUMN id BIGINT NULL FIRST")

    # 2. 查出所有行的联合主键值
    pk_cols = ', '.join(t['old_pk'])
    cursor.execute(f"SELECT {pk_cols} FROM {full}")
    rows = cursor.fetchall()
    print(f"  填充 {len(rows)} 行雪花ID...")

    # 3. 逐行填充雪花ID
    where_clause = ' AND '.join(f"{col}=%s" for col in t['old_pk'])
    for row in rows:
        sid = snowflake_id()
        cursor.execute(f"UPDATE {full} SET id={sid} WHERE {where_clause}", row)

    # 4. 改 NOT NULL
    cursor.execute(f"ALTER TABLE {full} MODIFY COLUMN id BIGINT NOT NULL")

    # 5. 改主键
    print(f"  修改主键...")
    cursor.execute(f"ALTER TABLE {full} DROP PRIMARY KEY, ADD PRIMARY KEY (id)")

    # 6. 加唯一索引
    uk_cols = ', '.join(t['uk_cols'])
    cursor.execute(f"ALTER TABLE {full} ADD UNIQUE INDEX {t['uk_name']} ({uk_cols})")

    print(f"  完成!")

def main():
    conn = mysql.connector.connect(**DB_CONFIG)
    conn.autocommit = True
    cursor = conn.cursor()

    for t in TABLES:
        try:
            migrate_table(cursor, t)
        except Exception as e:
            print(f"  错误: {e}")

    # p_tenant_metadata 可能不存在
    try:
        t_tenant = {
            'db': 'paas_metarepo',
            'table': 'p_tenant_metadata',
            'old_pk': ['tenant_id', 'metamodel_api_key', 'api_key', 'metadata_api_key'],
            'uk_name': 'uk_tenant_metadata_tid_mm_ak_mk',
            'uk_cols': ['tenant_id', 'metamodel_api_key', 'api_key', 'metadata_api_key'],
        }
        migrate_table(cursor, t_tenant)
    except Exception as e:
        print(f"  p_tenant_metadata 跳过: {e}")

    cursor.close()
    conn.close()
    print("\n全部完成!")

if __name__ == '__main__':
    main()
