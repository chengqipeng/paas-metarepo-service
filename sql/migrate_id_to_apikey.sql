-- ============================================================
-- Step 1: paas_metarepo_common 库 - ID 关联改为 api_key 关联
-- ============================================================
USE paas_metarepo_common;

-- p_meta_item: metamodel_id -> metamodel_api_key
ALTER TABLE p_meta_item ADD COLUMN metamodel_api_key VARCHAR(255);
UPDATE p_meta_item mi SET mi.metamodel_api_key = (SELECT mm.api_key FROM p_meta_model mm WHERE mm.id = mi.metamodel_id);
ALTER TABLE p_meta_item DROP COLUMN metamodel_id;

-- p_meta_option: metamodel_id -> metamodel_api_key, item_id -> item_api_key
ALTER TABLE p_meta_option ADD COLUMN metamodel_api_key VARCHAR(255);
ALTER TABLE p_meta_option ADD COLUMN item_api_key VARCHAR(255);
UPDATE p_meta_option mo SET mo.metamodel_api_key = (SELECT mm.api_key FROM p_meta_model mm WHERE mm.id = mo.metamodel_id);
UPDATE p_meta_option mo SET mo.item_api_key = (SELECT mi.api_key FROM p_meta_item mi WHERE mi.id = mo.item_id);
ALTER TABLE p_meta_option DROP COLUMN metamodel_id;
ALTER TABLE p_meta_option DROP COLUMN item_id;

-- p_meta_link: refer_item_id, child/parent_metamodel_id -> api_key
ALTER TABLE p_meta_link ADD COLUMN refer_item_api_key VARCHAR(255);
ALTER TABLE p_meta_link ADD COLUMN child_metamodel_api_key VARCHAR(255);
ALTER TABLE p_meta_link ADD COLUMN parent_metamodel_api_key VARCHAR(255);

UPDATE p_meta_link ml SET ml.refer_item_api_key = (SELECT mi.api_key FROM p_meta_item mi WHERE mi.id = ml.refer_item_id);
UPDATE p_meta_link ml SET ml.child_metamodel_api_key = (SELECT mm.api_key FROM p_meta_model mm WHERE mm.id = ml.child_metamodel_id);
UPDATE p_meta_link ml SET ml.parent_metamodel_api_key = (SELECT mm.api_key FROM p_meta_model mm WHERE mm.id = ml.parent_metamodel_id);
ALTER TABLE p_meta_link DROP COLUMN refer_item_id;
ALTER TABLE p_meta_link DROP COLUMN child_metamodel_id;
ALTER TABLE p_meta_link DROP COLUMN parent_metamodel_id;

-- p_common_entity: 删除 entity_id（关联大宽表的旧 ID）
ALTER TABLE p_common_entity DROP COLUMN entity_id;

-- p_common_item: entity_id -> entity_api_key, refer_entity_id -> refer_entity_api_key, refer_link_id -> refer_link_api_key
ALTER TABLE p_common_item ADD COLUMN entity_api_key VARCHAR(255) NOT NULL DEFAULT '';
UPDATE p_common_item ci SET ci.entity_api_key = (SELECT ce.api_key FROM p_common_entity ce WHERE ce.id = ci.entity_id);
ALTER TABLE p_common_item DROP COLUMN entity_id;
ALTER TABLE p_common_item ADD COLUMN refer_entity_api_key VARCHAR(255);
UPDATE p_common_item ci SET ci.refer_entity_api_key = (SELECT ce.api_key FROM p_common_entity ce WHERE ce.id = ci.refer_entity_id);
ALTER TABLE p_common_item DROP COLUMN refer_entity_id;
ALTER TABLE p_common_item ADD COLUMN refer_link_api_key VARCHAR(255);
ALTER TABLE p_common_item DROP COLUMN refer_link_id;

-- p_common_pickoption: entity_id -> entity_api_key, item_id -> item_api_key
ALTER TABLE p_common_pickoption ADD COLUMN entity_api_key VARCHAR(255);
ALTER TABLE p_common_pickoption ADD COLUMN item_api_key VARCHAR(255);
UPDATE p_common_pickoption cp SET cp.entity_api_key = (SELECT ce.api_key FROM p_common_entity ce WHERE ce.id = cp.entity_id);
UPDATE p_common_pickoption cp SET cp.item_api_key = (SELECT ci.api_key FROM p_common_item ci WHERE ci.id = cp.item_id);
ALTER TABLE p_common_pickoption DROP COLUMN entity_id;
ALTER TABLE p_common_pickoption DROP COLUMN item_id;

-- p_common_entity_link: parent/child_entity_id -> api_key
ALTER TABLE p_common_entity_link ADD COLUMN parent_entity_api_key VARCHAR(255);
ALTER TABLE p_common_entity_link ADD COLUMN child_entity_api_key VARCHAR(255);
UPDATE p_common_entity_link cl SET cl.parent_entity_api_key = (SELECT ce.api_key FROM p_common_entity ce WHERE ce.id = cl.parent_entity_id);
UPDATE p_common_entity_link cl SET cl.child_entity_api_key = (SELECT ce.api_key FROM p_common_entity ce WHERE ce.id = cl.child_entity_id);
ALTER TABLE p_common_entity_link DROP COLUMN parent_entity_id;
ALTER TABLE p_common_entity_link DROP COLUMN child_entity_id;

-- p_common_check_rule: entity_id -> entity_api_key, check_error_item_id -> check_error_item_api_key
ALTER TABLE p_common_check_rule ADD COLUMN entity_api_key VARCHAR(255);
ALTER TABLE p_common_check_rule ADD COLUMN check_error_item_api_key VARCHAR(255);
ALTER TABLE p_common_check_rule DROP COLUMN entity_id;
ALTER TABLE p_common_check_rule DROP COLUMN check_error_item_id;

-- p_common_metadata: metamodel_id -> metamodel_api_key, parent_entity_id -> parent_entity_api_key, owner_id -> owner_api_key
ALTER TABLE p_common_metadata ADD COLUMN metamodel_api_key VARCHAR(255);
ALTER TABLE p_common_metadata ADD COLUMN parent_entity_api_key VARCHAR(255);
ALTER TABLE p_common_metadata ADD COLUMN owner_api_key VARCHAR(255);
ALTER TABLE p_common_metadata DROP COLUMN metamodel_id;
ALTER TABLE p_common_metadata DROP COLUMN parent_entity_id;
ALTER TABLE p_common_metadata DROP COLUMN owner_id;

-- ============================================================
-- Step 2: paas_metarepo 库 - Tenant 级表同样改造
-- ============================================================
USE paas_metarepo;

-- p_tenant_entity: 删除 entity_id
ALTER TABLE p_tenant_entity DROP COLUMN entity_id;

-- p_tenant_item
ALTER TABLE p_tenant_item ADD COLUMN entity_api_key VARCHAR(255);
ALTER TABLE p_tenant_item ADD COLUMN refer_entity_api_key VARCHAR(255);
ALTER TABLE p_tenant_item ADD COLUMN refer_link_api_key VARCHAR(255);
ALTER TABLE p_tenant_item DROP COLUMN entity_id;
ALTER TABLE p_tenant_item DROP COLUMN refer_entity_id;
ALTER TABLE p_tenant_item DROP COLUMN refer_link_id;

-- p_tenant_pickoption
ALTER TABLE p_tenant_pickoption ADD COLUMN entity_api_key VARCHAR(255);
ALTER TABLE p_tenant_pickoption ADD COLUMN item_api_key VARCHAR(255);
ALTER TABLE p_tenant_pickoption DROP COLUMN entity_id;
ALTER TABLE p_tenant_pickoption DROP COLUMN item_id;

-- p_tenant_entity_link
ALTER TABLE p_tenant_entity_link ADD COLUMN parent_entity_api_key VARCHAR(255);
ALTER TABLE p_tenant_entity_link ADD COLUMN child_entity_api_key VARCHAR(255);
ALTER TABLE p_tenant_entity_link DROP COLUMN parent_entity_id;
ALTER TABLE p_tenant_entity_link DROP COLUMN child_entity_id;

-- p_tenant_check_rule
ALTER TABLE p_tenant_check_rule ADD COLUMN entity_api_key VARCHAR(255);
ALTER TABLE p_tenant_check_rule ADD COLUMN check_error_item_api_key VARCHAR(255);
ALTER TABLE p_tenant_check_rule DROP COLUMN entity_id;
ALTER TABLE p_tenant_check_rule DROP COLUMN check_error_item_id;

-- p_tenant_metadata
ALTER TABLE p_tenant_metadata ADD COLUMN metamodel_api_key VARCHAR(255);
ALTER TABLE p_tenant_metadata ADD COLUMN parent_entity_api_key VARCHAR(255);
ALTER TABLE p_tenant_metadata ADD COLUMN owner_api_key VARCHAR(255);
ALTER TABLE p_tenant_metadata DROP COLUMN metamodel_id;
ALTER TABLE p_tenant_metadata DROP COLUMN parent_entity_id;
ALTER TABLE p_tenant_metadata DROP COLUMN owner_id;
