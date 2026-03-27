# Entity 模型字段完整对比表

metamodel: xobject → entity

| # | Entity.java 字段 | Java 类型 | 老 p_meta_item api_key | 老 db_column | 新 db_column | 存储位置 | 操作 |
|---|---|---|---|---|---|---|---|
| 1 | apiKey | String | apiKey | api_key | — | 固定列 | 不变 |
| 2 | label | String | label | label | — | 固定列 | 不变 |
| 3 | labelKey | String | labelKey | label_key | — | 固定列 | 不变 |
| 4 | namespace | String | namespace | namespace | — | 固定列 | 不变 |
| 5 | description | String | description | description | — | 固定列 | 不变 |
| 6 | customFlg | Integer | isCustom | custom_flg | — | 固定列 | 不变 |
| 7 | deleteFlg | Integer | isDelete | delete_flg | — | 固定列 | 不变 |
| 8 | createdAt | Long | createdAt | created_at | — | 固定列 | 不变 |
| 9 | createdBy | Long | createdBy | created_by | — | 固定列 | 不变 |
| 10 | updatedAt | Long | updatedAt | updated_at | — | 固定列 | 不变 |
| 11 | updatedBy | Long | updatedBy | updated_by | — | 固定列 | 不变 |
| 12 | entityType | Integer | objectType | object_type | dbc_int_1 | dbc 列 | 重命名 api_key + 更新 db_column |
| 13 | svgApiKey | String | iconId | svg_id | dbc_varchar_1 | dbc 列 | 重命名 + 类型变(ID→svg_class) |
| 14 | businessCategory | Integer | businessCategory | business_category | dbc_int_2 | dbc 列 | 更新 db_column |
| 15 | customEntitySeq | Integer | customEntitySeq | custom_entityseq | dbc_bigint_1 | dbc 列 | 更新 db_column |
| 16 | enableFlg | Integer | isActive | enable_flg | dbc_smallint_1 | dbc 列 | 重命名 + 更新 db_column |
| 17 | detailFlg | Integer | isDetail | detail_flg | dbc_smallint_2 | dbc 列 | 重命名 + 更新 db_column |
| 18 | enableHistoryLog | Integer | enableHistoryLog | enable_history_log | dbc_smallint_3 | dbc 列 | 更新 db_column |
| 19 | dbTable | String | dbTable | db_table | dbc_varchar_2 | dbc 列 | 更新 db_column |
| 20 | enableDynamicFeed | Integer | enableDynamicFeed | NULL | dbc_smallint_4 | dbc 列 | 分配 db_column |
| 21 | enableGroupMember | Integer | enableGroupMember | NULL | dbc_smallint_5 | dbc 列 | 分配 db_column |
| 22 | isArchived | Integer | isArchived | NULL | dbc_smallint_6 | dbc 列 | 分配 db_column |
| 23 | enableScriptExecutor | Integer | enableScriptExecutor | NULL | dbc_smallint_7 | dbc 列 | 分配 db_column |
| 24 | enableDuplicaterule | Integer | enableDuplicaterule | NULL | dbc_smallint_8 | dbc 列 | 分配 db_column |
| 25 | enableCheckrule | Integer | enableCheckrule | NULL | dbc_smallint_9 | dbc 列 | 分配 db_column |
| 26 | enableBusitype | Integer | enableBusitype | NULL | dbc_smallint_10 | dbc 列 | 分配 db_column |
| 27 | svgColor | String | — | — | dbc_varchar_3 | dbc 列 | **新增** |
| 28 | descriptionKey | String | — | — | dbc_varchar_4 | dbc 列 | **新增** |
| 29 | typeProperty | String | — | — | dbc_varchar_5 | dbc 列 | **新增** |
| 30 | extendProperty | String | — | — | dbc_varchar_6 | dbc 列 | **新增** |
| 31 | enableTeam | Integer | — | — | dbc_smallint_11 | dbc 列 | **新增** |
| 32 | enableSocial | Integer | — | — | dbc_smallint_12 | dbc 列 | **新增** |
| 33 | hiddenFlg | Integer | — | — | dbc_smallint_13 | dbc 列 | **新增** |
| 34 | searchable | Integer | — | — | dbc_smallint_14 | dbc 列 | **新增** |
| 35 | enableSharing | Integer | — | — | dbc_smallint_15 | dbc 列 | **新增** |
| 36 | enableScriptTrigger | Integer | — | — | dbc_int_3 | dbc 列 | **新增** |
| 37 | enableActivity | Integer | — | — | dbc_int_4 | dbc 列 | **新增** |
| 38 | enableReport | Integer | — | — | dbc_int_5 | dbc 列 | **新增** |
| 39 | enableRefer | Integer | — | — | dbc_int_6 | dbc 列 | **新增** |
| 40 | enableApi | Integer | — | — | dbc_int_7 | dbc 列 | **新增** |
| 41 | enableConfig | Long | — | — | dbc_bigint_2 | dbc 列 | **新增** |
| 42 | enableFlow | Long | — | — | dbc_bigint_3 | dbc 列 | **新增** |
| 43 | enablePackage | Long | — | — | dbc_bigint_4 | dbc 列 | **新增** |
| — | — | — | name | name | — | — | **丢弃** |
| — | — | — | objectId | object_id | — | — | **丢弃** |
| — | — | — | enableOwner | NULL | — | — | **丢弃** |

## 汇总

| 类别 | 数量 |
|---|---|
| 固定列（不需要 dbc 映射） | 11 |
| 更新 db_column（老前缀→新前缀） | 8 |
| 重命名 api_key + 更新 db_column | 4 |
| 分配 db_column（老记录有但 db_column 为 NULL） | 7 |
| 新增 p_meta_item 记录 | 17 |
| 丢弃（老库有新库不需要） | 3 |
| **Entity.java 业务字段总计** | **43** |

## dbc 列使用汇总

| 列类型 | 使用编号 | 总数 |
|---|---|---|
| dbc_varchar_ | 1~6 | 6 |
| dbc_int_ | 1~7 | 7 |
| dbc_bigint_ | 1~4 | 4 |
| dbc_smallint_ | 1~15 | 15 |
| dbc_textarea_ | — | 0 |
| dbc_decimal_ | — | 0 |
| **合计** | | **32** |
