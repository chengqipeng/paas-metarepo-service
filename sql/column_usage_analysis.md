# p_meta_common_metadata 列使用量分析

## 1. p_meta_item 定义的字段数（= metadata_json 中最多可能的 key 数）

| metamodel | 总字段数 | Common 数据量 |
|---|---|---|
| item | 97 | 24,028 |
| wxContactSetting | 44 | 0 |
| highSeaTerritoryObjectConfig | 39 | 0 |
| messageSetting | 36 | 0 |
| highSeaModelObjectConfig | 35 | 0 |
| neoExtension | 32 | 0 |
| neoComponent | 32 | 81 |
| neoComponentItem | 32 | 423 |
| aiAgentAction | 32 | 53 |
| serviceReportTemplate | 32 | 0 |
| xActionButton | 31 | 23,607 |
| neoExtensionV2 | 31 | 0 |
| rule | 30 | 0 |
| customMenu | 30 | 2 |
| aiLibraryAction | 30 | 36 |
| objectRule | 29 | 0 |
| xobject | 29 | 892 |

总体：411 个 metamodel，字段数 max=97, min=4, avg=15

## 2. 合并后各列类型在单个 metamodel 中的最大占用（来自 p_meta_item.db_column）

| 合并后类型 | DDL 类型 | 最大占用 | 占用最多的 metamodel |
|---|---|---|---|
| BIGINT (integer+relation+date) | BIGINT | 10 | privilegeObjectTenantConfig |
| INTEGER (select) | INTEGER | 10 | territoryManagementConfig |
| SMALLINT (tinyint) | SMALLINT | 10 | wxContactSetting |
| VARCHAR | VARCHAR(500) | 14 | neoComponentUpgrade |
| TEXT (textarea) | TEXT | 6 | mcEmailPluginSetting |
| DECIMAL (real) | DECIMAL(20,4) | 1 | neoComponentConvert |

## 3. 关键发现

- **item 元模型有 97 个字段**，远超其他 metamodel（第二名 44 个）
- 但 item 的 97 个字段中，p_meta_item.db_column 只映射了 46 个到 dbc 列
  （其余 51 个字段是固定列如 api_key/label/tenant_id 等，不占 dbc 列）
- **实际 dbc 列最大占用是 46 个**（item 元模型），不是 97 个
- 合并后各类型最大占用都是 10-14 个，当前设计的列数（varchar 30, bigint 20, int 15, smallint 15, text 10, decimal 5 = 95 列）完全够用
- 有 Common 数据的 metamodel 中，字段数最多的是 item（97/46）和 neoComponentItem（32）

## 4. 建议列数

| 列类型 | DDL 类型 | 建议数量 | 当前最大占用 | 余量 |
|---|---|---|---|---|
| dbc_varchar_ | VARCHAR(500) | 30 | 14 | 2.1x |
| dbc_textarea_ | TEXT | 10 | 6 | 1.7x |
| dbc_bigint_ | BIGINT | 20 | 10 | 2.0x |
| dbc_int_ | INTEGER | 15 | 10 | 1.5x |
| dbc_smallint_ | SMALLINT | 15 | 10 | 1.5x |
| dbc_decimal_ | DECIMAL(20,4) | 5 | 1 | 5.0x |
| **总计** | | **95** | **51** | |
