-- ============================================================
-- Common/Tenant 表拆分
-- Common 表：存放平台预置元数据，无 tenant_id 列
-- Tenant 表：存放租户自定义/覆盖元数据，有 tenant_id 列
-- ============================================================

-- 1. p_custom_entity_common（Common 级对象，无 tenant_id）
CREATE TABLE IF NOT EXISTS p_custom_entity_common (
    id BIGINT NOT NULL,
    name_space VARCHAR(100),
    entity_id BIGINT,
    name VARCHAR(255), name_key VARCHAR(255),
    api_key VARCHAR(255),
    label VARCHAR(255), label_key VARCHAR(255),
    object_type SMALLINT,
    svg_id BIGINT, svg_color VARCHAR(20),
    description VARCHAR(500), description_key VARCHAR(255),
    custom_entityseq INTEGER,
    delete_flg SMALLINT DEFAULT 0,
    enable_flg SMALLINT DEFAULT 1,
    custom_flg SMALLINT DEFAULT 0,
    business_category SMALLINT,
    type_property VARCHAR(500),
    db_table VARCHAR(50),
    detail_flg SMALLINT,
    enable_team SMALLINT, enable_social SMALLINT,
    enable_config BIGINT, hidden_flg SMALLINT,
    searchable SMALLINT, enable_sharing SMALLINT,
    enable_script_trigger SMALLINT, enable_activity SMALLINT,
    enable_history_log SMALLINT, enable_report SMALLINT,
    enable_refer SMALLINT, enable_api SMALLINT,
    enable_flow BIGINT, enable_package BIGINT,
    extend_property VARCHAR(100),
    created_at BIGINT, created_by BIGINT,
    updated_at BIGINT, updated_by BIGINT,
    PRIMARY KEY (id)
);
CREATE UNIQUE INDEX uk_entity_common_apikey ON p_custom_entity_common(api_key);

-- 2. p_custom_item_common（Common 级字段，无 tenant_id）
CREATE TABLE IF NOT EXISTS p_custom_item_common (
    id BIGINT NOT NULL,
    entity_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL, name_key VARCHAR(255),
    api_key VARCHAR(255) NOT NULL,
    label VARCHAR(255) NOT NULL, label_key VARCHAR(255),
    item_type SMALLINT, data_type SMALLINT,
    type_property VARCHAR(4000),
    help_text VARCHAR(500), help_text_key VARCHAR(255),
    description VARCHAR(500), description_key VARCHAR(255),
    custom_itemseq INTEGER, default_value VARCHAR(4000),
    require_flg SMALLINT NOT NULL DEFAULT 0,
    delete_flg SMALLINT NOT NULL DEFAULT 0,
    custom_flg SMALLINT NOT NULL DEFAULT 0,
    enable_flg SMALLINT NOT NULL DEFAULT 1,
    creatable SMALLINT, updatable SMALLINT,
    unique_key_flg SMALLINT,
    enable_history_log SMALLINT DEFAULT 1,
    enable_config BIGINT, enable_package BIGINT,
    readonly_status SMALLINT, visible_status SMALLINT,
    hidden_flg SMALLINT,
    refer_entity_id BIGINT, refer_link_id BIGINT,
    db_column VARCHAR(255),
    item_order SMALLINT NOT NULL DEFAULT 0,
    sort_flg SMALLINT DEFAULT 0, column_name VARCHAR(100),
    created_at BIGINT, created_by BIGINT,
    updated_at BIGINT, updated_by BIGINT,
    PRIMARY KEY (id)
);
CREATE UNIQUE INDEX uk_item_common_apikey ON p_custom_item_common(entity_id, api_key);

-- 3. p_custom_pickoption_common（Common 级选项值，无 tenant_id）
CREATE TABLE IF NOT EXISTS p_custom_pickoption_common (
    id BIGINT NOT NULL,
    entity_id BIGINT, item_id BIGINT,
    api_key VARCHAR(255),
    option_code SMALLINT,
    option_label VARCHAR(255), option_label_key VARCHAR(255),
    option_order SMALLINT,
    default_flg SMALLINT, global_flg SMALLINT,
    custom_flg SMALLINT DEFAULT 0,
    delete_flg SMALLINT NOT NULL DEFAULT 0,
    enable_flg SMALLINT DEFAULT 1,
    description VARCHAR(500), description_key VARCHAR(255),
    created_at BIGINT, created_by BIGINT,
    updated_at BIGINT, updated_by BIGINT,
    PRIMARY KEY (id)
);
CREATE UNIQUE INDEX uk_pick_common_apikey ON p_custom_pickoption_common(item_id, api_key);

-- 4. p_custom_entity_link_common（Common 级关联关系，无 tenant_id）
CREATE TABLE IF NOT EXISTS p_custom_entity_link_common (
    id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL, name_key VARCHAR(255),
    api_key VARCHAR(255),
    label VARCHAR(255) NOT NULL, label_key VARCHAR(255) DEFAULT '',
    type_property VARCHAR(500),
    link_type SMALLINT NOT NULL DEFAULT 0,
    parent_entity_id BIGINT NOT NULL,
    child_entity_id BIGINT NOT NULL,
    detail_link SMALLINT,
    cascade_delete SMALLINT NOT NULL DEFAULT 0,
    access_control SMALLINT NOT NULL DEFAULT 0,
    enable_flg SMALLINT NOT NULL DEFAULT 1,
    description VARCHAR(500), description_key VARCHAR(255),
    delete_flg SMALLINT DEFAULT 0,
    created_at BIGINT, created_by BIGINT,
    updated_at BIGINT, updated_by BIGINT,
    PRIMARY KEY (id)
);
CREATE UNIQUE INDEX uk_link_common_apikey ON p_custom_entity_link_common(api_key);

-- ============================================================
-- 迁移数据：从原表中将 Common 数据迁移到 Common 表
-- ============================================================

-- p_custom_entity -> p_custom_entity_common
INSERT IGNORE INTO p_custom_entity_common
SELECT id, name_space, entity_id, name, name_key, api_key, label, label_key,
       object_type, svg_id, svg_color, description, description_key,
       custom_entityseq, delete_flg, enable_flg, custom_flg, business_category,
       type_property, db_table, detail_flg, enable_team, enable_social,
       enable_config, hidden_flg, searchable, enable_sharing,
       enable_script_trigger, enable_activity, enable_history_log,
       enable_report, enable_refer, enable_api, enable_flow, enable_package,
       extend_property, created_at, created_by, updated_at, updated_by
FROM p_custom_entity WHERE tenant_id <= 0;

-- p_custom_item -> p_custom_item_common
INSERT IGNORE INTO p_custom_item_common
SELECT id, entity_id, name, name_key, api_key, label, label_key,
       item_type, data_type, type_property, help_text, help_text_key,
       description, description_key, custom_itemseq, default_value,
       require_flg, delete_flg, custom_flg, enable_flg, creatable, updatable,
       unique_key_flg, enable_history_log, enable_config, enable_package,
       readonly_status, visible_status, hidden_flg, refer_entity_id,
       refer_link_id, db_column, item_order, sort_flg, column_name,
       created_at, created_by, updated_at, updated_by
FROM p_custom_item WHERE tenant_id <= 0;

-- p_custom_pickoption -> p_custom_pickoption_common
INSERT IGNORE INTO p_custom_pickoption_common
SELECT id, entity_id, item_id, api_key, option_code, option_label,
       option_label_key, option_order, default_flg, global_flg, custom_flg,
       delete_flg, enable_flg, description, description_key,
       created_at, created_by, updated_at, updated_by
FROM p_custom_pickoption WHERE tenant_id <= 0;

-- p_custom_entity_link -> p_custom_entity_link_common
INSERT IGNORE INTO p_custom_entity_link_common
SELECT id, name, name_key, api_key, label, label_key, type_property,
       link_type, parent_entity_id, child_entity_id, detail_link,
       cascade_delete, access_control, enable_flg, description,
       description_key, delete_flg, created_at, created_by, updated_at, updated_by
FROM p_custom_entity_link WHERE tenant_id <= 0;
