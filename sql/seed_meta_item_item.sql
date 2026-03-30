-- item 元模型的 p_meta_item 字段定义（101 个字段）
-- 基于 METAREPO-METAMODEL-DESIGN.md 3.5.2 节完整字段列表

-- 基础信息（固定列映射）9 个
INSERT INTO p_meta_item (id, metamodel_api_key, api_key, namespace, label, label_key, item_type, data_type, db_column, item_order, require_flg, creatable, updatable, enable_package, delete_flg, created_by, created_at, updated_by, updated_at) VALUES
(20001, 'item', 'namespace',    'system', '命名空间',   'meta.item.namespace',     1, 1, 'namespace',      1,  0, 1, 0, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20002, 'item', 'entityApiKey', 'system', '所属对象',   'meta.item.entity_api_key',10, 3, 'entity_api_key', 2,  1, 1, 0, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20003, 'item', 'apiKey',       'system', '字段apiKey', 'meta.item.api_key',        1, 1, 'api_key',        3,  1, 1, 0, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20004, 'item', 'label',        'system', '显示标签',   'meta.item.label',          1, 1, 'label',          4,  1, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20005, 'item', 'labelKey',     'system', '多语言Key',  'meta.item.label_key',      1, 1, 'label_key',      5,  0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20006, 'item', 'name',         'system', '字段名称',   'meta.item.name',           1, 1, 'name',           6,  0, 1, 0, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20007, 'item', 'description',  'system', '描述',       'meta.item.description',    1, 1, 'description',    7,  0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20008, 'item', 'isCustom',     'system', '是否定制',   'meta.item.custom_flg',    31, 6, 'custom_flg',     8,  0, 1, 0, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20009, 'item', 'isDeleted',    'system', '删除标识',   'meta.item.delete_flg',    31, 6, 'delete_flg',     9,  0, 0, 0, 0, 0, 1, 1711929600000, 1, 1711929600000);

-- 核心属性（dbc 列映射）10 个
INSERT INTO p_meta_item (id, metamodel_api_key, api_key, namespace, label, label_key, item_type, data_type, db_column, item_order, require_flg, creatable, updatable, enable_package, delete_flg, created_by, created_at, updated_by, updated_at) VALUES
(20010, 'item', 'itemType',       'system', '字段数据类型',     'meta.item.item_type',        3, 3, 'dbc_int1',      10, 1, 1, 0, 1, 0, 1, 1711929600000, 1, 1711929600000),
(20011, 'item', 'dataType',       'system', '底层数据类型',     'meta.item.data_type',        5, 3, 'dbc_int2',      11, 0, 1, 0, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20012, 'item', 'itemOrder',      'system', '排序序号',         'meta.item.item_order',       5, 3, 'dbc_int3',      12, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20013, 'item', 'dbColumn',       'system', '数据库列名',       'meta.item.db_column',        1, 1, 'dbc_varchar3',  13, 0, 1, 0, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20014, 'item', 'helpText',       'system', '帮助文本',         'meta.item.help_text',        1, 1, 'dbc_varchar4',  14, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20015, 'item', 'helpTextKey',    'system', '帮助文本Key',      'meta.item.help_text_key',    1, 1, 'dbc_varchar5',  15, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20016, 'item', 'descriptionKey', 'system', '描述Key',          'meta.item.description_key',  1, 1, 'dbc_varchar6',  16, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20017, 'item', 'columnName',     'system', '列显示名',         'meta.item.column_name',      1, 1, 'dbc_varchar7',  17, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20018, 'item', 'defaultValue',   'system', '默认值',           'meta.item.default_value',    2, 1, 'dbc_textarea2', 18, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20019, 'item', 'typeProperty',   'system', '类型扩展属性JSON', 'meta.item.type_property',    2, 1, 'dbc_textarea1', 19, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000);

-- 权限控制 15 个
INSERT INTO p_meta_item (id, metamodel_api_key, api_key, namespace, label, label_key, item_type, data_type, db_column, item_order, require_flg, creatable, updatable, enable_package, delete_flg, created_by, created_at, updated_by, updated_at) VALUES
(20020, 'item', 'requireFlg',           'system', '是否必填',       'meta.item.require_flg',            31, 6, 'dbc_smallint1',  20, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20021, 'item', 'enableFlg',            'system', '是否启用',       'meta.item.enable_flg',             31, 6, 'dbc_smallint2',  21, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20022, 'item', 'hiddenFlg',            'system', '是否隐藏',       'meta.item.hidden_flg',             31, 6, 'dbc_smallint3',  22, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20023, 'item', 'uniqueKeyFlg',         'system', '是否唯一键',     'meta.item.unique_key_flg',         31, 6, 'dbc_smallint4',  23, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20024, 'item', 'creatable',            'system', '新建时可赋值',   'meta.item.creatable',              31, 6, 'dbc_smallint5',  24, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20025, 'item', 'updatable',            'system', '可更新',         'meta.item.updatable',              31, 6, 'dbc_smallint6',  25, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20026, 'item', 'enableHistoryLog',     'system', '历史记录跟踪',   'meta.item.enable_history_log',     31, 6, 'dbc_smallint7',  26, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20027, 'item', 'enableDeactive',       'system', '允许禁用',       'meta.item.enable_deactive',        31, 6, 'dbc_smallint8',  27, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20028, 'item', 'readonlyStatus',       'system', '只读状态',       'meta.item.readonly_status',         3, 3, 'dbc_int4',       28, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20029, 'item', 'visibleStatus',        'system', '可见状态',       'meta.item.visible_status',          3, 3, 'dbc_int5',       29, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20030, 'item', 'sortFlg',              'system', '允许排序',       'meta.item.sort_flg',               31, 6, 'dbc_int6',       30, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20031, 'item', 'encrypt',              'system', '加密字段',       'meta.item.encrypt',                31, 6, 'dbc_smallint10', 31, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20032, 'item', 'markdown',             'system', 'Markdown编辑器', 'meta.item.markdown',               31, 6, 'dbc_smallint11', 32, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20033, 'item', 'referItemFilterEnable','system', '关联字段增强过滤','meta.item.refer_item_filter_enable',31,6, 'dbc_smallint12', 33, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20034, 'item', 'globalSearch',         'system', '忽略权限检索',   'meta.item.global_search',          31, 6, 'dbc_smallint39', 34, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000);

-- 关联/LOOKUP 相关 16 个
INSERT INTO p_meta_item (id, metamodel_api_key, api_key, namespace, label, label_key, item_type, data_type, db_column, item_order, require_flg, creatable, updatable, enable_package, delete_flg, created_by, created_at, updated_by, updated_at) VALUES
(20035, 'item', 'referEntityApiKey',   'system', '关联对象apiKey',     'meta.item.refer_entity_api_key',  10, 3, 'dbc_varchar1',   35, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20036, 'item', 'referLinkApiKey',     'system', '关联Link apiKey',    'meta.item.refer_link_api_key',    10, 3, 'dbc_varchar2',   36, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20037, 'item', 'cascadeDelete',       'system', '级联删除规则',       'meta.item.cascade_delete',         3, 3, 'dbc_smallint22', 37, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20038, 'item', 'accessControl',       'system', '访问控制规则',       'meta.item.access_control',         3, 3, 'dbc_smallint37', 38, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20039, 'item', 'isDetail',            'system', '是否明细实体',       'meta.item.is_detail',             31, 6, 'dbc_smallint23', 39, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20040, 'item', 'canBatchCreate',      'system', '允许批量新建',       'meta.item.can_batch_create',      31, 6, 'dbc_smallint24', 40, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20041, 'item', 'isCopyWithParent',    'system', '随父复制',           'meta.item.is_copy_with_parent',   31, 6, 'dbc_smallint25', 41, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20042, 'item', 'batchCreateBaseLink', 'system', '批量新建依据',       'meta.item.batch_create_base_link',10, 3, 'dbc_smallint46', 42, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20043, 'item', 'joinItem',            'system', '引用字段',           'meta.item.join_item',             10, 3, 'dbc_varchar12',  43, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20044, 'item', 'joinObject',          'system', '引用实体',           'meta.item.join_object',           10, 3, 'dbc_varchar13',  44, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20045, 'item', 'joinLink',            'system', '引用Link',           'meta.item.join_link',             10, 3, 'dbc_varchar14',  45, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20046, 'item', 'joinItemKey',         'system', '引用字段属性名',     'meta.item.join_item_key',          1, 1, 'dbc_varchar21',  46, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20047, 'item', 'linkLabel',           'system', '关联标签',           'meta.item.link_label',             1, 1, 'dbc_varchar15',  47, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20048, 'item', 'referEntityApiKeys',  'system', '多态引用实体列表',   'meta.item.refer_entity_api_keys',  1, 1, 'dbc_varchar16',  48, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20049, 'item', 'entityOrData',        'system', '多态属性标识',       'meta.item.entity_or_data',         5, 3, 'dbc_smallint27', 49, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20050, 'item', 'groupKey',            'system', '多态分组Key',        'meta.item.group_key',              1, 1, 'dbc_varchar17',  50, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000);

-- 选项集相关 4 个
INSERT INTO p_meta_item (id, metamodel_api_key, api_key, namespace, label, label_key, item_type, data_type, db_column, item_order, require_flg, creatable, updatable, enable_package, delete_flg, created_by, created_at, updated_by, updated_at) VALUES
(20051, 'item', 'referGlobal',          'system', '引用全局选项集',   'meta.item.refer_global',            31, 6, 'dbc_smallint19', 51, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20052, 'item', 'globalPickItem',       'system', '全局选项集apiKey', 'meta.item.global_pick_item',         1, 1, 'dbc_varchar10',  52, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20053, 'item', 'globalPickItemApiKey', 'system', '全局选项集apiKey', 'meta.item.global_pick_item_api_key', 1, 1, 'dbc_varchar11',  53, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20054, 'item', 'isExternal',           'system', '外部选项源',       'meta.item.is_external',             31, 6, 'dbc_smallint20', 54, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000);

-- 货币相关 5 个
INSERT INTO p_meta_item (id, metamodel_api_key, api_key, namespace, label, label_key, item_type, data_type, db_column, item_order, require_flg, creatable, updatable, enable_package, delete_flg, created_by, created_at, updated_by, updated_at) VALUES
(20055, 'item', 'isCurrency',                  'system', '是否货币',       'meta.item.is_currency',                     31, 6, 'dbc_smallint13', 55, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20056, 'item', 'currencyPart',                'system', '货币组成',       'meta.item.currency_part',                    3, 3, 'dbc_smallint14', 56, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20057, 'item', 'isMultiCurrency',             'system', '多币种',         'meta.item.is_multi_currency',               31, 6, 'dbc_smallint15', 57, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20058, 'item', 'isComputeMultiCurrencyUnit',  'system', '展示币种信息',   'meta.item.is_compute_multi_currency_unit',   1, 1, 'dbc_varchar8',   58, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20059, 'item', 'isComputeMultiCurrency',      'system', '计算多货币',     'meta.item.is_compute_multi_currency',       31, 6, 'dbc_varchar26',  59, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000);

-- 日期相关 1 个
INSERT INTO p_meta_item (id, metamodel_api_key, api_key, namespace, label, label_key, item_type, data_type, db_column, item_order, require_flg, creatable, updatable, enable_package, delete_flg, created_by, created_at, updated_by, updated_at) VALUES
(20060, 'item', 'dateMode', 'system', '日期模式', 'meta.item.date_mode', 3, 3, 'dbc_smallint21', 60, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000);

-- 公式/汇总相关 3 个
INSERT INTO p_meta_item (id, metamodel_api_key, api_key, namespace, label, label_key, item_type, data_type, db_column, item_order, require_flg, creatable, updatable, enable_package, delete_flg, created_by, created_at, updated_by, updated_at) VALUES
(20061, 'item', 'computeType',          'system', '计算结果子类型',   'meta.item.compute_type',           3, 3, 'dbc_smallint17', 61, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20062, 'item', 'realTimeCompute',      'system', '实时计算',         'meta.item.real_time_compute',     31, 6, 'dbc_smallint18', 62, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20063, 'item', 'aggregateComputeType', 'system', '汇总计算结果类型', 'meta.item.aggregate_compute_type', 3, 3, 'dbc_varchar29',  63, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000);

-- 自动编号相关 5 个
INSERT INTO p_meta_item (id, metamodel_api_key, api_key, namespace, label, label_key, item_type, data_type, db_column, item_order, require_flg, creatable, updatable, enable_package, delete_flg, created_by, created_at, updated_by, updated_at) VALUES
(20064, 'item', 'format',              'system', '编号格式',       'meta.item.format',               1, 1, 'dbc_varchar9',   64, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20065, 'item', 'startNumber',         'system', '起始值',         'meta.item.start_number',          1, 1, 'dbc_varchar23',  65, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20066, 'item', 'incrementStrategy',   'system', '递增策略',       'meta.item.increment_strategy',    5, 3, 'dbc_int12',      66, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20067, 'item', 'isRebuild',           'system', '重建编号',       'meta.item.is_rebuild',           31, 6, 'dbc_smallint36', 67, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20068, 'item', 'dataFormat',          'system', '编号数据格式',   'meta.item.data_format',           1, 1, 'dbc_varchar22',  68, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000);

-- 文本/长度相关 5 个
INSERT INTO p_meta_item (id, metamodel_api_key, api_key, namespace, label, label_key, item_type, data_type, db_column, item_order, require_flg, creatable, updatable, enable_package, delete_flg, created_by, created_at, updated_by, updated_at) VALUES
(20069, 'item', 'maxLength',     'system', '最大长度',   'meta.item.max_length',      5, 3, 'dbc_int13',      69, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20070, 'item', 'minLength',     'system', '最小长度',   'meta.item.min_length',      5, 3, 'dbc_int14',      70, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20071, 'item', 'decimal',       'system', '小数位数',   'meta.item.decimal',         5, 3, 'dbc_int15',      71, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20072, 'item', 'textFormat',    'system', '文本格式',   'meta.item.text_format',      1, 1, 'dbc_smallint41', 72, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20073, 'item', 'multiLineText', 'system', '多行文本',   'meta.item.multi_line_text', 31, 6, 'dbc_smallint29', 73, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000);

-- 掩码相关 6 个
INSERT INTO p_meta_item (id, metamodel_api_key, api_key, namespace, label, label_key, item_type, data_type, db_column, item_order, require_flg, creatable, updatable, enable_package, delete_flg, created_by, created_at, updated_by, updated_at) VALUES
(20074, 'item', 'isMask',         'system', '是否掩码',       'meta.item.is_mask',          31, 6, 'dbc_smallint26', 74, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20075, 'item', 'maskType',       'system', '掩码格式类型',   'meta.item.mask_type',         3, 3, 'dbc_smallint40', 75, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20076, 'item', 'maskSymbolType', 'system', '掩码字符类型',   'meta.item.mask_symbol_type',  3, 3, 'dbc_int11',      76, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20077, 'item', 'maskPrefix',     'system', '掩码前缀字符数', 'meta.item.mask_prefix',       5, 3, 'dbc_int7',       77, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20078, 'item', 'maskSuffix',     'system', '掩码后缀字符数', 'meta.item.mask_suffix',       5, 3, 'dbc_int8',       78, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20079, 'item', 'syncMask',       'system', '掩码同步',       'meta.item.sync_mask',        31, 6, 'dbc_varchar27',  79, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000);

-- 图片水印相关 7 个
INSERT INTO p_meta_item (id, metamodel_api_key, api_key, namespace, label, label_key, item_type, data_type, db_column, item_order, require_flg, creatable, updatable, enable_package, delete_flg, created_by, created_at, updated_by, updated_at) VALUES
(20080, 'item', 'watermarkFlg',              'system', '水印方式',         'meta.item.watermark_flg',               3, 3, 'dbc_smallint30', 80, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20081, 'item', 'uploadFlg',                 'system', '上传方式',         'meta.item.upload_flg',                  3, 3, 'dbc_smallint38', 81, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20082, 'item', 'enableWatermarkTime',       'system', '水印时间',         'meta.item.enable_watermark_time',      31, 6, 'dbc_smallint31', 82, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20083, 'item', 'enableWatermarkLoginUser',  'system', '水印登录用户',     'meta.item.enable_watermark_login_user',31, 6, 'dbc_smallint32', 83, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20084, 'item', 'enableWatermarkLocation',   'system', '水印定位',         'meta.item.enable_watermark_location',  31, 6, 'dbc_smallint33', 84, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20085, 'item', 'enableWatermarkJoinField',  'system', '水印引用字段开关', 'meta.item.enable_watermark_join_field',31, 6, 'dbc_varchar20',  85, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20086, 'item', 'watermarkJoinField',        'system', '水印引用字段apiKey','meta.item.watermark_join_field',        1, 1, 'dbc_varchar19',  86, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000);

-- 地理位置相关 2 个
INSERT INTO p_meta_item (id, metamodel_api_key, api_key, namespace, label, label_key, item_type, data_type, db_column, item_order, require_flg, creatable, updatable, enable_package, delete_flg, created_by, created_at, updated_by, updated_at) VALUES
(20087, 'item', 'displayFormat', 'system', '地理定位显示格式', 'meta.item.display_format', 3, 3, 'dbc_varchar24', 87, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20088, 'item', 'locationType',  'system', '地理位置类型',     'meta.item.location_type',  5, 3, 'dbc_varchar25', 88, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000);

-- 复合字段相关 3 个
INSERT INTO p_meta_item (id, metamodel_api_key, api_key, namespace, label, label_key, item_type, data_type, db_column, item_order, require_flg, creatable, updatable, enable_package, delete_flg, created_by, created_at, updated_by, updated_at) VALUES
(20089, 'item', 'compound',       'system', '是否复合字段',   'meta.item.compound',         31, 6, 'dbc_smallint9',  89, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20090, 'item', 'compoundSub',    'system', '复合子字段',     'meta.item.compound_sub',     31, 6, 'dbc_smallint28', 90, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20091, 'item', 'compoundApiKey', 'system', '复合字段apiKey', 'meta.item.compound_api_key',  1, 1, 'dbc_varchar18',  91, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000);

-- 索引相关 2 个
INSERT INTO p_meta_item (id, metamodel_api_key, api_key, namespace, label, label_key, item_type, data_type, db_column, item_order, require_flg, creatable, updatable, enable_package, delete_flg, created_by, created_at, updated_by, updated_at) VALUES
(20092, 'item', 'indexType',  'system', '索引类型', 'meta.item.index_type',  5, 3, 'dbc_int10', 92, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20093, 'item', 'indexOrder', 'system', '索引顺序', 'meta.item.index_order', 5, 3, 'dbc_int9',  93, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000);

-- 其他 8 个
INSERT INTO p_meta_item (id, metamodel_api_key, api_key, namespace, label, label_key, item_type, data_type, db_column, item_order, require_flg, creatable, updatable, enable_package, delete_flg, created_by, created_at, updated_by, updated_at) VALUES
(20094, 'item', 'customItemSeq', 'system', '字段排序号',   'meta.item.custom_item_seq', 5, 3, 'dbc_bigint1',  94, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20095, 'item', 'enableConfig',  'system', '配置位掩码',   'meta.item.enable_config',   5, 3, 'dbc_bigint2',  95, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20096, 'item', 'enablePackage', 'system', '包配置位掩码', 'meta.item.enable_package',  5, 3, 'dbc_bigint3',  96, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20097, 'item', 'unique',        'system', '唯一约束',     'meta.item.unique',         31, 6, 'dbc_varchar28', 97, 0, 1, 1, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20098, 'item', 'createdBy',     'system', '创建人',       'meta.item.created_by',      5, 3, 'created_by',   98, 0, 0, 0, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20099, 'item', 'createdAt',     'system', '创建时间',     'meta.item.created_at',      5, 3, 'created_at',   99, 0, 0, 0, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20100, 'item', 'updatedBy',     'system', '修改人',       'meta.item.updated_by',      5, 3, 'updated_by',  100, 0, 0, 0, 0, 0, 1, 1711929600000, 1, 1711929600000),
(20101, 'item', 'updatedAt',     'system', '修改时间',     'meta.item.updated_at',      5, 3, 'updated_at',  101, 0, 0, 0, 0, 0, 1, 1711929600000, 1, 1711929600000);
