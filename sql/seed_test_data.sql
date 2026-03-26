-- 验证数据：模拟 Account（客户）对象的完整元数据
-- tenant_id=0 为 Common 级，tenant_id=1001 为租户级

-- 1. p_meta_model：注册"对象"和"字段"两种元模型类型
INSERT INTO p_meta_model (id, tenant_id, api_key, label, label_key, metamodel_type, enable_package, enable_app, enable_delta, enable_log, enable_module_control, db_table, description, description_key, delete_flg, visible, created_by, created_at, updated_by, updated_at)
VALUES
(1, 0, 'CustomEntity', '自定义对象', 'meta.model.custom_entity', 1, 1, 1, 0, 1, 0, 'p_custom_entity', '自定义对象元模型定义', 'meta.model.custom_entity.desc', 0, 1, 1, 1711929600000, 1, 1711929600000),
(2, 0, 'CustomItem', '自定义字段', 'meta.model.custom_item', 2, 1, 0, 0, 1, 0, 'p_custom_item', '自定义字段元模型定义', 'meta.model.custom_item.desc', 0, 1, 1, 1711929600000, 1, 1711929600000),
(3, 0, 'CustomEntityLink', '关联关系', 'meta.model.custom_entity_link', 3, 1, 0, 0, 1, 0, 'p_custom_entity_link', '对象关联关系元模型定义', 'meta.model.custom_entity_link.desc', 0, 1, 1, 1711929600000, 1, 1711929600000);

-- 2. p_meta_item：定义"对象"元模型的字段项（api_key, label, object_type 等映射到大宽表列）
INSERT INTO p_meta_item (id, tenant_id, metamodel_id, api_key, label, label_key, item_type, data_type, item_order, require_flg, creatable, updatable, enable_package, db_column, description, description_key, delete_flg, created_by, created_at, updated_by, updated_at)
VALUES
(101, 0, 1, 'api_key', 'API标识', 'meta.item.api_key', 1, 1, 1, 1, 1, 0, 1, 'dbc_varchar_1', '对象的唯一API标识', 'meta.item.api_key.desc', 0, 1, 1711929600000, 1, 1711929600000),
(102, 0, 1, 'label', '显示标签', 'meta.item.label', 1, 1, 2, 1, 1, 1, 1, 'dbc_varchar_2', '对象的显示名称', 'meta.item.label.desc', 0, 1, 1711929600000, 1, 1711929600000),
(103, 0, 1, 'object_type', '对象类型', 'meta.item.object_type', 6, 3, 3, 0, 1, 0, 1, 'dbc_select_1', '对象的业务类型', 'meta.item.object_type.desc', 0, 1, 1711929600000, 1, 1711929600000),
(104, 0, 1, 'db_table', '数据库表名', 'meta.item.db_table', 1, 1, 4, 1, 1, 0, 0, 'dbc_varchar_3', '对象对应的物理表名', 'meta.item.db_table.desc', 0, 1, 1711929600000, 1, 1711929600000);

-- 3. p_meta_option：定义 object_type 的选项值
INSERT INTO p_meta_option (id, tenant_id, metamodel_id, item_id, option_code, option_key, label, label_key, option_order, default_flg, enable_flg, description, description_key, delete_flg, created_by, created_at, updated_by, updated_at)
VALUES
(201, 0, 1, 103, 1, 'STANDARD', '标准对象', 'meta.option.standard', 1, 1, 1, '平台预置的标准对象', 'meta.option.standard.desc', 0, 1, 1711929600000, 1, 1711929600000),
(202, 0, 1, 103, 2, 'CUSTOM', '自定义对象', 'meta.option.custom', 2, 0, 1, '租户创建的自定义对象', 'meta.option.custom.desc', 0, 1, 1711929600000, 1, 1711929600000),
(203, 0, 1, 103, 3, 'DETAIL', '明细对象', 'meta.option.detail', 3, 0, 1, '主从关系中的从对象', 'meta.option.detail.desc', 0, 1, 1711929600000, 1, 1711929600000);

-- 4. p_custom_entity：创建 Account（客户）和 Contact（联系人）两个标准对象
INSERT INTO p_custom_entity (id, tenant_id, name_space, entity_id, name, name_key, api_key, label, label_key, object_type, description, description_key, custom_entityseq, delete_flg, enable_flg, custom_flg, business_category, db_table, enable_sharing, enable_history_log, enable_report, enable_api, created_at, created_by, updated_at, updated_by)
VALUES
(1001, 0, 'paas', NULL, 'Account', 'entity.account.name', 'Account__c', '客户', 'entity.account.label', 1, '客户管理对象', 'entity.account.desc', 1, 0, 1, 0, 1, 'x_account', 1, 1, 1, 1, 1711929600000, 1, 1711929600000, 1),
(1002, 0, 'paas', NULL, 'Contact', 'entity.contact.name', 'Contact__c', '联系人', 'entity.contact.label', 1, '联系人管理对象', 'entity.contact.desc', 2, 0, 1, 0, 1, 'x_contact', 1, 1, 1, 1, 1711929600000, 1, 1711929600000, 1);

-- 5. p_custom_item：Account 对象的字段
INSERT INTO p_custom_item (id, tenant_id, entity_id, name, name_key, api_key, label, label_key, item_type, data_type, description, description_key, require_flg, delete_flg, custom_flg, enable_flg, creatable, updatable, item_order, created_at, created_by, updated_at, updated_by)
VALUES
(2001, 0, 1001, 'AccountName', 'item.account_name.name', 'Name__c', '客户名称', 'item.account_name.label', 1, 1, '客户的名称', 'item.account_name.desc', 1, 0, 0, 1, 1, 1, 1, 1711929600000, 1, 1711929600000, 1),
(2002, 0, 1001, 'AccountPhone', 'item.account_phone.name', 'Phone__c', '电话', 'item.account_phone.label', 2, 1, '客户电话号码', 'item.account_phone.desc', 0, 0, 0, 1, 1, 1, 2, 1711929600000, 1, 1711929600000, 1),
(2003, 0, 1001, 'AccountIndustry', 'item.account_industry.name', 'Industry__c', '行业', 'item.account_industry.label', 6, 3, '客户所属行业', 'item.account_industry.desc', 0, 0, 0, 1, 1, 1, 3, 1711929600000, 1, 1711929600000, 1),
(2004, 0, 1001, 'AccountStatus', 'item.account_status.name', 'Status__c', '状态', 'item.account_status.label', 6, 3, '客户状态', 'item.account_status.desc', 1, 0, 0, 1, 1, 1, 4, 1711929600000, 1, 1711929600000, 1),
(2005, 0, 1001, 'AccountWebsite', 'item.account_website.name', 'Website__c', '网站', 'item.account_website.label', 3, 1, '客户网站地址', 'item.account_website.desc', 0, 0, 0, 1, 1, 1, 5, 1711929600000, 1, 1711929600000, 1);

-- Contact 对象的字段
INSERT INTO p_custom_item (id, tenant_id, entity_id, name, name_key, api_key, label, label_key, item_type, data_type, description, description_key, require_flg, delete_flg, custom_flg, enable_flg, creatable, updatable, item_order, refer_entity_id, created_at, created_by, updated_at, updated_by)
VALUES
(2101, 0, 1002, 'ContactName', 'item.contact_name.name', 'Name__c', '姓名', 'item.contact_name.label', 1, 1, '联系人姓名', 'item.contact_name.desc', 1, 0, 0, 1, 1, 1, 1, NULL, 1711929600000, 1, 1711929600000, 1),
(2102, 0, 1002, 'ContactEmail', 'item.contact_email.name', 'Email__c', '邮箱', 'item.contact_email.label', 4, 1, '联系人邮箱', 'item.contact_email.desc', 0, 0, 0, 1, 1, 1, 2, NULL, 1711929600000, 1, 1711929600000, 1),
(2103, 0, 1002, 'ContactAccount', 'item.contact_account.name', 'AccountId__c', '所属客户', 'item.contact_account.label', 19, 5, '联系人所属的客户', 'item.contact_account.desc', 0, 0, 0, 1, 1, 1, 3, 1001, 1711929600000, 1, 1711929600000, 1);

-- 6. p_custom_pickoption：Industry 和 Status 字段的选项值
INSERT INTO p_custom_pickoption (id, tenant_id, entity_id, item_id, api_key, option_code, label, label_key, option_order, default_flg, global_flg, custom_flg, delete_flg, enable_flg, description, description_key, created_at, created_by, updated_at, updated_by)
VALUES
(3001, 0, 1001, 2003, 'IT', 1, 'IT/互联网', 'pick.industry.it', 1, 0, 0, 0, 0, 1, 'IT和互联网行业', 'pick.industry.it.desc', 1711929600000, 1, 1711929600000, 1),
(3002, 0, 1001, 2003, 'FINANCE', 2, '金融', 'pick.industry.finance', 2, 0, 0, 0, 0, 1, '金融行业', 'pick.industry.finance.desc', 1711929600000, 1, 1711929600000, 1),
(3003, 0, 1001, 2003, 'MANUFACTURING', 3, '制造业', 'pick.industry.manufacturing', 3, 0, 0, 0, 0, 1, '制造业', 'pick.industry.manufacturing.desc', 1711929600000, 1, 1711929600000, 1),
(3004, 0, 1001, 2003, 'EDUCATION', 4, '教育', 'pick.industry.education', 4, 0, 0, 0, 0, 1, '教育行业', 'pick.industry.education.desc', 1711929600000, 1, 1711929600000, 1),
(3011, 0, 1001, 2004, 'ACTIVE', 1, '活跃', 'pick.status.active', 1, 1, 0, 0, 0, 1, '活跃客户', 'pick.status.active.desc', 1711929600000, 1, 1711929600000, 1),
(3012, 0, 1001, 2004, 'INACTIVE', 2, '不活跃', 'pick.status.inactive', 2, 0, 0, 0, 0, 1, '不活跃客户', 'pick.status.inactive.desc', 1711929600000, 1, 1711929600000, 1),
(3013, 0, 1001, 2004, 'POTENTIAL', 3, '潜在', 'pick.status.potential', 3, 0, 0, 0, 0, 1, '潜在客户', 'pick.status.potential.desc', 1711929600000, 1, 1711929600000, 1);

-- 7. p_custom_entity_link：Contact -> Account 的 LOOKUP 关联
INSERT INTO p_custom_entity_link (id, tenant_id, name, name_key, api_key, label, label_key, link_type, parent_entity_id, child_entity_id, cascade_delete, access_control, enable_flg, description, description_key, delete_flg, created_at, created_by, updated_at, updated_by)
VALUES
(4001, 0, 'ContactToAccount', 'link.contact_to_account.name', 'ContactToAccount__c', '联系人-客户关联', 'link.contact_to_account.label', 1, 1001, 1002, 0, 0, 1, '联系人到客户的LOOKUP关联', 'link.contact_to_account.desc', 0, 1711929600000, 1, 1711929600000, 1);

-- 8. p_custom_check_rule：Account 的校验规则
INSERT INTO p_custom_check_rule (id, tenant_id, entity_id, name, name_key, api_key, label, label_key, active_flg, description, description_key, check_formula, check_error_msg, check_error_msg_key, check_error_location, check_all_items_flg, created_by, created_at, updated_by, updated_at)
VALUES
(5001, 0, 1001, 'AccountNameRequired', 'rule.account_name_required.name', 'AccountNameRequired__c', '客户名称必填校验', 'rule.account_name_required.label', 1, '校验客户名称不能为空', 'rule.account_name_required.desc', 'NOT(ISBLANK(Name__c))', '客户名称不能为空', 'rule.account_name_required.error', 1, 0, 1, 1711929600000, 1, 1711929600000);

-- 9. 租户级数据：tenant_id=1001 的租户自定义字段
INSERT INTO p_custom_item (id, tenant_id, entity_id, name, name_key, api_key, label, label_key, item_type, data_type, description, description_key, require_flg, delete_flg, custom_flg, enable_flg, creatable, updatable, item_order, created_at, created_by, updated_at, updated_by)
VALUES
(6001, 1001, 1001, 'CustomField1', 'item.custom_field1.name', 'CustomField1__c', '自定义字段1', 'item.custom_field1.label', 1, 1, '租户自定义的文本字段', 'item.custom_field1.desc', 0, 0, 1, 1, 1, 1, 10, 1711929600000, 100, 1711929600000, 100),
(6002, 1001, 1001, 'CustomField2', 'item.custom_field2.name', 'CustomField2__c', '自定义字段2', 'item.custom_field2.label', 7, 3, '租户自定义的数字字段', 'item.custom_field2.desc', 0, 0, 1, 1, 1, 1, 11, 1711929600000, 100, 1711929600000, 100);
