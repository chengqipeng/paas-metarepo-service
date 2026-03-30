-- entity 元模型的 p_meta_item 字段定义（16 个字段）
INSERT INTO p_meta_item (id, metamodel_api_key, api_key, namespace, label, label_key, item_type, data_type, db_column, item_order, require_flg, creatable, updatable, enable_package, delete_flg, created_by, created_at, updated_by, updated_at) VALUES
(10001, 'entity', 'svg_api_key',            'system', 'SVG图标',       'meta.entity.svg_api_key',            1, 1, 'dbc_varchar1',  1,  0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(10002, 'entity', 'db_table',               'system', '数据库表名',   'meta.entity.db_table',               1, 1, 'dbc_varchar2',  2,  1, 1, 0, 0, 0, 1, 1711929600000, 1, 1711929600000),
(10003, 'entity', 'entity_type',            'system', '对象类型',     'meta.entity.entity_type',            3, 3, 'dbc_int1',      3,  0, 1, 0, 1, 0, 1, 1711929600000, 1, 1711929600000),
(10004, 'entity', 'custom_entity_seq',      'system', '对象排序号',   'meta.entity.custom_entity_seq',      5, 3, 'dbc_int2',      4,  0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(10005, 'entity', 'business_category',      'system', '业务分类',     'meta.entity.business_category',      3, 3, 'dbc_int3',      5,  0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(10006, 'entity', 'enable_flg',             'system', '启用标记',     'meta.entity.enable_flg',            31, 6, 'dbc_smallint3', 6,  0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(10007, 'entity', 'custom_flg',             'system', '自定义标记',   'meta.entity.custom_flg',            31, 6, 'dbc_smallint4', 7,  0, 1, 0, 0, 0, 1, 1711929600000, 1, 1711929600000),
(10008, 'entity', 'enable_history_log',     'system', '启用历史日志', 'meta.entity.enable_history_log',    31, 6, 'dbc_smallint5', 8,  0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(10009, 'entity', 'enable_config',          'system', '启用配置位',   'meta.entity.enable_config',          5, 3, 'dbc_bigint1',   9,  0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(10010, 'entity', 'enable_busitype',        'system', '启用业务类型', 'meta.entity.enable_busitype',       31, 6, 'dbc_smallint1', 10, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(10011, 'entity', 'enable_checkrule',       'system', '启用校验规则', 'meta.entity.enable_checkrule',      31, 6, 'dbc_smallint2', 11, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(10012, 'entity', 'enable_duplicaterule',   'system', '启用查重规则', 'meta.entity.enable_duplicaterule',   3, 3, 'dbc_int4',      12, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(10013, 'entity', 'enable_script_executor', 'system', '启用脚本执行器','meta.entity.enable_script_executor',3, 3, 'dbc_int5',      13, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(10014, 'entity', 'is_archived',            'system', '已归档',       'meta.entity.is_archived',            3, 3, 'dbc_int6',      14, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(10015, 'entity', 'enable_group_member',    'system', '启用组成员',   'meta.entity.enable_group_member',    3, 3, 'dbc_int7',      15, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(10016, 'entity', 'enable_dynamic_feed',    'system', '启用动态',     'meta.entity.enable_dynamic_feed',    3, 3, 'dbc_int8',      16, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000);

-- entityLink 元模型的 p_meta_item 字段定义（9 个字段）
INSERT INTO p_meta_item (id, metamodel_api_key, api_key, namespace, label, label_key, item_type, data_type, db_column, item_order, require_flg, creatable, updatable, enable_package, delete_flg, created_by, created_at, updated_by, updated_at) VALUES
(30001, 'entityLink', 'type_property',         'system', '类型属性', 'meta.link.type_property',         1, 1, 'dbc_varchar_1',  1, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(30002, 'entityLink', 'parent_entity_api_key', 'system', '父对象',   'meta.link.parent_entity_api_key', 1, 1, 'dbc_varchar_2',  2, 1, 1, 0, 0, 0, 1, 1711929600000, 1, 1711929600000),
(30003, 'entityLink', 'child_entity_api_key',  'system', '子对象',   'meta.link.child_entity_api_key',  1, 1, 'dbc_varchar_3',  3, 1, 1, 0, 0, 0, 1, 1711929600000, 1, 1711929600000),
(30004, 'entityLink', 'description_key',       'system', '描述Key',  'meta.link.description_key',       1, 1, 'dbc_varchar_4',  4, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(30005, 'entityLink', 'link_type',             'system', '关联类型', 'meta.link.link_type',             3, 3, 'dbc_int_1',      5, 1, 1, 0, 1, 0, 1, 1711929600000, 1, 1711929600000),
(30006, 'entityLink', 'detail_link',           'system', '明细关联', 'meta.link.detail_link',          31, 6, 'dbc_smallint_1', 6, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(30007, 'entityLink', 'cascade_delete',        'system', '级联删除', 'meta.link.cascade_delete',        3, 3, 'dbc_smallint_2', 7, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(30008, 'entityLink', 'access_control',        'system', '访问控制', 'meta.link.access_control',        3, 3, 'dbc_smallint_3', 8, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(30009, 'entityLink', 'enable_flg',            'system', '启用标记', 'meta.link.enable_flg',           31, 6, 'dbc_smallint_4', 9, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000);

-- checkRule 元模型的 p_meta_item 字段定义（18 个字段）
INSERT INTO p_meta_item (id, metamodel_api_key, api_key, namespace, label, label_key, item_type, data_type, db_column, item_order, require_flg, creatable, updatable, enable_package, delete_flg, created_by, created_at, updated_by, updated_at) VALUES
(40001, 'checkRule', 'namespace',              'system', '命名空间',         'meta.rule.namespace',              1, 1, 'namespace',                1,  0, 1, 0, 0, 0, 1, 1711929600000, 1, 1711929600000),
(40002, 'checkRule', 'entityApiKey',           'system', '所属对象apiKey',   'meta.rule.entity_api_key',        10, 3, 'entity_api_key',           2,  1, 1, 0, 0, 0, 1, 1711929600000, 1, 1711929600000),
(40003, 'checkRule', 'apiKey',                 'system', '规则apiKey',       'meta.rule.api_key',                1, 1, 'api_key',                  3,  1, 1, 0, 0, 0, 1, 1711929600000, 1, 1711929600000),
(40004, 'checkRule', 'description',            'system', '描述',             'meta.rule.description',            1, 1, 'description',              4,  0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(40005, 'checkRule', 'label',                  'system', '规则标签',         'meta.rule.label',                  1, 1, 'label',                    5,  1, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(40006, 'checkRule', 'labelKey',               'system', '规则标签Key',      'meta.rule.label_key',              1, 1, 'label_key',                6,  0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(40007, 'checkRule', 'activeFlg',              'system', '激活状态',         'meta.rule.active_flg',             3, 3, 'active_flg',               7,  0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(40008, 'checkRule', 'checkFormula',           'system', '校验公式',         'meta.rule.check_formula',          1, 1, 'check_formula',            8,  1, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(40009, 'checkRule', 'checkErrorMsg',          'system', '错误提示信息',     'meta.rule.check_error_msg',        1, 1, 'check_error_msg',          9,  0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(40010, 'checkRule', 'checkErrorMsgKey',       'system', '错误提示Key',      'meta.rule.check_error_msg_key',    1, 1, 'check_error_msg_key',     10,  0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(40011, 'checkRule', 'checkErrorLocation',     'system', '错误显示位置',     'meta.rule.check_error_location',   5, 3, 'check_error_location',    11,  0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(40012, 'checkRule', 'checkErrorWay',          'system', '弱校验错误类型',   'meta.rule.check_error_way',        5, 3, 'check_error_way',         12,  0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(40013, 'checkRule', 'checkErrorItemApiKey',   'system', '错误关联字段apiKey','meta.rule.check_error_item_api_key',1,1,'check_error_item_api_key', 13,  0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(40014, 'checkRule', 'checkAllItemsFlg',       'system', '全量更新标识',     'meta.rule.check_all_items_flg',    3, 3, 'check_all_items_flg',     14,  0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(40015, 'checkRule', 'createdBy',              'system', '创建人',           'meta.rule.created_by',             5, 3, 'created_by',              15,  0, 0, 0, 0, 0, 1, 1711929600000, 1, 1711929600000),
(40016, 'checkRule', 'createdAt',              'system', '创建时间',         'meta.rule.created_at',             5, 3, 'created_at',              16,  0, 0, 0, 0, 0, 1, 1711929600000, 1, 1711929600000),
(40017, 'checkRule', 'updatedBy',              'system', '修改人',           'meta.rule.updated_by',             5, 3, 'updated_by',              17,  0, 0, 0, 0, 0, 1, 1711929600000, 1, 1711929600000),
(40018, 'checkRule', 'updatedAt',              'system', '修改时间',         'meta.rule.updated_at',             5, 3, 'updated_at',              18,  0, 0, 0, 0, 0, 1, 1711929600000, 1, 1711929600000);

-- pickOption 元模型的 p_meta_item 字段定义（19 个字段）
INSERT INTO p_meta_item (id, metamodel_api_key, api_key, namespace, label, label_key, item_type, data_type, db_column, item_order, require_flg, creatable, updatable, enable_package, delete_flg, created_by, created_at, updated_by, updated_at) VALUES
(50001, 'pickOption', 'namespace',      'system', '命名空间',       'meta.pick.namespace',       1, 1, 'namespace',       1,  0, 1, 0, 0, 0, 1, 1711929600000, 1, 1711929600000),
(50002, 'pickOption', 'entityApiKey',   'system', '所属对象apiKey', 'meta.pick.entity_api_key', 10, 3, 'entity_api_key',  2,  1, 1, 0, 0, 0, 1, 1711929600000, 1, 1711929600000),
(50003, 'pickOption', 'itemApiKey',     'system', '所属字段apiKey', 'meta.pick.item_api_key',   10, 3, 'item_api_key',    3,  1, 1, 0, 0, 0, 1, 1711929600000, 1, 1711929600000),
(50004, 'pickOption', 'apiKey',       'system', '选项apiKey',     'meta.pick.api_key',         1, 1, 'api_key',         4,  1, 1, 0, 0, 0, 1, 1711929600000, 1, 1711929600000),
(50005, 'pickOption', 'label',        'system', '选项标签',       'meta.pick.label',           1, 1, 'label',           5,  1, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(50006, 'pickOption', 'labelKey',     'system', '选项标签Key',    'meta.pick.label_key',       1, 1, 'label_key',       6,  0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(50007, 'pickOption', 'optionOrder',    'system', '排序序号',       'meta.pick.option_order',    5, 3, 'option_order',    7,  0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(50008, 'pickOption', 'isDefault',      'system', '是否默认',       'meta.pick.default_flg',    31, 6, 'default_flg',     8,  0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(50009, 'pickOption', 'isGlobal',       'system', '是否全局',       'meta.pick.global_flg',     31, 6, 'global_flg',      9,  0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(50010, 'pickOption', 'isCustom',       'system', '是否定制',       'meta.pick.custom_flg',     31, 6, 'custom_flg',     10,  0, 1, 0, 0, 0, 1, 1711929600000, 1, 1711929600000),
(50011, 'pickOption', 'isDeleted',      'system', '是否删除',       'meta.pick.delete_flg',     31, 6, 'delete_flg',     11,  0, 0, 0, 0, 0, 1, 1711929600000, 1, 1711929600000),
(50012, 'pickOption', 'isActive',       'system', '是否启用',       'meta.pick.enable_flg',     31, 6, 'enable_flg',     12,  0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(50013, 'pickOption', 'description',    'system', '描述',           'meta.pick.description',     1, 1, 'description',    13,  0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(50014, 'pickOption', 'specialFlg',     'system', '特殊标志',       'meta.pick.special_flg',     5, 3, 'special_flg',    14,  0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(50015, 'pickOption', 'optionType',     'system', '选项类型',       'meta.pick.option_type',     5, 3, NULL,             15,  0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(50016, 'pickOption', 'createdBy',      'system', '创建人',         'meta.pick.created_by',      5, 3, 'created_by',     16,  0, 0, 0, 0, 0, 1, 1711929600000, 1, 1711929600000),
(50017, 'pickOption', 'createdAt',      'system', '创建时间',       'meta.pick.created_at',      5, 3, 'created_at',     17,  0, 0, 0, 0, 0, 1, 1711929600000, 1, 1711929600000),
(50018, 'pickOption', 'updatedBy',      'system', '修改人',         'meta.pick.updated_by',      5, 3, 'updated_by',     18,  0, 0, 0, 0, 0, 1, 1711929600000, 1, 1711929600000),
(50019, 'pickOption', 'updatedAt',      'system', '修改时间',       'meta.pick.updated_at',      5, 3, 'updated_at',     19,  0, 0, 0, 0, 0, 1, 1711929600000, 1, 1711929600000);

-- referenceFilter 元模型的 p_meta_item 字段定义（12 个字段）
INSERT INTO p_meta_item (id, metamodel_api_key, api_key, namespace, label, label_key, item_type, data_type, db_column, item_order, require_flg, creatable, updatable, enable_package, delete_flg, created_by, created_at, updated_by, updated_at) VALUES
(60001, 'referenceFilter', 'namespace',     'system', '命名空间',       'meta.ref.namespace',      1, 1, 'namespace',       1,  0, 1, 0, 0, 0, 1, 1711929600000, 1, 1711929600000),
(60002, 'referenceFilter', 'entityApiKey',  'system', '所属对象apiKey', 'meta.ref.entity_api_key',10, 3, 'entity_api_key',  2,  1, 1, 0, 0, 0, 1, 1711929600000, 1, 1711929600000),
(60003, 'referenceFilter', 'itemApiKey',    'system', '所属字段apiKey', 'meta.ref.item_api_key',  10, 3, 'item_api_key',    3,  1, 1, 0, 0, 0, 1, 1711929600000, 1, 1711929600000),
(60004, 'referenceFilter', 'filterMode',    'system', '过滤模式',       'meta.ref.filter_mode',    3, 3, 'filter_mode',     4,  0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(60005, 'referenceFilter', 'filterFormula', 'system', '过滤表达式',     'meta.ref.filter_formula', 1, 1, 'filter_formula',  5,  0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(60006, 'referenceFilter', 'isActive',      'system', '是否启用',       'meta.ref.active_flg',    31, 6, 'active_flg',      6,  0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(60007, 'referenceFilter', 'andor',         'system', '操作符',         'meta.ref.andor',          5, 3, 'andor',           7,  0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(60008, 'referenceFilter', 'isDeleted',     'system', '删除标识',       'meta.ref.delete_flg',    31, 6, 'delete_flg',      8,  0, 0, 0, 0, 0, 1, 1711929600000, 1, 1711929600000),
(60009, 'referenceFilter', 'createdBy',     'system', '创建人',         'meta.ref.created_by',     5, 3, 'created_by',      9,  0, 0, 0, 0, 0, 1, 1711929600000, 1, 1711929600000),
(60010, 'referenceFilter', 'createdAt',     'system', '创建时间',       'meta.ref.created_at',     5, 3, 'created_at',     10,  0, 0, 0, 0, 0, 1, 1711929600000, 1, 1711929600000),
(60011, 'referenceFilter', 'updatedBy',     'system', '修改人',         'meta.ref.updated_by',     5, 3, 'updated_by',     11,  0, 0, 0, 0, 0, 1, 1711929600000, 1, 1711929600000),
(60012, 'referenceFilter', 'updatedAt',     'system', '修改时间',       'meta.ref.updated_at',     5, 3, 'updated_at',     12,  0, 0, 0, 0, 0, 1, 1711929600000, 1, 1711929600000);
