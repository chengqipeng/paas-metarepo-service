-- ============================================================
-- paas-metarepo-service 全新单库初始化脚本（开发环境）
-- 基于当前最新 Entity 类定义生成，兼容 MySQL / PostgreSQL
-- 生成时间：2026-03-27
--
-- 所有表在同一个 paas_metarepo 数据库中
-- Common 表（p_common_*）和 Tenant 表（p_tenant_*）通过
-- DynamicTableNameHolder 在运行时切换，共用同一个 DataSource
--
-- 执行方式：
--   mysql -h 106.14.194.144 -u root -p < init_fresh_single_db.sql
-- ============================================================

DROP DATABASE IF EXISTS paas_metarepo;
CREATE DATABASE paas_metarepo;
USE paas_metarepo;

-- ************************************************************
-- A. 元模型定义表（平台级，TenantInterceptor 忽略）
-- ************************************************************

-- A1. p_meta_model（元模型 Schema 定义）
-- Entity: MetaModel extends BaseMetaCommonEntity → @TableName("p_meta_model")
CREATE TABLE p_meta_model (
    api_key                 VARCHAR(255)    NOT NULL,
    namespace               VARCHAR(50)     NOT NULL DEFAULT 'system',
    label                   VARCHAR(255)    NOT NULL,
    label_key               VARCHAR(255),
    metamodel_type          SMALLINT,
    enable_package          SMALLINT        DEFAULT 0,
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
    created_by BIGINT, created_at BIGINT, updated_by BIGINT, updated_at BIGINT,
    PRIMARY KEY (api_key)
);

-- A2. p_meta_item（元模型字段项定义）
-- Entity: MetaItem extends BaseMetaCommonEntity → @TableName("p_meta_item")
CREATE TABLE p_meta_item (
    metamodel_api_key       VARCHAR(255)    NOT NULL,
    api_key                 VARCHAR(255)    NOT NULL,
    namespace               VARCHAR(50)     NOT NULL DEFAULT 'system',
    label                   VARCHAR(255)    NOT NULL,
    label_key               VARCHAR(255),
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
    created_by BIGINT, created_at BIGINT, updated_by BIGINT, updated_at BIGINT,
    PRIMARY KEY (metamodel_api_key, api_key)
);

-- A3. p_meta_option（元模型选项值定义）
-- Entity: MetaOption extends BaseMetaCommonEntity → @TableName("p_meta_option")
CREATE TABLE p_meta_option (
    metamodel_api_key       VARCHAR(255)    NOT NULL,
    item_api_key            VARCHAR(255)    NOT NULL,
    api_key                 VARCHAR(255)    NOT NULL,
    namespace               VARCHAR(50)     NOT NULL DEFAULT 'system',
    label                   VARCHAR(255)    NOT NULL,
    label_key               VARCHAR(255),
    option_key              VARCHAR(100),
    option_order            SMALLINT        DEFAULT 0,
    default_flg             SMALLINT        DEFAULT 0,
    enable_flg              SMALLINT        DEFAULT 1,
    description             VARCHAR(500),
    description_key         VARCHAR(255),
    delete_flg              SMALLINT        DEFAULT 0,
    created_by BIGINT, created_at BIGINT, updated_by BIGINT, updated_at BIGINT,
    PRIMARY KEY (metamodel_api_key, item_api_key, api_key)
);

-- A4. p_meta_link（元模型关联关系）
-- Entity: MetaLink extends BaseMetaCommonEntity → @TableName("p_meta_link")
CREATE TABLE p_meta_link (
    api_key                 VARCHAR(255)    NOT NULL,
    namespace               VARCHAR(50)     NOT NULL DEFAULT 'system',
    label                   VARCHAR(255)    NOT NULL,
    label_key               VARCHAR(255),
    link_type               SMALLINT        DEFAULT 2,
    refer_item_api_key      BIGINT,
    child_metamodel_api_key BIGINT,
    parent_metamodel_api_key BIGINT,
    cascade_delete          SMALLINT        DEFAULT 2,
    description             VARCHAR(500),
    description_key         VARCHAR(255),
    delete_flg              SMALLINT        DEFAULT 0,
    created_by BIGINT, created_at BIGINT, updated_by BIGINT, updated_at BIGINT,
    PRIMARY KEY (api_key)
);

-- ************************************************************
-- B. Common 级业务元数据表（无 tenant_id，TenantInterceptor 忽略 p_common_ 前缀）
-- ************************************************************

-- B1. p_common_entity
CREATE TABLE p_common_entity (
    api_key VARCHAR(255) NOT NULL, namespace VARCHAR(50) NOT NULL DEFAULT 'system',
    label VARCHAR(255) NOT NULL, label_key VARCHAR(255),
    entity_type SMALLINT, svg_api_key VARCHAR(255), svg_color VARCHAR(20),
    description VARCHAR(500), description_key VARCHAR(255), custom_entity_seq INTEGER,
    delete_flg SMALLINT DEFAULT 0, enable_flg SMALLINT DEFAULT 1, custom_flg SMALLINT DEFAULT 0,
    business_category SMALLINT, type_property VARCHAR(500), db_table VARCHAR(50),
    detail_flg SMALLINT, enable_team SMALLINT, enable_social SMALLINT, enable_config BIGINT,
    hidden_flg SMALLINT, searchable SMALLINT, enable_sharing SMALLINT,
    enable_script_trigger SMALLINT, enable_activity SMALLINT, enable_history_log SMALLINT,
    enable_report SMALLINT, enable_refer SMALLINT, enable_api SMALLINT,
    enable_flow BIGINT, enable_package BIGINT, extend_property VARCHAR(100),
    created_at BIGINT, created_by BIGINT, updated_at BIGINT, updated_by BIGINT,
    PRIMARY KEY (api_key)
);

-- B2. p_common_item
CREATE TABLE p_common_item (
    entity_api_key VARCHAR(255) NOT NULL, api_key VARCHAR(255) NOT NULL,
    namespace VARCHAR(50) NOT NULL DEFAULT 'system',
    label VARCHAR(255) NOT NULL, label_key VARCHAR(255),
    item_type SMALLINT, data_type SMALLINT, type_property VARCHAR(4000),
    help_text VARCHAR(500), help_text_key VARCHAR(255),
    description VARCHAR(500), description_key VARCHAR(255),
    custom_item_seq INTEGER, default_value VARCHAR(4000),
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

-- B3. p_common_pick_option
CREATE TABLE p_common_pick_option (
    entity_api_key VARCHAR(255) NOT NULL, item_api_key VARCHAR(255) NOT NULL,
    api_key VARCHAR(255) NOT NULL, namespace VARCHAR(50) NOT NULL DEFAULT 'system',
    label VARCHAR(255) NOT NULL, label_key VARCHAR(255),
    option_order SMALLINT, default_flg SMALLINT, global_flg SMALLINT,
    custom_flg SMALLINT DEFAULT 0, enable_flg SMALLINT DEFAULT 1,
    description VARCHAR(500), description_key VARCHAR(255), delete_flg SMALLINT DEFAULT 0,
    created_at BIGINT, created_by BIGINT, updated_at BIGINT, updated_by BIGINT,
    PRIMARY KEY (entity_api_key, item_api_key, api_key)
);

-- B4. p_common_entity_link
CREATE TABLE p_common_entity_link (
    api_key VARCHAR(255) NOT NULL, namespace VARCHAR(50) NOT NULL DEFAULT 'system',
    label VARCHAR(255) NOT NULL, label_key VARCHAR(255),
    type_property VARCHAR(500), link_type SMALLINT DEFAULT 0,
    parent_entity_api_key VARCHAR(255) NOT NULL, child_entity_api_key VARCHAR(255) NOT NULL,
    detail_link SMALLINT, cascade_delete SMALLINT DEFAULT 0, access_control SMALLINT DEFAULT 0,
    enable_flg SMALLINT DEFAULT 1, description VARCHAR(500), description_key VARCHAR(255),
    delete_flg SMALLINT DEFAULT 0,
    created_at BIGINT, created_by BIGINT, updated_at BIGINT, updated_by BIGINT,
    PRIMARY KEY (api_key)
);

-- B5. p_common_check_rule
CREATE TABLE p_common_check_rule (
    entity_api_key VARCHAR(255) NOT NULL, api_key VARCHAR(255) NOT NULL,
    namespace VARCHAR(50) NOT NULL DEFAULT 'system',
    label VARCHAR(255) NOT NULL, label_key VARCHAR(255),
    active_flg SMALLINT DEFAULT 1, description VARCHAR(500), description_key VARCHAR(255),
    check_formula VARCHAR(5000), check_error_msg VARCHAR(5000), check_error_msg_key VARCHAR(200),
    check_error_location SMALLINT, check_error_item_api_key VARCHAR(255),
    check_all_items_flg SMALLINT DEFAULT 0, check_error_way SMALLINT,
    delete_flg SMALLINT DEFAULT 0,
    created_by BIGINT, created_at BIGINT, updated_by BIGINT, updated_at BIGINT,
    PRIMARY KEY (entity_api_key, api_key)
);

-- B6. p_common_refer_filter
CREATE TABLE p_common_refer_filter (
    entity_api_key VARCHAR(255) NOT NULL, item_api_key VARCHAR(255) NOT NULL,
    api_key VARCHAR(255) NOT NULL, namespace VARCHAR(50) NOT NULL DEFAULT 'system',
    label VARCHAR(255), label_key VARCHAR(255),
    link_api_key VARCHAR(255), filter_field VARCHAR(255), filter_operator VARCHAR(50),
    filter_value VARCHAR(500), filter_order SMALLINT, description VARCHAR(500),
    description_key VARCHAR(255), delete_flg SMALLINT DEFAULT 0,
    created_at BIGINT, created_by BIGINT, updated_at BIGINT, updated_by BIGINT,
    PRIMARY KEY (entity_api_key, item_api_key, api_key)
);

-- B7. p_common_metadata（Common 级大宽表元数据）
CREATE TABLE p_common_metadata (
    metamodel_api_key VARCHAR(255) NOT NULL, api_key VARCHAR(255) NOT NULL,
    namespace VARCHAR(50) NOT NULL DEFAULT 'system',
    parent_entity_api_key VARCHAR(255), label VARCHAR(255), label_key VARCHAR(255),
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
    PRIMARY KEY (metamodel_api_key, api_key)
);

-- ************************************************************
-- C. Tenant 级业务元数据表（有 tenant_id，TenantInterceptor 自动注入）
-- ************************************************************

-- C1. p_tenant_entity
CREATE TABLE p_tenant_entity (
    tenant_id BIGINT NOT NULL, api_key VARCHAR(255) NOT NULL,
    namespace VARCHAR(50) NOT NULL DEFAULT 'tenant',
    label VARCHAR(255) NOT NULL, label_key VARCHAR(255),
    entity_type SMALLINT, svg_api_key VARCHAR(255), svg_color VARCHAR(20),
    description VARCHAR(500), description_key VARCHAR(255), custom_entity_seq INTEGER,
    delete_flg SMALLINT DEFAULT 0, enable_flg SMALLINT DEFAULT 1, custom_flg SMALLINT DEFAULT 1,
    business_category SMALLINT, type_property VARCHAR(500), db_table VARCHAR(50),
    detail_flg SMALLINT, enable_team SMALLINT, enable_social SMALLINT, enable_config BIGINT,
    hidden_flg SMALLINT, searchable SMALLINT, enable_sharing SMALLINT,
    enable_script_trigger SMALLINT, enable_activity SMALLINT, enable_history_log SMALLINT,
    enable_report SMALLINT, enable_refer SMALLINT, enable_api SMALLINT,
    enable_flow BIGINT, enable_package BIGINT, extend_property VARCHAR(100),
    created_at BIGINT, created_by BIGINT, updated_at BIGINT, updated_by BIGINT,
    PRIMARY KEY (tenant_id, api_key)
);

-- C2. p_tenant_item
CREATE TABLE p_tenant_item (
    tenant_id BIGINT NOT NULL, entity_api_key VARCHAR(255) NOT NULL,
    api_key VARCHAR(255) NOT NULL, namespace VARCHAR(50) NOT NULL DEFAULT 'tenant',
    label VARCHAR(255) NOT NULL, label_key VARCHAR(255),
    item_type SMALLINT, data_type SMALLINT, type_property VARCHAR(4000),
    help_text VARCHAR(500), help_text_key VARCHAR(255),
    description VARCHAR(500), description_key VARCHAR(255),
    custom_item_seq INTEGER, default_value VARCHAR(4000),
    require_flg SMALLINT DEFAULT 0, delete_flg SMALLINT DEFAULT 0,
    custom_flg SMALLINT DEFAULT 1, enable_flg SMALLINT DEFAULT 1,
    creatable SMALLINT, updatable SMALLINT, unique_key_flg SMALLINT,
    enable_history_log SMALLINT DEFAULT 1, enable_config BIGINT, enable_package BIGINT,
    readonly_status SMALLINT, visible_status SMALLINT, hidden_flg SMALLINT,
    refer_entity_api_key VARCHAR(255), refer_link_api_key VARCHAR(255),
    db_column VARCHAR(255), item_order SMALLINT DEFAULT 0,
    sort_flg SMALLINT DEFAULT 0, column_name VARCHAR(100),
    created_at BIGINT, created_by BIGINT, updated_at BIGINT, updated_by BIGINT,
    PRIMARY KEY (tenant_id, entity_api_key, api_key)
);

-- C3. p_tenant_pick_option
CREATE TABLE p_tenant_pick_option (
    tenant_id BIGINT NOT NULL, entity_api_key VARCHAR(255) NOT NULL,
    item_api_key VARCHAR(255) NOT NULL, api_key VARCHAR(255) NOT NULL,
    namespace VARCHAR(50) NOT NULL DEFAULT 'tenant',
    label VARCHAR(255) NOT NULL, label_key VARCHAR(255),
    option_order SMALLINT, default_flg SMALLINT, global_flg SMALLINT,
    custom_flg SMALLINT DEFAULT 1, enable_flg SMALLINT DEFAULT 1,
    description VARCHAR(500), description_key VARCHAR(255), delete_flg SMALLINT DEFAULT 0,
    created_at BIGINT, created_by BIGINT, updated_at BIGINT, updated_by BIGINT,
    PRIMARY KEY (tenant_id, entity_api_key, item_api_key, api_key)
);

-- C4. p_tenant_entity_link
CREATE TABLE p_tenant_entity_link (
    tenant_id BIGINT NOT NULL, api_key VARCHAR(255) NOT NULL,
    namespace VARCHAR(50) NOT NULL DEFAULT 'tenant',
    label VARCHAR(255) NOT NULL, label_key VARCHAR(255),
    type_property VARCHAR(500), link_type SMALLINT DEFAULT 0,
    parent_entity_api_key VARCHAR(255) NOT NULL, child_entity_api_key VARCHAR(255) NOT NULL,
    detail_link SMALLINT, cascade_delete SMALLINT DEFAULT 0, access_control SMALLINT DEFAULT 0,
    enable_flg SMALLINT DEFAULT 1, description VARCHAR(500), description_key VARCHAR(255),
    delete_flg SMALLINT DEFAULT 0,
    created_at BIGINT, created_by BIGINT, updated_at BIGINT, updated_by BIGINT,
    PRIMARY KEY (tenant_id, api_key)
);

-- C5. p_tenant_check_rule
CREATE TABLE p_tenant_check_rule (
    tenant_id BIGINT NOT NULL, entity_api_key VARCHAR(255) NOT NULL,
    api_key VARCHAR(255) NOT NULL, namespace VARCHAR(50) NOT NULL DEFAULT 'tenant',
    label VARCHAR(255) NOT NULL, label_key VARCHAR(255),
    active_flg SMALLINT DEFAULT 1, description VARCHAR(500), description_key VARCHAR(255),
    check_formula VARCHAR(5000), check_error_msg VARCHAR(5000), check_error_msg_key VARCHAR(200),
    check_error_location SMALLINT, check_error_item_api_key VARCHAR(255),
    check_all_items_flg SMALLINT DEFAULT 0, check_error_way SMALLINT,
    delete_flg SMALLINT DEFAULT 0,
    created_by BIGINT, created_at BIGINT, updated_by BIGINT, updated_at BIGINT,
    PRIMARY KEY (tenant_id, entity_api_key, api_key)
);

-- C6. p_tenant_refer_filter
CREATE TABLE p_tenant_refer_filter (
    tenant_id BIGINT NOT NULL, entity_api_key VARCHAR(255) NOT NULL,
    item_api_key VARCHAR(255) NOT NULL, api_key VARCHAR(255) NOT NULL,
    namespace VARCHAR(50) NOT NULL DEFAULT 'tenant',
    label VARCHAR(255), label_key VARCHAR(255),
    link_api_key VARCHAR(255), filter_field VARCHAR(255), filter_operator VARCHAR(50),
    filter_value VARCHAR(500), filter_order SMALLINT, description VARCHAR(500),
    description_key VARCHAR(255), delete_flg SMALLINT DEFAULT 0,
    created_at BIGINT, created_by BIGINT, updated_at BIGINT, updated_by BIGINT,
    PRIMARY KEY (tenant_id, entity_api_key, item_api_key, api_key)
);

-- C7. p_tenant_metadata（Tenant 级大宽表元数据）
CREATE TABLE p_tenant_metadata (
    tenant_id BIGINT NOT NULL, metamodel_api_key VARCHAR(255) NOT NULL,
    api_key VARCHAR(255) NOT NULL, namespace VARCHAR(50) NOT NULL DEFAULT 'tenant',
    parent_entity_api_key VARCHAR(255), label VARCHAR(255), label_key VARCHAR(255),
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
    PRIMARY KEY (tenant_id, metamodel_api_key, api_key)
);

-- ************************************************************
-- D. 独立功能表（BaseEntity 继承，有 id + tenant_id）
-- ************************************************************

-- D1. p_meta_metamodel_data（通用元数据实例大宽表）
-- Entity: MetaMetamodelData extends BaseEntity → @DynamicTableNameEntity
CREATE TABLE p_meta_metamodel_data (
    id BIGINT NOT NULL, tenant_id BIGINT NOT NULL, namespace VARCHAR(100),
    metamodel_id BIGINT NOT NULL, parent_object_id BIGINT,
    api_key VARCHAR(255), label VARCHAR(255), label_key VARCHAR(255),
    custom_flg SMALLINT, metadata_order SMALLINT, owner_id BIGINT,
    description VARCHAR(500), delete_flg SMALLINT DEFAULT 0,
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

-- D2. p_meta_tenant_metadata（租户级元数据，结构同大宽表，DynamicTableNameHolder 切换）
CREATE TABLE p_meta_tenant_metadata LIKE p_meta_metamodel_data;
CREATE INDEX idx_tmd_tenant_mm ON p_meta_tenant_metadata(tenant_id, metamodel_id);

-- D3. p_tenant_meta_log（变更日志）
-- Entity: MetaLog extends BaseEntity → @TableName("p_tenant_meta_log")
CREATE TABLE p_tenant_meta_log (
    id BIGINT NOT NULL, tenant_id BIGINT NOT NULL,
    metadata_api_key VARCHAR(255), metamodel_api_key VARCHAR(255),
    trace_id VARCHAR(255), old_value TEXT, new_value TEXT,
    op_type SMALLINT, from_type SMALLINT, sync SMALLINT,
    delete_flg SMALLINT DEFAULT 0,
    created_by BIGINT, created_at BIGINT, updated_by BIGINT, updated_at BIGINT,
    PRIMARY KEY (id)
);
CREATE INDEX idx_meta_log_tenant ON p_tenant_meta_log(tenant_id, created_at);

-- D4. p_meta_i18n_resource（多语言资源）
-- Entity: MetaI18nResource extends BaseEntity → @TableName("p_meta_i18n_resource")
CREATE TABLE p_meta_i18n_resource (
    id BIGINT NOT NULL, tenant_id BIGINT NOT NULL,
    metamodel_api_key BIGINT, metadata_api_key BIGINT, entity_api_key BIGINT,
    resource_key VARCHAR(256) NOT NULL, lang_code VARCHAR(8) NOT NULL,
    resource_value TEXT, description VARCHAR(256),
    delete_flg SMALLINT DEFAULT 0,
    created_at BIGINT, created_by BIGINT, updated_at BIGINT, updated_by BIGINT,
    PRIMARY KEY (id)
);
CREATE INDEX idx_i18n_key ON p_meta_i18n_resource(tenant_id, resource_key, lang_code);

-- D5. p_meta_migration_process（迁移任务）
CREATE TABLE p_meta_migration_process (
    id BIGINT NOT NULL, tenant_id BIGINT NOT NULL,
    package_name VARCHAR(255), migration_stage SMALLINT NOT NULL,
    start_time BIGINT, end_time BIGINT, status SMALLINT NOT NULL,
    process_content TEXT, delete_flg SMALLINT DEFAULT 0,
    created_at BIGINT, created_by BIGINT, updated_at BIGINT, updated_by BIGINT,
    PRIMARY KEY (id)
);

-- D6. p_meta_migration_process_unit（迁移任务单元）
CREATE TABLE p_meta_migration_process_unit (
    id BIGINT NOT NULL, tenant_id BIGINT NOT NULL,
    pid BIGINT, process_id BIGINT NOT NULL, migration_stage SMALLINT NOT NULL,
    unit_name VARCHAR(100), rollback_unit_flg SMALLINT,
    start_time BIGINT, end_time BIGINT, time_consuming BIGINT,
    exec_exception TEXT, rollback_exception TEXT, status SMALLINT NOT NULL,
    delete_flg SMALLINT DEFAULT 0,
    created_at BIGINT, created_by BIGINT, updated_at BIGINT, updated_by BIGINT,
    PRIMARY KEY (id)
);
CREATE INDEX idx_mpu_process ON p_meta_migration_process_unit(process_id);

-- D7. p_meta_module（模块定义）
CREATE TABLE p_meta_module (
    id BIGINT NOT NULL, tenant_id BIGINT NOT NULL,
    name VARCHAR(255), api_key VARCHAR(255), label VARCHAR(255),
    version VARCHAR(50), status SMALLINT, description VARCHAR(500),
    delete_flg SMALLINT DEFAULT 0,
    created_at BIGINT, created_by BIGINT, updated_at BIGINT, updated_by BIGINT,
    PRIMARY KEY (id)
);

-- D8. p_meta_module_metadata（模块元数据清单）
CREATE TABLE p_meta_module_metadata (
    id BIGINT NOT NULL, tenant_id BIGINT NOT NULL,
    module_id BIGINT, metadata_id BIGINT, metamodel_id BIGINT, api_key VARCHAR(255),
    delete_flg SMALLINT DEFAULT 0,
    created_at BIGINT, created_by BIGINT, updated_at BIGINT, updated_by BIGINT,
    PRIMARY KEY (id)
);

-- D9. p_tenant_global_pickitem（全局选项集）
-- Entity: GlobalPickItem → @TableName("p_tenant_global_pickitem")
CREATE TABLE p_tenant_global_pickitem (
    id BIGINT NOT NULL, tenant_id BIGINT NOT NULL,
    api_key VARCHAR(100) NOT NULL,
    name VARCHAR(100), label VARCHAR(100) NOT NULL, label_key VARCHAR(100),
    custom_flg SMALLINT NOT NULL,
    description VARCHAR(500), description_key VARCHAR(255),
    created_by BIGINT, created_at BIGINT, updated_by BIGINT, updated_at BIGINT,
    PRIMARY KEY (tenant_id, api_key)
);

-- ************************************************************
-- E. 种子数据
-- ************************************************************

-- E1. 元模型定义
INSERT INTO p_meta_model (api_key, namespace, label, label_key, metamodel_type, enable_package, enable_app, enable_delta, enable_log, enable_module_control, enable_common, enable_tenant, db_table, description, description_key, delete_flg, visible, created_by, created_at, updated_by, updated_at) VALUES
('entity',          'system', '自定义对象',   'meta.model.entity',      1, 1, 1, 0, 1, 0, 1, 1, 'p_tenant_entity',       '自定义对象元模型',     'meta.model.entity.desc',       0, 1, 1, 1711929600000, 1, 1711929600000),
('item',            'system', '自定义字段',   'meta.model.item',        2, 1, 0, 0, 1, 0, 1, 1, 'p_tenant_item',         '自定义字段元模型',     'meta.model.item.desc',         0, 1, 1, 1711929600000, 1, 1711929600000),
('entityLink',      'system', '关联关系',     'meta.model.link',        3, 1, 0, 0, 1, 0, 1, 1, 'p_tenant_entity_link',  '对象关联关系元模型',   'meta.model.link.desc',         0, 1, 1, 1711929600000, 1, 1711929600000),
('checkRule',       'system', '校验规则',     'meta.model.check_rule',  4, 1, 0, 0, 1, 0, 1, 1, 'p_tenant_check_rule',   '校验规则元模型',       'meta.model.check_rule.desc',   0, 1, 1, 1711929600000, 1, 1711929600000),
('pickOption',      'system', '选项值',       'meta.model.pick_option', 5, 0, 0, 0, 0, 0, 1, 1, 'p_tenant_pick_option',  '字段选项值元模型',     'meta.model.pick_option.desc',  0, 0, 1, 1711929600000, 1, 1711929600000),
('referenceFilter', 'system', '关联过滤条件', 'meta.model.refer_filter',10,0, 0, 0, 0, 0, 1, 1, 'p_tenant_refer_filter', '关联过滤条件元模型',   'meta.model.refer_filter.desc', 0, 0, 1, 1711929600000, 1, 1711929600000),
('MetaLink',         'system', '元模型关联',   'meta.model.meta_link',   6, 0, 0, 0, 0, 0, 0, 0, 'p_meta_link',           '元模型间关联关系',     'meta.model.meta_link.desc',    0, 0, 1, 1711929600000, 1, 1711929600000),
('MetaOption',       'system', '元模型选项',   'meta.model.meta_option', 7, 0, 0, 0, 0, 0, 0, 0, 'p_meta_option',         '元模型字段选项值',     'meta.model.meta_option.desc',  0, 0, 1, 1711929600000, 1, 1711929600000),
('I18nResource',     'system', '多语言资源',   'meta.model.i18n',        8, 0, 0, 0, 0, 0, 0, 0, 'p_meta_i18n_resource',  '多语言资源元模型',     'meta.model.i18n.desc',         0, 0, 1, 1711929600000, 1, 1711929600000),
('GlobalPickItem',   'system', '全局选项集',   'meta.model.global_pick', 9, 0, 0, 0, 0, 0, 0, 0, 'p_tenant_global_pickitem', '全局选项集元模型',     'meta.model.global_pick.desc',  0, 1, 1, 1711929600000, 1, 1711929600000);


-- E2. 元模型字段项（完整映射，与 Entity Java 类对齐）
-- 固定列由 Converter 自动映射（apiKey/label/labelKey/namespace/description/entityApiKey），无需定义
-- db_column: dbc_varchar(短文本) dbc_textarea(长文本) dbc_bigint(Long) dbc_int(枚举) dbc_smallint(布尔) dbc_decimal(小数)

INSERT INTO p_meta_item (metamodel_api_key, api_key, label, label_key, item_type, data_type, item_order, require_flg, creatable, updatable, enable_package, db_column, description, description_key, delete_flg, created_by, created_at, updated_by, updated_at) VALUES
-- Integer 枚举/选择类 → dbc_int_*
('entity', 'entity_type',              '对象类型',       'meta.entity.entity_type',              6, 3, 1,  0, 1, 0, 1, 'dbc_int_1',       '对象业务类型',           'meta.entity.entity_type.desc',              0, 1, 1711929600000, 1, 1711929600000),
('entity', 'custom_entity_seq',        '对象排序号',     'meta.entity.custom_entity_seq',        8, 3, 2,  0, 1, 1, 0, 'dbc_int_2',       '对象显示排序号',         'meta.entity.custom_entity_seq.desc',        0, 1, 1711929600000, 1, 1711929600000),
('entity', 'business_category',        '业务分类',       'meta.entity.business_category',        6, 3, 3,  0, 1, 1, 0, 'dbc_int_3',       '对象业务分类',           'meta.entity.business_category.desc',        0, 1, 1711929600000, 1, 1711929600000),
-- String 短文本类 → dbc_varchar_*
('entity', 'svg_api_key',              'SVG图标',        'meta.entity.svg_api_key',              1, 1, 4,  0, 1, 1, 0, 'dbc_varchar_1',   '对象图标API标识',        'meta.entity.svg_api_key.desc',              0, 1, 1711929600000, 1, 1711929600000),
('entity', 'svg_color',                'SVG颜色',        'meta.entity.svg_color',                1, 1, 5,  0, 1, 1, 0, 'dbc_varchar_2',   '对象图标颜色',           'meta.entity.svg_color.desc',                0, 1, 1711929600000, 1, 1711929600000),
('entity', 'description_key',          '描述多语言Key',  'meta.entity.description_key',          1, 1, 6,  0, 1, 1, 0, 'dbc_varchar_3',   '描述的多语言Key',        'meta.entity.description_key.desc',          0, 1, 1711929600000, 1, 1711929600000),
('entity', 'type_property',            '类型属性',       'meta.entity.type_property',            1, 1, 7,  0, 1, 1, 0, 'dbc_varchar_4',   '对象类型扩展属性',       'meta.entity.type_property.desc',            0, 1, 1711929600000, 1, 1711929600000),
('entity', 'db_table',                 '数据库表名',     'meta.entity.db_table',                 1, 1, 8,  1, 1, 0, 0, 'dbc_varchar_5',   '对象对应物理表名',       'meta.entity.db_table.desc',                 0, 1, 1711929600000, 1, 1711929600000),
-- String 长文本类 → dbc_textarea_*
('entity', 'extend_property',          '扩展属性JSON',   'meta.entity.extend_property',          2, 1, 9,  0, 1, 1, 0, 'dbc_textarea_1',  '对象扩展属性JSON',       'meta.entity.extend_property.desc',          0, 1, 1711929600000, 1, 1711929600000),
-- Integer 布尔/开关类 → dbc_smallint_*
('entity', 'enable_flg',               '启用标记',       'meta.entity.enable_flg',               6, 3, 10, 0, 1, 1, 0, 'dbc_smallint_1',  '是否启用',               'meta.entity.enable_flg.desc',               0, 1, 1711929600000, 1, 1711929600000),
('entity', 'custom_flg',               '自定义标记',     'meta.entity.custom_flg',               6, 3, 11, 0, 1, 0, 0, 'dbc_smallint_2',  '0=标准 1=自定义',        'meta.entity.custom_flg.desc',               0, 1, 1711929600000, 1, 1711929600000),
('entity', 'detail_flg',               '明细标记',       'meta.entity.detail_flg',               6, 3, 12, 0, 1, 1, 0, 'dbc_smallint_3',  '是否明细对象',           'meta.entity.detail_flg.desc',               0, 1, 1711929600000, 1, 1711929600000),
('entity', 'enable_team',              '启用团队',       'meta.entity.enable_team',              6, 3, 13, 0, 1, 1, 0, 'dbc_smallint_4',  '是否启用团队协作',       'meta.entity.enable_team.desc',              0, 1, 1711929600000, 1, 1711929600000),
('entity', 'enable_social',            '启用社交',       'meta.entity.enable_social',            6, 3, 14, 0, 1, 1, 0, 'dbc_smallint_5',  '是否启用社交功能',       'meta.entity.enable_social.desc',            0, 1, 1711929600000, 1, 1711929600000),
('entity', 'hidden_flg',               '隐藏标记',       'meta.entity.hidden_flg',               6, 3, 15, 0, 1, 1, 0, 'dbc_smallint_6',  '是否隐藏',               'meta.entity.hidden_flg.desc',               0, 1, 1711929600000, 1, 1711929600000),
('entity', 'searchable',               '可搜索',         'meta.entity.searchable',               6, 3, 16, 0, 1, 1, 0, 'dbc_smallint_7',  '是否可搜索',             'meta.entity.searchable.desc',               0, 1, 1711929600000, 1, 1711929600000),
('entity', 'enable_sharing',           '启用共享',       'meta.entity.enable_sharing',           6, 3, 17, 0, 1, 1, 0, 'dbc_smallint_8',  '是否启用共享',           'meta.entity.enable_sharing.desc',           0, 1, 1711929600000, 1, 1711929600000),
('entity', 'enable_script_trigger',    '启用脚本触发器', 'meta.entity.enable_script_trigger',    6, 3, 18, 0, 1, 1, 0, 'dbc_smallint_9',  '是否启用脚本触发器',     'meta.entity.enable_script_trigger.desc',    0, 1, 1711929600000, 1, 1711929600000),
('entity', 'enable_activity',          '启用活动',       'meta.entity.enable_activity',          6, 3, 19, 0, 1, 1, 0, 'dbc_smallint_10', '是否启用活动',           'meta.entity.enable_activity.desc',          0, 1, 1711929600000, 1, 1711929600000),
('entity', 'enable_history_log',       '启用历史日志',   'meta.entity.enable_history_log',       6, 3, 20, 0, 1, 1, 0, 'dbc_smallint_11', '是否启用历史日志',       'meta.entity.enable_history_log.desc',       0, 1, 1711929600000, 1, 1711929600000),
('entity', 'enable_report',            '启用报表',       'meta.entity.enable_report',            6, 3, 21, 0, 1, 1, 0, 'dbc_smallint_12', '是否启用报表',           'meta.entity.enable_report.desc',            0, 1, 1711929600000, 1, 1711929600000),
('entity', 'enable_refer',             '启用关联',       'meta.entity.enable_refer',             6, 3, 22, 0, 1, 1, 0, 'dbc_smallint_13', '是否启用关联引用',       'meta.entity.enable_refer.desc',             0, 1, 1711929600000, 1, 1711929600000),
('entity', 'enable_api',               '启用API',        'meta.entity.enable_api',               6, 3, 23, 0, 1, 1, 0, 'dbc_smallint_14', '是否启用API',            'meta.entity.enable_api.desc',               0, 1, 1711929600000, 1, 1711929600000),
('entity', 'enable_dynamic_feed',      '启用动态',       'meta.entity.enable_dynamic_feed',      6, 3, 24, 0, 1, 1, 0, 'dbc_smallint_15', '是否启用动态Feed',       'meta.entity.enable_dynamic_feed.desc',      0, 1, 1711929600000, 1, 1711929600000),
-- Long 位掩码/大整数类 → dbc_bigint_*
('entity', 'enable_config',            '启用配置位',     'meta.entity.enable_config',            8, 2, 25, 0, 1, 1, 0, 'dbc_bigint_1',    '功能配置位掩码',         'meta.entity.enable_config.desc',            0, 1, 1711929600000, 1, 1711929600000),
('entity', 'enable_flow',              '启用流程位',     'meta.entity.enable_flow',              8, 2, 26, 0, 1, 1, 0, 'dbc_bigint_2',    '流程配置位掩码',         'meta.entity.enable_flow.desc',              0, 1, 1711929600000, 1, 1711929600000),
('entity', 'enable_package',           '启用包位',       'meta.entity.enable_package',           8, 2, 27, 0, 1, 1, 0, 'dbc_bigint_3',    '包配置位掩码',           'meta.entity.enable_package.desc',           0, 1, 1711929600000, 1, 1711929600000),
-- 剩余 Integer 布尔开关（dbc_int_* 继续）
('entity', 'enable_group_member',      '启用组成员',     'meta.entity.enable_group_member',      6, 3, 28, 0, 1, 1, 0, 'dbc_int_4',       '是否启用组成员',         'meta.entity.enable_group_member.desc',      0, 1, 1711929600000, 1, 1711929600000),
('entity', 'is_archived',              '已归档',         'meta.entity.is_archived',              6, 3, 29, 0, 1, 1, 0, 'dbc_int_5',       '是否已归档',             'meta.entity.is_archived.desc',              0, 1, 1711929600000, 1, 1711929600000),
('entity', 'enable_script_executor',   '启用脚本执行器', 'meta.entity.enable_script_executor',   6, 3, 30, 0, 1, 1, 0, 'dbc_int_6',       '是否启用脚本执行器',     'meta.entity.enable_script_executor.desc',   0, 1, 1711929600000, 1, 1711929600000),
('entity', 'enable_duplicaterule',     '启用查重规则',   'meta.entity.enable_duplicaterule',     6, 3, 31, 0, 1, 1, 0, 'dbc_int_7',       '是否启用查重规则',       'meta.entity.enable_duplicaterule.desc',     0, 1, 1711929600000, 1, 1711929600000),
('entity', 'enable_checkrule',         '启用校验规则',   'meta.entity.enable_checkrule',         6, 3, 32, 0, 1, 1, 0, 'dbc_int_8',       '是否启用校验规则',       'meta.entity.enable_checkrule.desc',         0, 1, 1711929600000, 1, 1711929600000),
('entity', 'enable_busitype',          '启用业务类型',   'meta.entity.enable_busitype',          6, 3, 33, 0, 1, 1, 0, 'dbc_int_9',       '是否启用业务类型',       'meta.entity.enable_busitype.desc',          0, 1, 1711929600000, 1, 1711929600000);

INSERT INTO p_meta_item (metamodel_api_key, api_key, label, label_key, item_type, data_type, item_order, require_flg, creatable, updatable, enable_package, db_column, description, description_key, delete_flg, created_by, created_at, updated_by, updated_at) VALUES
-- Integer 枚举/选择类 → dbc_int_*
('item', 'item_type',                    '字段UI类型',       'meta.item.item_type',                    6, 3, 1,  1, 1, 0, 1, 'dbc_int_1',       '字段的UI展示类型',         'meta.item.item_type.desc',                    0, 1, 1711929600000, 1, 1711929600000),
('item', 'data_type',                    '数据类型',         'meta.item.data_type',                    6, 3, 2,  1, 1, 0, 1, 'dbc_int_2',       '字段的数据存储类型',       'meta.item.data_type.desc',                    0, 1, 1711929600000, 1, 1711929600000),
('item', 'custom_item_seq',              '字段排序号',       'meta.item.custom_item_seq',              8, 3, 3,  0, 1, 1, 0, 'dbc_int_3',       '字段显示排序号',           'meta.item.custom_item_seq.desc',              0, 1, 1711929600000, 1, 1711929600000),
('item', 'item_order',                   '字段序号',         'meta.item.item_order',                   8, 3, 4,  0, 1, 1, 0, 'dbc_int_4',       '字段排列序号',             'meta.item.item_order.desc',                   0, 1, 1711929600000, 1, 1711929600000),
('item', 'readonly_status',              '只读状态',         'meta.item.readonly_status',              6, 3, 5,  0, 1, 1, 0, 'dbc_int_5',       '只读状态值',               'meta.item.readonly_status.desc',              0, 1, 1711929600000, 1, 1711929600000),
('item', 'visible_status',               '可见状态',         'meta.item.visible_status',               6, 3, 6,  0, 1, 1, 0, 'dbc_int_6',       '可见状态值',               'meta.item.visible_status.desc',               0, 1, 1711929600000, 1, 1711929600000),
('item', 'index_order',                  '索引顺序',         'meta.item.index_order',                  8, 3, 7,  0, 1, 1, 0, 'dbc_int_7',       '索引排列顺序',             'meta.item.index_order.desc',                  0, 1, 1711929600000, 1, 1711929600000),
('item', 'index_type',                   '索引类型',         'meta.item.index_type',                   6, 3, 8,  0, 1, 1, 0, 'dbc_int_8',       '索引类型',                 'meta.item.index_type.desc',                   0, 1, 1711929600000, 1, 1711929600000),
('item', 'mask_prefix',                  '掩码前缀位数',     'meta.item.mask_prefix',                  8, 3, 9,  0, 1, 1, 0, 'dbc_int_9',       '掩码前缀位数',             'meta.item.mask_prefix.desc',                  0, 1, 1711929600000, 1, 1711929600000),
('item', 'mask_suffix',                  '掩码后缀位数',     'meta.item.mask_suffix',                  8, 3, 10, 0, 1, 1, 0, 'dbc_int_10',      '掩码后缀位数',             'meta.item.mask_suffix.desc',                  0, 1, 1711929600000, 1, 1711929600000),
('item', 'mask_symbol_type',             '掩码符号类型',     'meta.item.mask_symbol_type',             6, 3, 11, 0, 1, 1, 0, 'dbc_int_11',      '掩码符号类型',             'meta.item.mask_symbol_type.desc',             0, 1, 1711929600000, 1, 1711929600000),
('item', 'increment_strategy',           '自动编号递增策略', 'meta.item.increment_strategy',           6, 3, 12, 0, 1, 1, 0, 'dbc_int_12',      '自动编号递增策略',         'meta.item.increment_strategy.desc',           0, 1, 1711929600000, 1, 1711929600000),
-- String 短文本类 → dbc_varchar_*
('item', 'type_property',                '类型属性',         'meta.item.type_property',                1, 1, 13, 0, 1, 1, 0, 'dbc_varchar_1',   '字段类型扩展属性',         'meta.item.type_property.desc',                0, 1, 1711929600000, 1, 1711929600000),
('item', 'help_text',                    '帮助文本',         'meta.item.help_text',                    1, 1, 14, 0, 1, 1, 0, 'dbc_varchar_2',   '字段帮助提示文本',         'meta.item.help_text.desc',                    0, 1, 1711929600000, 1, 1711929600000),
('item', 'help_text_key',                '帮助文本Key',      'meta.item.help_text_key',                1, 1, 15, 0, 1, 1, 0, 'dbc_varchar_3',   '帮助文本多语言Key',        'meta.item.help_text_key.desc',                0, 1, 1711929600000, 1, 1711929600000),
('item', 'description_key',              '描述Key',          'meta.item.description_key',              1, 1, 16, 0, 1, 1, 0, 'dbc_varchar_4',   '描述多语言Key',            'meta.item.description_key.desc',              0, 1, 1711929600000, 1, 1711929600000),
('item', 'default_value',                '默认值',           'meta.item.default_value',                1, 1, 17, 0, 1, 1, 0, 'dbc_varchar_5',   '字段默认值',               'meta.item.default_value.desc',                0, 1, 1711929600000, 1, 1711929600000),
('item', 'refer_entity_api_key',         '关联对象',         'meta.item.refer_entity_api_key',         1, 1, 18, 0, 1, 1, 0, 'dbc_varchar_6',   '关联对象apiKey',           'meta.item.refer_entity_api_key.desc',         0, 1, 1711929600000, 1, 1711929600000),
('item', 'refer_link_api_key',           '关联关系',         'meta.item.refer_link_api_key',           1, 1, 19, 0, 1, 1, 0, 'dbc_varchar_7',   '关联关系apiKey',           'meta.item.refer_link_api_key.desc',           0, 1, 1711929600000, 1, 1711929600000),
('item', 'db_column',                    '数据库列名',       'meta.item.db_column',                    1, 1, 20, 0, 1, 0, 0, 'dbc_varchar_8',   '字段对应数据库列名',       'meta.item.db_column.desc',                    0, 1, 1711929600000, 1, 1711929600000),
('item', 'column_name',                  '列显示名',         'meta.item.column_name',                  1, 1, 21, 0, 1, 1, 0, 'dbc_varchar_9',   '列显示名称',               'meta.item.column_name.desc',                  0, 1, 1711929600000, 1, 1711929600000),
('item', 'is_compute_multi_currency_unit','汇总币种单位',    'meta.item.is_compute_multi_currency_unit',1,1, 22, 0, 1, 1, 0, 'dbc_varchar_10',  '汇总字段币种单位',         'meta.item.is_compute_multi_currency_unit.desc',0,1, 1711929600000, 1, 1711929600000),
('item', 'format',                       '自动编号格式',     'meta.item.format',                       1, 1, 23, 0, 1, 1, 0, 'dbc_varchar_11',  '自动编号格式',             'meta.item.format.desc',                       0, 1, 1711929600000, 1, 1711929600000),
-- Integer 布尔/开关类 → dbc_smallint_*
('item', 'require_flg',                  '是否必填',         'meta.item.require_flg',                  6, 3, 24, 0, 1, 1, 0, 'dbc_smallint_1',  '字段是否必填',             'meta.item.require_flg.desc',                  0, 1, 1711929600000, 1, 1711929600000),
('item', 'custom_flg',                   '自定义标记',       'meta.item.custom_flg',                   6, 3, 25, 0, 1, 0, 0, 'dbc_smallint_2',  '0=标准 1=自定义',          'meta.item.custom_flg.desc',                   0, 1, 1711929600000, 1, 1711929600000),
('item', 'enable_flg',                   '启用标记',         'meta.item.enable_flg',                   6, 3, 26, 0, 1, 1, 0, 'dbc_smallint_3',  '是否启用',                 'meta.item.enable_flg.desc',                   0, 1, 1711929600000, 1, 1711929600000),
('item', 'creatable',                    '可创建',           'meta.item.creatable',                    6, 3, 27, 0, 1, 1, 0, 'dbc_smallint_4',  '创建时是否可填',           'meta.item.creatable.desc',                    0, 1, 1711929600000, 1, 1711929600000),
('item', 'updatable',                    '可更新',           'meta.item.updatable',                    6, 3, 28, 0, 1, 1, 0, 'dbc_smallint_5',  '更新时是否可改',           'meta.item.updatable.desc',                    0, 1, 1711929600000, 1, 1711929600000),
('item', 'unique_key_flg',               '唯一键标记',       'meta.item.unique_key_flg',               6, 3, 29, 0, 1, 1, 0, 'dbc_smallint_6',  '是否唯一键',               'meta.item.unique_key_flg.desc',               0, 1, 1711929600000, 1, 1711929600000),
('item', 'enable_history_log',           '启用历史日志',     'meta.item.enable_history_log',           6, 3, 30, 0, 1, 1, 0, 'dbc_smallint_7',  '是否启用历史日志',         'meta.item.enable_history_log.desc',           0, 1, 1711929600000, 1, 1711929600000),
('item', 'hidden_flg',                   '隐藏标记',         'meta.item.hidden_flg',                   6, 3, 31, 0, 1, 1, 0, 'dbc_smallint_8',  '是否隐藏',                 'meta.item.hidden_flg.desc',                   0, 1, 1711929600000, 1, 1711929600000),
('item', 'sort_flg',                     '排序标记',         'meta.item.sort_flg',                     6, 3, 32, 0, 1, 1, 0, 'dbc_smallint_9',  '是否可排序',               'meta.item.sort_flg.desc',                     0, 1, 1711929600000, 1, 1711929600000),
('item', 'enable_deactive',              '启用停用',         'meta.item.enable_deactive',              6, 3, 33, 0, 1, 1, 0, 'dbc_smallint_10', '是否可停用',               'meta.item.enable_deactive.desc',              0, 1, 1711929600000, 1, 1711929600000),
('item', 'compound',                     '复合字段',         'meta.item.compound',                     6, 3, 34, 0, 1, 1, 0, 'dbc_smallint_11', '是否复合字段',             'meta.item.compound.desc',                     0, 1, 1711929600000, 1, 1711929600000),
('item', 'encrypt',                      '加密存储',         'meta.item.encrypt',                      6, 3, 35, 0, 1, 1, 0, 'dbc_smallint_12', '是否加密存储',             'meta.item.encrypt.desc',                      0, 1, 1711929600000, 1, 1711929600000),
('item', 'markdown',                     'Markdown编辑器',   'meta.item.markdown',                     6, 3, 36, 0, 1, 1, 0, 'dbc_smallint_13', '是否Markdown编辑器',       'meta.item.markdown.desc',                     0, 1, 1711929600000, 1, 1711929600000),
('item', 'refer_item_filter_enable',     '关联字段过滤',     'meta.item.refer_item_filter_enable',     6, 3, 37, 0, 1, 1, 0, 'dbc_smallint_14', '关联字段过滤开关',         'meta.item.refer_item_filter_enable.desc',     0, 1, 1711929600000, 1, 1711929600000),
-- Long 位掩码类 → dbc_bigint_*
('item', 'enable_config',                '启用配置位',       'meta.item.enable_config',                8, 2, 38, 0, 1, 1, 0, 'dbc_bigint_1',    '功能配置位掩码',           'meta.item.enable_config.desc',                0, 1, 1711929600000, 1, 1711929600000),
('item', 'enable_package',               '启用包位',         'meta.item.enable_package',               8, 2, 39, 0, 1, 1, 0, 'dbc_bigint_2',    '包配置位掩码',             'meta.item.enable_package.desc',               0, 1, 1711929600000, 1, 1711929600000);

INSERT INTO p_meta_item (metamodel_api_key, api_key, label, label_key, item_type, data_type, item_order, require_flg, creatable, updatable, enable_package, db_column, description, description_key, delete_flg, created_by, created_at, updated_by, updated_at) VALUES
('pickOption', 'item_api_key',       '所属字段',       'meta.pick.item_api_key',       1, 1, 1, 1, 1, 0, 0, 'dbc_varchar_1',   '所属字段apiKey',         'meta.pick.item_api_key.desc',       0, 1, 1711929600000, 1, 1711929600000),
('pickOption', 'description_key',    '描述Key',        'meta.pick.description_key',    1, 1, 2, 0, 1, 1, 0, 'dbc_varchar_2',   '描述多语言Key',          'meta.pick.description_key.desc',    0, 1, 1711929600000, 1, 1711929600000),
('pickOption', 'option_order',       '排序号',         'meta.pick.option_order',       8, 3, 3, 0, 1, 1, 0, 'dbc_int_1',       '选项排序号',             'meta.pick.option_order.desc',       0, 1, 1711929600000, 1, 1711929600000),
('pickOption', 'default_flg',        '默认选中',       'meta.pick.default_flg',        6, 3, 4, 0, 1, 1, 0, 'dbc_smallint_1',  '是否默认选中',           'meta.pick.default_flg.desc',        0, 1, 1711929600000, 1, 1711929600000),
('pickOption', 'global_flg',         '全局标记',       'meta.pick.global_flg',         6, 3, 5, 0, 1, 1, 0, 'dbc_smallint_2',  '是否全局选项',           'meta.pick.global_flg.desc',         0, 1, 1711929600000, 1, 1711929600000),
('pickOption', 'custom_flg',         '自定义标记',     'meta.pick.custom_flg',         6, 3, 6, 0, 1, 0, 0, 'dbc_smallint_3',  '0=标准 1=自定义',        'meta.pick.custom_flg.desc',         0, 1, 1711929600000, 1, 1711929600000),
('pickOption', 'enable_flg',         '启用标记',       'meta.pick.enable_flg',         6, 3, 7, 0, 1, 1, 0, 'dbc_smallint_4',  '是否启用',               'meta.pick.enable_flg.desc',         0, 1, 1711929600000, 1, 1711929600000);

INSERT INTO p_meta_item (metamodel_api_key, api_key, label, label_key, item_type, data_type, item_order, require_flg, creatable, updatable, enable_package, db_column, description, description_key, delete_flg, created_by, created_at, updated_by, updated_at) VALUES
('entityLink', 'type_property',          '类型属性',       'meta.link.type_property',          1, 1, 1, 0, 1, 1, 0, 'dbc_varchar_1',   '关联类型扩展属性',       'meta.link.type_property.desc',          0, 1, 1711929600000, 1, 1711929600000),
('entityLink', 'parent_entity_api_key',  '父对象',         'meta.link.parent_entity_api_key',  1, 1, 2, 1, 1, 0, 0, 'dbc_varchar_2',   '父对象apiKey',           'meta.link.parent_entity_api_key.desc',  0, 1, 1711929600000, 1, 1711929600000),
('entityLink', 'child_entity_api_key',   '子对象',         'meta.link.child_entity_api_key',   1, 1, 3, 1, 1, 0, 0, 'dbc_varchar_3',   '子对象apiKey',           'meta.link.child_entity_api_key.desc',   0, 1, 1711929600000, 1, 1711929600000),
('entityLink', 'description_key',        '描述Key',        'meta.link.description_key',        1, 1, 4, 0, 1, 1, 0, 'dbc_varchar_4',   '描述多语言Key',          'meta.link.description_key.desc',        0, 1, 1711929600000, 1, 1711929600000),
('entityLink', 'link_type',              '关联类型',       'meta.link.link_type',              6, 3, 5, 1, 1, 0, 0, 'dbc_int_1',       '关联类型(1=LOOKUP等)',   'meta.link.link_type.desc',              0, 1, 1711929600000, 1, 1711929600000),
('entityLink', 'detail_link',            '明细关联',       'meta.link.detail_link',            6, 3, 6, 0, 1, 1, 0, 'dbc_smallint_1',  '是否明细关联',           'meta.link.detail_link.desc',            0, 1, 1711929600000, 1, 1711929600000),
('entityLink', 'cascade_delete',         '级联删除',       'meta.link.cascade_delete',         6, 3, 7, 0, 1, 1, 0, 'dbc_smallint_2',  '是否级联删除',           'meta.link.cascade_delete.desc',         0, 1, 1711929600000, 1, 1711929600000),
('entityLink', 'access_control',         '访问控制',       'meta.link.access_control',         6, 3, 8, 0, 1, 1, 0, 'dbc_smallint_3',  '是否启用访问控制',       'meta.link.access_control.desc',         0, 1, 1711929600000, 1, 1711929600000),
('entityLink', 'enable_flg',             '启用标记',       'meta.link.enable_flg',             6, 3, 9, 0, 1, 1, 0, 'dbc_smallint_4',  '是否启用',               'meta.link.enable_flg.desc',             0, 1, 1711929600000, 1, 1711929600000);

INSERT INTO p_meta_item (metamodel_api_key, api_key, label, label_key, item_type, data_type, item_order, require_flg, creatable, updatable, enable_package, db_column, description, description_key, delete_flg, created_by, created_at, updated_by, updated_at) VALUES
('checkRule', 'description_key',        '描述Key',        'meta.rule.description_key',        1, 1, 1, 0, 1, 1, 0, 'dbc_varchar_1',   '描述多语言Key',          'meta.rule.description_key.desc',        0, 1, 1711929600000, 1, 1711929600000),
('checkRule', 'check_error_msg',        '错误消息',       'meta.rule.check_error_msg',        1, 1, 2, 0, 1, 1, 0, 'dbc_varchar_2',   '校验失败错误消息',       'meta.rule.check_error_msg.desc',        0, 1, 1711929600000, 1, 1711929600000),
('checkRule', 'check_error_msg_key',    '错误消息Key',    'meta.rule.check_error_msg_key',    1, 1, 3, 0, 1, 1, 0, 'dbc_varchar_3',   '错误消息多语言Key',      'meta.rule.check_error_msg_key.desc',    0, 1, 1711929600000, 1, 1711929600000),
('checkRule', 'check_error_item_api_key','错误字段',      'meta.rule.check_error_item_api_key',1,1, 4, 0, 1, 1, 0, 'dbc_varchar_4',   '校验失败关联字段',       'meta.rule.check_error_item_api_key.desc',0,1, 1711929600000, 1, 1711929600000),
('checkRule', 'check_formula',          '校验公式',       'meta.rule.check_formula',          2, 1, 5, 1, 1, 1, 0, 'dbc_textarea_1',  '校验规则公式表达式',     'meta.rule.check_formula.desc',          0, 1, 1711929600000, 1, 1711929600000),
('checkRule', 'active_flg',             '激活标记',       'meta.rule.active_flg',             6, 3, 6, 0, 1, 1, 0, 'dbc_smallint_1',  '是否激活',               'meta.rule.active_flg.desc',             0, 1, 1711929600000, 1, 1711929600000),
('checkRule', 'check_error_location',   '错误位置',       'meta.rule.check_error_location',   6, 3, 7, 0, 1, 1, 0, 'dbc_int_1',       '错误提示位置',           'meta.rule.check_error_location.desc',   0, 1, 1711929600000, 1, 1711929600000),
('checkRule', 'check_all_items_flg',    '全字段校验',     'meta.rule.check_all_items_flg',    6, 3, 8, 0, 1, 1, 0, 'dbc_smallint_2',  '是否校验所有字段',       'meta.rule.check_all_items_flg.desc',    0, 1, 1711929600000, 1, 1711929600000),
('checkRule', 'check_error_way',        '错误方式',       'meta.rule.check_error_way',        6, 3, 9, 0, 1, 1, 0, 'dbc_int_2',       '错误提示方式',           'meta.rule.check_error_way.desc',        0, 1, 1711929600000, 1, 1711929600000);

INSERT INTO p_meta_item (metamodel_api_key, api_key, label, label_key, item_type, data_type, item_order, require_flg, creatable, updatable, enable_package, db_column, description, description_key, delete_flg, created_by, created_at, updated_by, updated_at) VALUES
('referenceFilter', 'item_api_key',       '所属字段',       'meta.refer.item_api_key',       1, 1, 1, 1, 1, 0, 0, 'dbc_varchar_1',   '所属字段apiKey',         'meta.refer.item_api_key.desc',       0, 1, 1711929600000, 1, 1711929600000),
('referenceFilter', 'link_api_key',       '关联关系',       'meta.refer.link_api_key',       1, 1, 2, 0, 1, 1, 0, 'dbc_varchar_2',   '关联关系apiKey',         'meta.refer.link_api_key.desc',       0, 1, 1711929600000, 1, 1711929600000),
('referenceFilter', 'filter_field',       '过滤字段',       'meta.refer.filter_field',       1, 1, 3, 1, 1, 1, 0, 'dbc_varchar_3',   '过滤条件字段名',         'meta.refer.filter_field.desc',       0, 1, 1711929600000, 1, 1711929600000),
('referenceFilter', 'filter_operator',    '过滤操作符',     'meta.refer.filter_operator',    1, 1, 4, 1, 1, 1, 0, 'dbc_varchar_4',   '过滤操作符',             'meta.refer.filter_operator.desc',    0, 1, 1711929600000, 1, 1711929600000),
('referenceFilter', 'filter_value',       '过滤值',         'meta.refer.filter_value',       1, 1, 5, 0, 1, 1, 0, 'dbc_varchar_5',   '过滤条件值',             'meta.refer.filter_value.desc',       0, 1, 1711929600000, 1, 1711929600000),
('referenceFilter', 'description_key',    '描述Key',        'meta.refer.description_key',    1, 1, 6, 0, 1, 1, 0, 'dbc_varchar_6',   '描述多语言Key',          'meta.refer.description_key.desc',    0, 1, 1711929600000, 1, 1711929600000),
('referenceFilter', 'filter_order',       '过滤排序',       'meta.refer.filter_order',       8, 3, 7, 0, 1, 1, 0, 'dbc_int_1',       '过滤条件排序号',         'meta.refer.filter_order.desc',       0, 1, 1711929600000, 1, 1711929600000);

-- E3. 标准对象（Common 级）
INSERT INTO p_common_entity (api_key, namespace, label, label_key, entity_type, description, description_key, custom_entity_seq, delete_flg, enable_flg, custom_flg, business_category, db_table, enable_sharing, enable_history_log, enable_report, enable_api, created_at, created_by, updated_at, updated_by) VALUES
('Account__c',     'system', '客户',     'entity.account.label',     1, '客户管理对象',     'entity.account.desc',     1, 0, 1, 0, 1, 'x_account',     1, 1, 1, 1, 1711929600000, 1, 1711929600000, 1),
('Contact__c',     'system', '联系人',   'entity.contact.label',     1, '联系人管理对象',   'entity.contact.desc',     2, 0, 1, 0, 1, 'x_contact',     1, 1, 1, 1, 1711929600000, 1, 1711929600000, 1),
('Opportunity__c', 'system', '商机',     'entity.opportunity.label', 1, '商机管理对象',     'entity.opportunity.desc', 3, 0, 1, 0, 1, 'x_opportunity', 1, 1, 1, 1, 1711929600000, 1, 1711929600000, 1),
('Lead__c',        'system', '线索',     'entity.lead.label',        1, '线索管理对象',     'entity.lead.desc',        4, 0, 1, 0, 1, 'x_lead',        1, 1, 1, 1, 1711929600000, 1, 1711929600000, 1),
('Product__c',     'system', '产品',     'entity.product.label',     1, '产品管理对象',     'entity.product.desc',     5, 0, 1, 0, 1, 'x_product',     1, 1, 1, 1, 1711929600000, 1, 1711929600000, 1);

-- E4. 标准字段（Common 级）
-- item_type 使用新 ItemTypeEnum 编码：1=TEXT,2=NUMBER,3=DATE,4=PICKLIST,5=LOOKUP,6=FORMULA,7=ROLLUP,8=TEXTAREA,9=BOOLEAN,10=CURRENCY,11=PERCENT,12=EMAIL,13=PHONE,14=URL,15=DATETIME,16=MULTIPICKLIST,17=MASTER_DETAIL,18=GEOLOCATION,19=IMAGE,20=AUTONUMBER,21=JOIN,22=AUDIO
-- data_type：1=VARCHAR,2=INTEGER,3=BIGINT,4=DOUBLE,5=TEXT,6=SMALLINT
INSERT INTO p_common_item (entity_api_key, api_key, namespace, label, label_key, item_type, data_type, description, description_key, require_flg, delete_flg, custom_flg, enable_flg, creatable, updatable, item_order, refer_entity_api_key, db_column, created_at, created_by, updated_at, updated_by) VALUES
('Account__c', 'Name__c',     'system', '客户名称', 'item.account.name.label',     1,  1, '客户名称', 'item.account.name.desc',     1, 0, 0, 1, 1, 1, 1, NULL, 'dbc_varchar_1',  1711929600000, 1, 1711929600000, 1),
('Account__c', 'Phone__c',    'system', '电话',     'item.account.phone.label',    13, 1, '客户电话', 'item.account.phone.desc',    0, 0, 0, 1, 1, 1, 2, NULL, 'dbc_varchar_2',  1711929600000, 1, 1711929600000, 1),
('Account__c', 'Industry__c', 'system', '行业',     'item.account.industry.label', 4,  2, '客户行业', 'item.account.industry.desc', 0, 0, 0, 1, 1, 1, 3, NULL, 'dbc_int_1',      1711929600000, 1, 1711929600000, 1),
('Account__c', 'Status__c',   'system', '状态',     'item.account.status.label',   4,  2, '客户状态', 'item.account.status.desc',   1, 0, 0, 1, 1, 1, 4, NULL, 'dbc_int_2',      1711929600000, 1, 1711929600000, 1),
('Account__c', 'Website__c',  'system', '网站',     'item.account.website.label',  14, 1, '客户网站', 'item.account.website.desc',  0, 0, 0, 1, 1, 1, 5, NULL, 'dbc_varchar_3',  1711929600000, 1, 1711929600000, 1),
('Account__c', 'Email__c',    'system', '邮箱',     'item.account.email.label',    12, 1, '客户邮箱', 'item.account.email.desc',    0, 0, 0, 1, 1, 1, 6, NULL, 'dbc_varchar_4',  1711929600000, 1, 1711929600000, 1),
('Account__c', 'Address__c',  'system', '地址',     'item.account.address.label',  8,  5, '客户地址', 'item.account.address.desc',  0, 0, 0, 1, 1, 1, 7, NULL, 'dbc_textarea_1', 1711929600000, 1, 1711929600000, 1),
('Contact__c', 'Name__c',      'system', '姓名',     'item.contact.name.label',    1,  1, '联系人姓名',   'item.contact.name.desc',    1, 0, 0, 1, 1, 1, 1, NULL,          'dbc_varchar_1',  1711929600000, 1, 1711929600000, 1),
('Contact__c', 'Email__c',     'system', '邮箱',     'item.contact.email.label',   12, 1, '联系人邮箱',   'item.contact.email.desc',   0, 0, 0, 1, 1, 1, 2, NULL,          'dbc_varchar_2',  1711929600000, 1, 1711929600000, 1),
('Contact__c', 'Phone__c',     'system', '电话',     'item.contact.phone.label',   13, 1, '联系人电话',   'item.contact.phone.desc',   0, 0, 0, 1, 1, 1, 3, NULL,          'dbc_varchar_3',  1711929600000, 1, 1711929600000, 1),
('Contact__c', 'AccountId__c', 'system', '所属客户', 'item.contact.account.label', 5,  3, '所属客户关联', 'item.contact.account.desc', 0, 0, 0, 1, 1, 1, 4, 'Account__c',  'dbc_bigint_1',   1711929600000, 1, 1711929600000, 1),
('Contact__c', 'Title__c',     'system', '职位',     'item.contact.title.label',   1,  1, '联系人职位',   'item.contact.title.desc',   0, 0, 0, 1, 1, 1, 5, NULL,          'dbc_varchar_4',  1711929600000, 1, 1711929600000, 1),
('Opportunity__c', 'Name__c',      'system', '商机名称', 'item.opp.name.label',    1,  1, '商机名称',     'item.opp.name.desc',    1, 0, 0, 1, 1, 1, 1, NULL,         'dbc_varchar_1',  1711929600000, 1, 1711929600000, 1),
('Opportunity__c', 'Amount__c',    'system', '金额',     'item.opp.amount.label',  10, 4, '商机金额',     'item.opp.amount.desc',  0, 0, 0, 1, 1, 1, 2, NULL,         'dbc_decimal_1',  1711929600000, 1, 1711929600000, 1),
('Opportunity__c', 'Stage__c',     'system', '阶段',     'item.opp.stage.label',   4,  2, '商机阶段',     'item.opp.stage.desc',   1, 0, 0, 1, 1, 1, 3, NULL,         'dbc_int_1',      1711929600000, 1, 1711929600000, 1),
('Opportunity__c', 'CloseDate__c', 'system', '预计成交', 'item.opp.close.label',   3,  3, '预计成交日期', 'item.opp.close.desc',   1, 0, 0, 1, 1, 1, 4, NULL,         'dbc_bigint_1',   1711929600000, 1, 1711929600000, 1),
('Opportunity__c', 'AccountId__c', 'system', '关联客户', 'item.opp.account.label', 5,  3, '关联客户',     'item.opp.account.desc', 0, 0, 0, 1, 1, 1, 5, 'Account__c', 'dbc_bigint_2',   1711929600000, 1, 1711929600000, 1);

-- E5. 选项值（Common 级）
INSERT INTO p_common_pick_option (entity_api_key, item_api_key, api_key, namespace, label, label_key, option_order, default_flg, global_flg, custom_flg, enable_flg, description, description_key, delete_flg, created_at, created_by, updated_at, updated_by) VALUES
('Account__c', 'Industry__c', 'IT',            'system', 'IT/互联网', 'pick.industry.it',            1, 0, 0, 0, 1, 'IT和互联网行业', 'pick.industry.it.desc',            0, 1711929600000, 1, 1711929600000, 1),
('Account__c', 'Industry__c', 'FINANCE',       'system', '金融',     'pick.industry.finance',       2, 0, 0, 0, 1, '金融行业',       'pick.industry.finance.desc',       0, 1711929600000, 1, 1711929600000, 1),
('Account__c', 'Industry__c', 'MANUFACTURING', 'system', '制造业',   'pick.industry.manufacturing', 3, 0, 0, 0, 1, '制造业',         'pick.industry.manufacturing.desc', 0, 1711929600000, 1, 1711929600000, 1),
('Account__c', 'Industry__c', 'EDUCATION',     'system', '教育',     'pick.industry.education',     4, 0, 0, 0, 1, '教育行业',       'pick.industry.education.desc',     0, 1711929600000, 1, 1711929600000, 1),
('Account__c', 'Industry__c', 'HEALTHCARE',    'system', '医疗',     'pick.industry.healthcare',    5, 0, 0, 0, 1, '医疗行业',       'pick.industry.healthcare.desc',    0, 1711929600000, 1, 1711929600000, 1),
('Account__c', 'Status__c', 'ACTIVE',    'system', '活跃',   'pick.status.active',    1, 1, 0, 0, 1, '活跃客户',   'pick.status.active.desc',    0, 1711929600000, 1, 1711929600000, 1),
('Account__c', 'Status__c', 'INACTIVE',  'system', '不活跃', 'pick.status.inactive',  2, 0, 0, 0, 1, '不活跃客户', 'pick.status.inactive.desc',  0, 1711929600000, 1, 1711929600000, 1),
('Account__c', 'Status__c', 'POTENTIAL', 'system', '潜在',   'pick.status.potential', 3, 0, 0, 0, 1, '潜在客户',   'pick.status.potential.desc', 0, 1711929600000, 1, 1711929600000, 1),
('Opportunity__c', 'Stage__c', 'PROSPECTING',   'system', '初步接触', 'pick.stage.prospecting',   1, 1, 0, 0, 1, '初步接触阶段', 'pick.stage.prospecting.desc',   0, 1711929600000, 1, 1711929600000, 1),
('Opportunity__c', 'Stage__c', 'QUALIFICATION', 'system', '需求确认', 'pick.stage.qualification', 2, 0, 0, 0, 1, '需求确认阶段', 'pick.stage.qualification.desc', 0, 1711929600000, 1, 1711929600000, 1),
('Opportunity__c', 'Stage__c', 'PROPOSAL',      'system', '方案报价', 'pick.stage.proposal',      3, 0, 0, 0, 1, '方案报价阶段', 'pick.stage.proposal.desc',      0, 1711929600000, 1, 1711929600000, 1),
('Opportunity__c', 'Stage__c', 'NEGOTIATION',   'system', '商务谈判', 'pick.stage.negotiation',   4, 0, 0, 0, 1, '商务谈判阶段', 'pick.stage.negotiation.desc',   0, 1711929600000, 1, 1711929600000, 1),
('Opportunity__c', 'Stage__c', 'CLOSED_WON',    'system', '赢单',     'pick.stage.closed_won',    5, 0, 0, 0, 1, '赢单',         'pick.stage.closed_won.desc',    0, 1711929600000, 1, 1711929600000, 1),
('Opportunity__c', 'Stage__c', 'CLOSED_LOST',   'system', '输单',     'pick.stage.closed_lost',   6, 0, 0, 0, 1, '输单',         'pick.stage.closed_lost.desc',   0, 1711929600000, 1, 1711929600000, 1);

-- E6. 关联关系（Common 级）
INSERT INTO p_common_entity_link (api_key, namespace, label, label_key, link_type, parent_entity_api_key, child_entity_api_key, cascade_delete, access_control, enable_flg, description, description_key, delete_flg, created_at, created_by, updated_at, updated_by) VALUES
('ContactToAccount__c', 'system', '联系人-客户', 'link.contact_account.label', 1, 'Account__c', 'Contact__c',     0, 0, 1, '联系人到客户LOOKUP', 'link.contact_account.desc', 0, 1711929600000, 1, 1711929600000, 1),
('OppToAccount__c',     'system', '商机-客户',   'link.opp_account.label',     1, 'Account__c', 'Opportunity__c', 0, 0, 1, '商机到客户LOOKUP',   'link.opp_account.desc',     0, 1711929600000, 1, 1711929600000, 1);

-- E7. 校验规则（Common 级）
INSERT INTO p_common_check_rule (entity_api_key, api_key, namespace, label, label_key, active_flg, description, description_key, check_formula, check_error_msg, check_error_msg_key, check_error_location, check_all_items_flg, delete_flg, created_by, created_at, updated_by, updated_at) VALUES
('Account__c',     'AccountNameRequired__c',  'system', '客户名称必填',     'rule.account_name.label', 1, '校验客户名称不能为空', 'rule.account_name.desc', 'NOT(ISBLANK(Name__c))',      '客户名称不能为空',     'rule.account_name.error', 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
('Opportunity__c', 'OppCloseDateRequired__c', 'system', '预计成交日期必填', 'rule.opp_close.label',    1, '校验商机预计成交日期', 'rule.opp_close.desc',    'NOT(ISBLANK(CloseDate__c))', '预计成交日期不能为空', 'rule.opp_close.error',    1, 0, 0, 1, 1711929600000, 1, 1711929600000);

-- E8. 租户测试数据（tenant_id=1001）
INSERT INTO p_tenant_item (tenant_id, entity_api_key, api_key, namespace, label, label_key, item_type, data_type, description, description_key, require_flg, delete_flg, custom_flg, enable_flg, creatable, updatable, item_order, created_at, created_by, updated_at, updated_by) VALUES
(1001, 'Account__c', 'Region__c',        'tenant', '区域',   'item.custom_region.label',   6, 3, '客户所在区域', 'item.custom_region.desc',   0, 0, 1, 1, 1, 1, 10, 1711929600000, 100, 1711929600000, 100),
(1001, 'Account__c', 'AnnualRevenue__c', 'tenant', '年营收', 'item.custom_revenue.label',  9, 4, '客户年营收',   'item.custom_revenue.desc',  0, 0, 1, 1, 1, 1, 11, 1711929600000, 100, 1711929600000, 100),
(1001, 'Account__c', 'Employees__c',     'custom', '员工数', 'item.custom_employee.label', 8, 3, '客户员工数量', 'item.custom_employee.desc', 0, 0, 1, 1, 1, 1, 12, 1711929600000, 100, 1711929600000, 100);

-- ============================================================
-- 完成
-- ============================================================
