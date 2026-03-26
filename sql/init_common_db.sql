USE paas_metarepo_common;

-- 1. p_meta_model（元模型定义，PK=api_key）
CREATE TABLE p_meta_model (
    api_key VARCHAR(255) NOT NULL,
    namespace VARCHAR(50) NOT NULL DEFAULT 'system',
    label VARCHAR(255) NOT NULL, label_key VARCHAR(255),
    metamodel_type SMALLINT,
    enable_package SMALLINT DEFAULT 0, enable_app SMALLINT DEFAULT 0,
    enable_deprecation SMALLINT DEFAULT 0, enable_deactivation SMALLINT DEFAULT 0,
    enable_delta SMALLINT DEFAULT 0, enable_log SMALLINT DEFAULT 0,
    delta_scope SMALLINT, delta_mode SMALLINT,
    enable_module_control SMALLINT DEFAULT 0,
    db_table VARCHAR(50),
    description VARCHAR(500), description_key VARCHAR(255),
    delete_flg SMALLINT DEFAULT 0, xobject_dependency SMALLINT DEFAULT 0,
    visible SMALLINT DEFAULT 0,
    created_by BIGINT, created_at BIGINT, updated_by BIGINT, updated_at BIGINT,
    PRIMARY KEY (api_key)
);

-- 2. p_meta_item（PK=metamodel_api_key+api_key）
CREATE TABLE p_meta_item (
    metamodel_api_key VARCHAR(255) NOT NULL,
    api_key VARCHAR(255) NOT NULL,
    label VARCHAR(255) NOT NULL, label_key VARCHAR(255),
    item_type SMALLINT, data_type SMALLINT,
    item_order SMALLINT, require_flg SMALLINT,
    unique_key_flg SMALLINT DEFAULT 0,
    creatable SMALLINT, updatable SMALLINT,
    enable_package SMALLINT, enable_delta SMALLINT DEFAULT 0,
    enable_log SMALLINT DEFAULT 0,
    db_column VARCHAR(255),
    description VARCHAR(500), description_key VARCHAR(255),
    min_length INTEGER, max_length INTEGER,
    text_format SMALLINT, json_schema TEXT,
    name_field SMALLINT DEFAULT 0, delete_flg SMALLINT DEFAULT 0,
    created_by BIGINT, created_at BIGINT, updated_by BIGINT, updated_at BIGINT,
    PRIMARY KEY (metamodel_api_key, api_key)
);

-- 3. p_meta_option（PK=metamodel_api_key+item_api_key+option_code）
CREATE TABLE p_meta_option (
    metamodel_api_key VARCHAR(255) NOT NULL,
    item_api_key VARCHAR(255) NOT NULL,
    option_code INTEGER NOT NULL,
    option_key VARCHAR(100), label VARCHAR(255), label_key VARCHAR(255),
    option_order SMALLINT DEFAULT 0, default_flg SMALLINT DEFAULT 0,
    enable_flg SMALLINT DEFAULT 1,
    description VARCHAR(500), description_key VARCHAR(255),
    delete_flg SMALLINT DEFAULT 0,
    created_by BIGINT, created_at BIGINT, updated_by BIGINT, updated_at BIGINT,
    PRIMARY KEY (metamodel_api_key, item_api_key, option_code)
);

-- 4. p_meta_link（PK=api_key）
CREATE TABLE p_meta_link (
    api_key VARCHAR(255) NOT NULL,
    label VARCHAR(255) NOT NULL, label_key VARCHAR(255),
    name VARCHAR(255), name_key VARCHAR(255),
    link_type SMALLINT DEFAULT 2,
    refer_item_api_key VARCHAR(255),
    child_metamodel_api_key VARCHAR(255), parent_metamodel_api_key VARCHAR(255),
    cascade_delete SMALLINT DEFAULT 2,
    description VARCHAR(500), description_key VARCHAR(255),
    delete_flg SMALLINT DEFAULT 0,
    created_by BIGINT, created_at BIGINT, updated_by BIGINT, updated_at BIGINT,
    PRIMARY KEY (api_key)
);

-- 5. p_common_entity（PK=api_key）
CREATE TABLE p_common_entity (
    api_key VARCHAR(255) NOT NULL,
    namespace VARCHAR(50) NOT NULL DEFAULT 'system',
    label VARCHAR(255) NOT NULL, label_key VARCHAR(255),
    name VARCHAR(255), name_key VARCHAR(255),
    object_type SMALLINT, svg_id BIGINT, svg_color VARCHAR(20),
    description VARCHAR(500), description_key VARCHAR(255),
    custom_entityseq INTEGER,
    delete_flg SMALLINT DEFAULT 0, enable_flg SMALLINT DEFAULT 1,
    custom_flg SMALLINT DEFAULT 0, business_category SMALLINT,
    type_property VARCHAR(500), db_table VARCHAR(50), detail_flg SMALLINT,
    enable_team SMALLINT, enable_social SMALLINT, enable_config BIGINT,
    hidden_flg SMALLINT, searchable SMALLINT, enable_sharing SMALLINT,
    enable_script_trigger SMALLINT, enable_activity SMALLINT,
    enable_history_log SMALLINT, enable_report SMALLINT,
    enable_refer SMALLINT, enable_api SMALLINT,
    enable_flow BIGINT, enable_package BIGINT, extend_property VARCHAR(100),
    created_at BIGINT, created_by BIGINT, updated_at BIGINT, updated_by BIGINT,
    PRIMARY KEY (api_key)
);

-- 6. p_common_item（PK=entity_api_key+api_key）
CREATE TABLE p_common_item (
    entity_api_key VARCHAR(255) NOT NULL,
    api_key VARCHAR(255) NOT NULL,
    namespace VARCHAR(50) NOT NULL DEFAULT 'system',
    label VARCHAR(255) NOT NULL, label_key VARCHAR(255),
    name VARCHAR(255), name_key VARCHAR(255),
    item_type SMALLINT, data_type SMALLINT, type_property VARCHAR(4000),
    help_text VARCHAR(500), help_text_key VARCHAR(255),
    description VARCHAR(500), description_key VARCHAR(255),
    custom_itemseq INTEGER, default_value VARCHAR(4000),
    require_flg SMALLINT DEFAULT 0, delete_flg SMALLINT DEFAULT 0,
    custom_flg SMALLINT DEFAULT 0, enable_flg SMALLINT DEFAULT 1,
    creatable SMALLINT, updatable SMALLINT, unique_key_flg SMALLINT,
    enable_history_log SMALLINT DEFAULT 1, enable_config BIGINT, enable_package BIGINT,
    readonly_status SMALLINT, visible_status SMALLINT, hidden_flg SMALLINT,
    refer_entity_api_key VARCHAR(255), refer_link_api_key VARCHAR(255),
    db_column VARCHAR(255), item_order SMALLINT DEFAULT 0,
    sort_flg SMALLINT DEFAULT 0, column_name VARCHAR(100),
    created_at BIGINT, created_by BIGINT, updated_at BIGINT, updated_by BIGINT,
    PRIMARY KEY (entity_api_key, api_key)
);

-- 7. p_common_pickoption（PK=entity_api_key+item_api_key+api_key）
CREATE TABLE p_common_pickoption (
    entity_api_key VARCHAR(255) NOT NULL,
    item_api_key VARCHAR(255) NOT NULL,
    api_key VARCHAR(255) NOT NULL,
    namespace VARCHAR(50) NOT NULL DEFAULT 'system',
    label VARCHAR(255) NOT NULL, label_key VARCHAR(255),
    option_code SMALLINT, option_order SMALLINT,
    default_flg SMALLINT, global_flg SMALLINT,
    custom_flg SMALLINT DEFAULT 0, delete_flg SMALLINT DEFAULT 0, enable_flg SMALLINT DEFAULT 1,
    description VARCHAR(500), description_key VARCHAR(255),
    created_at BIGINT, created_by BIGINT, updated_at BIGINT, updated_by BIGINT,
    PRIMARY KEY (entity_api_key, item_api_key, api_key)
);

-- 8. p_common_entity_link（PK=api_key）
CREATE TABLE p_common_entity_link (
    api_key VARCHAR(255) NOT NULL,
    namespace VARCHAR(50) NOT NULL DEFAULT 'system',
    label VARCHAR(255) NOT NULL, label_key VARCHAR(255),
    name VARCHAR(255), name_key VARCHAR(255),
    type_property VARCHAR(500), link_type SMALLINT DEFAULT 0,
    parent_entity_api_key VARCHAR(255) NOT NULL, child_entity_api_key VARCHAR(255) NOT NULL,
    detail_link SMALLINT, cascade_delete SMALLINT DEFAULT 0, access_control SMALLINT DEFAULT 0,
    enable_flg SMALLINT DEFAULT 1,
    description VARCHAR(500), description_key VARCHAR(255), delete_flg SMALLINT DEFAULT 0,
    created_at BIGINT, created_by BIGINT, updated_at BIGINT, updated_by BIGINT,
    PRIMARY KEY (api_key)
);

-- 9. p_common_check_rule（PK=entity_api_key+api_key）
CREATE TABLE p_common_check_rule (
    entity_api_key VARCHAR(255) NOT NULL,
    api_key VARCHAR(255) NOT NULL,
    namespace VARCHAR(50) NOT NULL DEFAULT 'system',
    label VARCHAR(255) NOT NULL, label_key VARCHAR(255),
    name VARCHAR(255), name_key VARCHAR(255),
    active_flg SMALLINT DEFAULT 1,
    description VARCHAR(500), description_key VARCHAR(255),
    check_formula VARCHAR(5000), check_error_msg VARCHAR(5000), check_error_msg_key VARCHAR(200),
    check_error_location SMALLINT, check_error_item_api_key VARCHAR(255),
    check_all_items_flg SMALLINT DEFAULT 0, check_error_way SMALLINT,
    created_by BIGINT, created_at BIGINT, updated_by BIGINT, updated_at BIGINT,
    PRIMARY KEY (entity_api_key, api_key)
);

-- 10. p_common_metadata（PK=metamodel_api_key+api_key）
CREATE TABLE p_common_metadata (
    metamodel_api_key VARCHAR(255) NOT NULL,
    api_key VARCHAR(255) NOT NULL,
    namespace VARCHAR(50) NOT NULL DEFAULT 'system',
    parent_entity_api_key VARCHAR(255),
    label VARCHAR(255), label_key VARCHAR(255),
    custom_flg SMALLINT, metadata_order SMALLINT, owner_api_key VARCHAR(255),
    description VARCHAR(500), delete_flg SMALLINT,
    dbc_varchar_1 VARCHAR(300), dbc_varchar_2 VARCHAR(300), dbc_varchar_3 VARCHAR(300),
    dbc_varchar_4 VARCHAR(300), dbc_varchar_5 VARCHAR(300), dbc_varchar_6 VARCHAR(300),
    dbc_varchar_7 VARCHAR(300), dbc_varchar_8 VARCHAR(300), dbc_varchar_9 VARCHAR(300),
    dbc_varchar_10 VARCHAR(300), dbc_varchar_11 VARCHAR(300), dbc_varchar_12 VARCHAR(300),
    dbc_varchar_13 VARCHAR(300), dbc_varchar_14 VARCHAR(300), dbc_varchar_15 VARCHAR(300),
    dbc_varchar_16 VARCHAR(300), dbc_varchar_17 VARCHAR(300), dbc_varchar_18 VARCHAR(300),
    dbc_varchar_19 VARCHAR(300), dbc_varchar_20 VARCHAR(300),
    dbc_textarea_1 TEXT, dbc_textarea_2 TEXT, dbc_textarea_3 TEXT, dbc_textarea_4 TEXT,
    dbc_textarea_5 TEXT, dbc_textarea_6 TEXT, dbc_textarea_7 TEXT, dbc_textarea_8 TEXT,
    dbc_textarea_9 TEXT, dbc_textarea_10 TEXT,
    dbc_select_1 INTEGER, dbc_select_2 INTEGER, dbc_select_3 INTEGER, dbc_select_4 INTEGER,
    dbc_select_5 INTEGER, dbc_select_6 INTEGER, dbc_select_7 INTEGER, dbc_select_8 INTEGER,
    dbc_select_9 INTEGER, dbc_select_10 INTEGER,
    dbc_integer_1 BIGINT, dbc_integer_2 BIGINT, dbc_integer_3 BIGINT, dbc_integer_4 BIGINT,
    dbc_integer_5 BIGINT, dbc_integer_6 BIGINT, dbc_integer_7 BIGINT, dbc_integer_8 BIGINT,
    dbc_integer_9 BIGINT, dbc_integer_10 BIGINT,
    dbc_real_1 DOUBLE, dbc_real_2 DOUBLE, dbc_real_3 DOUBLE, dbc_real_4 DOUBLE,
    dbc_real_5 DOUBLE, dbc_real_6 DOUBLE, dbc_real_7 DOUBLE, dbc_real_8 DOUBLE,
    dbc_real_9 DOUBLE, dbc_real_10 DOUBLE,
    dbc_date_1 BIGINT, dbc_date_2 BIGINT, dbc_date_3 BIGINT, dbc_date_4 BIGINT,
    dbc_date_5 BIGINT, dbc_date_6 BIGINT, dbc_date_7 BIGINT, dbc_date_8 BIGINT,
    dbc_date_9 BIGINT, dbc_date_10 BIGINT,
    dbc_relation_1 BIGINT, dbc_relation_2 BIGINT, dbc_relation_3 BIGINT, dbc_relation_4 BIGINT,
    dbc_relation_5 BIGINT, dbc_relation_6 BIGINT, dbc_relation_7 BIGINT, dbc_relation_8 BIGINT,
    dbc_relation_9 BIGINT, dbc_relation_10 BIGINT,
    dbc_tinyint_1 SMALLINT, dbc_tinyint_2 SMALLINT, dbc_tinyint_3 SMALLINT, dbc_tinyint_4 SMALLINT,
    dbc_tinyint_5 SMALLINT, dbc_tinyint_6 SMALLINT, dbc_tinyint_7 SMALLINT, dbc_tinyint_8 SMALLINT,
    dbc_tinyint_9 SMALLINT, dbc_tinyint_10 SMALLINT,
    created_at BIGINT, created_by BIGINT, updated_at BIGINT, updated_by BIGINT,
    PRIMARY KEY (metamodel_api_key, api_key)
);
