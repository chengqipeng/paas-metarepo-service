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

## 1. xobject（对象定义，892 行）

老 p_meta_item 中无 db_column 的字段（JSON only）：
enableDynamicFeed, enableGroupMember, isArchived, enableScriptExecutor,
enableDuplicaterule, enableCheckrule, enableBusitype
→ 这些字段在新 Entity.java 中**新增**，需要分配 dbc 列

| JSON key | 老 db_column | 新 db_column | 处理                                         |
|---|---|---|--------------------------------------------|
| apiKey | api_key | 固定列 api_key | 直接映射                                       |
| label | label | 固定列 label | 直接映射                                       |
| labelKey | label_key | 固定列 label_key | 直接映射                                       |
| namespace | namespace | 固定列 namespace | 直接映射                                       |
| description | description | 固定列 description | 直接映射                                       |
| isCustom | custom_flg | 固定列 custom_flg | 直接映射                                       |
| isDelete | delete_flg | 固定列 delete_flg | 直接映射                                       |
| name | name | **丢弃** | 新 Entity 无此字段                              |
| objectId | object_id | **丢弃** | 老库内部 ID，Entity 是顶层对象无父引用，entity_api_key 留空 |
| objectType | object_type | → dbc_int_1 | select→int                                 |
| iconId | svg_id | → dbc_varchar_1 | ID→字符串                                     |
| businessCategory | business_category | → dbc_int_2 | select→int                                 |
| customEntitySeq | custom_entityseq | → dbc_bigint_1 | integer→bigint                             |
| isActive | enable_flg | → dbc_smallint_1 | tinyint→smallint                           |
| isDetail | detail_flg | → dbc_smallint_2 | tinyint→smallint                           |
| enableHistoryLog | enable_history_log | → dbc_smallint_3 | tinyint→smallint                           |
| dbTable | db_table | → dbc_varchar_2 | 不变                                         |
| enableDynamicFeed | 无（JSON only） | → dbc_smallint_4 | **新增**                                     |
| enableGroupMember | 无（JSON only） | → dbc_smallint_5 | **新增**                                     |
| isArchived | 无（JSON only） | → dbc_smallint_6 | **新增**                                     |
| enableScriptExecutor | 无（JSON only） | → dbc_smallint_7 | **新增**                                     |
| enableDuplicaterule | 无（JSON only） | → dbc_smallint_8 | **新增**                                     |
| enableCheckrule | 无（JSON only） | → dbc_smallint_9 | **新增**                                     |
| enableBusitype | 无（JSON only） | → dbc_smallint_10 | **新增**                                     |
| createdAt/createdBy/updatedAt/updatedBy | 审计列 | 固定列 | 直接映射                                       |

**新 p_meta_item 更新（xobject）：**
- `object_type` → `dbc_int_1`
- `svg_id` → `dbc_varchar_1`（类型从 bigint 改为 varchar，存 api_key）
- `business_category` → `dbc_int_2`
- `custom_entityseq` → `dbc_bigint_1`
- `enable_flg` → `dbc_smallint_1`
- `detail_flg` → `dbc_smallint_2`
- `enable_history_log` → `dbc_smallint_3`
- `db_table` → `dbc_varchar_2`
- `enable_dynamic_feed` → `dbc_smallint_4`（**新增 p_meta_item 记录**）
- `enable_group_member` → `dbc_smallint_5`（**新增**）
- `is_archived` → `dbc_smallint_6`（**新增**）
- `enable_script_executor` → `dbc_smallint_7`（**新增**）
- `enable_duplicaterule` → `dbc_smallint_8`（**新增**）
- `enable_checkrule` → `dbc_smallint_9`（**新增**）
- `enable_busitype` → `dbc_smallint_10`（**新增**）

---

## 2. item（字段定义，24,028 行）

老 p_meta_item 中无 db_column 的字段（JSON only）：
compound, isExternalId, maskSuffix, maskPrefix, encrypt, indexOrder,
indexType, markdown, maskSymbolType, incrementStrategy,
referItemFilterEnable, isComputeMultiCurrencyUnit, format
→ 新 EntityItem.java 中不存在的 **丢弃**

| JSON key | 老 db_column | 新 db_column          | 处理 |
|---|---|----------------------|---|
| apiKey | api_key | 固定列 api_key          | |
| label | label | 固定列 label            | |
| labelKey | label_key | 固定列 label_key        | |
| namespace | namespace | 固定列 namespace        | |
| description | description | 固定列 description      | |
| isCustom | custom_flg | 固定列 custom_flg       | |
| isDeleted | delete_flg | 固定列 delete_flg       | |
| name | name | **丢弃**               | |
| objectId | entity_id | → 固定列 entity_api_key | ID→api_key |
| itemType | item_type | → dbc_int_1          | select→int |
| dataType | data_type | → dbc_int_2          | select→int |
| itemOrder | item_order | → dbc_int_3          | select→int |
| readonlyStatus | readonly_status | → dbc_int_4          | select→int |
| visibleStatus | visible_status | → dbc_int_5          | select→int |
| sortable | sort_flg | → dbc_int_6          | select→int |
| isRequired | require_flg | → dbc_smallint_1     | tinyint→smallint |
| isActive | enable_flg | → dbc_smallint_2     | tinyint→smallint |
| isHidden | hidden_flg | → dbc_smallint_3     | tinyint→smallint |
| isUniqueKey | unique_key_flg | → dbc_smallint_4     | tinyint→smallint |
| creatable | creatable | → dbc_smallint_5     | tinyint→smallint |
| updatable | updatable | → dbc_smallint_6     | tinyint→smallint |
| enableHistoryLog | enable_historylog | → dbc_smallint_7     | tinyint→smallint |
| enableDeactive | enable_deactive | → dbc_smallint_8     | tinyint→smallint |
| referObjectId | refer_entity_id | → dbc_varchar_1      | ID→api_key |
| referLinkId | refer_link_id | → dbc_varchar_2      | ID→api_key |
| dbColumn | db_column | → dbc_varchar_3      | 不变 |
| helpText | help_text | → dbc_varchar_4      | 不变 |
| helpTextKey | help_text_key | → dbc_varchar_5      | 不变 |
| defaultValue | default_value | → dbc_textarea_1     | 不变 |
| customEntitySeq | custom_entityseq（无） | → dbc_bigint_1       | 新增 |

**ID 转换字段：**
- `objectId`（entity_id 列的值）→ 查 p_custom_entity id→api_key → 写入 entity_api_key
- `referObjectId`（refer_entity_id 列的值）→ 查 p_custom_entity id→api_key → 写入 dbc_varchar_1
- `referLinkId`（refer_link_id 列的值）→ 查 p_custom_entity_link id→api_key → 写入 dbc_varchar_2

---

## 3. pickOption（选项值，18,218 行）

| JSON key | 老 db_column | 新 db_column | 处理 |
|---|---|---|---|
| optionApiKey | api_key | 固定列 api_key | |
| namespace | namespace | 固定列 namespace | |
| description | description | 固定列 description | |
| isCustom | custom_flg | 固定列 custom_flg | |
| isDeleted | delete_flg | 固定列 delete_flg | |
| objectId | entity_id | → 固定列 object_api_key | ID→api_key |
| itemId | item_id | → dbc_varchar_1 | ID→api_key |
| optionLabel | option_label | → 固定列 label | 映射到 label |
| optionLabelKey | option_label_key | → 固定列 label_key | 映射到 label_key |
| optionCode | option_code | → dbc_int_1 | select→int |
| optionOrder | option_order | → dbc_int_2 | select→int |
| isDefault | default_flg | → dbc_smallint_1 | tinyint→smallint |
| isActive | enable_flg | → dbc_smallint_2 | tinyint→smallint |
| isGlobal | global_flg | → dbc_smallint_3 | tinyint→smallint |
| specialFlg | special_flg | → dbc_smallint_4 | tinyint→smallint |

**ID 转换：**
- `objectId` → p_custom_entity id→api_key → entity_api_key
- `itemId` → p_custom_item id→api_key → dbc_varchar_1

**特殊处理：**
- `optionLabel` → 写入固定列 `label`（新表统一用 label）
- `optionLabelKey` → 写入固定列 `label_key`

---

## 4. xobjectLink（关联关系，7,024 行）

| JSON key | 老 db_column | 新 db_column | 处理 |
|---|---|---|---|
| apiKey | api_key | 固定列 api_key | |
| label | label | 固定列 label | |
| labelKey | label_key | 固定列 label_key | |
| namespace | namespace | 固定列 namespace | |
| description | description | 固定列 description | |
| isDeleted | delete_flg | 固定列 delete_flg | |
| name | name | **丢弃** | |
| parentObjectId | parent_entity_id | → dbc_varchar_1 | ID→api_key |
| childObjectId | child_entity_id | → dbc_varchar_2 | ID→api_key |
| referItemId | refer_item_id | → dbc_varchar_3 | ID→api_key |
| linkType | link_type | → dbc_int_1 | select→int |
| cascadeDelete | cascade_delete | → dbc_int_2 | select→int |
| accessControl | access_control | → dbc_int_3 | select→int |
| isActive | enable_flg | → dbc_smallint_1 | tinyint→smallint |
| isDetailLink | detail_link | → dbc_smallint_2 | tinyint→smallint |

**ID 转换：**
- `parentObjectId` → p_custom_entity id→api_key → dbc_varchar_1
- `childObjectId` → p_custom_entity id→api_key → dbc_varchar_2
- `referItemId` → p_custom_item id→api_key → dbc_varchar_3

---

## 5. checkRule（校验规则，36 行）

| JSON key | 老 db_column | 新 db_column | 处理 |
|---|---|---|---|
| apiKey | api_key | 固定列 api_key | |
| namespace | namespace | 固定列 namespace | |
| description | description | 固定列 description | |
| name | name | **丢弃** | |
| objectId | object_id | → 固定列 object_api_key | ID→api_key |
| ruleLabel | rule_label | → 固定列 label | 映射到 label |
| ruleLabelKey | rule_label_key | → 固定列 label_key | 映射到 label_key |
| activeFlg | active_flg | → dbc_smallint_1 | tinyint→smallint |
| checkAllItemsFlg | check_all_items_flg | → dbc_smallint_2 | tinyint→smallint |
| checkErrorLocation | check_error_location | → dbc_int_1 | select→int |
| checkErrorWay | check_error_way | → dbc_int_2 | select→int |
| checkErrorItemId | check_error_item_id | → dbc_varchar_1 | ID→api_key |
| checkFormula | check_formula | → dbc_textarea_1 | 不变 |
| checkErrorMsg | check_error_msg | → dbc_textarea_2 | 不变 |
| checkErrorMsgKey | check_error_msg_key | → dbc_varchar_2 | 不变 |

**ID 转换：**
- `objectId` → p_custom_entity id→api_key → object_api_key
- `checkErrorItemId` → p_custom_item id→api_key → dbc_varchar_1

**特殊处理：**
- `ruleLabel` → 写入固定列 `label`
- `ruleLabelKey` → 写入固定列 `label_key`

---

## 6. 迁移后 p_meta_item 需要同步更新的 db_column

迁移完成后，`p_meta_item` 表中的 `db_column` 值必须同步更新为新前缀+新编号，
否则运行时 `CommonMetadataConverter` 的列映射会对不上。

更新规则按上述各模型的映射表执行。
