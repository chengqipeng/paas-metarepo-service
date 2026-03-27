-- ============================================================
-- 从老表迁移数据到新表（单库模式，开发环境）
-- 源库：paas_metarepo（老表结构，id-based）
-- 目标库：paas_metarepo（新表结构，api_key-based）
--
-- 执行前提：
--   1. 已在目标库执行 init_fresh_single_db.sql 创建新表（不含种子数据部分）
--   2. 老库数据仍在原 paas_metarepo 中
--
-- 执行策略：
--   先将老库 rename 为 paas_metarepo_old，
--   再创建新库 paas_metarepo 并建表，
--   最后从 paas_metarepo_old 迁移数据到 paas_metarepo
--
-- 字段映射规则：
--   - id/tenant_id 列在元模型表中丢弃（新表 PK=api_key）
--   - metamodel_id → metamodel_api_key（通过 p_meta_model 关联转换）
--   - entity_id → entity_api_key（通过 p_custom_entity 关联转换）
--   - item_id → item_api_key（通过 p_custom_item 关联转换）
--   - object_type → entity_type
--   - svg_id → svg_api_key（暂置 NULL，老表是 BIGINT）
--   - name_space → namespace
--   - custom_entityseq → custom_entity_seq
--   - custom_itemseq → custom_item_seq
--   - option_label → label, option_label_key → label_key
--   - rule_label → label, rule_label_key → label_key
--   - object_id → entity_api_key（通过 p_custom_entity 关联转换）
--   - parent_entity_id → parent_entity_api_key
--   - child_entity_id → child_entity_api_key
--   - check_error_item_id → check_error_item_api_key（暂置 NULL）
--   - refer_entity_id → refer_entity_api_key
--   - refer_link_id → refer_link_api_key（暂置 NULL）
--   - name/name_key 列在新表中已删除，不迁移
--   - custom_flg=0 的数据 → p_common_xxx（Common 级）
--   - custom_flg=1 或 custom_flg IS NULL 的数据 → p_tenant_xxx（Tenant 级）
-- ============================================================

-- ************************************************************
-- 步骤 0：准备工作
-- ************************************************************

-- 假设老库已 rename 为 paas_metarepo_old，新库为 paas_metarepo
-- 如果还没做，执行：
-- RENAME DATABASE paas_metarepo TO paas_metarepo_old;  -- MySQL 不支持，需要手动操作
-- 替代方案：
--   1. CREATE DATABASE paas_metarepo_new;
--   2. 在 paas_metarepo_new 中执行 init_fresh_single_db.sql 的建表部分（不含种子数据）
--   3. 下面的 SQL 从 paas_metarepo（老库）迁移到 paas_metarepo_new（新库）
--   4. 迁移完成后 rename：
--      RENAME TABLE paas_metarepo.xxx TO paas_metarepo_old.xxx;  -- 逐表移走老表
--      RENAME TABLE paas_metarepo_new.xxx TO paas_metarepo.xxx;  -- 逐表移入新表

-- 为简化脚本，下面使用 old_db / new_db 前缀标记
-- 实际执行时请替换为真实库名

SET @old_db = 'paas_metarepo_old';
SET @new_db = 'paas_metarepo';

-- ************************************************************
-- 1. p_meta_model（元模型定义）
-- 老表：id(BIGINT PK), tenant_id, api_key, label, label_key, ...
-- 新表：api_key(VARCHAR PK), namespace, label, label_key, ...
-- 变更：丢弃 id/tenant_id，新增 namespace/enable_common/enable_tenant/description_key/entity_dependency
--       xobject_dependency → entity_dependency
-- ************************************************************

INSERT INTO paas_metarepo.p_meta_model (
    api_key, namespace, label, label_key,
    metamodel_type, enable_package, enable_app,
    enable_deprecation, enable_deactivation,
    enable_delta, enable_log, delta_scope, delta_mode,
    enable_module_control, enable_common, enable_tenant,
    db_table, description, description_key,
    entity_dependency, visible,
    delete_flg, created_by, created_at, updated_by, updated_at
)
SELECT
    o.api_key,
    COALESCE(o.namespace, 'system') AS namespace,
    o.label,
    o.label_key,
    o.metamodel_type,
    o.enable_package,
    COALESCE(o.enable_app, 0),
    COALESCE(o.enable_deprecation, 0),
    COALESCE(o.enable_deactivation, 0),
    COALESCE(o.enable_delta, 0),
    COALESCE(o.enable_log, 0),
    o.delta_scope,
    o.delta_mode,
    COALESCE(o.enable_module_control, 0),
    COALESCE(o.enable_common, 1),
    COALESCE(o.enable_tenant, 1),
    o.db_table,
    o.description,
    o.description_key,
    COALESCE(o.entity_dependency, o.xobject_dependency, 0),
    COALESCE(o.visible, 0),
    COALESCE(o.delete_flg, 0),
    o.created_by, o.created_at, o.updated_by, o.updated_at
FROM paas_metarepo_old.p_meta_model o
WHERE o.delete_flg = 0 OR o.delete_flg IS NULL;


-- ************************************************************
-- 2. p_meta_item（元模型字段项定义）
-- 老表：id, tenant_id, metamodel_id(BIGINT), api_key, ...
-- 新表：metamodel_api_key(VARCHAR), api_key, namespace, ...
-- 变更：metamodel_id → metamodel_api_key（通过 p_meta_model 关联）
--       丢弃 id/tenant_id，新增 namespace/text_format
-- ************************************************************

INSERT INTO paas_metarepo.p_meta_item (
    metamodel_api_key, api_key, namespace, label, label_key,
    item_type, data_type, item_order, require_flg, unique_key_flg,
    creatable, updatable, enable_package, enable_delta, enable_log,
    db_column, description, description_key,
    min_length, max_length, text_format, json_schema, name_field,
    delete_flg, created_by, created_at, updated_by, updated_at
)
SELECT
    m.api_key AS metamodel_api_key,
    o.api_key,
    COALESCE(o.namespace, 'system') AS namespace,
    o.label,
    o.label_key,
    o.item_type, o.data_type, o.item_order, o.require_flg,
    COALESCE(o.unique_key_flg, 0),
    o.creatable, o.updatable, o.enable_package,
    COALESCE(o.enable_delta, 0),
    COALESCE(o.enable_log, 0),
    o.db_column, o.description, o.description_key,
    o.min_length, o.max_length,
    NULL AS text_format,
    o.json_schema,
    COALESCE(o.name_field, 0),
    COALESCE(o.delete_flg, 0),
    o.created_by, o.created_at, o.updated_by, o.updated_at
FROM paas_metarepo_old.p_meta_item o
INNER JOIN paas_metarepo_old.p_meta_model m ON o.metamodel_id = m.id
WHERE o.delete_flg = 0 OR o.delete_flg IS NULL;


-- ************************************************************
-- 3. p_meta_link（元模型关联关系）
-- 老表：id, tenant_id, api_key, label, label_key, name, name_key,
--       refer_item_id, child_metamodel_id, parent_metamodel_id, ...
-- 新表：api_key(PK), namespace, label, label_key,
--       refer_item_api_key(BIGINT), child_metamodel_api_key(BIGINT),
--       parent_metamodel_api_key(BIGINT), ...
-- 变更：丢弃 id/tenant_id/name/name_key，新增 namespace
--       refer_item_id → refer_item_api_key
--       child_metamodel_id → child_metamodel_api_key
--       parent_metamodel_id → parent_metamodel_api_key
-- 注意：新表中这三个字段仍是 BIGINT 类型（保持老值）
-- ************************************************************

INSERT INTO paas_metarepo.p_meta_link (
    api_key, namespace, label, label_key,
    link_type, refer_item_api_key,
    child_metamodel_api_key, parent_metamodel_api_key,
    cascade_delete, description, description_key,
    delete_flg, created_by, created_at, updated_by, updated_at
)
SELECT
    o.api_key,
    COALESCE(o.namespace, 'system') AS namespace,
    o.label,
    o.label_key,
    o.link_type,
    o.refer_item_id AS refer_item_api_key,
    o.child_metamodel_id AS child_metamodel_api_key,
    o.parent_metamodel_id AS parent_metamodel_api_key,
    o.cascade_delete,
    o.description,
    o.description_key,
    COALESCE(o.delete_flg, 0),
    o.created_by, o.created_at, o.updated_by, o.updated_at
FROM paas_metarepo_old.p_meta_link o
WHERE o.delete_flg = 0 OR o.delete_flg IS NULL;


-- ************************************************************
-- 4. p_meta_option（元模型选项值定义）
-- 老表：id, tenant_id, metamodel_id, item_id, option_code,
--       option_label, option_label_key, option_order, ...
-- 新表：metamodel_api_key, item_api_key, api_key, namespace,
--       label, label_key, option_key, option_order, ...
-- 变更：metamodel_id → metamodel_api_key, item_id → item_api_key
--       option_label → label, option_label_key → label_key
--       option_code → option_key（转为 api_key）
--       丢弃 id/tenant_id，新增 namespace
-- ************************************************************

INSERT INTO paas_metarepo.p_meta_option (
    metamodel_api_key, item_api_key, api_key, namespace,
    label, label_key, option_key, option_order,
    default_flg, enable_flg, description, description_key,
    delete_flg, created_by, created_at, updated_by, updated_at
)
SELECT
    m.api_key AS metamodel_api_key,
    i.api_key AS item_api_key,
    COALESCE(o.api_key, CAST(o.option_code AS CHAR)) AS api_key,
    COALESCE(o.namespace, 'system') AS namespace,
    o.option_label AS label,
    o.option_label_key AS label_key,
    CAST(o.option_code AS CHAR) AS option_key,
    o.option_order,
    COALESCE(o.default_flg, 0),
    COALESCE(o.enable_flg, 1),
    o.description,
    o.description_key,
    COALESCE(o.delete_flg, 0),
    o.created_by, o.created_at, o.updated_by, o.updated_at
FROM paas_metarepo_old.p_meta_option o
INNER JOIN paas_metarepo_old.p_meta_model m ON o.metamodel_id = m.id
INNER JOIN paas_metarepo_old.p_meta_item i ON o.item_id = i.id
WHERE o.delete_flg = 0 OR o.delete_flg IS NULL;


-- ************************************************************
-- 5. p_custom_entity → p_common_entity + p_tenant_entity
-- 老表：id, tenant_id, name_space, object_id, name, api_key, label,
--       label_key, object_type, svg_id, svg_color, description,
--       custom_entityseq, delete_flg, enable_flg, custom_flg, ...
-- 新表 Common：api_key(PK), namespace, label, label_key,
--       entity_type, svg_api_key, svg_color, description, description_key,
--       custom_entity_seq, ...（无 tenant_id）
-- 新表 Tenant：tenant_id, api_key(PK), ...（有 tenant_id）
-- 变更：object_type → entity_type, svg_id → svg_api_key(NULL)
--       name_space → namespace, custom_entityseq → custom_entity_seq
--       丢弃 id/object_id/name/name_key
--       custom_flg=0 → p_common_entity, custom_flg=1 → p_tenant_entity
-- ************************************************************

-- 5a. Common 级（custom_flg=0，标准对象）
INSERT INTO paas_metarepo.p_common_entity (
    api_key, namespace, label, label_key,
    entity_type, svg_api_key, svg_color,
    description, description_key, custom_entity_seq,
    delete_flg, enable_flg, custom_flg, business_category,
    type_property, db_table, detail_flg,
    enable_team, enable_social, enable_config,
    hidden_flg, searchable, enable_sharing,
    enable_script_trigger, enable_activity, enable_history_log,
    enable_report, enable_refer, enable_api,
    enable_flow, enable_package, extend_property,
    created_at, created_by, updated_at, updated_by
)
SELECT
    o.api_key,
    COALESCE(o.name_space, 'system') AS namespace,
    o.label,
    o.label_key,
    o.object_type AS entity_type,
    NULL AS svg_api_key,
    o.svg_color,
    o.description,
    o.description_key,
    o.custom_entityseq AS custom_entity_seq,
    COALESCE(o.delete_flg, 0),
    COALESCE(o.enable_flg, 1),
    0 AS custom_flg,
    o.business_category,
    o.type_property, o.db_table, o.detail_flg,
    o.enable_team, o.enable_social, o.enable_config,
    o.hidden_flg, o.searchable, o.enable_sharing,
    o.enable_script_trigger, o.enable_activity, o.enable_history_log,
    o.enable_report, o.enable_refer, o.enable_api,
    o.enable_flow, o.enable_package, o.extend_property,
    o.created_at, o.created_by, o.updated_at, o.updated_by
FROM paas_metarepo_old.p_custom_entity o
WHERE o.custom_flg = 0
  AND (o.delete_flg = 0 OR o.delete_flg IS NULL)
ON DUPLICATE KEY UPDATE updated_at = VALUES(updated_at);

-- 5b. Tenant 级（custom_flg=1 或 custom_flg IS NULL，租户自定义对象）
INSERT INTO paas_metarepo.p_tenant_entity (
    tenant_id, api_key, namespace, label, label_key,
    entity_type, svg_api_key, svg_color,
    description, description_key, custom_entity_seq,
    delete_flg, enable_flg, custom_flg, business_category,
    type_property, db_table, detail_flg,
    enable_team, enable_social, enable_config,
    hidden_flg, searchable, enable_sharing,
    enable_script_trigger, enable_activity, enable_history_log,
    enable_report, enable_refer, enable_api,
    enable_flow, enable_package, extend_property,
    created_at, created_by, updated_at, updated_by
)
SELECT
    o.tenant_id,
    o.api_key,
    COALESCE(o.name_space, 'custom') AS namespace,
    o.label,
    o.label_key,
    o.object_type AS entity_type,
    NULL AS svg_api_key,
    o.svg_color,
    o.description,
    o.description_key,
    o.custom_entityseq AS custom_entity_seq,
    COALESCE(o.delete_flg, 0),
    COALESCE(o.enable_flg, 1),
    COALESCE(o.custom_flg, 1),
    o.business_category,
    o.type_property, o.db_table, o.detail_flg,
    o.enable_team, o.enable_social, o.enable_config,
    o.hidden_flg, o.searchable, o.enable_sharing,
    o.enable_script_trigger, o.enable_activity, o.enable_history_log,
    o.enable_report, o.enable_refer, o.enable_api,
    o.enable_flow, o.enable_package, o.extend_property,
    o.created_at, o.created_by, o.updated_at, o.updated_by
FROM paas_metarepo_old.p_custom_entity o
WHERE (o.custom_flg = 1 OR o.custom_flg IS NULL)
  AND (o.delete_flg = 0 OR o.delete_flg IS NULL);


-- ************************************************************
-- 6. p_custom_item → p_common_item + p_tenant_item
-- 老表：id, tenant_id, entity_id, name, api_key, label, label_key,
--       item_type, data_type, type_property, help_text, help_text_key,
--       description, custom_itemseq, default_value, require_flg,
--       delete_flg, custom_flg, enable_flg, creatable, updatable,
--       unique_key_flg, enable_history_log, enable_config, enable_package,
--       readonly_status, visible_status, hidden_flg,
--       refer_entity_id, refer_link_id, db_column, item_order, sort_flg,
--       column_name, ...
-- 新表：entity_api_key, api_key, namespace, label, label_key, ...
--       refer_entity_api_key, refer_link_api_key, ...
-- 变更：entity_id → entity_api_key, refer_entity_id → refer_entity_api_key
--       refer_link_id → refer_link_api_key(NULL)
--       custom_itemseq → custom_item_seq
--       丢弃 id/tenant_id/name/name_key
-- ************************************************************

-- 6a. Common 级（custom_flg=0）
INSERT INTO paas_metarepo.p_common_item (
    entity_api_key, api_key, namespace, label, label_key,
    item_type, data_type, type_property,
    help_text, help_text_key, description, description_key,
    custom_item_seq, default_value,
    require_flg, delete_flg, custom_flg, enable_flg,
    creatable, updatable, unique_key_flg,
    enable_history_log, enable_config, enable_package,
    readonly_status, visible_status, hidden_flg,
    refer_entity_api_key, refer_link_api_key,
    db_column, item_order, sort_flg, column_name,
    created_at, created_by, updated_at, updated_by
)
SELECT
    e.api_key AS entity_api_key,
    o.api_key,
    COALESCE(o.name_space, 'system') AS namespace,
    o.label,
    o.label_key,
    o.item_type, o.data_type, o.type_property,
    o.help_text, o.help_text_key, o.description, o.description_key,
    o.custom_itemseq AS custom_item_seq,
    o.default_value,
    COALESCE(o.require_flg, 0),
    COALESCE(o.delete_flg, 0),
    0 AS custom_flg,
    COALESCE(o.enable_flg, 1),
    o.creatable, o.updatable, o.unique_key_flg,
    COALESCE(o.enable_history_log, 1),
    o.enable_config, o.enable_package,
    o.readonly_status, o.visible_status, o.hidden_flg,
    re.api_key AS refer_entity_api_key,
    NULL AS refer_link_api_key,
    o.db_column,
    COALESCE(o.item_order, 0),
    COALESCE(o.sort_flg, 0),
    o.column_name,
    o.created_at, o.created_by, o.updated_at, o.updated_by
FROM paas_metarepo_old.p_custom_item o
INNER JOIN paas_metarepo_old.p_custom_entity e ON o.entity_id = e.id AND o.tenant_id = e.tenant_id
LEFT JOIN paas_metarepo_old.p_custom_entity re ON o.refer_entity_id = re.id AND o.tenant_id = re.tenant_id
WHERE o.custom_flg = 0
  AND (o.delete_flg = 0 OR o.delete_flg IS NULL)
ON DUPLICATE KEY UPDATE updated_at = VALUES(updated_at);

-- 6b. Tenant 级（custom_flg=1 或 custom_flg IS NULL）
INSERT INTO paas_metarepo.p_tenant_item (
    tenant_id, entity_api_key, api_key, namespace, label, label_key,
    item_type, data_type, type_property,
    help_text, help_text_key, description, description_key,
    custom_item_seq, default_value,
    require_flg, delete_flg, custom_flg, enable_flg,
    creatable, updatable, unique_key_flg,
    enable_history_log, enable_config, enable_package,
    readonly_status, visible_status, hidden_flg,
    refer_entity_api_key, refer_link_api_key,
    db_column, item_order, sort_flg, column_name,
    created_at, created_by, updated_at, updated_by
)
SELECT
    o.tenant_id,
    e.api_key AS entity_api_key,
    o.api_key,
    COALESCE(o.name_space, 'custom') AS namespace,
    o.label,
    o.label_key,
    o.item_type, o.data_type, o.type_property,
    o.help_text, o.help_text_key, o.description, o.description_key,
    o.custom_itemseq AS custom_item_seq,
    o.default_value,
    COALESCE(o.require_flg, 0),
    COALESCE(o.delete_flg, 0),
    COALESCE(o.custom_flg, 1),
    COALESCE(o.enable_flg, 1),
    o.creatable, o.updatable, o.unique_key_flg,
    COALESCE(o.enable_history_log, 1),
    o.enable_config, o.enable_package,
    o.readonly_status, o.visible_status, o.hidden_flg,
    re.api_key AS refer_entity_api_key,
    NULL AS refer_link_api_key,
    o.db_column,
    COALESCE(o.item_order, 0),
    COALESCE(o.sort_flg, 0),
    o.column_name,
    o.created_at, o.created_by, o.updated_at, o.updated_by
FROM paas_metarepo_old.p_custom_item o
INNER JOIN paas_metarepo_old.p_custom_entity e ON o.entity_id = e.id AND o.tenant_id = e.tenant_id
LEFT JOIN paas_metarepo_old.p_custom_entity re ON o.refer_entity_id = re.id AND o.tenant_id = re.tenant_id
WHERE (o.custom_flg = 1 OR o.custom_flg IS NULL)
  AND (o.delete_flg = 0 OR o.delete_flg IS NULL);


-- ************************************************************
-- 7. p_custom_entity_link → p_common_entity_link + p_tenant_entity_link
-- 老表：id, tenant_id, name, api_key, label, label_key,
--       type_property, link_type, parent_entity_id, child_entity_id,
--       detail_link, cascade_delete, access_control, enable_flg,
--       description, delete_flg, ...
-- 新表：api_key(PK), namespace, label, label_key,
--       type_property, link_type, parent_entity_api_key, child_entity_api_key, ...
-- 变更：parent_entity_id → parent_entity_api_key
--       child_entity_id → child_entity_api_key
--       丢弃 id/tenant_id/name/name_key，新增 namespace/description_key
-- ************************************************************

-- 7a. Common 级（通过 parent entity 的 custom_flg=0 判断）
INSERT INTO paas_metarepo.p_common_entity_link (
    api_key, namespace, label, label_key,
    type_property, link_type,
    parent_entity_api_key, child_entity_api_key,
    detail_link, cascade_delete, access_control, enable_flg,
    description, description_key, delete_flg,
    created_at, created_by, updated_at, updated_by
)
SELECT
    o.api_key,
    'system' AS namespace,
    o.label,
    o.label_key,
    o.type_property,
    o.link_type,
    pe.api_key AS parent_entity_api_key,
    ce.api_key AS child_entity_api_key,
    o.detail_link,
    COALESCE(o.cascade_delete, 0),
    COALESCE(o.access_control, 0),
    COALESCE(o.enable_flg, 1),
    o.description,
    NULL AS description_key,
    COALESCE(o.delete_flg, 0),
    o.created_at, o.created_by, o.updated_at, o.updated_by
FROM paas_metarepo_old.p_custom_entity_link o
INNER JOIN paas_metarepo_old.p_custom_entity pe ON o.parent_entity_id = pe.id AND o.tenant_id = pe.tenant_id
INNER JOIN paas_metarepo_old.p_custom_entity ce ON o.child_entity_id = ce.id AND o.tenant_id = ce.tenant_id
WHERE pe.custom_flg = 0 AND ce.custom_flg = 0
  AND (o.delete_flg = 0 OR o.delete_flg IS NULL)
ON DUPLICATE KEY UPDATE updated_at = VALUES(updated_at);

-- 7b. Tenant 级（其余所有）
INSERT INTO paas_metarepo.p_tenant_entity_link (
    tenant_id, api_key, namespace, label, label_key,
    type_property, link_type,
    parent_entity_api_key, child_entity_api_key,
    detail_link, cascade_delete, access_control, enable_flg,
    description, description_key, delete_flg,
    created_at, created_by, updated_at, updated_by
)
SELECT
    o.tenant_id,
    o.api_key,
    'custom' AS namespace,
    o.label,
    o.label_key,
    o.type_property,
    o.link_type,
    pe.api_key AS parent_entity_api_key,
    ce.api_key AS child_entity_api_key,
    o.detail_link,
    COALESCE(o.cascade_delete, 0),
    COALESCE(o.access_control, 0),
    COALESCE(o.enable_flg, 1),
    o.description,
    NULL AS description_key,
    COALESCE(o.delete_flg, 0),
    o.created_at, o.created_by, o.updated_at, o.updated_by
FROM paas_metarepo_old.p_custom_entity_link o
INNER JOIN paas_metarepo_old.p_custom_entity pe ON o.parent_entity_id = pe.id AND o.tenant_id = pe.tenant_id
INNER JOIN paas_metarepo_old.p_custom_entity ce ON o.child_entity_id = ce.id AND o.tenant_id = ce.tenant_id
WHERE NOT (pe.custom_flg = 0 AND ce.custom_flg = 0)
  AND (o.delete_flg = 0 OR o.delete_flg IS NULL);


-- ************************************************************
-- 8. p_custom_pickoption → p_common_pick_option + p_tenant_pick_option
-- 老表：id, tenant_id, entity_id, item_id, api_key, option_code,
--       option_label, option_label_key, option_order, default_flg,
--       global_flg, custom_flg, delete_flg, enable_flg, description, ...
-- 新表：entity_api_key, item_api_key, api_key(PK), namespace,
--       label, label_key, option_order, default_flg, global_flg,
--       custom_flg, enable_flg, description, description_key, ...
-- 变更：entity_id → entity_api_key, item_id → item_api_key
--       option_label → label, option_label_key → label_key
--       丢弃 id/tenant_id/option_code，新增 namespace/description_key
-- ************************************************************

-- 8a. Common 级（custom_flg=0）
INSERT INTO paas_metarepo.p_common_pick_option (
    entity_api_key, item_api_key, api_key, namespace,
    label, label_key, option_order, default_flg, global_flg,
    custom_flg, enable_flg, description, description_key,
    delete_flg, created_at, created_by, updated_at, updated_by
)
SELECT
    e.api_key AS entity_api_key,
    i.api_key AS item_api_key,
    COALESCE(o.api_key, CAST(o.option_code AS CHAR)) AS api_key,
    'system' AS namespace,
    o.option_label AS label,
    o.option_label_key AS label_key,
    o.option_order,
    COALESCE(o.default_flg, 0),
    o.global_flg,
    0 AS custom_flg,
    COALESCE(o.enable_flg, 1),
    o.description,
    NULL AS description_key,
    COALESCE(o.delete_flg, 0),
    o.created_at, o.created_by, o.updated_at, o.updated_by
FROM paas_metarepo_old.p_custom_pickoption o
INNER JOIN paas_metarepo_old.p_custom_entity e ON o.entity_id = e.id AND o.tenant_id = e.tenant_id
INNER JOIN paas_metarepo_old.p_custom_item i ON o.item_id = i.id AND o.tenant_id = i.tenant_id
WHERE o.custom_flg = 0
  AND (o.delete_flg = 0 OR o.delete_flg IS NULL)
ON DUPLICATE KEY UPDATE updated_at = VALUES(updated_at);

-- 8b. Tenant 级（custom_flg=1 或 custom_flg IS NULL）
INSERT INTO paas_metarepo.p_tenant_pick_option (
    tenant_id, entity_api_key, item_api_key, api_key, namespace,
    label, label_key, option_order, default_flg, global_flg,
    custom_flg, enable_flg, description, description_key,
    delete_flg, created_at, created_by, updated_at, updated_by
)
SELECT
    o.tenant_id,
    e.api_key AS entity_api_key,
    i.api_key AS item_api_key,
    COALESCE(o.api_key, CAST(o.option_code AS CHAR)) AS api_key,
    'custom' AS namespace,
    o.option_label AS label,
    o.option_label_key AS label_key,
    o.option_order,
    COALESCE(o.default_flg, 0),
    o.global_flg,
    COALESCE(o.custom_flg, 1),
    COALESCE(o.enable_flg, 1),
    o.description,
    NULL AS description_key,
    COALESCE(o.delete_flg, 0),
    o.created_at, o.created_by, o.updated_at, o.updated_by
FROM paas_metarepo_old.p_custom_pickoption o
INNER JOIN paas_metarepo_old.p_custom_entity e ON o.entity_id = e.id AND o.tenant_id = e.tenant_id
INNER JOIN paas_metarepo_old.p_custom_item i ON o.item_id = i.id AND o.tenant_id = i.tenant_id
WHERE (o.custom_flg = 1 OR o.custom_flg IS NULL)
  AND (o.delete_flg = 0 OR o.delete_flg IS NULL);


-- ************************************************************
-- 9. p_custom_check_rule → p_common_check_rule + p_tenant_check_rule
-- 老表：id, tenant_id, object_id, name, api_key,
--       rule_label, rule_label_key, active_flg, description,
--       check_formula, check_error_msg, check_error_msg_key,
--       check_error_location, check_error_item_id,
--       check_all_items_flg, check_error_way, ...
-- 新表：entity_api_key, api_key(PK), namespace, label, label_key,
--       active_flg, description, description_key, check_formula, ...
--       check_error_item_api_key, ...
-- 变更：object_id → entity_api_key, rule_label → label,
--       rule_label_key → label_key,
--       check_error_item_id → check_error_item_api_key(NULL)
--       丢弃 id/tenant_id/name/name_key，新增 namespace/description_key
-- ************************************************************

-- 9a. Common 级（通过 entity 的 custom_flg=0 判断）
INSERT INTO paas_metarepo.p_common_check_rule (
    entity_api_key, api_key, namespace, label, label_key,
    active_flg, description, description_key,
    check_formula, check_error_msg, check_error_msg_key,
    check_error_location, check_error_item_api_key,
    check_all_items_flg, check_error_way,
    delete_flg, created_by, created_at, updated_by, updated_at
)
SELECT
    e.api_key AS entity_api_key,
    o.api_key,
    'system' AS namespace,
    COALESCE(o.rule_label, o.label) AS label,
    COALESCE(o.rule_label_key, o.label_key) AS label_key,
    COALESCE(o.active_flg, 1),
    o.description,
    NULL AS description_key,
    o.check_formula,
    o.check_error_msg,
    o.check_error_msg_key,
    o.check_error_location,
    NULL AS check_error_item_api_key,
    COALESCE(o.check_all_items_flg, 0),
    o.check_error_way,
    COALESCE(o.delete_flg, 0),
    o.created_by, o.created_at, o.updated_by, o.updated_at
FROM paas_metarepo_old.p_custom_check_rule o
INNER JOIN paas_metarepo_old.p_custom_entity e ON o.object_id = e.id AND o.tenant_id = e.tenant_id
WHERE e.custom_flg = 0
  AND (o.delete_flg = 0 OR o.delete_flg IS NULL)
ON DUPLICATE KEY UPDATE updated_at = VALUES(updated_at);

-- 9b. Tenant 级（其余所有）
INSERT INTO paas_metarepo.p_tenant_check_rule (
    tenant_id, entity_api_key, api_key, namespace, label, label_key,
    active_flg, description, description_key,
    check_formula, check_error_msg, check_error_msg_key,
    check_error_location, check_error_item_api_key,
    check_all_items_flg, check_error_way,
    delete_flg, created_by, created_at, updated_by, updated_at
)
SELECT
    o.tenant_id,
    e.api_key AS entity_api_key,
    o.api_key,
    'custom' AS namespace,
    COALESCE(o.rule_label, o.label) AS label,
    COALESCE(o.rule_label_key, o.label_key) AS label_key,
    COALESCE(o.active_flg, 1),
    o.description,
    NULL AS description_key,
    o.check_formula,
    o.check_error_msg,
    o.check_error_msg_key,
    o.check_error_location,
    NULL AS check_error_item_api_key,
    COALESCE(o.check_all_items_flg, 0),
    o.check_error_way,
    COALESCE(o.delete_flg, 0),
    o.created_by, o.created_at, o.updated_by, o.updated_at
FROM paas_metarepo_old.p_custom_check_rule o
INNER JOIN paas_metarepo_old.p_custom_entity e ON o.object_id = e.id AND o.tenant_id = e.tenant_id
WHERE e.custom_flg != 0 OR e.custom_flg IS NULL
  AND (o.delete_flg = 0 OR o.delete_flg IS NULL);


-- ************************************************************
-- 10. p_custom_refer_filter → p_common_refer_filter + p_tenant_refer_filter
-- 老表结构推测（基于老代码引用）：
--   id, tenant_id, entity_id, item_id, api_key, label, label_key,
--   link_id, filter_field, filter_operator, filter_value,
--   filter_order, description, delete_flg, ...
-- 新表：entity_api_key, item_api_key, api_key(PK), namespace,
--       label, label_key, link_api_key, filter_field, filter_operator,
--       filter_value, filter_order, description, description_key, ...
-- 变更：entity_id → entity_api_key, item_id → item_api_key
--       link_id → link_api_key
--       丢弃 id/tenant_id，新增 namespace/description_key
-- 注意：如果老表不存在 p_custom_refer_filter，此段可跳过
-- ************************************************************

-- 10a. Common 级
INSERT IGNORE INTO paas_metarepo.p_common_refer_filter (
    entity_api_key, item_api_key, api_key, namespace,
    label, label_key, link_api_key,
    filter_field, filter_operator, filter_value, filter_order,
    description, description_key, delete_flg,
    created_at, created_by, updated_at, updated_by
)
SELECT
    e.api_key AS entity_api_key,
    i.api_key AS item_api_key,
    o.api_key,
    'system' AS namespace,
    o.label,
    o.label_key,
    lk.api_key AS link_api_key,
    o.filter_field,
    o.filter_operator,
    o.filter_value,
    o.filter_order,
    o.description,
    NULL AS description_key,
    COALESCE(o.delete_flg, 0),
    o.created_at, o.created_by, o.updated_at, o.updated_by
FROM paas_metarepo_old.p_custom_refer_filter o
INNER JOIN paas_metarepo_old.p_custom_entity e ON o.entity_id = e.id AND o.tenant_id = e.tenant_id
INNER JOIN paas_metarepo_old.p_custom_item i ON o.item_id = i.id AND o.tenant_id = i.tenant_id
LEFT JOIN paas_metarepo_old.p_custom_entity_link lk ON o.link_id = lk.id AND o.tenant_id = lk.tenant_id
WHERE e.custom_flg = 0
  AND (o.delete_flg = 0 OR o.delete_flg IS NULL);

-- 10b. Tenant 级
INSERT IGNORE INTO paas_metarepo.p_tenant_refer_filter (
    tenant_id, entity_api_key, item_api_key, api_key, namespace,
    label, label_key, link_api_key,
    filter_field, filter_operator, filter_value, filter_order,
    description, description_key, delete_flg,
    created_at, created_by, updated_at, updated_by
)
SELECT
    o.tenant_id,
    e.api_key AS entity_api_key,
    i.api_key AS item_api_key,
    o.api_key,
    'custom' AS namespace,
    o.label,
    o.label_key,
    lk.api_key AS link_api_key,
    o.filter_field,
    o.filter_operator,
    o.filter_value,
    o.filter_order,
    o.description,
    NULL AS description_key,
    COALESCE(o.delete_flg, 0),
    o.created_at, o.created_by, o.updated_at, o.updated_by
FROM paas_metarepo_old.p_custom_refer_filter o
INNER JOIN paas_metarepo_old.p_custom_entity e ON o.entity_id = e.id AND o.tenant_id = e.tenant_id
INNER JOIN paas_metarepo_old.p_custom_item i ON o.item_id = i.id AND o.tenant_id = i.tenant_id
LEFT JOIN paas_metarepo_old.p_custom_entity_link lk ON o.link_id = lk.id AND o.tenant_id = lk.tenant_id
WHERE (e.custom_flg != 0 OR e.custom_flg IS NULL)
  AND (o.delete_flg = 0 OR o.delete_flg IS NULL);


-- ************************************************************
-- 11. 直接复制的表（结构基本不变或仅表名变化）
-- ************************************************************

-- 11a. p_meta_metamodel_data（通用大宽表，结构不变）
INSERT INTO paas_metarepo.p_meta_metamodel_data
SELECT * FROM paas_metarepo_old.p_meta_metamodel_data;

-- 11b. p_meta_tenant_metadata（租户级大宽表，结构不变）
INSERT INTO paas_metarepo.p_meta_tenant_metadata
SELECT * FROM paas_metarepo_old.p_meta_tenant_metadata;

-- 11c. p_meta_log → p_tenant_meta_log（表名变化，字段映射）
-- 老表：id, tenant_id, metadata_id, trace_id, object_id, metamodel_id,
--       old_value, new_value, op_type, from_type,
--       parent_metamodel_id, parent_metadata_id, sync,
--       created_by, created_at, entrust_tenant_id, origin_tenant_id
-- 新表：id, tenant_id, metadata_api_key, metamodel_api_key,
--       trace_id, old_value, new_value, op_type, from_type, sync,
--       delete_flg, created_by, created_at, updated_by, updated_at
INSERT INTO paas_metarepo.p_tenant_meta_log (
    id, tenant_id, metadata_api_key, metamodel_api_key,
    trace_id, old_value, new_value,
    op_type, from_type, sync,
    delete_flg, created_by, created_at, updated_by, updated_at
)
SELECT
    o.id,
    o.tenant_id,
    CAST(o.metadata_id AS CHAR) AS metadata_api_key,
    m.api_key AS metamodel_api_key,
    o.trace_id,
    o.old_value,
    o.new_value,
    o.op_type,
    o.from_type,
    o.sync,
    0 AS delete_flg,
    o.created_by,
    o.created_at,
    o.created_by AS updated_by,
    o.created_at AS updated_at
FROM paas_metarepo_old.p_meta_log o
LEFT JOIN paas_metarepo_old.p_meta_model m ON o.metamodel_id = m.id;

-- 11d. p_meta_i18n_resource（结构基本不变）
INSERT INTO paas_metarepo.p_meta_i18n_resource (
    id, tenant_id, metamodel_api_key, metadata_api_key, entity_api_key,
    resource_key, lang_code, resource_value, description,
    delete_flg, created_at, created_by, updated_at, updated_by
)
SELECT
    o.id,
    o.tenant_id,
    o.metamodel_id AS metamodel_api_key,
    o.metadata_id AS metadata_api_key,
    o.object_id AS entity_api_key,
    o.resource_key,
    o.lang_code,
    o.resource_value,
    o.description,
    COALESCE(o.delete_flg, 0),
    o.created_at, o.created_by, o.updated_at, o.updated_by
FROM paas_metarepo_old.p_meta_i18n_resource o;

-- 11e. p_meta_migration_process（结构不变）
INSERT INTO paas_metarepo.p_meta_migration_process
SELECT
    id, tenant_id, package_name, migration_stage,
    start_time, end_time, status, process_content,
    COALESCE(delete_flg, 0),
    created_at, created_by, updated_at, updated_by
FROM paas_metarepo_old.p_meta_migration_process;

-- 11f. p_meta_migration_process_unit（结构不变）
INSERT INTO paas_metarepo.p_meta_migration_process_unit
SELECT
    id, tenant_id, pid, process_id, migration_stage,
    unit_name, rollback_unit_flg,
    start_time, end_time, time_consuming,
    exec_exception, rollback_exception, status,
    COALESCE(delete_flg, 0),
    created_at, created_by, updated_at, updated_by
FROM paas_metarepo_old.p_meta_migration_process_unit;

-- 11g. p_meta_module（如果老表存在）
-- INSERT INTO paas_metarepo.p_meta_module
-- SELECT * FROM paas_metarepo_old.p_meta_module;

-- 11h. p_meta_module_metadata（如果老表存在）
-- INSERT INTO paas_metarepo.p_meta_module_metadata
-- SELECT * FROM paas_metarepo_old.p_meta_module_metadata;

-- 11i. x_global_pickitem（结构基本不变，新增 description_key）
INSERT INTO paas_metarepo.x_global_pickitem (
    id, tenant_id, api_key, name, label, label_key,
    custom_flg, description, description_key,
    created_by, created_at, updated_by, updated_at
)
SELECT
    o.id,
    o.tenant_id,
    o.api_key,
    o.name,
    o.label,
    o.label_key,
    o.custom_flg,
    o.description,
    NULL AS description_key,
    o.created_by, o.created_at, o.updated_by, o.updated_at
FROM paas_metarepo_old.x_global_pickitem o;


-- ************************************************************
-- 12. 数据校验查询（迁移后执行，确认数据完整性）
-- ************************************************************

-- 12a. 元模型表行数对比
SELECT 'p_meta_model' AS tbl,
    (SELECT COUNT(*) FROM paas_metarepo_old.p_meta_model WHERE delete_flg=0 OR delete_flg IS NULL) AS old_cnt,
    (SELECT COUNT(*) FROM paas_metarepo.p_meta_model) AS new_cnt;

SELECT 'p_meta_item' AS tbl,
    (SELECT COUNT(*) FROM paas_metarepo_old.p_meta_item WHERE delete_flg=0 OR delete_flg IS NULL) AS old_cnt,
    (SELECT COUNT(*) FROM paas_metarepo.p_meta_item) AS new_cnt;

SELECT 'p_meta_option' AS tbl,
    (SELECT COUNT(*) FROM paas_metarepo_old.p_meta_option WHERE delete_flg=0 OR delete_flg IS NULL) AS old_cnt,
    (SELECT COUNT(*) FROM paas_metarepo.p_meta_option) AS new_cnt;

SELECT 'p_meta_link' AS tbl,
    (SELECT COUNT(*) FROM paas_metarepo_old.p_meta_link WHERE delete_flg=0 OR delete_flg IS NULL) AS old_cnt,
    (SELECT COUNT(*) FROM paas_metarepo.p_meta_link) AS new_cnt;

-- 12b. 业务元数据表行数对比（Common + Tenant 合计应等于老表）
SELECT 'entity' AS tbl,
    (SELECT COUNT(*) FROM paas_metarepo_old.p_custom_entity WHERE delete_flg=0 OR delete_flg IS NULL) AS old_cnt,
    (SELECT COUNT(*) FROM paas_metarepo.p_common_entity) + (SELECT COUNT(*) FROM paas_metarepo.p_tenant_entity) AS new_cnt;

SELECT 'item' AS tbl,
    (SELECT COUNT(*) FROM paas_metarepo_old.p_custom_item WHERE delete_flg=0 OR delete_flg IS NULL) AS old_cnt,
    (SELECT COUNT(*) FROM paas_metarepo.p_common_item) + (SELECT COUNT(*) FROM paas_metarepo.p_tenant_item) AS new_cnt;

SELECT 'entity_link' AS tbl,
    (SELECT COUNT(*) FROM paas_metarepo_old.p_custom_entity_link WHERE delete_flg=0 OR delete_flg IS NULL) AS old_cnt,
    (SELECT COUNT(*) FROM paas_metarepo.p_common_entity_link) + (SELECT COUNT(*) FROM paas_metarepo.p_tenant_entity_link) AS new_cnt;

SELECT 'pick_option' AS tbl,
    (SELECT COUNT(*) FROM paas_metarepo_old.p_custom_pickoption WHERE delete_flg=0 OR delete_flg IS NULL) AS old_cnt,
    (SELECT COUNT(*) FROM paas_metarepo.p_common_pick_option) + (SELECT COUNT(*) FROM paas_metarepo.p_tenant_pick_option) AS new_cnt;

SELECT 'check_rule' AS tbl,
    (SELECT COUNT(*) FROM paas_metarepo_old.p_custom_check_rule WHERE delete_flg=0 OR delete_flg IS NULL) AS old_cnt,
    (SELECT COUNT(*) FROM paas_metarepo.p_common_check_rule) + (SELECT COUNT(*) FROM paas_metarepo.p_tenant_check_rule) AS new_cnt;

SELECT 'meta_log' AS tbl,
    (SELECT COUNT(*) FROM paas_metarepo_old.p_meta_log) AS old_cnt,
    (SELECT COUNT(*) FROM paas_metarepo.p_tenant_meta_log) AS new_cnt;

-- ============================================================
-- 迁移完成
-- ============================================================
