-- ============================================================
-- 为元模型/元数据表补加 id 列（雪花主键）
-- MetaServiceImpl 的两阶段缓存需要 SELECT id，表必须有 id 列
-- id 仅供框架内部缓存使用，API 不对外暴露，统一用 apiKey 定位
--
-- 执行顺序：先 paas_metarepo_common，再 paas_metarepo
-- ============================================================

-- ************************************************************
-- paas_metarepo_common 库
-- ************************************************************

-- p_meta_model：原主键 api_key，加 id 列并改主键
ALTER TABLE paas_metarepo_common.p_meta_model
    DROP PRIMARY KEY,
    ADD COLUMN id BIGINT NOT NULL FIRST,
    ADD PRIMARY KEY (id),
    ADD UNIQUE INDEX uk_meta_model_apikey (api_key);

-- p_meta_item：原主键 (metamodel_api_key, api_key)，加 id 列并改主键
ALTER TABLE paas_metarepo_common.p_meta_item
    DROP PRIMARY KEY,
    ADD COLUMN id BIGINT NOT NULL FIRST,
    ADD PRIMARY KEY (id),
    ADD UNIQUE INDEX uk_meta_item_mm_ak (metamodel_api_key, api_key);

-- p_meta_option：原主键 (metamodel_api_key, item_api_key, api_key)
ALTER TABLE paas_metarepo_common.p_meta_option
    DROP PRIMARY KEY,
    ADD COLUMN id BIGINT NOT NULL FIRST,
    ADD PRIMARY KEY (id),
    ADD UNIQUE INDEX uk_meta_option_mm_item_ak (metamodel_api_key, item_api_key, api_key);

-- p_meta_link：原主键 api_key
ALTER TABLE paas_metarepo_common.p_meta_link
    DROP PRIMARY KEY,
    ADD COLUMN id BIGINT NOT NULL FIRST,
    ADD PRIMARY KEY (id),
    ADD UNIQUE INDEX uk_meta_link_apikey (api_key);

-- p_common_metadata：原主键 (metamodel_api_key, api_key, metadata_api_key)
ALTER TABLE paas_metarepo_common.p_common_metadata
    DROP PRIMARY KEY,
    ADD COLUMN id BIGINT NOT NULL FIRST,
    ADD PRIMARY KEY (id),
    ADD UNIQUE INDEX uk_common_metadata_mm_ak_mk (metamodel_api_key, api_key, metadata_api_key);


-- ************************************************************
-- paas_metarepo 库
-- ************************************************************

-- p_tenant_metadata：原主键 (tenant_id, metamodel_api_key, api_key, metadata_api_key)
-- 检查是否已有 id 列（如果之前已执行 add_metadata_tables.sql 则已有）
-- 如果表不存在或已有 id 列，此语句会报错，可忽略
ALTER TABLE paas_metarepo.p_tenant_metadata
    DROP PRIMARY KEY,
    ADD COLUMN id BIGINT NOT NULL FIRST,
    ADD PRIMARY KEY (id),
    ADD UNIQUE INDEX uk_tenant_metadata_tid_mm_ak_mk (tenant_id, metamodel_api_key, api_key, metadata_api_key);
