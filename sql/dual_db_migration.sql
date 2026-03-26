-- ============================================================
-- paas_metarepo_common 库：元模型定义 + Common 级元数据
-- ============================================================
USE paas_metarepo_common;

-- 元模型定义表（从 paas_metarepo 复制结构和数据）
CREATE TABLE IF NOT EXISTS p_meta_model LIKE paas_metarepo.p_meta_model;
INSERT IGNORE INTO p_meta_model SELECT * FROM paas_metarepo.p_meta_model;

CREATE TABLE IF NOT EXISTS p_meta_item LIKE paas_metarepo.p_meta_item;
INSERT IGNORE INTO p_meta_item SELECT * FROM paas_metarepo.p_meta_item;

CREATE TABLE IF NOT EXISTS p_meta_option LIKE paas_metarepo.p_meta_option;
INSERT IGNORE INTO p_meta_option SELECT * FROM paas_metarepo.p_meta_option;

CREATE TABLE IF NOT EXISTS p_meta_link LIKE paas_metarepo.p_meta_link;
INSERT IGNORE INTO p_meta_link SELECT * FROM paas_metarepo.p_meta_link;

-- p_common_entity（从 p_custom_entity 中 tenant_id<=0 的数据，去掉 tenant_id）
CREATE TABLE IF NOT EXISTS p_common_entity LIKE paas_metarepo.p_common_entity;
INSERT IGNORE INTO p_common_entity SELECT * FROM paas_metarepo.p_common_entity;

-- p_common_metadata（从 p_meta_common_metadata 复制）
CREATE TABLE IF NOT EXISTS p_common_metadata LIKE paas_metarepo.p_meta_common_metadata;
INSERT IGNORE INTO p_common_metadata SELECT * FROM paas_metarepo.p_meta_common_metadata;

-- p_common_item（从 p_custom_item 中 tenant_id<=0，去掉 tenant_id）
CREATE TABLE IF NOT EXISTS p_common_item (
    id BIGINT NOT NULL, entity_id BIGINT NOT NULL,
    name VARCHAR(255), name_key VARCHAR(255), api_key VARCHAR(255),
    label VARCHAR(255), label_key VARCHAR(255),
    item_type SMALLINT, data_type SMALLINT, type_property VARCHAR(4000),
    help_text VARCHAR(500), help_text_key VARCHAR(255),
    description VARCHAR(500), description_key VARCHAR(255),
    custom_itemseq INTEGER, default_value VARCHAR(4000),
    require_flg SMALLINT DEFAULT 0, delete_flg SMALLINT DEFAULT 0,
    custom_flg SMALLINT DEFAULT 0, enable_flg SMALLINT DEFAULT 1,
    creatable SMALLINT, updatable SMALLINT, unique_key_flg SMALLINT,
    enable_history_log SMALLINT DEFAULT 1, enable_config BIGINT,
    enable_package BIGINT, readonly_status SMALLINT, visible_status SMALLINT,
    hidden_flg SMALLINT, refer_entity_id BIGINT, refer_link_id BIGINT,
    db_column VARCHAR(255), item_order SMALLINT DEFAULT 0,
    sort_flg SMALLINT DEFAULT 0, column_name VARCHAR(100),
    created_at BIGINT, created_by BIGINT, updated_at BIGINT, updated_by BIGINT,
    PRIMARY KEY (id)
);
CREATE UNIQUE INDEX uk_common_item_apikey ON p_common_item(entity_id, api_key);

INSERT IGNORE INTO p_common_item
SELECT id, entity_id, name, name_key, api_key, label, label_key,
       item_type, data_type, type_property, help_text, help_text_key,
       description, description_key, custom_itemseq, default_value,
       require_flg, delete_flg, custom_flg, enable_flg, creatable, updatable,
       unique_key_flg, enable_history_log, enable_config, enable_package,
       readonly_status, visible_status, hidden_flg, refer_entity_id,
       refer_link_id, db_column, item_order, sort_flg, column_name,
       created_at, created_by, updated_at, updated_by
FROM paas_metarepo.p_custom_item WHERE tenant_id <= 0;

-- p_common_pickoption
CREATE TABLE IF NOT EXISTS p_common_pickoption (
    id BIGINT NOT NULL, entity_id BIGINT, item_id BIGINT,
    api_key VARCHAR(255), option_code SMALLINT,
    option_label VARCHAR(255), option_label_key VARCHAR(255),
    option_order SMALLINT, default_flg SMALLINT, global_flg SMALLINT,
    custom_flg SMALLINT DEFAULT 0, delete_flg SMALLINT DEFAULT 0,
    enable_flg SMALLINT DEFAULT 1,
    description VARCHAR(500), description_key VARCHAR(255),
    created_at BIGINT, created_by BIGINT, updated_at BIGINT, updated_by BIGINT,
    PRIMARY KEY (id)
);
INSERT IGNORE INTO p_common_pickoption
SELECT id, entity_id, item_id, api_key, option_code, option_label,
       option_label_key, option_order, default_flg, global_flg, custom_flg,
       delete_flg, enable_flg, description, description_key,
       created_at, created_by, updated_at, updated_by
FROM paas_metarepo.p_custom_pickoption WHERE tenant_id <= 0;

-- p_common_entity_link
CREATE TABLE IF NOT EXISTS p_common_entity_link (
    id BIGINT NOT NULL, name VARCHAR(255), name_key VARCHAR(255),
    api_key VARCHAR(255), label VARCHAR(255), label_key VARCHAR(255),
    type_property VARCHAR(500), link_type SMALLINT DEFAULT 0,
    parent_entity_id BIGINT NOT NULL, child_entity_id BIGINT NOT NULL,
    detail_link SMALLINT, cascade_delete SMALLINT DEFAULT 0,
    access_control SMALLINT DEFAULT 0, enable_flg SMALLINT DEFAULT 1,
    description VARCHAR(500), description_key VARCHAR(255),
    delete_flg SMALLINT DEFAULT 0,
    created_at BIGINT, created_by BIGINT, updated_at BIGINT, updated_by BIGINT,
    PRIMARY KEY (id)
);

INSERT IGNORE INTO p_common_entity_link
SELECT id, name, name_key, api_key, label, label_key, type_property,
       link_type, parent_entity_id, child_entity_id, detail_link,
       cascade_delete, access_control, enable_flg, description,
       description_key, delete_flg, created_at, created_by, updated_at, updated_by
FROM paas_metarepo.p_custom_entity_link WHERE tenant_id <= 0;

-- p_common_check_rule
CREATE TABLE IF NOT EXISTS p_common_check_rule (
    id BIGINT NOT NULL, entity_id BIGINT DEFAULT 0,
    name VARCHAR(255), name_key VARCHAR(255), api_key VARCHAR(255),
    label VARCHAR(100), label_key VARCHAR(100),
    active_flg SMALLINT, description VARCHAR(500), description_key VARCHAR(255),
    check_formula VARCHAR(5000), check_error_msg VARCHAR(5000),
    check_error_msg_key VARCHAR(200), check_error_location SMALLINT,
    check_error_item_id BIGINT, check_all_items_flg SMALLINT DEFAULT 0,
    check_error_way SMALLINT,
    created_by BIGINT, created_at BIGINT, updated_by BIGINT, updated_at BIGINT,
    PRIMARY KEY (id)
);

-- ============================================================
-- paas_metarepo 库：重命名 Tenant 级表为 p_tenant_* 前缀
-- ============================================================
USE paas_metarepo;

-- 重命名 Tenant 级表
RENAME TABLE p_custom_entity TO p_tenant_entity;
RENAME TABLE p_custom_item TO p_tenant_item;
RENAME TABLE p_custom_pickoption TO p_tenant_pickoption;
RENAME TABLE p_custom_entity_link TO p_tenant_entity_link;
RENAME TABLE p_custom_check_rule TO p_tenant_check_rule;
RENAME TABLE p_meta_tenant_metadata TO p_tenant_metadata;
