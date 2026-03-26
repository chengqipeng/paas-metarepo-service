-- 补齐所有表的 name_key / description_key 国际化字段
-- 兼容 MySQL 和 PostgreSQL（ALTER TABLE ADD COLUMN IF NOT EXISTS 仅 PG 支持，MySQL 需手动检查）

-- p_meta_model
ALTER TABLE p_meta_model ADD COLUMN description_key VARCHAR(255);

-- p_meta_item
ALTER TABLE p_meta_item ADD COLUMN description_key VARCHAR(255);

-- p_custom_entity
ALTER TABLE p_custom_entity ADD COLUMN name_key VARCHAR(255);
ALTER TABLE p_custom_entity ADD COLUMN description_key VARCHAR(255);

-- p_custom_item
ALTER TABLE p_custom_item ADD COLUMN name_key VARCHAR(255);
ALTER TABLE p_custom_item ADD COLUMN description_key VARCHAR(255);

-- p_custom_entity_link
ALTER TABLE p_custom_entity_link ADD COLUMN name_key VARCHAR(255);
ALTER TABLE p_custom_entity_link ADD COLUMN description_key VARCHAR(255);

-- p_custom_pickoption
ALTER TABLE p_custom_pickoption ADD COLUMN description_key VARCHAR(255);

-- p_custom_check_rule
ALTER TABLE p_custom_check_rule ADD COLUMN name_key VARCHAR(255);
ALTER TABLE p_custom_check_rule ADD COLUMN description_key VARCHAR(255);

-- p_meta_option
ALTER TABLE p_meta_option ADD COLUMN description_key VARCHAR(255);

-- p_meta_link
ALTER TABLE p_meta_link ADD COLUMN name VARCHAR(255);
ALTER TABLE p_meta_link ADD COLUMN name_key VARCHAR(255);
ALTER TABLE p_meta_link ADD COLUMN description_key VARCHAR(255);

-- x_global_pickitem
ALTER TABLE x_global_pickitem ADD COLUMN name_key VARCHAR(100);
ALTER TABLE x_global_pickitem ADD COLUMN description_key VARCHAR(255);

-- 补齐唯一索引（如果不存在）
CREATE UNIQUE INDEX uk_rule_apikey ON p_custom_check_rule(tenant_id, entity_id, api_key);
CREATE UNIQUE INDEX uk_link_apikey ON p_custom_entity_link(tenant_id, api_key);
CREATE UNIQUE INDEX uk_pick_apikey ON p_custom_pickoption(tenant_id, item_id, api_key);
