# p_meta_common_metadata → p_common_metadata 迁移方案

## 1. 表结构差异

### 老表 p_meta_common_metadata（PG）
```
id BIGINT PK, tenant_id BIGINT, namespace VARCHAR(100),
api_key VARCHAR(255), label VARCHAR(255), label_key VARCHAR(255),
metamodel_id BIGINT, metadata_id BIGINT, object_id BIGINT,
parent_metadata_id BIGINT, metadata_json TEXT,
meta_version VARCHAR(100), description VARCHAR(500),
created_by/created_at/updated_by/updated_at BIGINT, delete_flg SMALLINT
```
业务字段全部序列化在 `metadata_json`（JSON TEXT）中。

### 新表 p_common_metadata（MySQL）
```
metamodel_api_key VARCHAR(255) PK, api_key VARCHAR(255) PK, metadata_id BIGINT PK,
namespace VARCHAR(50), label VARCHAR(255), label_key VARCHAR(255),
object_api_key VARCHAR(255), parent_metadata_id BIGINT,
custom_flg INTEGER, metadata_order INTEGER,
description VARCHAR(500), meta_version VARCHAR(100),
dbc_varchar_1~30 VARCHAR(500), dbc_textarea_1~10 TEXT,
dbc_bigint_1~20 BIGINT, dbc_int_1~15 INTEGER,
dbc_smallint_1~15 SMALLINT, dbc_decimal_1~5 DECIMAL(20,4),
created_by/created_at/updated_by/updated_at BIGINT, delete_flg SMALLINT
```
业务字段拆解到 `dbc_xxx_N` 大宽列中。

## 2. 固定列转换

| 老列 | 新列 | 转换规则 |
|---|---|---|
| id | 无 | 丢弃 |
| tenant_id | 无 | 丢弃（只同步 tenant_id<=0） |
| metamodel_id | metamodel_api_key | 查 p_meta_model id→api_key |
| object_id | object_api_key | 查 p_custom_entity id→api_key |
| metadata_id | metadata_id | 直接复制 |
| parent_metadata_id | parent_metadata_id | 直接复制 |
| api_key | api_key | 直接复制 |
| namespace | namespace | 直接复制，NULL→'system' |
| label | label | 直接复制 |
| label_key | label_key | 直接复制 |
| description | description | 直接复制 |
| meta_version | meta_version | 直接复制 |
| metadata_json | 无（拆解到 dbc 列） | 见下方 |
| 无 | custom_flg | 从 metadata_json 中提取 |
| 无 | metadata_order | 从 metadata_json 中提取 |

## 3. metadata_json → dbc_xxx_N 拆解

### 3.1 流程

```
1. 查 p_meta_item 获取该 metamodel 的字段定义：
   {api_key: 'entityType', db_column: 'dbc_select_1', data_type: 5}

2. 解析 metadata_json：
   {"entityType": 1, "svgId": 100, "dbTable": "p_data_account", ...}

3. 按 p_meta_item 的 api_key 匹配 JSON key，
   将值写入对应的 db_column（经过列类型映射）

4. 列类型映射（老→新，需要重新编号）：
   dbc_integer_N  → dbc_bigint_M
   dbc_relation_N → dbc_bigint_M  （M 重新分配，避免冲突）
   dbc_date_N     → dbc_bigint_M
   dbc_select_N   → dbc_int_M
   dbc_tinyint_N  → dbc_smallint_M
   dbc_real_N     → dbc_decimal_M
   dbc_varchar_N  → dbc_varchar_N （不变）
   dbc_textarea_N → dbc_textarea_N（不变）
```

### 3.2 列类型合并重新编号

老库中 `dbc_integer_`、`dbc_relation_`、`dbc_date_` 底层都是 BIGINT，
合并到新库的 `dbc_bigint_` 时存在 22 个编号冲突（同一 metamodel 中
`dbc_integer_1` 和 `dbc_relation_1` 都用了编号 1）。

**解决方案：按 metamodel 重新分配 dbc_bigint_ 编号。**

对每个 metamodel：
1. 收集所有 `dbc_integer_N`、`dbc_relation_N`、`dbc_date_N` 的字段
2. 按原编号排序（integer 优先，relation 次之，date 最后）
3. 从 1 开始重新分配 `dbc_bigint_` 编号
4. 同时更新 p_meta_item 的 db_column 值

同理 `dbc_select_` → `dbc_int_`、`dbc_tinyint_` → `dbc_smallint_` 编号不变（无冲突），
`dbc_real_` → `dbc_decimal_` 编号不变。

### 3.3 JSON 中的 ID 值转换

metadata_json 中部分字段存储的是 BIGINT ID，需要转为 api_key（VARCHAR）：

| JSON key | 查询表 | 转换后 key |
|---|---|---|
| entityId / objectId | p_custom_entity | entityApiKey |
| parentEntityId / childEntityId | p_custom_entity | parentEntityApiKey / childEntityApiKey |
| referEntityId | p_custom_entity | referEntityApiKey |
| itemId | p_custom_item | itemApiKey |
| checkErrorItemId | p_custom_item | checkErrorItemApiKey |
| referLinkId / linkId | p_custom_entity_link | referLinkApiKey / linkApiKey |
| svgId | 直接转字符串 | svgApiKey |

### 3.4 JSON 中需要丢弃的字段

| JSON key | 原因 |
|---|---|
| tenantId | Common 表无 tenant_id |
| metaModelId | 已转到固定列 metamodel_api_key |
| id | 新表无 id 列 |
| deleteFlg | 已在固定列 delete_flg |

### 3.5 JSON 中提取到固定列的字段

| JSON key | 新固定列 |
|---|---|
| customFlg | custom_flg |
| metadataOrder | metadata_order |

## 4. 迁移步骤

```
Step 1: 构建 ID→api_key 映射
  - p_meta_model: id → api_key
  - p_custom_entity: id → api_key
  - p_custom_item: id → api_key
  - p_custom_entity_link: id → api_key

Step 2: 加载 p_meta_item 字段定义，按 metamodel 分组
  - 构建 {metamodel_id: [{api_key, db_column}, ...]}
  - 计算列类型合并后的新 db_column 编号映射

Step 3: 查老库 p_meta_common_metadata（WHERE tenant_id<=0 AND delete_flg=0）

Step 4: 逐行转换
  4.1 固定列转换（metamodel_id→api_key, object_id→api_key）
  4.2 解析 metadata_json
  4.3 按 p_meta_item 映射将 JSON 值写入对应的新 dbc_xxx_N 列
  4.4 ID 值转 api_key
  4.5 丢弃无用字段
  4.6 提取 customFlg/metadataOrder 到固定列

Step 5: 批量 INSERT INTO p_common_metadata

Step 6: 同步更新 p_meta_item 的 db_column（老前缀→新前缀+新编号）

Step 7: 验证数据量和字段值
```

## 5. 数据量

- 老库 p_meta_common_metadata: 212,628 行
- 130+ 个不同 metamodel 类型
- 22 个 metamodel 存在 BIGINT 组编号冲突需要重新编号
