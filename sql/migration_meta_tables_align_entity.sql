-- 迁移脚本：将 p_meta_model / p_meta_item / p_meta_link / p_meta_option 表结构对齐 Entity 类字段
-- 兼容 MySQL 和 PostgreSQL（需分别执行对应语法）

-- ============================================================
-- 1. p_meta_model：添加 namespace 列，重命名 xobject_dependency → entity_dependency
-- ============================================================

-- MySQL:
ALTER TABLE p_meta_model ADD COLUMN namespace VARCHAR(50) AFTER label_key;
ALTER TABLE p_meta_model CHANGE COLUMN xobject_dependency entity_dependency SMALLINT DEFAULT 0;

-- PostgreSQL:
-- ALTER TABLE p_meta_model ADD COLUMN namespace VARCHAR(50);
-- ALTER TABLE p_meta_model RENAME COLUMN xobject_dependency TO entity_dependency;

-- ============================================================
-- 2. p_meta_item：metamodel_id (BIGINT) → metamodel_api_key (VARCHAR)，添加 namespace
-- ============================================================

-- MySQL:
ALTER TABLE p_meta_item ADD COLUMN metamodel_api_key VARCHAR(255) NOT NULL DEFAULT '' AFTER tenant_id;
-- 数据迁移：将 metamodel_id 通过 p_meta_model 关联转换为 api_key
UPDATE p_meta_item i
    INNER JOIN p_meta_model m ON i.metamodel_id = m.id
SET i.metamodel_api_key = m.api_key;
ALTER TABLE p_meta_item DROP COLUMN metamodel_id;
ALTER TABLE p_meta_item ADD COLUMN namespace VARCHAR(50) AFTER label_key;
DROP INDEX idx_meta_item_metamodel ON p_meta_item;
CREATE INDEX idx_meta_item_metamodel ON p_meta_item(tenant_id, metamodel_api_key);

-- PostgreSQL:
-- ALTER TABLE p_meta_item ADD COLUMN metamodel_api_key VARCHAR(255) NOT NULL DEFAULT '';
-- UPDATE p_meta_item i SET metamodel_api_key = m.api_key FROM p_meta_model m WHERE i.metamodel_id = m.id;
-- ALTER TABLE p_meta_item DROP COLUMN metamodel_id;
-- ALTER TABLE p_meta_item ADD COLUMN namespace VARCHAR(50);
-- DROP INDEX idx_meta_item_metamodel;
-- CREATE INDEX idx_meta_item_metamodel ON p_meta_item(tenant_id, metamodel_api_key);

-- ============================================================
-- 3. p_meta_link：重命名列，删除 name/name_key，添加 namespace
-- ============================================================

-- MySQL:
ALTER TABLE p_meta_link ADD COLUMN namespace VARCHAR(50) AFTER label_key;
ALTER TABLE p_meta_link CHANGE COLUMN refer_item_id refer_item_api_key BIGINT NOT NULL;
ALTER TABLE p_meta_link CHANGE COLUMN child_metamodel_id child_metamodel_api_key BIGINT NOT NULL DEFAULT 0;
ALTER TABLE p_meta_link CHANGE COLUMN parent_metamodel_id parent_metamodel_api_key BIGINT NOT NULL DEFAULT 0;
ALTER TABLE p_meta_link DROP COLUMN name;
ALTER TABLE p_meta_link DROP COLUMN name_key;

-- PostgreSQL:
-- ALTER TABLE p_meta_link ADD COLUMN namespace VARCHAR(50);
-- ALTER TABLE p_meta_link RENAME COLUMN refer_item_id TO refer_item_api_key;
-- ALTER TABLE p_meta_link RENAME COLUMN child_metamodel_id TO child_metamodel_api_key;
-- ALTER TABLE p_meta_link RENAME COLUMN parent_metamodel_id TO parent_metamodel_api_key;
-- ALTER TABLE p_meta_link DROP COLUMN name;
-- ALTER TABLE p_meta_link DROP COLUMN name_key;

-- ============================================================
-- 4. p_meta_option：metamodel_id → metamodel_api_key，item_id → item_api_key，
--    option_label → label，option_label_key → label_key，添加 api_key/namespace，删除 option_code
-- ============================================================

-- MySQL:
ALTER TABLE p_meta_option ADD COLUMN metamodel_api_key VARCHAR(255) NOT NULL DEFAULT '' AFTER tenant_id;
ALTER TABLE p_meta_option ADD COLUMN item_api_key VARCHAR(255) NOT NULL DEFAULT '' AFTER metamodel_api_key;
-- 数据迁移：metamodel_id → metamodel_api_key
UPDATE p_meta_option o
    INNER JOIN p_meta_model m ON o.metamodel_id = m.id
SET o.metamodel_api_key = m.api_key;
-- 数据迁移：item_id → item_api_key
UPDATE p_meta_option o
    INNER JOIN p_meta_item i ON o.item_id = i.id
SET o.item_api_key = i.api_key;
ALTER TABLE p_meta_option DROP COLUMN metamodel_id;
ALTER TABLE p_meta_option DROP COLUMN item_id;
ALTER TABLE p_meta_option ADD COLUMN api_key VARCHAR(255) NOT NULL DEFAULT '';
-- 用 option_key 填充 api_key（如果业务上 option_key 就是 api_key）
UPDATE p_meta_option SET api_key = option_key WHERE api_key = '';
ALTER TABLE p_meta_option CHANGE COLUMN option_label label VARCHAR(255) NOT NULL;
ALTER TABLE p_meta_option CHANGE COLUMN option_label_key label_key VARCHAR(255);
ALTER TABLE p_meta_option ADD COLUMN namespace VARCHAR(50);
ALTER TABLE p_meta_option DROP COLUMN option_code;
DROP INDEX uk_option_code ON p_meta_option;
DROP INDEX idx_option_item ON p_meta_option;
CREATE UNIQUE INDEX uk_option_apikey ON p_meta_option(tenant_id, metamodel_api_key, item_api_key, api_key);
CREATE INDEX idx_option_item ON p_meta_option(tenant_id, metamodel_api_key, item_api_key);

-- PostgreSQL:
-- ALTER TABLE p_meta_option ADD COLUMN metamodel_api_key VARCHAR(255) NOT NULL DEFAULT '';
-- ALTER TABLE p_meta_option ADD COLUMN item_api_key VARCHAR(255) NOT NULL DEFAULT '';
-- UPDATE p_meta_option o SET metamodel_api_key = m.api_key FROM p_meta_model m WHERE o.metamodel_id = m.id;
-- UPDATE p_meta_option o SET item_api_key = i.api_key FROM p_meta_item i WHERE o.item_id = i.id;
-- ALTER TABLE p_meta_option DROP COLUMN metamodel_id;
-- ALTER TABLE p_meta_option DROP COLUMN item_id;
-- ALTER TABLE p_meta_option ADD COLUMN api_key VARCHAR(255) NOT NULL DEFAULT '';
-- UPDATE p_meta_option SET api_key = option_key WHERE api_key = '';
-- ALTER TABLE p_meta_option RENAME COLUMN option_label TO label;
-- ALTER TABLE p_meta_option RENAME COLUMN option_label_key TO label_key;
-- ALTER TABLE p_meta_option ADD COLUMN namespace VARCHAR(50);
-- ALTER TABLE p_meta_option DROP COLUMN option_code;
-- DROP INDEX uk_option_code;
-- DROP INDEX idx_option_item;
-- CREATE UNIQUE INDEX uk_option_apikey ON p_meta_option(tenant_id, metamodel_api_key, item_api_key, api_key);
-- CREATE INDEX idx_option_item ON p_meta_option(tenant_id, metamodel_api_key, item_api_key);
