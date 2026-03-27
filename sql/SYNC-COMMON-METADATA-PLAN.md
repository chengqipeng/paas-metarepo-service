# p_common_metadata 数据同步方案

## 1. 背景

老库（PG xsy_metarepo）的 `p_common_metadata` 是大宽表，使用 `id`（BIGINT）+ `metamodel_id`（BIGINT）+ `tenant_id` 做主键和关联。

新库（MySQL paas_metarepo_common）的 `p_common_metadata` 也是大宽表，但使用 `metamodel_api_key`（VARCHAR）+ `api_key`（VARCHAR）做联合主键，无 `id`/`tenant_id` 列。

## 2. 核心转换规则

### 2.1 结构转换

| 老库列 | 新库列 | 转换规则 |
|---|---|---|
| `id` | 无 | 丢弃，新表无 id 列 |
| `tenant_id` | 无 | 丢弃，Common 表无 tenant_id（只同步 tenant_id<=0 的数据） |
| `metamodel_id` | `metamodel_api_key` | 通过 p_meta_model 的 id→api_key 映射转换 |
| `parent_object_id` | `parent_entity_api_key` | 通过 p_custom_entity 的 id→api_key 映射转换 |
| `api_key` | `api_key` | 直接复制 |
| `label` | `label` | 直接复制 |
| `label_key` | `label_key` | 直接复制 |
| `namespace` | `namespace` | 直接复制，NULL 默认 'system' |
| `custom_flg` | `custom_flg` | 直接复制 |
| `metadata_order` | `metadata_order` | 直接复制 |
| `owner_id` | `owner_api_key` | 通过 p_meta_module 的 id→api_key 映射转换（如有） |
| `description` | `description` | 直接复制 |
| `delete_flg` | `delete_flg` | 只同步 delete_flg=0 的数据 |
| `dbc_xxx_N` | `dbc_xxx_N` | **按模型逐列处理**（见下方各模型分析） |

### 2.2 dbc_xxx_N 列中的 ID 关联转换

老库的 `dbc_xxx_N` 列中可能存储了 ID 值（BIGINT），需要转为 api_key（VARCHAR）。
具体哪些列需要转换，取决于该 metamodel 的 p_meta_item 定义。

**通用规则：**
- 老 p_meta_item 中 `api_key` 为以下值的字段，其 `db_column` 对应的 dbc 列存储的是 ID，需要转换：
  - `entityId` / `objectId` → 查 p_custom_entity 的 id→api_key
  - `itemId` → 查 p_custom_item 的 id→api_key
  - `referEntityId` → 查 p_custom_entity 的 id→api_key
  - `referLinkId` → 查 p_custom_entity_link 的 id→api_key
  - `checkErrorItemId` → 查 p_custom_item 的 id→api_key
  - `svgId` → 转为字符串（或查 svg 表的 id→api_key）
- 老 p_meta_item 中 `api_key` 为 `tenantId` / `metaModelId` 的字段 → **丢弃**（不写入新库）

### 2.3 老库中被删除的模型字段（不同步到新库）

以下老 p_meta_item 中定义的字段在新 Entity 中已不存在，同步时跳过：

| metamodel | 老字段 api_key | 原因 |
|---|---|---|
| 所有 | `tenantId` | Common 表无 tenant_id |
| 所有 | `metaModelId` | 新表用 metamodel_api_key 固定列替代 |
| entity | `name` / `nameKey` | 新 Entity 类无此字段，用 label 替代 |
| item | `name` / `nameKey` | 同上 |
| entity_link | `name` / `nameKey` | 同上 |
| check_rule | `name` / `nameKey` | 同上 |
| pick_option | `optionCode` | 新 PickOption 无此字段 |

## 3. 各模型同步分析

### 3.1 entity（对象定义）

老库 metamodel_api_key（或 metamodel_id 对应的 api_key）= `entity`

**新 Entity.java 字段 → 期望的 p_meta_item api_key → db_column：**

| Entity 字段 | p_meta_item api_key | 类型 | ID转换 | 说明 |
|---|---|---|---|---|
| entityType | entity_type | select | 否 | |
| svgApiKey | svg_api_key | varchar | 是(svgId→api_key) | 老库存 svgId(BIGINT) |
| svgColor | svg_color | varchar | 否 | |
| descriptionKey | description_key | varchar | 否 | |
| customEntitySeq | custom_entity_seq | integer | 否 | |
| enableFlg | enable_flg | tinyint | 否 | |
| customFlg | custom_flg | tinyint | 否 | 也在固定列，dbc 列优先 |
| businessCategory | business_category | select | 否 | |
| typeProperty | type_property | varchar | 否 | |
| dbTable | db_table | varchar | 否 | |
| detailFlg | detail_flg | tinyint | 否 | |
| enableTeam | enable_team | tinyint | 否 | |
| enableSocial | enable_social | tinyint | 否 | |
| enableConfig | enable_config | integer | 否 | |
| hiddenFlg | hidden_flg | tinyint | 否 | |
| searchable | searchable | tinyint | 否 | |
| enableSharing | enable_sharing | tinyint | 否 | |
| enableScriptTrigger | enable_script_trigger | tinyint | 否 | |
| enableActivity | enable_activity | tinyint | 否 | |
| enableHistoryLog | enable_history_log | select | 否 | |
| enableReport | enable_report | select | 否 | |
| enableRefer | enable_refer | select | 否 | |
| enableApi | enable_api | select | 否 | |
| enableFlow | enable_flow | integer | 否 | |
| enablePackage | enable_package | integer | 否 | |
| extendProperty | extend_property | varchar | 否 | |

**丢弃字段：** name, nameKey, tenantId, metaModelId, objectId/entityId

### 3.2 item（字段定义）

| Entity 字段 | p_meta_item api_key | 类型 | ID转换 | 说明 |
|---|---|---|---|---|
| entityApiKey | entity_api_key | — | — | 走固定列 parent_entity_api_key |
| itemType | item_type | select | 否 | |
| dataType | data_type | select | 否 | |
| typeProperty | type_property | textarea | 否 | |
| helpText | help_text | varchar | 否 | |
| helpTextKey | help_text_key | varchar | 否 | |
| descriptionKey | description_key | varchar | 否 | |
| customItemSeq | custom_item_seq | integer | 否 | |
| defaultValue | default_value | textarea | 否 | |
| requireFlg | require_flg | tinyint | 否 | |
| customFlg | custom_flg | tinyint | 否 | |
| enableFlg | enable_flg | tinyint | 否 | |
| creatable | creatable | tinyint | 否 | |
| updatable | updatable | tinyint | 否 | |
| uniqueKeyFlg | unique_key_flg | tinyint | 否 | |
| enableHistoryLog | enable_history_log | tinyint | 否 | |
| enableConfig | enable_config | integer | 否 | |
| enablePackage | enable_package | integer | 否 | |
| readonlyStatus | readonly_status | tinyint | 否 | |
| visibleStatus | visible_status | tinyint | 否 | |
| hiddenFlg | hidden_flg | tinyint | 否 | |
| referEntityApiKey | refer_entity_api_key | varchar | 是(referEntityId→api_key) | 老库存 referEntityId(BIGINT) |
| referLinkApiKey | refer_link_api_key | varchar | 是(referLinkId→api_key) | 老库存 referLinkId(BIGINT) |
| dbColumn | db_column | varchar | 否 | |
| itemOrder | item_order | select | 否 | |
| sortFlg | sort_flg | select | 否 | |
| columnName | column_name | varchar | 否 | |

**丢弃字段：** name, nameKey, tenantId, metaModelId, entityId(→固定列)

**ID 转换：**
- `referEntityId` 的 db_column 对应的 dbc 列 → 查 p_custom_entity id→api_key
- `referLinkId` 的 db_column 对应的 dbc 列 → 查 p_custom_entity_link id→api_key

### 3.3 pick_option（选项值）

| Entity 字段 | p_meta_item api_key | 类型 | ID转换 | 说明 |
|---|---|---|---|---|
| entityApiKey | entity_api_key | — | — | 走固定列 parent_entity_api_key |
| itemApiKey | item_api_key | varchar | 是(itemId→api_key) | 老库存 itemId(BIGINT) |
| optionOrder | option_order | select | 否 | |
| defaultFlg | default_flg | tinyint | 否 | |
| globalFlg | global_flg | tinyint | 否 | |
| customFlg | custom_flg | tinyint | 否 | |
| enableFlg | enable_flg | tinyint | 否 | |
| descriptionKey | description_key | varchar | 否 | |

**丢弃字段：** optionCode, optionLabel(→label), tenantId, metaModelId, entityId(→固定列)

**ID 转换：**
- `itemId` 的 db_column 对应的 dbc 列 → 查 p_custom_item id→api_key

### 3.4 entity_link（关联关系）

| Entity 字段 | p_meta_item api_key | 类型 | ID转换 | 说明 |
|---|---|---|---|---|
| typeProperty | type_property | varchar | 否 | |
| linkType | link_type | select | 否 | |
| parentEntityApiKey | parent_entity_api_key | varchar | 是(parentEntityId→api_key) | |
| childEntityApiKey | child_entity_api_key | varchar | 是(childEntityId→api_key) | |
| detailLink | detail_link | tinyint | 否 | |
| cascadeDelete | cascade_delete | tinyint | 否 | |
| accessControl | access_control | tinyint | 否 | |
| enableFlg | enable_flg | tinyint | 否 | |
| descriptionKey | description_key | varchar | 否 | |

**丢弃字段：** name, nameKey, tenantId, metaModelId

**ID 转换：**
- `parentEntityId` → 查 p_custom_entity id→api_key
- `childEntityId` → 查 p_custom_entity id→api_key

### 3.5 check_rule（校验规则）

| Entity 字段 | p_meta_item api_key | 类型 | ID转换 | 说明 |
|---|---|---|---|---|
| entityApiKey | entity_api_key | — | — | 走固定列 parent_entity_api_key |
| activeFlg | active_flg | tinyint | 否 | |
| descriptionKey | description_key | varchar | 否 | |
| checkFormula | check_formula | textarea | 否 | |
| checkErrorMsg | check_error_msg | textarea | 否 | |
| checkErrorMsgKey | check_error_msg_key | varchar | 否 | |
| checkErrorLocation | check_error_location | select | 否 | |
| checkErrorItemApiKey | check_error_item_api_key | varchar | 是(checkErrorItemId→api_key) | |
| checkAllItemsFlg | check_all_items_flg | tinyint | 否 | |
| checkErrorWay | check_error_way | select | 否 | |

**丢弃字段：** name, nameKey, tenantId, metaModelId, entityId(→固定列)

**ID 转换：**
- `checkErrorItemId` → 查 p_custom_item id→api_key

### 3.6 refer_filter（关联过滤条件）

| Entity 字段 | p_meta_item api_key | 类型 | ID转换 | 说明 |
|---|---|---|---|---|
| entityApiKey | entity_api_key | — | — | 走固定列 parent_entity_api_key |
| itemApiKey | item_api_key | varchar | 是(itemId→api_key) | |
| linkApiKey | link_api_key | varchar | 是(linkId→api_key) | |
| filterField | filter_field | varchar | 否 | |
| filterOperator | filter_operator | varchar | 否 | |
| filterValue | filter_value | varchar | 否 | |
| filterOrder | filter_order | select | 否 | |
| descriptionKey | description_key | varchar | 否 | |

**丢弃字段：** tenantId, metaModelId, entityId(→固定列)

**ID 转换：**
- `itemId` → 查 p_custom_item id→api_key
- `linkId` → 查 p_custom_entity_link id→api_key

## 4. 同步流程

```
Step 1: 构建 ID→api_key 映射表（从老 PG 查询）
  - p_meta_model: id → api_key
  - p_custom_entity: id → api_key
  - p_custom_item: id → api_key
  - p_custom_entity_link: id → api_key

Step 2: 从老 PG 查询 p_meta_item（获取每个 metamodel 的列映射）
  - 构建 {metamodel_id: {old_api_key: db_column}} 映射
  - 识别需要 ID 转换的字段（entityId, itemId, referEntityId 等）
  - 识别需要丢弃的字段（tenantId, metaModelId, name, nameKey 等）

Step 3: 从老 PG 查询 p_common_metadata（WHERE tenant_id<=0 AND delete_flg=0）
  - 按 metamodel_id 分组处理

Step 4: 逐行转换
  4.1 固定列转换：
    - metamodel_id → metamodel_api_key（查 Step 1 映射）
    - parent_object_id → parent_entity_api_key（查 Step 1 映射）
    - owner_id → owner_api_key（查 Step 1 映射，如有）
    - 去掉 id, tenant_id
    - namespace 默认 'system'

  4.2 dbc_xxx_N 列转换（按 Step 2 的列映射逐列处理）：
    - 该列对应的 old_api_key 在丢弃列表中 → 置 NULL
    - 该列对应的 old_api_key 需要 ID 转换 → 查 Step 1 映射替换值
    - 该列对应的 old_api_key 需要重命名（entityId→entityApiKey）→ 值不变，但新 p_meta_item 的 api_key 已改
    - 其他 → 直接复制

Step 5: 批量 INSERT INTO 新 MySQL p_common_metadata

Step 6: 验证数据量
```

## 5. 需要 ID 转换的 dbc 列汇总

| 老 api_key | 涉及 metamodel | 转换方式 | 查询表 |
|---|---|---|---|
| entityId / objectId | item, pick_option, check_rule, refer_filter | → parent_entity_api_key（固定列） | p_custom_entity |
| parentEntityId | entity_link | dbc 列值 id→api_key | p_custom_entity |
| childEntityId | entity_link | dbc 列值 id→api_key | p_custom_entity |
| referEntityId | item | dbc 列值 id→api_key | p_custom_entity |
| referLinkId | item | dbc 列值 id→api_key | p_custom_entity_link |
| itemId | pick_option, refer_filter | dbc 列值 id→api_key | p_custom_item |
| checkErrorItemId | check_rule | dbc 列值 id→api_key | p_custom_item |
| linkId | refer_filter | dbc 列值 id→api_key | p_custom_entity_link |
| svgId | entity | dbc 列值 id→字符串 | 直接 str(id) 或查 svg 表 |

## 6. 需要丢弃的 dbc 列汇总

| 老 api_key | 涉及 metamodel | 原因 |
|---|---|---|
| tenantId | 所有 | Common 表无 tenant_id |
| metaModelId | 所有 | 新表用 metamodel_api_key 固定列 |
| name | entity, item, entity_link, check_rule | 新 Entity 无此字段 |
| nameKey | entity, item, entity_link, check_rule | 新 Entity 无此字段 |
| optionCode | pick_option | 新 PickOption 无此字段 |
| optionLabel | pick_option | 已合并到固定列 label |
