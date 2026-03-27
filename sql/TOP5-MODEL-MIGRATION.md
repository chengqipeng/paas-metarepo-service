# 5 个主要模型迁移字段映射方案

## 总体说明

老库 `p_meta_common_metadata` 的业务字段存储在 `metadata_json`（JSON TEXT）中。
新库 `p_common_metadata` 改为大宽表，业务字段拆解到 `dbc_xxx_N` 列。

每个字段的新 `db_column` 由 `p_meta_item` 定义。迁移时需要同步更新 `p_meta_item.db_column`
从老前缀映射到新前缀（如 `dbc_integer_1` → `dbc_bigint_1`）。

**列类型映射规则：**
- `无 db_column`（NULL）的字段 → 从 JSON 中提取，映射到固定列或新分配 dbc 列
- 有 `db_column` 的字段 → 按前缀映射：

| 老前缀 | 新前缀 | 说明 |
|---|---|---|
| dbc_varchar_ | dbc_varchar_ | 不变 |
| dbc_textarea_ | dbc_textarea_ | 不变 |
| dbc_integer_ | dbc_bigint_ | 合并，需重新编号 |
| dbc_relation_ | dbc_bigint_ | 合并，需重新编号 |
| dbc_date_ | dbc_bigint_ | 合并，需重新编号 |
| dbc_select_ | dbc_int_ | 重命名 |
| dbc_tinyint_ | dbc_smallint_ | 重命名 |
| dbc_real_ | dbc_decimal_ | 重命名 |

**固定列映射（所有模型通用）：**

| JSON key | 处理方式 | 说明 |
|---|---|---|
| apiKey | → 固定列 api_key | |
| label | → 固定列 label | |
| labelKey | → 固定列 label_key | |
| namespace | → 固定列 namespace | |
| description | → 固定列 description | |
| isDelete / deleteFlg | → 固定列 delete_flg | |
| isCustom / customFlg | → 固定列 custom_flg | |
| createdAt/createdBy/updatedAt/updatedBy | → 固定列 | |
| name | 丢弃 | 新 Entity 无此字段，用 label 替代 |
| objectId / entityId | → 固定列 object_api_key | ID→api_key 转换 |

---
