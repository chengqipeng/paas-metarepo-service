#!/usr/bin/env python3
"""
从老系统 PostgreSQL (xsy_metarepo) 同步 p_meta_model / p_meta_item / p_meta_option 到新 MySQL (paas_metarepo)
"""
import psycopg2
import mysql.connector

PG_CONFIG = dict(host='10.65.2.6', port=5432, dbname='crm_cd_data', user='xsy_metarepo', password='sk29XGLI%iu88pF*', connect_timeout=10)
MY_CONFIG = dict(host='106.14.194.144', port=3306, user='root', password='MySql@888888', database='paas_metarepo')

TABLES = ['p_meta_model', 'p_meta_item', 'p_meta_option']

def get_pg_columns(pg_cur, table):
    pg_cur.execute(f"SELECT column_name FROM information_schema.columns WHERE table_schema='xsy_metarepo' AND table_name='{table}' ORDER BY ordinal_position")
    return [r[0] for r in pg_cur.fetchall()]

def get_my_columns(my_cur, table):
    my_cur.execute(f"DESCRIBE {table}")
    return [r[0] for r in my_cur.fetchall()]

def sync_table(pg_cur, my_conn, my_cur, table):
    pg_cols = get_pg_columns(pg_cur, table)
    my_cols = get_my_columns(my_cur, table)
    # 只同步两边都有的列
    common_cols = [c for c in pg_cols if c in my_cols]
    print(f"\n=== {table} ===")
    print(f"  PG cols: {len(pg_cols)}, MySQL cols: {len(my_cols)}, common: {len(common_cols)}")
    col_list = ', '.join(common_cols)
    pg_cur.execute(f"SELECT {col_list} FROM {table}")
    rows = pg_cur.fetchall()
    print(f"  PG rows: {len(rows)}")

    if not rows:
        return

    # 清空 MySQL 表
    my_cur.execute(f"DELETE FROM {table}")
    my_conn.commit()

    # 批量插入
    placeholders = ', '.join(['%s'] * len(common_cols))
    insert_sql = f"INSERT INTO {table} ({col_list}) VALUES ({placeholders})"
    batch_size = 500
    inserted = 0
    for i in range(0, len(rows), batch_size):
        batch = rows[i:i+batch_size]
        # 转换 None 和类型
        clean_batch = []
        for row in batch:
            clean_row = []
            for val in row:
                if val is None:
                    clean_row.append(None)
                else:
                    clean_row.append(val)
            clean_batch.append(tuple(clean_row))
        my_cur.executemany(insert_sql, clean_batch)
        my_conn.commit()
        inserted += len(clean_batch)
    print(f"  MySQL inserted: {inserted}")

def main():
    pg_conn = psycopg2.connect(**PG_CONFIG)
    pg_cur = pg_conn.cursor()
    my_conn = mysql.connector.connect(**MY_CONFIG)
    my_cur = my_conn.cursor()

    for table in TABLES:
        sync_table(pg_cur, my_conn, my_cur, table)

    pg_conn.close()
    my_conn.close()
    print("\nDone!")

if __name__ == '__main__':
    main()
