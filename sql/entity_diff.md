# Entity (xobject) 新老 p_meta_item 结构差异

## 老库 p_meta_item (metamodel=xobject) 28 个字段

| 老 api_key | 老 db_column | 新 api_key | 新 db_column | 操作 |
|---|---|---|---|---|
| apiKey | api_key | — | 固定列 | 保留（固定列） |
| label | label | — | 固定列 | 保留（固定列） |
| labelKey | label_key | — | 固定列 | 保留（固定列） |
| namespace | namespace | — | 固定列 | 保留（固定列） |
| description | description | — | 固定列 | 保留（固定列） |
| isCustom | custom_flg | — | 固定列 | 保留（固定列） |
| isDelete | delete_flg | — | 固定列 | 保留（固定列） |
| createdAt | created_at | — | 固定列 | 保留（固定列） |
| createdBy | created_by | — | 固定列 | 保留（固定列） |
| updatedAt | updated_at | — | 固定列 | 保留（固定列） |
| updatedBy | updated_by | — | 固定列 | 保留（固定列） |
| name | name | — | — | **丢弃** |
| objectId | object_id | — | — | **丢弃** |
| objectType | object_type | entityType | dbc_int_1 | **重命名** |
| iconId | svg_id | svgApiKey | dbc_varchar_1 | **重命名+类型变** |
| businessCategory | business_category | businessCategory | dbc_int_2 | 更新 db_column |
| customEntitySeq | custom_entityseq | customEntitySeq | dbc_bigint_1 | 更新 db_column |
| isActive | enable_flg | enableFlg | dbc_smallint_1 | **重命名** |
| isDetail | detail_flg | detailFlg | dbc_smallint_2 | **重命名** |
| enableHistoryLog | enable_history_log | enableHistoryLog | dbc_smallint_3 | 更新 db_column |
| dbTable | db_table | dbTable | dbc_varchar_2 | 更新 db_column |
| enableDynamicFeed | NULL | enableDynamicFeed | dbc_smallint_4 | 更新 db_column |
| enableGroupMember | NULL | enableGroupMember | dbc_smallint_5 | 更新 db_column |
| isArchived | NULL | isArchived | dbc_smallint_6 | 更新 db_column |
| enableScriptExecutor | NULL | enableScriptExecutor | dbc_smallint_7 | 更新 db_column |
| enableDuplicaterule | NULL | enableDuplicaterule | dbc_smallint_8 | 更新 db_column |
| enableCheckrule | NULL | enableCheckrule | dbc_smallint_9 | 更新 db_column |
| enableBusitype | NULL | enableBusitype | dbc_smallint_10 | 更新 db_column |
| enableOwner | NULL | — | — | **丢弃**（Entity 无此字段） |

## 新 Entity.java 有、老库 p_meta_item 没有的字段（17 个，需新增）

| Entity 字段 | 类型 | 建议 dbc 列 |
|---|---|---|
| svgColor | String | dbc_varchar_3 |
| descriptionKey | String | dbc_varchar_4 |
| typeProperty | String | dbc_varchar_5 |
| extendProperty | String | dbc_varchar_6 |
| enableTeam | Integer | dbc_smallint_11 |
| enableSocial | Integer | dbc_smallint_12 |
| hiddenFlg | Integer | dbc_smallint_13 |
| searchable | Integer | dbc_smallint_14 |
| enableSharing | Integer | dbc_smallint_15 |
| enableScriptTrigger | Integer | dbc_int_3 |
| enableActivity | Integer | dbc_int_4 |
| enableReport | Integer | dbc_int_5 |
| enableRefer | Integer | dbc_int_6 |
| enableApi | Integer | dbc_int_7 |
| enableConfig | Long | dbc_bigint_2 |
| enableFlow | Long | dbc_bigint_3 |
| enablePackage | Long | dbc_bigint_4 |

## 汇总

| 操作 | 数量 |
|---|---|
| 固定列（不需要 dbc） | 11 |
| 丢弃（name, objectId, enableOwner） | 3 |
| 重命名 api_key（objectType→entityType 等） | 4 |
| 仅更新 db_column（老前缀→新前缀） | 10 |
| 新增 p_meta_item 记录 | 17 |
| **总计** | **45** |
