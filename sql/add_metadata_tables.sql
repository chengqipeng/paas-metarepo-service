-- ============================================================
-- 补建 p_common_metadata 和 p_tenant_metadata 表
-- 在 paas_metarepo 数据库中执行（单库模式）
-- 与 CommonMetadata.java / TenantMetadata.java Entity 对齐
-- 包含 BaseEntity 的 id 列（MetaServiceImpl 需要 SELECT id）
--
-- 执行方式：
--   mysql -h 106.14.194.144 -u root -p paas_metarepo < add_metadata_tables.sql
-- ============================================================

-- ============================================================
-- p_common_metadata（Common 级通用大宽表元数据）
-- Entity: CommonMetadata extends BaseMetaCommonEntity
-- BaseEntity 字段: id, delete_flg, created_at/by, updated_at/by
-- BaseMetaCommonEntity 字段: api_key, label, label_key, namespace
-- ============================================================
CREATE TABLE IF NOT EXISTS p_common_metadata (
    -- BaseEntity 字段
    id                      BIGINT          NOT NULL,
    delete_flg              SMALLINT        DEFAULT 0,
    created_at              BIGINT,
    created_by              BIGINT,
    updated_at              BIGINT,
    updated_by              BIGINT,

    -- BaseMetaCommonEntity 字段
    api_key                 VARCHAR(255)    NOT NULL DEFAULT '',
    label                   VARCHAR(255),
    label_key               VARCHAR(255),
    namespace               VARCHAR(50)     NOT NULL DEFAULT 'system',

    -- CommonMetadata 固定列
    metamodel_api_key       VARCHAR(255)    NOT NULL,
    metadata_api_key        VARCHAR(255)    DEFAULT '',
    entity_api_key          VARCHAR(255),
    parent_metadata_api_key VARCHAR(255),
    custom_flg              INTEGER,
    metadata_order          INTEGER,
    description             VARCHAR(500),
    meta_version            VARCHAR(100),

    -- dbc_varchar_1~30
    dbc_varchar_1 VARCHAR(255), dbc_varchar_2 VARCHAR(255), dbc_varchar_3 VARCHAR(255),
    dbc_varchar_4 VARCHAR(255), dbc_varchar_5 VARCHAR(255), dbc_varchar_6 VARCHAR(255),
    dbc_varchar_7 VARCHAR(255), dbc_varchar_8 VARCHAR(255), dbc_varchar_9 VARCHAR(255),
    dbc_varchar_10 VARCHAR(255), dbc_varchar_11 VARCHAR(255), dbc_varchar_12 VARCHAR(255),
    dbc_varchar_13 VARCHAR(255), dbc_varchar_14 VARCHAR(255), dbc_varchar_15 VARCHAR(255),
    dbc_varchar_16 VARCHAR(255), dbc_varchar_17 VARCHAR(255), dbc_varchar_18 VARCHAR(255),
    dbc_varchar_19 VARCHAR(255), dbc_varchar_20 VARCHAR(255), dbc_varchar_21 VARCHAR(255),
    dbc_varchar_22 VARCHAR(255), dbc_varchar_23 VARCHAR(255), dbc_varchar_24 VARCHAR(255),
    dbc_varchar_25 VARCHAR(255), dbc_varchar_26 VARCHAR(255), dbc_varchar_27 VARCHAR(255),
    dbc_varchar_28 VARCHAR(255), dbc_varchar_29 VARCHAR(255), dbc_varchar_30 VARCHAR(255),

    -- dbc_textarea_1~10
    dbc_textarea_1 TEXT, dbc_textarea_2 TEXT, dbc_textarea_3 TEXT, dbc_textarea_4 TEXT,
    dbc_textarea_5 TEXT, dbc_textarea_6 TEXT, dbc_textarea_7 TEXT, dbc_textarea_8 TEXT,
    dbc_textarea_9 TEXT, dbc_textarea_10 TEXT,

    -- dbc_bigint_1~20
    dbc_bigint_1 BIGINT, dbc_bigint_2 BIGINT, dbc_bigint_3 BIGINT, dbc_bigint_4 BIGINT,
    dbc_bigint_5 BIGINT, dbc_bigint_6 BIGINT, dbc_bigint_7 BIGINT, dbc_bigint_8 BIGINT,
    dbc_bigint_9 BIGINT, dbc_bigint_10 BIGINT, dbc_bigint_11 BIGINT, dbc_bigint_12 BIGINT,
    dbc_bigint_13 BIGINT, dbc_bigint_14 BIGINT, dbc_bigint_15 BIGINT, dbc_bigint_16 BIGINT,
    dbc_bigint_17 BIGINT, dbc_bigint_18 BIGINT, dbc_bigint_19 BIGINT, dbc_bigint_20 BIGINT,

    -- dbc_int_1~15
    dbc_int_1 INTEGER, dbc_int_2 INTEGER, dbc_int_3 INTEGER, dbc_int_4 INTEGER,
    dbc_int_5 INTEGER, dbc_int_6 INTEGER, dbc_int_7 INTEGER, dbc_int_8 INTEGER,
    dbc_int_9 INTEGER, dbc_int_10 INTEGER, dbc_int_11 INTEGER, dbc_int_12 INTEGER,
    dbc_int_13 INTEGER, dbc_int_14 INTEGER, dbc_int_15 INTEGER,

    -- dbc_smallint_1~15
    dbc_smallint_1 SMALLINT, dbc_smallint_2 SMALLINT, dbc_smallint_3 SMALLINT,
    dbc_smallint_4 SMALLINT, dbc_smallint_5 SMALLINT, dbc_smallint_6 SMALLINT,
    dbc_smallint_7 SMALLINT, dbc_smallint_8 SMALLINT, dbc_smallint_9 SMALLINT,
    dbc_smallint_10 SMALLINT, dbc_smallint_11 SMALLINT, dbc_smallint_12 SMALLINT,
    dbc_smallint_13 SMALLINT, dbc_smallint_14 SMALLINT, dbc_smallint_15 SMALLINT,

    -- dbc_decimal_1~5
    dbc_decimal_1 DECIMAL(20,4), dbc_decimal_2 DECIMAL(20,4), dbc_decimal_3 DECIMAL(20,4),
    dbc_decimal_4 DECIMAL(20,4), dbc_decimal_5 DECIMAL(20,4),

    PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS idx_common_metadata_mm ON p_common_metadata (metamodel_api_key);
CREATE INDEX IF NOT EXISTS idx_common_metadata_ak ON p_common_metadata (metamodel_api_key, api_key);
CREATE INDEX IF NOT EXISTS idx_common_metadata_entity ON p_common_metadata (entity_api_key);


-- ============================================================
-- p_tenant_metadata（Tenant 级通用大宽表元数据）
-- Entity: TenantMetadata extends BaseMetaTenantEntity
-- BaseMetaTenantEntity 字段: tenant_id
-- ============================================================
CREATE TABLE IF NOT EXISTS p_tenant_metadata (
    -- BaseEntity 字段
    id                      BIGINT          NOT NULL,
    delete_flg              SMALLINT        DEFAULT 0,
    created_at              BIGINT,
    created_by              BIGINT,
    updated_at              BIGINT,
    updated_by              BIGINT,

    -- BaseMetaCommonEntity 字段
    api_key                 VARCHAR(255)    NOT NULL DEFAULT '',
    label                   VARCHAR(255),
    label_key               VARCHAR(255),
    namespace               VARCHAR(50)     NOT NULL DEFAULT 'tenant',

    -- BaseMetaTenantEntity 字段
    tenant_id               BIGINT          NOT NULL,

    -- TenantMetadata 固定列
    metamodel_api_key       VARCHAR(255)    NOT NULL,
    metadata_api_key        VARCHAR(255)    DEFAULT '',
    entity_api_key          VARCHAR(255),
    parent_metadata_api_key VARCHAR(255),
    custom_flg              INTEGER,
    metadata_order          INTEGER,
    description             VARCHAR(500),
    meta_version            VARCHAR(100),

    -- dbc_varchar_1~30
    dbc_varchar_1 VARCHAR(255), dbc_varchar_2 VARCHAR(255), dbc_varchar_3 VARCHAR(255),
    dbc_varchar_4 VARCHAR(255), dbc_varchar_5 VARCHAR(255), dbc_varchar_6 VARCHAR(255),
    dbc_varchar_7 VARCHAR(255), dbc_varchar_8 VARCHAR(255), dbc_varchar_9 VARCHAR(255),
    dbc_varchar_10 VARCHAR(255), dbc_varchar_11 VARCHAR(255), dbc_varchar_12 VARCHAR(255),
    dbc_varchar_13 VARCHAR(255), dbc_varchar_14 VARCHAR(255), dbc_varchar_15 VARCHAR(255),
    dbc_varchar_16 VARCHAR(255), dbc_varchar_17 VARCHAR(255), dbc_varchar_18 VARCHAR(255),
    dbc_varchar_19 VARCHAR(255), dbc_varchar_20 VARCHAR(255), dbc_varchar_21 VARCHAR(255),
    dbc_varchar_22 VARCHAR(255), dbc_varchar_23 VARCHAR(255), dbc_varchar_24 VARCHAR(255),
    dbc_varchar_25 VARCHAR(255), dbc_varchar_26 VARCHAR(255), dbc_varchar_27 VARCHAR(255),
    dbc_varchar_28 VARCHAR(255), dbc_varchar_29 VARCHAR(255), dbc_varchar_30 VARCHAR(255),

    -- dbc_textarea_1~10
    dbc_textarea_1 TEXT, dbc_textarea_2 TEXT, dbc_textarea_3 TEXT, dbc_textarea_4 TEXT,
    dbc_textarea_5 TEXT, dbc_textarea_6 TEXT, dbc_textarea_7 TEXT, dbc_textarea_8 TEXT,
    dbc_textarea_9 TEXT, dbc_textarea_10 TEXT,

    -- dbc_bigint_1~20
    dbc_bigint_1 BIGINT, dbc_bigint_2 BIGINT, dbc_bigint_3 BIGINT, dbc_bigint_4 BIGINT,
    dbc_bigint_5 BIGINT, dbc_bigint_6 BIGINT, dbc_bigint_7 BIGINT, dbc_bigint_8 BIGINT,
    dbc_bigint_9 BIGINT, dbc_bigint_10 BIGINT, dbc_bigint_11 BIGINT, dbc_bigint_12 BIGINT,
    dbc_bigint_13 BIGINT, dbc_bigint_14 BIGINT, dbc_bigint_15 BIGINT, dbc_bigint_16 BIGINT,
    dbc_bigint_17 BIGINT, dbc_bigint_18 BIGINT, dbc_bigint_19 BIGINT, dbc_bigint_20 BIGINT,

    -- dbc_int_1~15
    dbc_int_1 INTEGER, dbc_int_2 INTEGER, dbc_int_3 INTEGER, dbc_int_4 INTEGER,
    dbc_int_5 INTEGER, dbc_int_6 INTEGER, dbc_int_7 INTEGER, dbc_int_8 INTEGER,
    dbc_int_9 INTEGER, dbc_int_10 INTEGER, dbc_int_11 INTEGER, dbc_int_12 INTEGER,
    dbc_int_13 INTEGER, dbc_int_14 INTEGER, dbc_int_15 INTEGER,

    -- dbc_smallint_1~15
    dbc_smallint_1 SMALLINT, dbc_smallint_2 SMALLINT, dbc_smallint_3 SMALLINT,
    dbc_smallint_4 SMALLINT, dbc_smallint_5 SMALLINT, dbc_smallint_6 SMALLINT,
    dbc_smallint_7 SMALLINT, dbc_smallint_8 SMALLINT, dbc_smallint_9 SMALLINT,
    dbc_smallint_10 SMALLINT, dbc_smallint_11 SMALLINT, dbc_smallint_12 SMALLINT,
    dbc_smallint_13 SMALLINT, dbc_smallint_14 SMALLINT, dbc_smallint_15 SMALLINT,

    -- dbc_decimal_1~5
    dbc_decimal_1 DECIMAL(20,4), dbc_decimal_2 DECIMAL(20,4), dbc_decimal_3 DECIMAL(20,4),
    dbc_decimal_4 DECIMAL(20,4), dbc_decimal_5 DECIMAL(20,4),

    PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS idx_tenant_metadata_mm ON p_tenant_metadata (tenant_id, metamodel_api_key);
CREATE INDEX IF NOT EXISTS idx_tenant_metadata_ak ON p_tenant_metadata (tenant_id, metamodel_api_key, api_key);
CREATE INDEX IF NOT EXISTS idx_tenant_metadata_entity ON p_tenant_metadata (tenant_id, entity_api_key);
