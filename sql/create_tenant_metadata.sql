-- ============================================================
-- p_tenant_metadata 建表脚本
-- 结构与 p_common_metadata 一致，多一个 tenant_id 列
-- 兼容 MySQL / PostgreSQL
-- ============================================================

DROP TABLE IF EXISTS p_tenant_metadata;
CREATE TABLE p_tenant_metadata (
    -- 租户 ID
    tenant_id               BIGINT          NOT NULL,

    -- BaseMetaCommonEntity 基类字段
    api_key                 VARCHAR(255)    NOT NULL DEFAULT '',
    label                   VARCHAR(255),
    label_key               VARCHAR(255),
    namespace               VARCHAR(50)     NOT NULL DEFAULT 'tenant',
    delete_flg              SMALLINT        DEFAULT 0,
    created_at              BIGINT,
    created_by              BIGINT,
    updated_at              BIGINT,
    updated_by              BIGINT,

    -- 固定列（与 p_common_metadata 一致）
    metamodel_api_key       VARCHAR(255)    NOT NULL,
    metadata_api_key        VARCHAR(255),
    entity_api_key          VARCHAR(255),
    parent_metadata_api_key VARCHAR(255),
    custom_flg              INTEGER,
    metadata_order          INTEGER,
    description             VARCHAR(500),
    meta_version            VARCHAR(100),

    -- dbc_varchar_1~30 VARCHAR(255)
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

    -- dbc_textarea_1~10 TEXT
    dbc_textarea_1 TEXT, dbc_textarea_2 TEXT, dbc_textarea_3 TEXT, dbc_textarea_4 TEXT,
    dbc_textarea_5 TEXT, dbc_textarea_6 TEXT, dbc_textarea_7 TEXT, dbc_textarea_8 TEXT,
    dbc_textarea_9 TEXT, dbc_textarea_10 TEXT,

    -- dbc_bigint_1~20 BIGINT
    dbc_bigint_1 BIGINT, dbc_bigint_2 BIGINT, dbc_bigint_3 BIGINT, dbc_bigint_4 BIGINT,
    dbc_bigint_5 BIGINT, dbc_bigint_6 BIGINT, dbc_bigint_7 BIGINT, dbc_bigint_8 BIGINT,
    dbc_bigint_9 BIGINT, dbc_bigint_10 BIGINT, dbc_bigint_11 BIGINT, dbc_bigint_12 BIGINT,
    dbc_bigint_13 BIGINT, dbc_bigint_14 BIGINT, dbc_bigint_15 BIGINT, dbc_bigint_16 BIGINT,
    dbc_bigint_17 BIGINT, dbc_bigint_18 BIGINT, dbc_bigint_19 BIGINT, dbc_bigint_20 BIGINT,

    -- dbc_int_1~15 INTEGER
    dbc_int_1 INTEGER, dbc_int_2 INTEGER, dbc_int_3 INTEGER, dbc_int_4 INTEGER,
    dbc_int_5 INTEGER, dbc_int_6 INTEGER, dbc_int_7 INTEGER, dbc_int_8 INTEGER,
    dbc_int_9 INTEGER, dbc_int_10 INTEGER, dbc_int_11 INTEGER, dbc_int_12 INTEGER,
    dbc_int_13 INTEGER, dbc_int_14 INTEGER, dbc_int_15 INTEGER,

    -- dbc_smallint_1~15 SMALLINT
    dbc_smallint_1 SMALLINT, dbc_smallint_2 SMALLINT, dbc_smallint_3 SMALLINT,
    dbc_smallint_4 SMALLINT, dbc_smallint_5 SMALLINT, dbc_smallint_6 SMALLINT,
    dbc_smallint_7 SMALLINT, dbc_smallint_8 SMALLINT, dbc_smallint_9 SMALLINT,
    dbc_smallint_10 SMALLINT, dbc_smallint_11 SMALLINT, dbc_smallint_12 SMALLINT,
    dbc_smallint_13 SMALLINT, dbc_smallint_14 SMALLINT, dbc_smallint_15 SMALLINT,

    -- dbc_decimal_1~5 DECIMAL(20,4)
    dbc_decimal_1 DECIMAL(20,4), dbc_decimal_2 DECIMAL(20,4), dbc_decimal_3 DECIMAL(20,4),
    dbc_decimal_4 DECIMAL(20,4), dbc_decimal_5 DECIMAL(20,4),

    -- 主键：tenant_id + metamodel_api_key + api_key + metadata_api_key
    PRIMARY KEY (tenant_id, metamodel_api_key, api_key, metadata_api_key)
);

CREATE INDEX idx_tenant_metadata_mm ON p_tenant_metadata (tenant_id, metamodel_api_key);
CREATE INDEX idx_tenant_metadata_entity ON p_tenant_metadata (tenant_id, entity_api_key);
