-- 清理并重新插入干净的元模型定义数据
-- 目标库：paas_metarepo_common

-- p_meta_model（6 种实体相关元模型）
INSERT INTO p_meta_model (id, api_key, namespace, label, label_key, metamodel_type,
  enable_common, enable_tenant, enable_package, enable_app,
  enable_deprecation, enable_deactivation, enable_delta, enable_log,
  enable_module_control, entity_dependency, db_table,
  description, description_key, visible, delete_flg,
  created_by, created_at, updated_by, updated_at)
VALUES
(1, 'entity', 'system', '自定义业务对象', 'meta.model.entity', 1,
 1, 1, 1, 1, 0, 1, 0, 1, 0, 0, 'p_tenant_metadata',
 '对象定义元模型', 'meta.model.entity.desc', 1, 0,
 1, 1711929600000, 1, 1711929600000),
(2, 'item', 'system', '自定义字段', 'meta.model.item', 1,
 1, 1, 1, 0, 0, 1, 0, 1, 0, 1, 'p_tenant_metadata',
 '字段定义元模型', 'meta.model.item.desc', 1, 0,
 1, 1711929600000, 1, 1711929600000),
(3, 'entityLink', 'system', '实体Link', 'meta.model.entity_link', 1,
 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 'p_tenant_metadata',
 '对象关联关系元模型', 'meta.model.entity_link.desc', 1, 0,
 1, 1711929600000, 1, 1711929600000),
(4, 'checkRule', 'system', '校验规则', 'meta.model.check_rule', 1,
 1, 1, 1, 0, 0, 1, 0, 1, 0, 1, 'p_tenant_metadata',
 '数据校验规则元模型', 'meta.model.check_rule.desc', 1, 0,
 1, 1711929600000, 1, 1711929600000),
(5, 'pickOption', 'system', 'Pick选项', 'meta.model.pick_option', 1,
 1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 'p_tenant_metadata',
 '选项字段取值列表', 'meta.model.pick_option.desc', 1, 0,
 1, 1711929600000, 1, 1711929600000),
(6, 'referenceFilter', 'system', '关联过滤器', 'meta.model.reference_filter', 1,
 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 'p_tenant_metadata',
 'LOOKUP字段下拉过滤', 'meta.model.reference_filter.desc', 1, 0,
 1, 1711929600000, 1, 1711929600000);

-- p_meta_link（5 条关联关系）
INSERT INTO p_meta_link (id, api_key, namespace, label, label_key,
  parent_metamodel_api_key, child_metamodel_api_key, refer_item_api_key,
  link_type, cascade_delete, delete_flg, created_by, created_at, updated_by, updated_at)
VALUES
(900001, 'entity_to_item', 'system', '对象-字段', 'link.entity_to_item',
 'entity', 'item', 'entityApiKey', 1, 1, 0, 1, 1711929600000, 1, 1711929600000),
(900002, 'entity_to_link', 'system', '对象-关联', 'link.entity_to_link',
 'entity', 'entityLink', 'entityApiKey', 1, 1, 0, 1, 1711929600000, 1, 1711929600000),
(900003, 'entity_to_checkrule', 'system', '对象-校验规则', 'link.entity_to_checkrule',
 'entity', 'checkRule', 'entityApiKey', 1, 1, 0, 1, 1711929600000, 1, 1711929600000),
(900004, 'item_to_pickoption', 'system', '字段-选项值', 'link.item_to_pickoption',
 'item', 'pickOption', 'itemApiKey', 1, 1, 0, 1, 1711929600000, 1, 1711929600000),
(900005, 'item_to_reffilter', 'system', '字段-关联过滤', 'link.item_to_reffilter',
 'item', 'referenceFilter', 'itemApiKey', 1, 1, 0, 1, 1711929600000, 1, 1711929600000);
