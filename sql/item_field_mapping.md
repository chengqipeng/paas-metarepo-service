# Item 模型字段完整对比表

metamodel: item

老库 p_meta_item 有 46 个字段（含 13 个 JSON-only 无 db_column）
新 EntityItem.java 有 28 个业务字段

## 完整对比

| # | EntityItem.java 字段 | Java 类型 | 老 api_key | 老 db_column | 新 db_column | 存储位置 | 操作 |
|---|---|---|---|---|---|---|---|
| 1 | apiKey | String | apiKey/optionApiKey | api_key | — | 固定列 | 不变 |
| 2 | label | String | label | label | — | 固定列 | 不变 |
| 3 | labelKey | String | labelKey | label_key | — | 固定列 | 不变 |
| 4 | namespace | String | namespace | namespace | — | 固定列 | 不变 |
| 5 | description | String | description | description | — | 固定列 | 不变 |
| 6 | deleteFlg | Integer | isDeleted | delete_flg | — | 固定列 | 不变 |
| 7 | createdAt | Long | createdAt | created_at | — | 固定列 | 不变 |
| 8 | createdBy | Long | createdBy | created_by | — | 固定列 | 不变 |
| 9 | updatedAt | Long | updatedAt | updated_at | — | 固定列 | 不变 |
| 10 | updatedBy | Long | updatedBy | updated_by | — | 固定列 | 不变 |
| 11 | entityApiKey | String | objectId | entity_id | — | 固定列 entity_api_key | ID→api_key + 重命名 |
| 12 | customFlg | Integer | isCustom | custom_flg | — | 固定列 | 不变 |
| 13 | itemType | Integer | itemType | item_type | dbc_int_1 | dbc 列 | 更新 db_column |
| 14 | dataType | Integer | dataType | data_type | dbc_int_2 | dbc 列 | 更新 db_column |
| 15 | itemOrder | Integer | itemOrder | item_order | dbc_int_3 | dbc 列 | 更新 db_column |
| 16 | readonlyStatus | Integer | readonlyStatus | readonly_status | dbc_int_4 | dbc 列 | 更新 db_column |
| 17 | visibleStatus | Integer | visibleStatus | visible_status | dbc_int_5 | dbc 列 | 更新 db_column |
| 18 | sortFlg | Integer | sortable | sort_flg | dbc_int_6 | dbc 列 | 重命名 api_key |
| 19 | requireFlg | Integer | isRequired | require_flg | dbc_smallint_1 | dbc 列 | 重命名 api_key |
| 20 | enableFlg | Integer | isActive | enable_flg | dbc_smallint_2 | dbc 列 | 重命名 api_key |
| 21 | hiddenFlg | Integer | isHidden | hidden_flg | dbc_smallint_3 | dbc 列 | 重命名 api_key |
| 22 | uniqueKeyFlg | Integer | isUniqueKey | unique_key_flg | dbc_smallint_4 | dbc 列 | 重命名 api_key |
| 23 | creatable | Integer | creatable | creatable | dbc_smallint_5 | dbc 列 | 更新 db_column |
| 24 | updatable | Integer | updatable | updatable | dbc_smallint_6 | dbc 列 | 更新 db_column |
| 25 | enableHistoryLog | Integer | enableHistoryLog | enable_historylog | dbc_smallint_7 | dbc 列 | 更新 db_column |
| 26 | referEntityApiKey | String | referObjectId | refer_entity_id | dbc_varchar_1 | dbc 列 | 重命名 + ID→api_key |
| 27 | referLinkApiKey | String | referLinkId | refer_link_id | dbc_varchar_2 | dbc 列 | 重命名 + ID→api_key |
| 28 | dbColumn | String | dbColumn | db_column | dbc_varchar_3 | dbc 列 | 更新 db_column |
| 29 | helpText | String | helpText | help_text | dbc_varchar_4 | dbc 列 | 更新 db_column |
| 30 | helpTextKey | String | helpTextKey | help_text_key | dbc_varchar_5 | dbc 列 | 更新 db_column |
| 31 | typeProperty | String | — | — | dbc_textarea_1 | dbc 列 | **新增** |
| 32 | defaultValue | String | defaultValue | default_value | dbc_textarea_2 | dbc 列 | 更新 db_column |
| 33 | descriptionKey | String | — | — | dbc_varchar_6 | dbc 列 | **新增** |
| 34 | customItemSeq | Integer | — | — | dbc_bigint_1 | dbc 列 | **新增** |
| 35 | enableConfig | Long | — | — | dbc_bigint_2 | dbc 列 | **新增** |
| 36 | enablePackage | Long | — | — | dbc_bigint_3 | dbc 列 | **新增** |
| 37 | columnName | String | — | — | dbc_varchar_7 | dbc 列 | **新增** |
| 38 | enableDeactive | Integer | enableDeactive | enable_deactive | dbc_smallint_8 | dbc 列 | 更新 db_column |
| 39 | compound | Integer | compound | NULL | dbc_smallint_9 | dbc 列 | 分配 db_column |
| 40 | maskPrefix | Integer | maskPrefix | NULL | dbc_int_7 | dbc 列 | 分配 db_column |
| 41 | maskSuffix | Integer | maskSuffix | NULL | dbc_int_8 | dbc 列 | 分配 db_column |
| 42 | encrypt | Integer | encrypt | NULL | dbc_smallint_10 | dbc 列 | 分配 db_column |
| 43 | indexOrder | Integer | indexOrder | NULL | dbc_int_9 | dbc 列 | 分配 db_column |
| 44 | indexType | Integer | indexType | NULL | dbc_int_10 | dbc 列 | 分配 db_column |
| 45 | markdown | Integer | markdown | NULL | dbc_smallint_11 | dbc 列 | 分配 db_column |
| 46 | maskSymbolType | Integer | maskSymbolType | NULL | dbc_int_11 | dbc 列 | 分配 db_column |
| 47 | incrementStrategy | Integer | incrementStrategy | NULL | dbc_int_12 | dbc 列 | 分配 db_column |
| 48 | referItemFilterEnable | Integer | referItemFilterEnable | NULL | dbc_smallint_12 | dbc 列 | 分配 db_column |
| 49 | isComputeMultiCurrencyUnit | String | isComputeMultiCurrencyUnit | NULL | dbc_varchar_8 | dbc 列 | 分配 db_column |
| 50 | format | String | format | NULL | dbc_varchar_9 | dbc 列 | 分配 db_column |
| — | — | — | name | name | — | — | **丢弃** |
| — | — | — | objectId | entity_id | — | — | **→ 固定列 entity_api_key** |
| — | — | — | isExternalId | NULL | — | — | **丢弃** |

## JSON-only 字段 dbc 列分配

以下 13 个字段在老库中无 db_column（仅存在于 metadata_json），现已分配 dbc 列：

| 字段 | dbc 列 | 类型 |
|---|---|---|
| enableDeactive | dbc_smallint_8 | SMALLINT |
| compound | dbc_smallint_9 | SMALLINT |
| maskPrefix | dbc_int_7 | INTEGER |
| maskSuffix | dbc_int_8 | INTEGER |
| encrypt | dbc_smallint_10 | SMALLINT |
| indexOrder | dbc_int_9 | INTEGER |
| indexType | dbc_int_10 | INTEGER |
| markdown | dbc_smallint_11 | SMALLINT |
| maskSymbolType | dbc_int_11 | INTEGER |
| incrementStrategy | dbc_int_12 | INTEGER |
| referItemFilterEnable | dbc_smallint_12 | SMALLINT |
| isComputeMultiCurrencyUnit | dbc_varchar_8 | VARCHAR(255) |
| format | dbc_varchar_9 | VARCHAR(255) |

## 汇总

| 类别 | 数量 |
|---|---|
| 固定列 | 12 |
| 更新 db_column | 10 |
| 重命名 api_key + 更新 db_column | 6 |
| 分配 db_column（老记录有但 db_column 为 NULL） | 13 |
| 新增 p_meta_item 记录 | 7 |
| 丢弃 | 2 |
| **EntityItem.java 业务字段总计** | **50** |

## dbc 列使用汇总

| 列类型 | 使用编号 | 总数 |
|---|---|---|
| dbc_varchar_ | 1~9 | 9 |
| dbc_textarea_ | 1~2 | 2 |
| dbc_int_ | 1~12 | 12 |
| dbc_bigint_ | 1~3 | 3 |
| dbc_smallint_ | 1~12 | 12 |
| dbc_decimal_ | — | 0 |
| **合计** | | **38** |
