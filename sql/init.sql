-- paas-metarepo-service 初始化 DDL
-- 兼容 MySQL 和 PostgreSQL 语法
-- 主键: BIGINT（应用层生成ID，不用自增）
-- 时间: BIGINT（毫秒时间戳）
-- 布尔/枚举: SMALLINT
-- 字符串: VARCHAR(N)

-- 1. p_meta_model（元模型定义表）
CREATE TABLE IF NOT EXISTS p_meta_model (
    id                      BIGINT          NOT NULL,
    tenant_id               BIGINT          NOT NULL,
    api_key                 VARCHAR(255)    NOT NULL,
    label                   VARCHAR(255)    NOT NULL,
    label_key               VARCHAR(255)    NOT NULL,
    namespace               VARCHAR(50),
    metamodel_type          SMALLINT,
    enable_package          SMALLINT,
    enable_app              SMALLINT        DEFAULT 0,
    enable_deprecation      SMALLINT        DEFAULT 0,
    enable_deactivation     SMALLINT        DEFAULT 0,
    enable_delta            SMALLINT        DEFAULT 0,
    enable_log              SMALLINT        DEFAULT 0,
    delta_scope             SMALLINT,
    delta_mode              SMALLINT,
    enable_module_control   SMALLINT        DEFAULT 0,
    enable_common           SMALLINT        DEFAULT 1,
    enable_tenant           SMALLINT        DEFAULT 1,
    db_table                VARCHAR(50),
    description             VARCHAR(500),
    description_key         VARCHAR(255),
    entity_dependency       SMALLINT        DEFAULT 0,
    visible                 SMALLINT        DEFAULT 0,
    delete_flg              SMALLINT        DEFAULT 0,
    created_by              BIGINT,
    created_at              BIGINT,
    updated_by              BIGINT,
    updated_at              BIGINT,
    PRIMARY KEY (id)
);
CREATE UNIQUE INDEX uk_meta_model_apikey ON p_meta_model(tenant_id, api_key);

-- 2. p_meta_item（元模型字段项定义）
CREATE TABLE IF NOT EXISTS p_meta_item (
    id                      BIGINT          NOT NULL,
    tenant_id               BIGINT          NOT NULL,
    metamodel_api_key       VARCHAR(255)    NOT NULL,
    api_key                 VARCHAR(255)    NOT NULL,
    label                   VARCHAR(255)    NOT NULL,
    label_key               VARCHAR(255)    NOT NULL,
    namespace               VARCHAR(50),
    item_type               SMALLINT,
    data_type               SMALLINT,
    item_order              SMALLINT,
    require_flg             SMALLINT,
    unique_key_flg          SMALLINT        DEFAULT 0,
    creatable               SMALLINT,
    updatable               SMALLINT,
    enable_package          SMALLINT,
    enable_delta            SMALLINT        DEFAULT 0,
    enable_log              SMALLINT        DEFAULT 0,
    db_column               VARCHAR(255),
    description             VARCHAR(500),
    description_key         VARCHAR(255),
    min_length              INTEGER,
    max_length              INTEGER,
    text_format             SMALLINT,
    json_schema             TEXT,
    name_field              SMALLINT        DEFAULT 0,
    delete_flg              SMALLINT        DEFAULT 0,
    created_by              BIGINT,
    created_at              BIGINT,
    updated_by              BIGINT,
    updated_at              BIGINT,
    PRIMARY KEY (id)
);
CREATE INDEX idx_meta_item_metamodel ON p_meta_item(tenant_id, metamodel_api_key);

-- 3. p_meta_metamodel_data（通用元数据实例大宽表）
CREATE TABLE IF NOT EXISTS p_meta_metamodel_data (
    id                      BIGINT          NOT NULL,
    namespace               VARCHAR(100),
    tenant_id               BIGINT          NOT NULL,
    metamodel_id            BIGINT          NOT NULL,
    parent_entity_id        BIGINT,
    api_key                 VARCHAR(255),
    label                   VARCHAR(255),
    label_key               VARCHAR(255),
    custom_flg              SMALLINT,
    metadata_order          SMALLINT,
    owner_id                BIGINT,
    description             VARCHAR(500),
    delete_flg              SMALLINT,
    dbc_varchar_1  VARCHAR(300), dbc_varchar_2  VARCHAR(300), dbc_varchar_3  VARCHAR(300),
    dbc_varchar_4  VARCHAR(300), dbc_varchar_5  VARCHAR(300), dbc_varchar_6  VARCHAR(300),
    dbc_varchar_7  VARCHAR(300), dbc_varchar_8  VARCHAR(300), dbc_varchar_9  VARCHAR(300),
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
    dbc_real_1 DOUBLE PRECISION, dbc_real_2 DOUBLE PRECISION, dbc_real_3 DOUBLE PRECISION,
    dbc_real_4 DOUBLE PRECISION, dbc_real_5 DOUBLE PRECISION, dbc_real_6 DOUBLE PRECISION,
    dbc_real_7 DOUBLE PRECISION, dbc_real_8 DOUBLE PRECISION, dbc_real_9 DOUBLE PRECISION,
    dbc_real_10 DOUBLE PRECISION,
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
    PRIMARY KEY (id)
);
CREATE INDEX idx_mmd_tenant_mm ON p_meta_metamodel_data(tenant_id, metamodel_id);
CREATE UNIQUE INDEX uk_mmd_apikey ON p_meta_metamodel_data(tenant_id, metamodel_id, api_key);
CREATE INDEX idx_mmd_parent ON p_meta_metamodel_data(tenant_id, parent_entity_id);

-- 4. p_meta_tenant_metadata（租户级元数据，结构同 p_meta_metamodel_data）
CREATE TABLE IF NOT EXISTS p_meta_tenant_metadata (
    id BIGINT NOT NULL, namespace VARCHAR(100), tenant_id BIGINT NOT NULL,
    metamodel_id BIGINT NOT NULL, parent_entity_id BIGINT,
    api_key VARCHAR(255), label VARCHAR(255), label_key VARCHAR(255),
    custom_flg SMALLINT, metadata_order SMALLINT, owner_id BIGINT,
    description VARCHAR(500), delete_flg SMALLINT,
    dbc_varchar_1  VARCHAR(300), dbc_varchar_2  VARCHAR(300), dbc_varchar_3  VARCHAR(300),
    dbc_varchar_4  VARCHAR(300), dbc_varchar_5  VARCHAR(300), dbc_varchar_6  VARCHAR(300),
    dbc_varchar_7  VARCHAR(300), dbc_varchar_8  VARCHAR(300), dbc_varchar_9  VARCHAR(300),
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
    dbc_real_1 DOUBLE PRECISION, dbc_real_2 DOUBLE PRECISION, dbc_real_3 DOUBLE PRECISION,
    dbc_real_4 DOUBLE PRECISION, dbc_real_5 DOUBLE PRECISION, dbc_real_6 DOUBLE PRECISION,
    dbc_real_7 DOUBLE PRECISION, dbc_real_8 DOUBLE PRECISION, dbc_real_9 DOUBLE PRECISION,
    dbc_real_10 DOUBLE PRECISION,
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
    PRIMARY KEY (id)
);
CREATE INDEX idx_tmd_tenant_mm ON p_meta_tenant_metadata(tenant_id, metamodel_id);
CREATE UNIQUE INDEX uk_tmd_apikey ON p_meta_tenant_metadata(tenant_id, metamodel_id, api_key);

-- 5. p_custom_entity（自定义对象定义）
CREATE TABLE IF NOT EXISTS p_custom_entity (
    id BIGINT NOT NULL, tenant_id BIGINT NOT NULL, name_space VARCHAR(100),
    entity_id BIGINT, name VARCHAR(255), name_key VARCHAR(255),
    api_key VARCHAR(255),
    label VARCHAR(255), label_key VARCHAR(255), object_type SMALLINT,
    svg_id BIGINT, svg_color VARCHAR(20),
    description VARCHAR(500), description_key VARCHAR(255),
    custom_entityseq INTEGER, delete_flg SMALLINT, enable_flg SMALLINT,
    custom_flg SMALLINT, business_category SMALLINT, type_property VARCHAR(500),
    db_table VARCHAR(50), detail_flg SMALLINT, enable_team SMALLINT,
    enable_social SMALLINT, enable_config BIGINT, hidden_flg SMALLINT,
    searchable SMALLINT, enable_sharing SMALLINT, enable_script_trigger SMALLINT,
    enable_activity SMALLINT, enable_history_log SMALLINT, enable_report SMALLINT,
    enable_refer SMALLINT, enable_api SMALLINT, enable_flow BIGINT,
    enable_package BIGINT, extend_property VARCHAR(100),
    created_at BIGINT, created_by BIGINT, updated_at BIGINT, updated_by BIGINT,
    PRIMARY KEY (id)
);
CREATE UNIQUE INDEX uk_entity_tenant_apikey ON p_custom_entity(tenant_id, api_key);
CREATE INDEX idx_entity_tenant ON p_custom_entity(tenant_id);

-- 6. p_custom_item（自定义字段定义）
CREATE TABLE IF NOT EXISTS p_custom_item (
    id BIGINT NOT NULL, tenant_id BIGINT NOT NULL, entity_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL, name_key VARCHAR(255),
    api_key VARCHAR(255) NOT NULL,
    label VARCHAR(255) NOT NULL, label_key VARCHAR(255),
    item_type SMALLINT, data_type SMALLINT, type_property VARCHAR(4000),
    help_text VARCHAR(500), help_text_key VARCHAR(255),
    description VARCHAR(500), description_key VARCHAR(255),
    custom_itemseq INTEGER, default_value VARCHAR(4000),
    require_flg SMALLINT NOT NULL, delete_flg SMALLINT NOT NULL,
    custom_flg SMALLINT NOT NULL, enable_flg SMALLINT NOT NULL,
    creatable SMALLINT, updatable SMALLINT, unique_key_flg SMALLINT,
    enable_history_log SMALLINT DEFAULT 1, enable_config BIGINT,
    enable_package BIGINT, readonly_status SMALLINT, visible_status SMALLINT,
    hidden_flg SMALLINT, refer_entity_id BIGINT, refer_link_id BIGINT,
    db_column VARCHAR(255), item_order SMALLINT NOT NULL DEFAULT 0,
    sort_flg SMALLINT DEFAULT 0, column_name VARCHAR(100),
    created_at BIGINT, created_by BIGINT, updated_at BIGINT, updated_by BIGINT,
    PRIMARY KEY (id)
);
CREATE UNIQUE INDEX uk_item_entity_apikey ON p_custom_item(tenant_id, entity_id, api_key);
CREATE INDEX idx_item_entity ON p_custom_item(tenant_id, entity_id);
CREATE INDEX idx_item_refer ON p_custom_item(tenant_id, refer_entity_id);

-- 7. p_custom_entity_link（对象关联关系）
CREATE TABLE IF NOT EXISTS p_custom_entity_link (
    id BIGINT NOT NULL, tenant_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL, name_key VARCHAR(255),
    api_key VARCHAR(255),
    label VARCHAR(255) NOT NULL, label_key VARCHAR(255) DEFAULT '',
    type_property VARCHAR(500), link_type SMALLINT NOT NULL DEFAULT 0,
    parent_entity_id BIGINT NOT NULL, child_entity_id BIGINT NOT NULL,
    detail_link SMALLINT, cascade_delete SMALLINT NOT NULL,
    access_control SMALLINT NOT NULL, enable_flg SMALLINT NOT NULL,
    description VARCHAR(500), description_key VARCHAR(255),
    delete_flg SMALLINT,
    created_at BIGINT, created_by BIGINT, updated_at BIGINT, updated_by BIGINT,
    PRIMARY KEY (id)
);
CREATE INDEX idx_link_parent ON p_custom_entity_link(tenant_id, parent_entity_id);
CREATE INDEX idx_link_child ON p_custom_entity_link(tenant_id, child_entity_id);
CREATE UNIQUE INDEX uk_link_apikey ON p_custom_entity_link(tenant_id, api_key);

-- 8. p_custom_pickoption（字段选项值）
CREATE TABLE IF NOT EXISTS p_custom_pickoption (
    id BIGINT NOT NULL, tenant_id BIGINT NOT NULL,
    entity_id BIGINT, item_id BIGINT, api_key VARCHAR(255),
    option_code SMALLINT, option_label VARCHAR(255),
    option_label_key VARCHAR(255), option_order SMALLINT,
    default_flg SMALLINT, global_flg SMALLINT, custom_flg SMALLINT,
    delete_flg SMALLINT NOT NULL DEFAULT 0, enable_flg SMALLINT,
    description VARCHAR(500), description_key VARCHAR(255),
    created_at BIGINT, created_by BIGINT, updated_at BIGINT, updated_by BIGINT,
    PRIMARY KEY (id)
);
CREATE INDEX idx_pick_item ON p_custom_pickoption(tenant_id, item_id);
CREATE UNIQUE INDEX uk_pick_apikey ON p_custom_pickoption(tenant_id, item_id, api_key);

-- 9. p_custom_check_rule（校验规则）
CREATE TABLE IF NOT EXISTS p_custom_check_rule (
    id BIGINT NOT NULL, tenant_id BIGINT NOT NULL,
    entity_id BIGINT NOT NULL DEFAULT 0,
    name VARCHAR(255) NOT NULL, name_key VARCHAR(255),
    api_key VARCHAR(255) NOT NULL,
    rule_label VARCHAR(100), rule_label_key VARCHAR(100),
    active_flg SMALLINT, description VARCHAR(500), description_key VARCHAR(255),
    check_formula VARCHAR(5000), check_error_msg VARCHAR(5000),
    check_error_msg_key VARCHAR(200), check_error_location SMALLINT,
    check_error_item_id BIGINT, check_all_items_flg SMALLINT DEFAULT 0,
    check_error_way SMALLINT,
    created_by BIGINT NOT NULL, created_at BIGINT NOT NULL,
    updated_by BIGINT NOT NULL, updated_at BIGINT NOT NULL,
    PRIMARY KEY (id)
);
CREATE INDEX idx_rule_object ON p_custom_check_rule(tenant_id, entity_id);
CREATE UNIQUE INDEX uk_rule_apikey ON p_custom_check_rule(tenant_id, entity_id, api_key);

-- 10. p_meta_option（元模型选项值定义）
CREATE TABLE IF NOT EXISTS p_meta_option (
    id BIGINT NOT NULL, tenant_id BIGINT NOT NULL,
    metamodel_api_key VARCHAR(255) NOT NULL, item_api_key VARCHAR(255) NOT NULL,
    api_key VARCHAR(255) NOT NULL, label VARCHAR(255) NOT NULL,
    label_key VARCHAR(255), namespace VARCHAR(50),
    option_key VARCHAR(100) NOT NULL,
    option_order SMALLINT DEFAULT 0, default_flg SMALLINT DEFAULT 0,
    enable_flg SMALLINT DEFAULT 1,
    description VARCHAR(500), description_key VARCHAR(255),
    delete_flg SMALLINT DEFAULT 0,
    created_by BIGINT, created_at BIGINT, updated_by BIGINT, updated_at BIGINT,
    PRIMARY KEY (id)
);
CREATE UNIQUE INDEX uk_option_apikey ON p_meta_option(tenant_id, metamodel_api_key, item_api_key, api_key);
CREATE INDEX idx_option_item ON p_meta_option(tenant_id, metamodel_api_key, item_api_key);

-- 11. p_meta_link（元模型关联关系）
CREATE TABLE IF NOT EXISTS p_meta_link (
    id BIGINT NOT NULL, tenant_id BIGINT NOT NULL,
    api_key VARCHAR(255) NOT NULL, label VARCHAR(255) NOT NULL,
    label_key VARCHAR(255), namespace VARCHAR(50),
    link_type SMALLINT NOT NULL DEFAULT 2,
    refer_item_api_key BIGINT NOT NULL,
    child_metamodel_api_key BIGINT NOT NULL DEFAULT 0,
    parent_metamodel_api_key BIGINT NOT NULL DEFAULT 0,
    cascade_delete SMALLINT DEFAULT 2,
    description VARCHAR(500), description_key VARCHAR(255),
    delete_flg SMALLINT DEFAULT 0,
    created_by BIGINT, created_at BIGINT, updated_by BIGINT, updated_at BIGINT,
    PRIMARY KEY (id)
);

-- 12. p_meta_log（元数据变更日志）
CREATE TABLE IF NOT EXISTS p_meta_log (
    id BIGINT NOT NULL, tenant_id BIGINT NOT NULL,
    metadata_id BIGINT NOT NULL, trace_id VARCHAR(255) NOT NULL,
    entity_id BIGINT, metamodel_id BIGINT NOT NULL,
    old_value TEXT, new_value TEXT, op_type SMALLINT,
    from_type SMALLINT, parent_metamodel_id BIGINT, parent_metadata_id BIGINT,
    sync SMALLINT, created_by BIGINT, created_at BIGINT,
    entrust_tenant_id BIGINT, origin_tenant_id BIGINT,
    PRIMARY KEY (id)
);
CREATE INDEX idx_log_tenant_obj ON p_meta_log(tenant_id, entity_id);
CREATE INDEX idx_log_metadata ON p_meta_log(tenant_id, metadata_id);
CREATE INDEX idx_log_created ON p_meta_log(tenant_id, created_at);

-- 13. p_meta_i18n_resource（多语言资源）
CREATE TABLE IF NOT EXISTS p_meta_i18n_resource (
    id BIGINT NOT NULL, tenant_id BIGINT NOT NULL,
    metamodel_id BIGINT, metadata_id BIGINT, entity_id BIGINT,
    resource_key VARCHAR(256) NOT NULL, lang_code VARCHAR(8) NOT NULL,
    resource_value TEXT, description VARCHAR(256),
    delete_flg SMALLINT DEFAULT 0,
    created_at BIGINT NOT NULL, created_by BIGINT NOT NULL,
    updated_at BIGINT NOT NULL, updated_by BIGINT NOT NULL,
    PRIMARY KEY (id)
);
CREATE INDEX idx_i18n_key ON p_meta_i18n_resource(tenant_id, resource_key, lang_code);

-- 14. p_meta_migration_process / p_meta_migration_process_unit
CREATE TABLE IF NOT EXISTS p_meta_migration_process (
    id BIGINT NOT NULL, tenant_id BIGINT NOT NULL,
    package_name VARCHAR(255), migration_stage SMALLINT NOT NULL,
    start_time BIGINT, end_time BIGINT, status SMALLINT NOT NULL,
    process_content TEXT,
    created_at BIGINT, created_by BIGINT, updated_at BIGINT, updated_by BIGINT,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS p_meta_migration_process_unit (
    id BIGINT NOT NULL, tenant_id BIGINT NOT NULL,
    pid BIGINT, process_id BIGINT NOT NULL,
    migration_stage SMALLINT NOT NULL, unit_name VARCHAR(100),
    rollback_unit_flg SMALLINT, start_time BIGINT, end_time BIGINT,
    time_consuming BIGINT, exec_exception TEXT, rollback_exception TEXT,
    status SMALLINT NOT NULL,
    created_at BIGINT, created_by BIGINT, updated_at BIGINT, updated_by BIGINT,
    PRIMARY KEY (id)
);
CREATE INDEX idx_mpu_process ON p_meta_migration_process_unit(process_id);

-- 15. x_global_pickitem（全局选项集）
CREATE TABLE IF NOT EXISTS x_global_pickitem (
    id BIGINT NOT NULL, tenant_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL, name_key VARCHAR(100),
    api_key VARCHAR(100),
    label VARCHAR(100), label_key VARCHAR(100),
    custom_flg SMALLINT NOT NULL,
    description VARCHAR(500), description_key VARCHAR(255),
    created_by BIGINT NOT NULL, created_at BIGINT NOT NULL,
    updated_by BIGINT NOT NULL, updated_at BIGINT NOT NULL,
    PRIMARY KEY (id)
);
