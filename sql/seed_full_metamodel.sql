-- 完整元模型初始化数据
-- 清除旧数据后重新插入

DELETE FROM p_custom_check_rule;
DELETE FROM p_custom_pickoption;
DELETE FROM p_custom_entity_link;
DELETE FROM p_custom_item;
DELETE FROM p_custom_entity;
DELETE FROM p_meta_option;
DELETE FROM p_meta_item;
DELETE FROM p_meta_model;
DELETE FROM p_meta_log;

-- ============================================================
-- 1. p_meta_model：注册所有元模型类型
-- ============================================================
INSERT INTO p_meta_model (id, tenant_id, api_key, label, label_key, metamodel_type, enable_package, enable_app, enable_delta, enable_log, enable_module_control, db_table, description, description_key, delete_flg, visible, created_by, created_at, updated_by, updated_at) VALUES
(1,  0, 'CustomEntity',     '自定义对象',   'meta.model.entity',      1, 1, 1, 0, 1, 0, 'p_custom_entity',      '自定义对象元模型',     'meta.model.entity.desc',      0, 1, 1, 1711929600000, 1, 1711929600000),
(2,  0, 'CustomItem',       '自定义字段',   'meta.model.item',        2, 1, 0, 0, 1, 0, 'p_custom_item',        '自定义字段元模型',     'meta.model.item.desc',        0, 1, 1, 1711929600000, 1, 1711929600000),
(3,  0, 'CustomEntityLink', '关联关系',     'meta.model.link',        3, 1, 0, 0, 1, 0, 'p_custom_entity_link', '对象关联关系元模型',   'meta.model.link.desc',        0, 1, 1, 1711929600000, 1, 1711929600000),
(4,  0, 'CustomCheckRule',  '校验规则',     'meta.model.check_rule',  4, 1, 0, 0, 1, 0, 'p_custom_check_rule',  '校验规则元模型',       'meta.model.check_rule.desc',  0, 1, 1, 1711929600000, 1, 1711929600000),
(5,  0, 'CustomPickOption', '选项值',       'meta.model.pick_option', 5, 0, 0, 0, 0, 0, 'p_custom_pickoption',  '字段选项值元模型',     'meta.model.pick_option.desc', 0, 0, 1, 1711929600000, 1, 1711929600000),
(6,  0, 'MetaLink',         '元模型关联',   'meta.model.meta_link',   6, 0, 0, 0, 0, 0, 'p_meta_link',          '元模型间关联关系',     'meta.model.meta_link.desc',   0, 0, 1, 1711929600000, 1, 1711929600000),
(7,  0, 'MetaOption',       '元模型选项',   'meta.model.meta_option', 7, 0, 0, 0, 0, 0, 'p_meta_option',        '元模型字段选项值',     'meta.model.meta_option.desc', 0, 0, 1, 1711929600000, 1, 1711929600000),
(8,  0, 'I18nResource',     '多语言资源',   'meta.model.i18n',        8, 0, 0, 0, 0, 0, 'p_meta_i18n_resource', '多语言资源元模型',     'meta.model.i18n.desc',        0, 0, 1, 1711929600000, 1, 1711929600000),
(9,  0, 'GlobalPickItem',   '全局选项集',   'meta.model.global_pick', 9, 0, 0, 0, 0, 0, 'x_global_pickitem',    '全局选项集元模型',     'meta.model.global_pick.desc', 0, 1, 1, 1711929600000, 1, 1711929600000);

-- ============================================================
-- 2. p_meta_item：定义每种元模型的字段项（db_column 映射到大宽表列）
-- ============================================================

-- CustomEntity (metamodel_id=1) 的字段项
INSERT INTO p_meta_item (id, tenant_id, metamodel_id, api_key, label, label_key, item_type, data_type, item_order, require_flg, creatable, updatable, enable_package, db_column, description, description_key, delete_flg, created_by, created_at, updated_by, updated_at) VALUES
(101, 0, 1, 'api_key',        'API标识',     'meta.item.api_key',        1, 1, 1,  1, 1, 0, 1, 'dbc_varchar_1',  '对象唯一API标识',       'meta.item.api_key.desc',        0, 1, 1711929600000, 1, 1711929600000),
(102, 0, 1, 'label',          '显示标签',    'meta.item.label',          1, 1, 2,  1, 1, 1, 1, 'dbc_varchar_2',  '对象显示名称',          'meta.item.label.desc',          0, 1, 1711929600000, 1, 1711929600000),
(103, 0, 1, 'object_type',    '对象类型',    'meta.item.object_type',    6, 3, 3,  0, 1, 0, 1, 'dbc_select_1',   '对象业务类型',          'meta.item.object_type.desc',    0, 1, 1711929600000, 1, 1711929600000),
(104, 0, 1, 'db_table',       '数据库表名',  'meta.item.db_table',       1, 1, 4,  1, 1, 0, 0, 'dbc_varchar_3',  '对象对应物理表名',      'meta.item.db_table.desc',       0, 1, 1711929600000, 1, 1711929600000),
(105, 0, 1, 'enable_sharing', '启用共享',    'meta.item.enable_sharing', 6, 3, 5,  0, 1, 1, 0, 'dbc_tinyint_1',  '是否启用数据共享',      'meta.item.enable_sharing.desc', 0, 1, 1711929600000, 1, 1711929600000),
(106, 0, 1, 'enable_team',    '启用团队',    'meta.item.enable_team',    6, 3, 6,  0, 1, 1, 0, 'dbc_tinyint_2',  '是否启用团队协作',      'meta.item.enable_team.desc',    0, 1, 1711929600000, 1, 1711929600000),
(107, 0, 1, 'searchable',     '可搜索',      'meta.item.searchable',     6, 3, 7,  0, 1, 1, 0, 'dbc_tinyint_3',  '是否支持全文搜索',      'meta.item.searchable.desc',     0, 1, 1711929600000, 1, 1711929600000),
(108, 0, 1, 'enable_report',  '启用报表',    'meta.item.enable_report',  6, 3, 8,  0, 1, 1, 0, 'dbc_tinyint_4',  '是否启用报表',          'meta.item.enable_report.desc',  0, 1, 1711929600000, 1, 1711929600000),
(109, 0, 1, 'enable_api',     '启用API',     'meta.item.enable_api',     6, 3, 9,  0, 1, 1, 0, 'dbc_tinyint_5',  '是否启用API访问',       'meta.item.enable_api.desc',     0, 1, 1711929600000, 1, 1711929600000),
(110, 0, 1, 'description',    '描述',        'meta.item.description',    2, 1, 10, 0, 1, 1, 1, 'dbc_textarea_1', '对象描述信息',          'meta.item.description.desc',    0, 1, 1711929600000, 1, 1711929600000);

-- CustomItem (metamodel_id=2) 的字段项
INSERT INTO p_meta_item (id, tenant_id, metamodel_id, api_key, label, label_key, item_type, data_type, item_order, require_flg, creatable, updatable, enable_package, db_column, description, description_key, delete_flg, created_by, created_at, updated_by, updated_at) VALUES
(201, 0, 2, 'api_key',       'API标识',     'meta.item.api_key',       1, 1, 1, 1, 1, 0, 1, 'dbc_varchar_1',  '字段唯一API标识',       'meta.item.api_key.desc',       0, 1, 1711929600000, 1, 1711929600000),
(202, 0, 2, 'label',         '显示标签',    'meta.item.label',         1, 1, 2, 1, 1, 1, 1, 'dbc_varchar_2',  '字段显示名称',          'meta.item.label.desc',         0, 1, 1711929600000, 1, 1711929600000),
(203, 0, 2, 'item_type',     '字段UI类型',  'meta.item.item_type',     6, 3, 3, 1, 1, 0, 1, 'dbc_select_1',   '字段的UI展示类型',      'meta.item.item_type.desc',     0, 1, 1711929600000, 1, 1711929600000),
(204, 0, 2, 'data_type',     '数据类型',    'meta.item.data_type',     6, 3, 4, 1, 1, 0, 1, 'dbc_select_2',   '字段的数据存储类型',    'meta.item.data_type.desc',     0, 1, 1711929600000, 1, 1711929600000),
(205, 0, 2, 'require_flg',   '是否必填',    'meta.item.require_flg',   6, 3, 5, 0, 1, 1, 0, 'dbc_tinyint_1',  '字段是否必填',          'meta.item.require_flg.desc',   0, 1, 1711929600000, 1, 1711929600000),
(206, 0, 2, 'default_value', '默认值',      'meta.item.default_value', 1, 1, 6, 0, 1, 1, 0, 'dbc_varchar_3',  '字段默认值',            'meta.item.default_value.desc', 0, 1, 1711929600000, 1, 1711929600000),
(207, 0, 2, 'help_text',     '帮助文本',    'meta.item.help_text',     2, 1, 7, 0, 1, 1, 0, 'dbc_textarea_1', '字段帮助提示文本',      'meta.item.help_text.desc',     0, 1, 1711929600000, 1, 1711929600000),
(208, 0, 2, 'description',   '描述',        'meta.item.description',   2, 1, 8, 0, 1, 1, 1, 'dbc_textarea_2', '字段描述信息',          'meta.item.description.desc',   0, 1, 1711929600000, 1, 1711929600000);

-- ============================================================
-- 3. p_meta_option：元模型字段的选项值定义
-- ============================================================

-- object_type 选项值 (metamodel_id=1, item_id=103)
INSERT INTO p_meta_option (id, tenant_id, metamodel_id, item_id, option_code, option_key, option_label, option_label_key, option_order, default_flg, enable_flg, description, description_key, delete_flg, created_by, created_at, updated_by, updated_at) VALUES
(301, 0, 1, 103, 1, 'STANDARD',  '标准对象',   'opt.obj_type.standard',  1, 1, 1, '平台预置标准对象', 'opt.obj_type.standard.desc',  0, 1, 1711929600000, 1, 1711929600000),
(302, 0, 1, 103, 2, 'CUSTOM',    '自定义对象', 'opt.obj_type.custom',    2, 0, 1, '租户自定义对象',   'opt.obj_type.custom.desc',    0, 1, 1711929600000, 1, 1711929600000),
(303, 0, 1, 103, 3, 'DETAIL',    '明细对象',   'opt.obj_type.detail',    3, 0, 1, '主从关系从对象',   'opt.obj_type.detail.desc',    0, 1, 1711929600000, 1, 1711929600000);

-- item_type 选项值 (metamodel_id=2, item_id=203)
INSERT INTO p_meta_option (id, tenant_id, metamodel_id, item_id, option_code, option_key, option_label, option_label_key, option_order, default_flg, enable_flg, description, description_key, delete_flg, created_by, created_at, updated_by, updated_at) VALUES
(311, 0, 2, 203, 1,  'TEXT',           '文本',       'opt.item_type.text',           1,  1, 1, '单行文本',     'opt.item_type.text.desc',           0, 1, 1711929600000, 1, 1711929600000),
(312, 0, 2, 203, 2,  'TEXTAREA',       '多行文本',   'opt.item_type.textarea',       2,  0, 1, '多行文本',     'opt.item_type.textarea.desc',       0, 1, 1711929600000, 1, 1711929600000),
(313, 0, 2, 203, 3,  'URL',            'URL',        'opt.item_type.url',            3,  0, 1, 'URL链接',      'opt.item_type.url.desc',            0, 1, 1711929600000, 1, 1711929600000),
(314, 0, 2, 203, 4,  'EMAIL',          '邮箱',       'opt.item_type.email',          4,  0, 1, '邮箱地址',     'opt.item_type.email.desc',          0, 1, 1711929600000, 1, 1711929600000),
(315, 0, 2, 203, 5,  'PHONE',          '电话',       'opt.item_type.phone',          5,  0, 1, '电话号码',     'opt.item_type.phone.desc',          0, 1, 1711929600000, 1, 1711929600000),
(316, 0, 2, 203, 6,  'PICKLIST',       '单选',       'opt.item_type.picklist',       6,  0, 1, '单选下拉',     'opt.item_type.picklist.desc',       0, 1, 1711929600000, 1, 1711929600000),
(317, 0, 2, 203, 7,  'MULTI_PICKLIST', '多选',       'opt.item_type.multi_picklist', 7,  0, 1, '多选下拉',     'opt.item_type.multi_picklist.desc', 0, 1, 1711929600000, 1, 1711929600000),
(318, 0, 2, 203, 8,  'NUMBER',         '数字',       'opt.item_type.number',         8,  0, 1, '数字',         'opt.item_type.number.desc',         0, 1, 1711929600000, 1, 1711929600000),
(319, 0, 2, 203, 9,  'CURRENCY',       '货币',       'opt.item_type.currency',       9,  0, 1, '货币金额',     'opt.item_type.currency.desc',       0, 1, 1711929600000, 1, 1711929600000),
(320, 0, 2, 203, 10, 'PERCENT',        '百分比',     'opt.item_type.percent',        10, 0, 1, '百分比',       'opt.item_type.percent.desc',        0, 1, 1711929600000, 1, 1711929600000),
(321, 0, 2, 203, 11, 'DATE',           '日期',       'opt.item_type.date',           11, 0, 1, '日期',         'opt.item_type.date.desc',           0, 1, 1711929600000, 1, 1711929600000),
(322, 0, 2, 203, 12, 'DATETIME',       '日期时间',   'opt.item_type.datetime',       12, 0, 1, '日期时间',     'opt.item_type.datetime.desc',       0, 1, 1711929600000, 1, 1711929600000),
(323, 0, 2, 203, 13, 'CHECKBOX',       '复选框',     'opt.item_type.checkbox',       13, 0, 1, '布尔复选框',   'opt.item_type.checkbox.desc',       0, 1, 1711929600000, 1, 1711929600000),
(324, 0, 2, 203, 19, 'LOOKUP',         '查找关系',   'opt.item_type.lookup',         14, 0, 1, 'LOOKUP关联',   'opt.item_type.lookup.desc',         0, 1, 1711929600000, 1, 1711929600000),
(325, 0, 2, 203, 20, 'MASTER_DETAIL',  '主从关系',   'opt.item_type.master_detail',  15, 0, 1, '主从关联',     'opt.item_type.master_detail.desc',  0, 1, 1711929600000, 1, 1711929600000),
(326, 0, 2, 203, 21, 'FORMULA',        '公式',       'opt.item_type.formula',        16, 0, 1, '公式计算字段', 'opt.item_type.formula.desc',        0, 1, 1711929600000, 1, 1711929600000),
(327, 0, 2, 203, 22, 'AUTO_NUMBER',    '自动编号',   'opt.item_type.auto_number',    17, 0, 1, '自动编号',     'opt.item_type.auto_number.desc',    0, 1, 1711929600000, 1, 1711929600000);

-- data_type 选项值 (metamodel_id=2, item_id=204)
INSERT INTO p_meta_option (id, tenant_id, metamodel_id, item_id, option_code, option_key, option_label, option_label_key, option_order, default_flg, enable_flg, description, description_key, delete_flg, created_by, created_at, updated_by, updated_at) VALUES
(341, 0, 2, 204, 1, 'VARCHAR',  '字符串',     'opt.data_type.varchar',  1, 1, 1, 'VARCHAR类型',  'opt.data_type.varchar.desc',  0, 1, 1711929600000, 1, 1711929600000),
(342, 0, 2, 204, 2, 'TEXT',     '长文本',     'opt.data_type.text',     2, 0, 1, 'TEXT类型',     'opt.data_type.text.desc',     0, 1, 1711929600000, 1, 1711929600000),
(343, 0, 2, 204, 3, 'INTEGER',  '整数',       'opt.data_type.integer',  3, 0, 1, 'INTEGER类型',  'opt.data_type.integer.desc',  0, 1, 1711929600000, 1, 1711929600000),
(344, 0, 2, 204, 4, 'DECIMAL',  '小数',       'opt.data_type.decimal',  4, 0, 1, 'DECIMAL类型',  'opt.data_type.decimal.desc',  0, 1, 1711929600000, 1, 1711929600000),
(345, 0, 2, 204, 5, 'BIGINT',   '长整数',     'opt.data_type.bigint',   5, 0, 1, 'BIGINT类型',   'opt.data_type.bigint.desc',   0, 1, 1711929600000, 1, 1711929600000),
(346, 0, 2, 204, 6, 'BOOLEAN',  '布尔',       'opt.data_type.boolean',  6, 0, 1, 'BOOLEAN类型',  'opt.data_type.boolean.desc',  0, 1, 1711929600000, 1, 1711929600000),
(347, 0, 2, 204, 7, 'DATE',     '日期',       'opt.data_type.date',     7, 0, 1, 'DATE类型',     'opt.data_type.date.desc',     0, 1, 1711929600000, 1, 1711929600000),
(348, 0, 2, 204, 8, 'DATETIME', '日期时间',   'opt.data_type.datetime', 8, 0, 1, 'DATETIME类型', 'opt.data_type.datetime.desc', 0, 1, 1711929600000, 1, 1711929600000);

-- ============================================================
-- 4. p_custom_entity：标准对象（Common 级，tenant_id=0）
-- ============================================================
INSERT INTO p_custom_entity (id, tenant_id, name_space, name, name_key, api_key, label, label_key, object_type, description, description_key, custom_entityseq, delete_flg, enable_flg, custom_flg, business_category, db_table, enable_sharing, enable_history_log, enable_report, enable_api, created_at, created_by, updated_at, updated_by) VALUES
(1001, 0, 'paas', 'Account',     'entity.account.name',     'Account__c',     '客户',     'entity.account.label',     1, '客户管理对象',     'entity.account.desc',     1, 0, 1, 0, 1, 'x_account',     1, 1, 1, 1, 1711929600000, 1, 1711929600000, 1),
(1002, 0, 'paas', 'Contact',     'entity.contact.name',     'Contact__c',     '联系人',   'entity.contact.label',     1, '联系人管理对象',   'entity.contact.desc',     2, 0, 1, 0, 1, 'x_contact',     1, 1, 1, 1, 1711929600000, 1, 1711929600000, 1),
(1003, 0, 'paas', 'Opportunity', 'entity.opportunity.name', 'Opportunity__c', '商机',     'entity.opportunity.label', 1, '商机管理对象',     'entity.opportunity.desc', 3, 0, 1, 0, 1, 'x_opportunity', 1, 1, 1, 1, 1711929600000, 1, 1711929600000, 1),
(1004, 0, 'paas', 'Lead',        'entity.lead.name',        'Lead__c',        '线索',     'entity.lead.label',        1, '线索管理对象',     'entity.lead.desc',        4, 0, 1, 0, 1, 'x_lead',        1, 1, 1, 1, 1711929600000, 1, 1711929600000, 1),
(1005, 0, 'paas', 'Product',     'entity.product.name',     'Product__c',     '产品',     'entity.product.label',     1, '产品管理对象',     'entity.product.desc',     5, 0, 1, 0, 1, 'x_product',     1, 1, 1, 1, 1711929600000, 1, 1711929600000, 1);

-- ============================================================
-- 5. p_custom_item：标准对象的字段
-- ============================================================

-- Account 字段
INSERT INTO p_custom_item (id, tenant_id, entity_id, name, name_key, api_key, label, label_key, item_type, data_type, description, description_key, require_flg, delete_flg, custom_flg, enable_flg, creatable, updatable, item_order, created_at, created_by, updated_at, updated_by) VALUES
(2001, 0, 1001, 'AccountName',     'item.account.name.name',     'Name__c',       '客户名称', 'item.account.name.label',     1, 1, '客户名称',     'item.account.name.desc',     1, 0, 0, 1, 1, 1, 1, 1711929600000, 1, 1711929600000, 1),
(2002, 0, 1001, 'AccountPhone',    'item.account.phone.name',    'Phone__c',      '电话',     'item.account.phone.label',    13, 1, '客户电话',     'item.account.phone.desc',    0, 0, 0, 1, 1, 1, 2, 1711929600000, 1, 1711929600000, 1),
(2003, 0, 1001, 'AccountIndustry', 'item.account.industry.name', 'Industry__c',   '行业',     'item.account.industry.label', 4, 3, '客户行业',     'item.account.industry.desc', 0, 0, 0, 1, 1, 1, 3, 1711929600000, 1, 1711929600000, 1),
(2004, 0, 1001, 'AccountStatus',   'item.account.status.name',   'Status__c',     '状态',     'item.account.status.label',   4, 3, '客户状态',     'item.account.status.desc',   1, 0, 0, 1, 1, 1, 4, 1711929600000, 1, 1711929600000, 1),
(2005, 0, 1001, 'AccountWebsite',  'item.account.website.name',  'Website__c',    '网站',     'item.account.website.label',  14, 1, '客户网站',     'item.account.website.desc',  0, 0, 0, 1, 1, 1, 5, 1711929600000, 1, 1711929600000, 1),
(2006, 0, 1001, 'AccountEmail',    'item.account.email.name',    'Email__c',      '邮箱',     'item.account.email.label',    12, 1, '客户邮箱',     'item.account.email.desc',    0, 0, 0, 1, 1, 1, 6, 1711929600000, 1, 1711929600000, 1),
(2007, 0, 1001, 'AccountAddress',  'item.account.address.name',  'Address__c',    '地址',     'item.account.address.label',  8, 1, '客户地址',     'item.account.address.desc',  0, 0, 0, 1, 1, 1, 7, 1711929600000, 1, 1711929600000, 1);

-- Contact 字段
INSERT INTO p_custom_item (id, tenant_id, entity_id, name, name_key, api_key, label, label_key, item_type, data_type, description, description_key, require_flg, delete_flg, custom_flg, enable_flg, creatable, updatable, item_order, refer_entity_id, created_at, created_by, updated_at, updated_by) VALUES
(2101, 0, 1002, 'ContactName',    'item.contact.name.name',    'Name__c',      '姓名',     'item.contact.name.label',    1,  1, '联系人姓名',   'item.contact.name.desc',    1, 0, 0, 1, 1, 1, 1, NULL, 1711929600000, 1, 1711929600000, 1),
(2102, 0, 1002, 'ContactEmail',   'item.contact.email.name',   'Email__c',     '邮箱',     'item.contact.email.label',   12, 1, '联系人邮箱',   'item.contact.email.desc',   0, 0, 0, 1, 1, 1, 2, NULL, 1711929600000, 1, 1711929600000, 1),
(2103, 0, 1002, 'ContactPhone',   'item.contact.phone.name',   'Phone__c',     '电话',     'item.contact.phone.label',   13, 1, '联系人电话',   'item.contact.phone.desc',   0, 0, 0, 1, 1, 1, 3, NULL, 1711929600000, 1, 1711929600000, 1),
(2104, 0, 1002, 'ContactAccount', 'item.contact.account.name', 'AccountId__c', '所属客户', 'item.contact.account.label', 5,  3, '所属客户关联', 'item.contact.account.desc', 0, 0, 0, 1, 1, 1, 4, 1001, 1711929600000, 1, 1711929600000, 1),
(2105, 0, 1002, 'ContactTitle',   'item.contact.title.name',   'Title__c',     '职位',     'item.contact.title.label',   1,  1, '联系人职位',   'item.contact.title.desc',   0, 0, 0, 1, 1, 1, 5, NULL, 1711929600000, 1, 1711929600000, 1);

-- Opportunity 字段
INSERT INTO p_custom_item (id, tenant_id, entity_id, name, name_key, api_key, label, label_key, item_type, data_type, description, description_key, require_flg, delete_flg, custom_flg, enable_flg, creatable, updatable, item_order, refer_entity_id, created_at, created_by, updated_at, updated_by) VALUES
(2201, 0, 1003, 'OppName',    'item.opp.name.name',    'Name__c',      '商机名称', 'item.opp.name.label',    1,  1, '商机名称',     'item.opp.name.desc',    1, 0, 0, 1, 1, 1, 1, NULL, 1711929600000, 1, 1711929600000, 1),
(2202, 0, 1003, 'OppAmount',  'item.opp.amount.name',  'Amount__c',    '金额',     'item.opp.amount.label',  10, 4, '商机金额',     'item.opp.amount.desc',  0, 0, 0, 1, 1, 1, 2, NULL, 1711929600000, 1, 1711929600000, 1),
(2203, 0, 1003, 'OppStage',   'item.opp.stage.name',   'Stage__c',     '阶段',     'item.opp.stage.label',   4,  3, '商机阶段',     'item.opp.stage.desc',   1, 0, 0, 1, 1, 1, 3, NULL, 1711929600000, 1, 1711929600000, 1),
(2204, 0, 1003, 'OppClose',   'item.opp.close.name',   'CloseDate__c', '预计成交', 'item.opp.close.label',   3,  3, '预计成交日期', 'item.opp.close.desc',   1, 0, 0, 1, 1, 1, 4, NULL, 1711929600000, 1, 1711929600000, 1),
(2205, 0, 1003, 'OppAccount', 'item.opp.account.name', 'AccountId__c', '关联客户', 'item.opp.account.label', 5,  3, '关联客户',     'item.opp.account.desc', 0, 0, 0, 1, 1, 1, 5, 1001, 1711929600000, 1, 1711929600000, 1);

-- ============================================================
-- 6. p_custom_pickoption：PICKLIST 字段的选项值
-- ============================================================

-- Account.Industry 选项
INSERT INTO p_custom_pickoption (id, tenant_id, entity_id, item_id, api_key, option_code, option_label, option_label_key, option_order, default_flg, global_flg, custom_flg, delete_flg, enable_flg, description, description_key, created_at, created_by, updated_at, updated_by) VALUES
(3001, 0, 1001, 2003, 'IT',            1, 'IT/互联网', 'pick.industry.it',            1, 0, 0, 0, 0, 1, 'IT和互联网行业', 'pick.industry.it.desc',            1711929600000, 1, 1711929600000, 1),
(3002, 0, 1001, 2003, 'FINANCE',       2, '金融',     'pick.industry.finance',       2, 0, 0, 0, 0, 1, '金融行业',       'pick.industry.finance.desc',       1711929600000, 1, 1711929600000, 1),
(3003, 0, 1001, 2003, 'MANUFACTURING', 3, '制造业',   'pick.industry.manufacturing', 3, 0, 0, 0, 0, 1, '制造业',         'pick.industry.manufacturing.desc', 1711929600000, 1, 1711929600000, 1),
(3004, 0, 1001, 2003, 'EDUCATION',     4, '教育',     'pick.industry.education',     4, 0, 0, 0, 0, 1, '教育行业',       'pick.industry.education.desc',     1711929600000, 1, 1711929600000, 1),
(3005, 0, 1001, 2003, 'HEALTHCARE',    5, '医疗',     'pick.industry.healthcare',    5, 0, 0, 0, 0, 1, '医疗行业',       'pick.industry.healthcare.desc',    1711929600000, 1, 1711929600000, 1);

-- Account.Status 选项
INSERT INTO p_custom_pickoption (id, tenant_id, entity_id, item_id, api_key, option_code, option_label, option_label_key, option_order, default_flg, global_flg, custom_flg, delete_flg, enable_flg, description, description_key, created_at, created_by, updated_at, updated_by) VALUES
(3011, 0, 1001, 2004, 'ACTIVE',    1, '活跃',   'pick.status.active',    1, 1, 0, 0, 0, 1, '活跃客户',   'pick.status.active.desc',    1711929600000, 1, 1711929600000, 1),
(3012, 0, 1001, 2004, 'INACTIVE',  2, '不活跃', 'pick.status.inactive',  2, 0, 0, 0, 0, 1, '不活跃客户', 'pick.status.inactive.desc',  1711929600000, 1, 1711929600000, 1),
(3013, 0, 1001, 2004, 'POTENTIAL', 3, '潜在',   'pick.status.potential', 3, 0, 0, 0, 0, 1, '潜在客户',   'pick.status.potential.desc', 1711929600000, 1, 1711929600000, 1);

-- Opportunity.Stage 选项
INSERT INTO p_custom_pickoption (id, tenant_id, entity_id, item_id, api_key, option_code, option_label, option_label_key, option_order, default_flg, global_flg, custom_flg, delete_flg, enable_flg, description, description_key, created_at, created_by, updated_at, updated_by) VALUES
(3021, 0, 1003, 2203, 'PROSPECTING',   1, '初步接触', 'pick.stage.prospecting',   1, 1, 0, 0, 0, 1, '初步接触阶段', 'pick.stage.prospecting.desc',   1711929600000, 1, 1711929600000, 1),
(3022, 0, 1003, 2203, 'QUALIFICATION', 2, '需求确认', 'pick.stage.qualification', 2, 0, 0, 0, 0, 1, '需求确认阶段', 'pick.stage.qualification.desc', 1711929600000, 1, 1711929600000, 1),
(3023, 0, 1003, 2203, 'PROPOSAL',      3, '方案报价', 'pick.stage.proposal',      3, 0, 0, 0, 0, 1, '方案报价阶段', 'pick.stage.proposal.desc',      1711929600000, 1, 1711929600000, 1),
(3024, 0, 1003, 2203, 'NEGOTIATION',   4, '商务谈判', 'pick.stage.negotiation',   4, 0, 0, 0, 0, 1, '商务谈判阶段', 'pick.stage.negotiation.desc',   1711929600000, 1, 1711929600000, 1),
(3025, 0, 1003, 2203, 'CLOSED_WON',    5, '赢单',     'pick.stage.closed_won',    5, 0, 0, 0, 0, 1, '赢单',         'pick.stage.closed_won.desc',    1711929600000, 1, 1711929600000, 1),
(3026, 0, 1003, 2203, 'CLOSED_LOST',   6, '输单',     'pick.stage.closed_lost',   6, 0, 0, 0, 0, 1, '输单',         'pick.stage.closed_lost.desc',   1711929600000, 1, 1711929600000, 1);

-- ============================================================
-- 7. p_custom_entity_link：对象关联关系
-- ============================================================
INSERT INTO p_custom_entity_link (id, tenant_id, name, name_key, api_key, label, label_key, link_type, parent_entity_id, child_entity_id, cascade_delete, access_control, enable_flg, description, description_key, delete_flg, created_at, created_by, updated_at, updated_by) VALUES
(4001, 0, 'ContactToAccount', 'link.contact_account.name', 'ContactToAccount__c', '联系人-客户', 'link.contact_account.label', 1, 1001, 1002, 0, 0, 1, '联系人到客户LOOKUP', 'link.contact_account.desc', 0, 1711929600000, 1, 1711929600000, 1),
(4002, 0, 'OppToAccount',     'link.opp_account.name',     'OppToAccount__c',     '商机-客户',   'link.opp_account.label',     1, 1001, 1003, 0, 0, 1, '商机到客户LOOKUP',   'link.opp_account.desc',     0, 1711929600000, 1, 1711929600000, 1);

-- ============================================================
-- 8. p_custom_check_rule：校验规则
-- ============================================================
INSERT INTO p_custom_check_rule (id, tenant_id, entity_id, name, name_key, api_key, rule_label, rule_label_key, active_flg, description, description_key, check_formula, check_error_msg, check_error_msg_key, check_error_location, check_all_items_flg, created_by, created_at, updated_by, updated_at) VALUES
(5001, 0, 1001, 'AccountNameRequired', 'rule.account_name.name', 'AccountNameRequired__c', '客户名称必填', 'rule.account_name.label', 1, '校验客户名称不能为空', 'rule.account_name.desc', 'NOT(ISBLANK(Name__c))', '客户名称不能为空', 'rule.account_name.error', 1, 0, 1, 1711929600000, 1, 1711929600000),
(5002, 0, 1003, 'OppCloseDateRequired', 'rule.opp_close.name', 'OppCloseDateRequired__c', '预计成交日期必填', 'rule.opp_close.label', 1, '校验商机预计成交日期', 'rule.opp_close.desc', 'NOT(ISBLANK(CloseDate__c))', '预计成交日期不能为空', 'rule.opp_close.error', 1, 0, 1, 1711929600000, 1, 1711929600000);

-- ============================================================
-- 9. 租户级自定义数据（tenant_id=1001）
-- ============================================================
INSERT INTO p_custom_item (id, tenant_id, entity_id, name, name_key, api_key, label, label_key, item_type, data_type, description, description_key, require_flg, delete_flg, custom_flg, enable_flg, creatable, updatable, item_order, created_at, created_by, updated_at, updated_by) VALUES
(6001, 1001, 1001, 'CustomRegion',   'item.custom_region.name',   'Region__c',       '区域',       'item.custom_region.label',   6, 3, '客户所在区域', 'item.custom_region.desc',   0, 0, 1, 1, 1, 1, 10, 1711929600000, 100, 1711929600000, 100),
(6002, 1001, 1001, 'CustomRevenue',  'item.custom_revenue.name',  'AnnualRevenue__c','年营收',     'item.custom_revenue.label',  9, 4, '客户年营收',   'item.custom_revenue.desc',  0, 0, 1, 1, 1, 1, 11, 1711929600000, 100, 1711929600000, 100),
(6003, 1001, 1001, 'CustomEmployee', 'item.custom_employee.name', 'Employees__c',    '员工数',     'item.custom_employee.label', 8, 3, '客户员工数量', 'item.custom_employee.desc', 0, 0, 1, 1, 1, 1, 12, 1711929600000, 100, 1711929600000, 100);
