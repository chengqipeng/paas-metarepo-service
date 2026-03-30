-- ============================================================
-- 修复 p_meta_model 和 p_meta_item 数据
--
-- 问题1: p_meta_model.api_key 使用 'CustomEntity'/'CustomItem' 等旧名称，
--        但 Java MetamodelApiKey 常量使用 'entity'/'item' 等新名称
-- 问题2: p_meta_item.db_column 使用了不存在的列类型（dbc_select_*, dbc_tinyint_*），
--        实际大宽表只有 dbc_varchar/textarea/bigint/int/smallint/decimal
-- 问题3: p_meta_item 只定义了少量字段，缺少 Entity 类的大部分字段映射
--
-- 执行方式：
--   mysql -h <host> -u root -p paas_metarepo < fix_meta_item_mapping.sql
-- ============================================================

-- ============================================================
-- Step 1: 更新 p_meta_model.api_key（旧名 → 新名）
-- ============================================================
UPDATE p_meta_model SET api_key = 'entity'          WHERE api_key = 'CustomEntity';
UPDATE p_meta_model SET api_key = 'item'             WHERE api_key = 'CustomItem';
UPDATE p_meta_model SET api_key = 'entityLink'       WHERE api_key = 'CustomEntityLink';
UPDATE p_meta_model SET api_key = 'checkRule'        WHERE api_key = 'CustomCheckRule';
UPDATE p_meta_model SET api_key = 'pickOption'       WHERE api_key = 'CustomPickOption';
UPDATE p_meta_model SET api_key = 'referenceFilter'  WHERE api_key = 'ReferFilter';

-- ============================================================
-- Step 2: 更新 p_meta_item.metamodel_api_key（旧名 → 新名）
-- ============================================================
UPDATE p_meta_item SET metamodel_api_key = 'entity'          WHERE metamodel_api_key = 'CustomEntity';
UPDATE p_meta_item SET metamodel_api_key = 'item'             WHERE metamodel_api_key = 'CustomItem';
UPDATE p_meta_item SET metamodel_api_key = 'entityLink'       WHERE metamodel_api_key = 'CustomEntityLink';
UPDATE p_meta_item SET metamodel_api_key = 'checkRule'        WHERE metamodel_api_key = 'CustomCheckRule';
UPDATE p_meta_item SET metamodel_api_key = 'pickOption'       WHERE metamodel_api_key = 'CustomPickOption';
UPDATE p_meta_item SET metamodel_api_key = 'referenceFilter'  WHERE metamodel_api_key = 'ReferFilter';

-- ============================================================
-- Step 3: 删除旧的 p_meta_item 数据，重新插入完整映射
-- ============================================================
DELETE FROM p_meta_item WHERE metamodel_api_key IN ('entity', 'item', 'pickOption', 'entityLink', 'checkRule', 'referenceFilter');

-- ============================================================
-- Step 4: 插入 entity（对象定义）的完整字段映射
-- ============================================================
-- 固定列自动映射（无需 p_meta_item）：apiKey, label, labelKey, namespace, description, entityApiKey
-- 需要 dbc 映射的字段：Entity.java 中除固定列外的所有业务字段
--
-- db_column 类型选择规则：
--   String  → dbc_varchar_N（短文本）或 dbc_textarea_N（长文本/JSON）
--   Integer → dbc_smallint_N（布尔/开关 0/1）或 dbc_int_N（枚举/选择值）
--   Long    → dbc_bigint_N（大整数/位掩码/时间戳）
-- ============================================================
INSERT INTO p_meta_item (metamodel_api_key, api_key, label, label_key, item_type, data_type, item_order, require_flg, creatable, updatable, enable_package, db_column, description, description_key, delete_flg, created_by, created_at, updated_by, updated_at) VALUES
-- Integer 枚举/选择类 → dbc_int_*
('entity', 'entity_type',              '对象类型',       'meta.entity.entity_type',              6, 3, 1,  0, 1, 0, 1, 'dbc_int_1',       '对象业务类型',           'meta.entity.entity_type.desc',              0, 1, 1711929600000, 1, 1711929600000),
('entity', 'custom_entity_seq',        '对象排序号',     'meta.entity.custom_entity_seq',        8, 3, 2,  0, 1, 1, 0, 'dbc_int_2',       '对象显示排序号',         'meta.entity.custom_entity_seq.desc',        0, 1, 1711929600000, 1, 1711929600000),
('entity', 'business_category',        '业务分类',       'meta.entity.business_category',        6, 3, 3,  0, 1, 1, 0, 'dbc_int_3',       '对象业务分类',           'meta.entity.business_category.desc',        0, 1, 1711929600000, 1, 1711929600000),
-- String 短文本类 → dbc_varchar_*
('entity', 'svg_api_key',              'SVG图标',        'meta.entity.svg_api_key',              1, 1, 4,  0, 1, 1, 0, 'dbc_varchar_1',   '对象图标API标识',        'meta.entity.svg_api_key.desc',              0, 1, 1711929600000, 1, 1711929600000),
('entity', 'svg_color',                'SVG颜色',        'meta.entity.svg_color',                1, 1, 5,  0, 1, 1, 0, 'dbc_varchar_2',   '对象图标颜色',           'meta.entity.svg_color.desc',                0, 1, 1711929600000, 1, 1711929600000),
('entity', 'description_key',          '描述多语言Key',  'meta.entity.description_key',          1, 1, 6,  0, 1, 1, 0, 'dbc_varchar_3',   '描述的多语言Key',        'meta.entity.description_key.desc',          0, 1, 1711929600000, 1, 1711929600000),
('entity', 'type_property',            '类型属性',       'meta.entity.type_property',            1, 1, 7,  0, 1, 1, 0, 'dbc_varchar_4',   '对象类型扩展属性',       'meta.entity.type_property.desc',            0, 1, 1711929600000, 1, 1711929600000),
('entity', 'db_table',                 '数据库表名',     'meta.entity.db_table',                 1, 1, 8,  1, 1, 0, 0, 'dbc_varchar_5',   '对象对应物理表名',       'meta.entity.db_table.desc',                 0, 1, 1711929600000, 1, 1711929600000),
-- String 长文本类 → dbc_textarea_*
('entity', 'extend_property',          '扩展属性JSON',   'meta.entity.extend_property',          2, 1, 9,  0, 1, 1, 0, 'dbc_textarea_1',  '对象扩展属性JSON',       'meta.entity.extend_property.desc',          0, 1, 1711929600000, 1, 1711929600000),
-- Integer 布尔/开关类 → dbc_smallint_*
('entity', 'enable_flg',               '启用标记',       'meta.entity.enable_flg',               6, 3, 10, 0, 1, 1, 0, 'dbc_smallint_1',  '是否启用',               'meta.entity.enable_flg.desc',               0, 1, 1711929600000, 1, 1711929600000),
('entity', 'custom_flg',               '自定义标记',     'meta.entity.custom_flg',               6, 3, 11, 0, 1, 0, 0, 'dbc_smallint_2',  '0=标准 1=自定义',        'meta.entity.custom_flg.desc',               0, 1, 1711929600000, 1, 1711929600000),
('entity', 'detail_flg',               '明细标记',       'meta.entity.detail_flg',               6, 3, 12, 0, 1, 1, 0, 'dbc_smallint_3',  '是否明细对象',           'meta.entity.detail_flg.desc',               0, 1, 1711929600000, 1, 1711929600000),
('entity', 'enable_team',              '启用团队',       'meta.entity.enable_team',              6, 3, 13, 0, 1, 1, 0, 'dbc_smallint_4',  '是否启用团队协作',       'meta.entity.enable_team.desc',              0, 1, 1711929600000, 1, 1711929600000),
('entity', 'enable_social',            '启用社交',       'meta.entity.enable_social',            6, 3, 14, 0, 1, 1, 0, 'dbc_smallint_5',  '是否启用社交功能',       'meta.entity.enable_social.desc',            0, 1, 1711929600000, 1, 1711929600000),
('entity', 'hidden_flg',               '隐藏标记',       'meta.entity.hidden_flg',               6, 3, 15, 0, 1, 1, 0, 'dbc_smallint_6',  '是否隐藏',               'meta.entity.hidden_flg.desc',               0, 1, 1711929600000, 1, 1711929600000),
('entity', 'searchable',               '可搜索',         'meta.entity.searchable',               6, 3, 16, 0, 1, 1, 0, 'dbc_smallint_7',  '是否可搜索',             'meta.entity.searchable.desc',               0, 1, 1711929600000, 1, 1711929600000),
('entity', 'enable_sharing',           '启用共享',       'meta.entity.enable_sharing',           6, 3, 17, 0, 1, 1, 0, 'dbc_smallint_8',  '是否启用共享',           'meta.entity.enable_sharing.desc',           0, 1, 1711929600000, 1, 1711929600000),
('entity', 'enable_script_trigger',    '启用脚本触发器', 'meta.entity.enable_script_trigger',    6, 3, 18, 0, 1, 1, 0, 'dbc_smallint_9',  '是否启用脚本触发器',     'meta.entity.enable_script_trigger.desc',    0, 1, 1711929600000, 1, 1711929600000),
('entity', 'enable_activity',          '启用活动',       'meta.entity.enable_activity',          6, 3, 19, 0, 1, 1, 0, 'dbc_smallint_10', '是否启用活动',           'meta.entity.enable_activity.desc',          0, 1, 1711929600000, 1, 1711929600000),
('entity', 'enable_history_log',       '启用历史日志',   'meta.entity.enable_history_log',       6, 3, 20, 0, 1, 1, 0, 'dbc_smallint_11', '是否启用历史日志',       'meta.entity.enable_history_log.desc',       0, 1, 1711929600000, 1, 1711929600000),
('entity', 'enable_report',            '启用报表',       'meta.entity.enable_report',            6, 3, 21, 0, 1, 1, 0, 'dbc_smallint_12', '是否启用报表',           'meta.entity.enable_report.desc',            0, 1, 1711929600000, 1, 1711929600000),
('entity', 'enable_refer',             '启用关联',       'meta.entity.enable_refer',             6, 3, 22, 0, 1, 1, 0, 'dbc_smallint_13', '是否启用关联引用',       'meta.entity.enable_refer.desc',             0, 1, 1711929600000, 1, 1711929600000),
('entity', 'enable_api',               '启用API',        'meta.entity.enable_api',               6, 3, 23, 0, 1, 1, 0, 'dbc_smallint_14', '是否启用API',            'meta.entity.enable_api.desc',               0, 1, 1711929600000, 1, 1711929600000),
('entity', 'enable_dynamic_feed',      '启用动态',       'meta.entity.enable_dynamic_feed',      6, 3, 24, 0, 1, 1, 0, 'dbc_smallint_15', '是否启用动态Feed',       'meta.entity.enable_dynamic_feed.desc',      0, 1, 1711929600000, 1, 1711929600000),
-- Long 位掩码/大整数类 → dbc_bigint_*
('entity', 'enable_config',            '启用配置位',     'meta.entity.enable_config',            8, 2, 25, 0, 1, 1, 0, 'dbc_bigint_1',    '功能配置位掩码',         'meta.entity.enable_config.desc',            0, 1, 1711929600000, 1, 1711929600000),
('entity', 'enable_flow',              '启用流程位',     'meta.entity.enable_flow',              8, 2, 26, 0, 1, 1, 0, 'dbc_bigint_2',    '流程配置位掩码',         'meta.entity.enable_flow.desc',              0, 1, 1711929600000, 1, 1711929600000),
('entity', 'enable_package',           '启用包位',       'meta.entity.enable_package',           8, 2, 27, 0, 1, 1, 0, 'dbc_bigint_3',    '包配置位掩码',           'meta.entity.enable_package.desc',           0, 1, 1711929600000, 1, 1711929600000),
-- 剩余 Integer 布尔开关（dbc_int_* 继续）
('entity', 'enable_group_member',      '启用组成员',     'meta.entity.enable_group_member',      6, 3, 28, 0, 1, 1, 0, 'dbc_int_4',       '是否启用组成员',         'meta.entity.enable_group_member.desc',      0, 1, 1711929600000, 1, 1711929600000),
('entity', 'is_archived',              '已归档',         'meta.entity.is_archived',              6, 3, 29, 0, 1, 1, 0, 'dbc_int_5',       '是否已归档',             'meta.entity.is_archived.desc',              0, 1, 1711929600000, 1, 1711929600000),
('entity', 'enable_script_executor',   '启用脚本执行器', 'meta.entity.enable_script_executor',   6, 3, 30, 0, 1, 1, 0, 'dbc_int_6',       '是否启用脚本执行器',     'meta.entity.enable_script_executor.desc',   0, 1, 1711929600000, 1, 1711929600000),
('entity', 'enable_duplicaterule',     '启用查重规则',   'meta.entity.enable_duplicaterule',     6, 3, 31, 0, 1, 1, 0, 'dbc_int_7',       '是否启用查重规则',       'meta.entity.enable_duplicaterule.desc',     0, 1, 1711929600000, 1, 1711929600000),
('entity', 'enable_checkrule',         '启用校验规则',   'meta.entity.enable_checkrule',         6, 3, 32, 0, 1, 1, 0, 'dbc_int_8',       '是否启用校验规则',       'meta.entity.enable_checkrule.desc',         0, 1, 1711929600000, 1, 1711929600000),
('entity', 'enable_busitype',          '启用业务类型',   'meta.entity.enable_busitype',          6, 3, 33, 0, 1, 1, 0, 'dbc_int_9',       '是否启用业务类型',       'meta.entity.enable_busitype.desc',          0, 1, 1711929600000, 1, 1711929600000);

-- ============================================================
-- Step 5: 插入 item（字段定义）的完整字段映射
-- ============================================================
-- 固定列自动映射：apiKey, label, labelKey, namespace, description, entityApiKey
-- 特殊映射：objectApiKey → entityApiKey（Converter 内部处理）
INSERT INTO p_meta_item (metamodel_api_key, api_key, label, label_key, item_type, data_type, item_order, require_flg, creatable, updatable, enable_package, db_column, description, description_key, delete_flg, created_by, created_at, updated_by, updated_at) VALUES
-- Integer 枚举/选择类 → dbc_int_*
('item', 'item_type',                    '字段UI类型',       'meta.item.item_type',                    6, 3, 1,  1, 1, 0, 1, 'dbc_int_1',       '字段的UI展示类型',         'meta.item.item_type.desc',                    0, 1, 1711929600000, 1, 1711929600000),
('item', 'data_type',                    '数据类型',         'meta.item.data_type',                    6, 3, 2,  1, 1, 0, 1, 'dbc_int_2',       '字段的数据存储类型',       'meta.item.data_type.desc',                    0, 1, 1711929600000, 1, 1711929600000),
('item', 'custom_item_seq',              '字段排序号',       'meta.item.custom_item_seq',              8, 3, 3,  0, 1, 1, 0, 'dbc_int_3',       '字段显示排序号',           'meta.item.custom_item_seq.desc',              0, 1, 1711929600000, 1, 1711929600000),
('item', 'item_order',                   '字段序号',         'meta.item.item_order',                   8, 3, 4,  0, 1, 1, 0, 'dbc_int_4',       '字段排列序号',             'meta.item.item_order.desc',                   0, 1, 1711929600000, 1, 1711929600000),
('item', 'readonly_status',              '只读状态',         'meta.item.readonly_status',              6, 3, 5,  0, 1, 1, 0, 'dbc_int_5',       '只读状态值',               'meta.item.readonly_status.desc',              0, 1, 1711929600000, 1, 1711929600000),
('item', 'visible_status',               '可见状态',         'meta.item.visible_status',               6, 3, 6,  0, 1, 1, 0, 'dbc_int_6',       '可见状态值',               'meta.item.visible_status.desc',               0, 1, 1711929600000, 1, 1711929600000),
('item', 'index_order',                  '索引顺序',         'meta.item.index_order',                  8, 3, 7,  0, 1, 1, 0, 'dbc_int_7',       '索引排列顺序',             'meta.item.index_order.desc',                  0, 1, 1711929600000, 1, 1711929600000),
('item', 'index_type',                   '索引类型',         'meta.item.index_type',                   6, 3, 8,  0, 1, 1, 0, 'dbc_int_8',       '索引类型',                 'meta.item.index_type.desc',                   0, 1, 1711929600000, 1, 1711929600000),
('item', 'mask_prefix',                  '掩码前缀位数',     'meta.item.mask_prefix',                  8, 3, 9,  0, 1, 1, 0, 'dbc_int_9',       '掩码前缀位数',             'meta.item.mask_prefix.desc',                  0, 1, 1711929600000, 1, 1711929600000),
('item', 'mask_suffix',                  '掩码后缀位数',     'meta.item.mask_suffix',                  8, 3, 10, 0, 1, 1, 0, 'dbc_int_10',      '掩码后缀位数',             'meta.item.mask_suffix.desc',                  0, 1, 1711929600000, 1, 1711929600000),
('item', 'mask_symbol_type',             '掩码符号类型',     'meta.item.mask_symbol_type',             6, 3, 11, 0, 1, 1, 0, 'dbc_int_11',      '掩码符号类型',             'meta.item.mask_symbol_type.desc',             0, 1, 1711929600000, 1, 1711929600000),
('item', 'increment_strategy',           '自动编号递增策略', 'meta.item.increment_strategy',           6, 3, 12, 0, 1, 1, 0, 'dbc_int_12',      '自动编号递增策略',         'meta.item.increment_strategy.desc',           0, 1, 1711929600000, 1, 1711929600000),
-- String 短文本类 → dbc_varchar_*
('item', 'help_text',                    '帮助文本',         'meta.item.help_text',                    1, 1, 14, 0, 1, 1, 0, 'dbc_varchar_2',   '字段帮助提示文本',         'meta.item.help_text.desc',                    0, 1, 1711929600000, 1, 1711929600000),
('item', 'help_text_key',                '帮助文本Key',      'meta.item.help_text_key',                1, 1, 15, 0, 1, 1, 0, 'dbc_varchar_3',   '帮助文本多语言Key',        'meta.item.help_text_key.desc',                0, 1, 1711929600000, 1, 1711929600000),
('item', 'description_key',              '描述Key',          'meta.item.description_key',              1, 1, 16, 0, 1, 1, 0, 'dbc_varchar_4',   '描述多语言Key',            'meta.item.description_key.desc',              0, 1, 1711929600000, 1, 1711929600000),
('item', 'default_value',                '默认值',           'meta.item.default_value',                1, 1, 17, 0, 1, 1, 0, 'dbc_varchar_5',   '字段默认值',               'meta.item.default_value.desc',                0, 1, 1711929600000, 1, 1711929600000),
('item', 'refer_entity_api_key',         '关联对象',         'meta.item.refer_entity_api_key',         1, 1, 18, 0, 1, 1, 0, 'dbc_varchar_6',   '关联对象apiKey',           'meta.item.refer_entity_api_key.desc',         0, 1, 1711929600000, 1, 1711929600000),
('item', 'refer_link_api_key',           '关联关系',         'meta.item.refer_link_api_key',           1, 1, 19, 0, 1, 1, 0, 'dbc_varchar_7',   '关联关系apiKey',           'meta.item.refer_link_api_key.desc',           0, 1, 1711929600000, 1, 1711929600000),
('item', 'db_column',                    '数据库列名',       'meta.item.db_column',                    1, 1, 20, 0, 1, 0, 0, 'dbc_varchar_8',   '字段对应数据库列名',       'meta.item.db_column.desc',                    0, 1, 1711929600000, 1, 1711929600000),
('item', 'column_name',                  '列显示名',         'meta.item.column_name',                  1, 1, 21, 0, 1, 1, 0, 'dbc_varchar_9',   '列显示名称',               'meta.item.column_name.desc',                  0, 1, 1711929600000, 1, 1711929600000),
('item', 'is_compute_multi_currency_unit','汇总币种单位',    'meta.item.is_compute_multi_currency_unit',1,1, 22, 0, 1, 1, 0, 'dbc_varchar_10',  '汇总字段币种单位',         'meta.item.is_compute_multi_currency_unit.desc',0,1, 1711929600000, 1, 1711929600000),
('item', 'format',                       '自动编号格式',     'meta.item.format',                       1, 1, 23, 0, 1, 1, 0, 'dbc_varchar_11',  '自动编号格式',             'meta.item.format.desc',                       0, 1, 1711929600000, 1, 1711929600000),
-- Integer 布尔/开关类 → dbc_smallint_*
('item', 'require_flg',                  '是否必填',         'meta.item.require_flg',                  6, 3, 24, 0, 1, 1, 0, 'dbc_smallint_1',  '字段是否必填',             'meta.item.require_flg.desc',                  0, 1, 1711929600000, 1, 1711929600000),
('item', 'custom_flg',                   '自定义标记',       'meta.item.custom_flg',                   6, 3, 25, 0, 1, 0, 0, 'dbc_smallint_2',  '0=标准 1=自定义',          'meta.item.custom_flg.desc',                   0, 1, 1711929600000, 1, 1711929600000),
('item', 'enable_flg',                   '启用标记',         'meta.item.enable_flg',                   6, 3, 26, 0, 1, 1, 0, 'dbc_smallint_3',  '是否启用',                 'meta.item.enable_flg.desc',                   0, 1, 1711929600000, 1, 1711929600000),
('item', 'creatable',                    '可创建',           'meta.item.creatable',                    6, 3, 27, 0, 1, 1, 0, 'dbc_smallint_4',  '创建时是否可填',           'meta.item.creatable.desc',                    0, 1, 1711929600000, 1, 1711929600000),
('item', 'updatable',                    '可更新',           'meta.item.updatable',                    6, 3, 28, 0, 1, 1, 0, 'dbc_smallint_5',  '更新时是否可改',           'meta.item.updatable.desc',                    0, 1, 1711929600000, 1, 1711929600000),
('item', 'unique_key_flg',               '唯一键标记',       'meta.item.unique_key_flg',               6, 3, 29, 0, 1, 1, 0, 'dbc_smallint_6',  '是否唯一键',               'meta.item.unique_key_flg.desc',               0, 1, 1711929600000, 1, 1711929600000),
('item', 'enable_history_log',           '启用历史日志',     'meta.item.enable_history_log',           6, 3, 30, 0, 1, 1, 0, 'dbc_smallint_7',  '是否启用历史日志',         'meta.item.enable_history_log.desc',           0, 1, 1711929600000, 1, 1711929600000),
('item', 'hidden_flg',                   '隐藏标记',         'meta.item.hidden_flg',                   6, 3, 31, 0, 1, 1, 0, 'dbc_smallint_8',  '是否隐藏',                 'meta.item.hidden_flg.desc',                   0, 1, 1711929600000, 1, 1711929600000),
('item', 'sort_flg',                     '排序标记',         'meta.item.sort_flg',                     6, 3, 32, 0, 1, 1, 0, 'dbc_smallint_9',  '是否可排序',               'meta.item.sort_flg.desc',                     0, 1, 1711929600000, 1, 1711929600000),
('item', 'enable_deactive',              '启用停用',         'meta.item.enable_deactive',              6, 3, 33, 0, 1, 1, 0, 'dbc_smallint_10', '是否可停用',               'meta.item.enable_deactive.desc',              0, 1, 1711929600000, 1, 1711929600000),
('item', 'compound',                     '复合字段',         'meta.item.compound',                     6, 3, 34, 0, 1, 1, 0, 'dbc_smallint_11', '是否复合字段',             'meta.item.compound.desc',                     0, 1, 1711929600000, 1, 1711929600000),
('item', 'encrypt',                      '加密存储',         'meta.item.encrypt',                      6, 3, 35, 0, 1, 1, 0, 'dbc_smallint_12', '是否加密存储',             'meta.item.encrypt.desc',                      0, 1, 1711929600000, 1, 1711929600000),
('item', 'markdown',                     'Markdown编辑器',   'meta.item.markdown',                     6, 3, 36, 0, 1, 1, 0, 'dbc_smallint_13', '是否Markdown编辑器',       'meta.item.markdown.desc',                     0, 1, 1711929600000, 1, 1711929600000),
('item', 'refer_item_filter_enable',     '关联字段过滤',     'meta.item.refer_item_filter_enable',     6, 3, 37, 0, 1, 1, 0, 'dbc_smallint_14', '关联字段过滤开关',         'meta.item.refer_item_filter_enable.desc',     0, 1, 1711929600000, 1, 1711929600000),
-- Long 位掩码类 → dbc_bigint_*
('item', 'enable_config',                '启用配置位',       'meta.item.enable_config',                8, 2, 38, 0, 1, 1, 0, 'dbc_bigint_1',    '功能配置位掩码',           'meta.item.enable_config.desc',                0, 1, 1711929600000, 1, 1711929600000),
('item', 'enable_package',               '启用包位',         'meta.item.enable_package',               8, 2, 39, 0, 1, 1, 0, 'dbc_bigint_2',    '包配置位掩码',             'meta.item.enable_package.desc',               0, 1, 1711929600000, 1, 1711929600000),
-- ============================================================
-- 原 typeProperty JSON 中的字段（独立化为 p_meta_item 行）
-- ============================================================
-- 通用长度/精度
('item', 'max_length',                   '最大长度',         'meta.item.max_length',                   8, 3, 40, 0, 1, 1, 0, 'dbc_int_13',      'TEXT/REAL等字段最大长度',   'meta.item.max_length.desc',                   0, 1, 1711929600000, 1, 1711929600000),
('item', 'min_length',                   '最小长度',         'meta.item.min_length',                   8, 3, 41, 0, 1, 1, 0, 'dbc_int_14',      'TEXT/TEXTAREA字段最小长度', 'meta.item.min_length.desc',                   0, 1, 1711929600000, 1, 1711929600000),
('item', 'decimal',                      '小数位数',         'meta.item.decimal',                      8, 3, 42, 0, 1, 1, 0, 'dbc_int_15',      'REAL/PERCENTAGE小数位数',   'meta.item.decimal.desc',                      0, 1, 1711929600000, 1, 1711929600000),
-- 货币相关
('item', 'is_currency',                  '是否货币',         'meta.item.is_currency',                  6, 3, 43, 0, 1, 1, 0, 'dbc_smallint_15', '0=否 1=是',                'meta.item.is_currency.desc',                  0, 1, 1711929600000, 1, 1711929600000),
('item', 'currency_part',                '货币部分',         'meta.item.currency_part',                6, 3, 44, 0, 1, 1, 0, 'dbc_smallint_16', '1=本币 2=原币',            'meta.item.currency_part.desc',                0, 1, 1711929600000, 1, 1711929600000),
('item', 'is_multi_currency',            '是否多币种',       'meta.item.is_multi_currency',            6, 3, 45, 0, 1, 1, 0, 'dbc_smallint_17', '0=单币种 1=多币种',        'meta.item.is_multi_currency.desc',            0, 1, 1711929600000, 1, 1711929600000),
('item', 'currency_flg',                 '货币标记',         'meta.item.currency_flg',                 6, 3, 46, 0, 1, 1, 0, 'dbc_smallint_18', '货币冗余标记',             'meta.item.currency_flg.desc',                 0, 1, 1711929600000, 1, 1711929600000),
-- 公式相关
('item', 'compute_type',                 '计算结果类型',     'meta.item.compute_type',                 6, 3, 47, 0, 1, 1, 0, 'dbc_smallint_19', '公式计算结果itemType',     'meta.item.compute_type.desc',                 0, 1, 1711929600000, 1, 1711929600000),
('item', 'real_time_compute',            '实时计算',         'meta.item.real_time_compute',            6, 3, 48, 0, 1, 1, 0, 'dbc_smallint_20', '0=保存时 1=实时',          'meta.item.real_time_compute.desc',            0, 1, 1711929600000, 1, 1711929600000),
-- 选项集相关
('item', 'refer_global',                 '引用全局选项集',   'meta.item.refer_global',                 6, 3, 49, 0, 1, 1, 0, 'dbc_smallint_21', '0=否 1=是',                'meta.item.refer_global.desc',                 0, 1, 1711929600000, 1, 1711929600000),
('item', 'global_pick_item',             '全局选项集ID',     'meta.item.global_pick_item',             1, 1, 50, 0, 1, 1, 0, 'dbc_varchar_1',   '全局选项集ID/apiKey',      'meta.item.global_pick_item.desc',              0, 1, 1711929600000, 1, 1711929600000),
('item', 'global_pick_item_apikey',      '全局选项集apiKey', 'meta.item.global_pick_item_apikey',      1, 1, 51, 0, 1, 1, 0, 'dbc_varchar_12',  '全局选项集apiKey',         'meta.item.global_pick_item_apikey.desc',       0, 1, 1711929600000, 1, 1711929600000),
('item', 'is_external',                  '外部选项源',       'meta.item.is_external',                  6, 3, 52, 0, 1, 1, 0, 'dbc_smallint_22', '0=否 1=是',                'meta.item.is_external.desc',                  0, 1, 1711929600000, 1, 1711929600000),
-- 日期相关
('item', 'date_mode',                    '日期模式',         'meta.item.date_mode',                    6, 3, 53, 0, 1, 1, 0, 'dbc_smallint_23', '1=仅日期 2=日期+时间',     'meta.item.date_mode.desc',                    0, 1, 1711929600000, 1, 1711929600000),
-- 关联/LOOKUP相关
('item', 'cascade_delete',               '级联删除',         'meta.item.cascade_delete',               6, 3, 54, 0, 1, 1, 0, 'dbc_smallint_24', '0=不级联 1=级联 2=阻止',   'meta.item.cascade_delete.desc',               0, 1, 1711929600000, 1, 1711929600000),
('item', 'is_detail',                    '明细关联',         'meta.item.is_detail',                    6, 3, 55, 0, 1, 1, 0, 'dbc_smallint_25', '0=否 1=是',                'meta.item.is_detail.desc',                    0, 1, 1711929600000, 1, 1711929600000),
('item', 'can_batch_create',             '可批量创建',       'meta.item.can_batch_create',             6, 3, 56, 0, 1, 1, 0, 'dbc_smallint_26', '0=否 1=是',                'meta.item.can_batch_create.desc',              0, 1, 1711929600000, 1, 1711929600000),
('item', 'is_copy_with_parent',          '随父复制',         'meta.item.is_copy_with_parent',          6, 3, 57, 0, 1, 1, 0, 'dbc_smallint_27', '0=否 1=是',                'meta.item.is_copy_with_parent.desc',          0, 1, 1711929600000, 1, 1711929600000),
('item', 'is_mask',                      '掩码显示',         'meta.item.is_mask',                      6, 3, 58, 0, 1, 1, 0, 'dbc_smallint_28', '0=否 1=是',                'meta.item.is_mask.desc',                      0, 1, 1711929600000, 1, 1711929600000),
('item', 'enable_multi_detail',          '启用多明细',       'meta.item.enable_multi_detail',          6, 3, 59, 0, 1, 1, 0, 'dbc_smallint_29', '0=否 1=是',                'meta.item.enable_multi_detail.desc',          0, 1, 1711929600000, 1, 1711929600000),
('item', 'batch_create_mode',            '批量创建模式',     'meta.item.batch_create_mode',            6, 3, 60, 0, 1, 1, 0, 'dbc_smallint_30', '批量创建模式',             'meta.item.batch_create_mode.desc',            0, 1, 1711929600000, 1, 1711929600000),
('item', 'batch_create_link_by_busitype','按业务类型创建',   'meta.item.batch_create_link_by_busitype',6, 3, 61, 0, 1, 1, 0, 'dbc_smallint_31', '按业务类型批量创建关联',   'meta.item.batch_create_link_by_busitype.desc',0, 1, 1711929600000, 1, 1711929600000),
-- JOIN/LOOKUP字段
('item', 'join_item',                    '关联字段',         'meta.item.join_item',                    1, 1, 62, 0, 1, 1, 0, 'dbc_varchar_13',  'LOOKUP关联的目标字段',     'meta.item.join_item.desc',                    0, 1, 1711929600000, 1, 1711929600000),
('item', 'join_object',                  '关联对象',         'meta.item.join_object',                  1, 1, 63, 0, 1, 1, 0, 'dbc_varchar_14',  'LOOKUP关联的目标对象',     'meta.item.join_object.desc',                  0, 1, 1711929600000, 1, 1711929600000),
('item', 'join_link',                    '关联关系',         'meta.item.join_link',                    1, 1, 64, 0, 1, 1, 0, 'dbc_varchar_15',  'LOOKUP使用的EntityLink',   'meta.item.join_link.desc',                    0, 1, 1711929600000, 1, 1711929600000),
('item', 'link_label',                   '关联标签',         'meta.item.link_label',                   1, 1, 65, 0, 1, 1, 0, 'dbc_varchar_16',  'REFER/MULTIREF UI显示名',  'meta.item.link_label.desc',                   0, 1, 1711929600000, 1, 1711929600000),
-- 多关联相关
('item', 'refer_entity_ids',             '关联实体列表',     'meta.item.refer_entity_ids',             1, 1, 66, 0, 1, 1, 0, 'dbc_varchar_17',  'MULTIREF目标对象(逗号分隔)','meta.item.refer_entity_ids.desc',             0, 1, 1711929600000, 1, 1711929600000),
('item', 'entity_or_data',               '实体或数据',       'meta.item.entity_or_data',               6, 3, 67, 0, 1, 1, 0, 'dbc_smallint_32', '1=实体关联 2=数据关联',    'meta.item.entity_or_data.desc',               0, 1, 1711929600000, 1, 1711929600000),
('item', 'group_key',                    '分组key',          'meta.item.group_key',                    1, 1, 68, 0, 1, 1, 0, 'dbc_varchar_18',  'MULTIREF数据分组标识',     'meta.item.group_key.desc',                    0, 1, 1711929600000, 1, 1711929600000),
('item', 'compound_sub',                 '复合子字段',       'meta.item.compound_sub',                 6, 3, 69, 0, 1, 1, 0, 'dbc_smallint_33', '0=否 1=是',                'meta.item.compound_sub.desc',                 0, 1, 1711929600000, 1, 1711929600000),
('item', 'compound_api_key',             '复合字段apiKey',   'meta.item.compound_api_key',             1, 1, 70, 0, 1, 1, 0, 'dbc_varchar_19',  '子字段指向的父复合字段',   'meta.item.compound_api_key.desc',              0, 1, 1711929600000, 1, 1711929600000),
-- 文本相关
('item', 'multi_line_text',              '多行文本',         'meta.item.multi_line_text',              6, 3, 71, 0, 1, 1, 0, 'dbc_smallint_34', '0=单行 1=多行',            'meta.item.multi_line_text.desc',               0, 1, 1711929600000, 1, 1711929600000),
('item', 'scan_code_entry_flg',          '扫码录入',         'meta.item.scan_code_entry_flg',          6, 3, 72, 0, 1, 1, 0, 'dbc_smallint_35', '0=否 1=是',                'meta.item.scan_code_entry_flg.desc',          0, 1, 1711929600000, 1, 1711929600000),
('item', 'case_sensitive',               '大小写敏感',       'meta.item.case_sensitive',               6, 3, 73, 0, 1, 1, 0, 'dbc_smallint_36', '0=不敏感 1=敏感',          'meta.item.case_sensitive.desc',                0, 1, 1711929600000, 1, 1711929600000),
-- 富文本相关
('item', 'show_rows',                    '显示行数',         'meta.item.show_rows',                    8, 3, 74, 0, 1, 1, 0, 'dbc_smallint_37', '富文本显示行数',           'meta.item.show_rows.desc',                    0, 1, 1711929600000, 1, 1711929600000),
-- 图片水印相关
('item', 'watermark_flg',                '水印开关',         'meta.item.watermark_flg',                6, 3, 75, 0, 1, 1, 0, 'dbc_smallint_38', '0=关闭 1=开启',            'meta.item.watermark_flg.desc',                0, 1, 1711929600000, 1, 1711929600000),
('item', 'watermark_time_flg',           '水印时间',         'meta.item.watermark_time_flg',           6, 3, 76, 0, 1, 1, 0, 'dbc_smallint_39', '0=不显示 1=显示拍摄时间',  'meta.item.watermark_time_flg.desc',           0, 1, 1711929600000, 1, 1711929600000),
('item', 'watermark_login_user_flg',     '水印用户',         'meta.item.watermark_login_user_flg',     6, 3, 77, 0, 1, 1, 0, 'dbc_smallint_40', '0=不显示 1=显示登录用户',  'meta.item.watermark_login_user_flg.desc',     0, 1, 1711929600000, 1, 1711929600000),
('item', 'watermark_location_flg',       '水印位置',         'meta.item.watermark_location_flg',       6, 3, 78, 0, 1, 1, 0, 'dbc_smallint_41', '0=不显示 1=显示拍摄位置',  'meta.item.watermark_location_flg.desc',       0, 1, 1711929600000, 1, 1711929600000),
('item', 'watermark_join_field',         '水印关联字段',     'meta.item.watermark_join_field',         1, 1, 79, 0, 1, 1, 0, 'dbc_varchar_20',  '水印显示的关联字段值',     'meta.item.watermark_join_field.desc',          0, 1, 1711929600000, 1, 1711929600000),
-- 大字段外置
('item', 'ext_table',                    '外置扩展表',       'meta.item.ext_table',                    6, 3, 80, 0, 1, 1, 0, 'dbc_smallint_42', '0=否 1=是(超长文本外置)',   'meta.item.ext_table.desc',                    0, 1, 1711929600000, 1, 1711929600000);

-- ============================================================
-- Step 6: 插入 pickOption（选项值）的完整字段映射
-- ============================================================
-- 固定列自动映射：apiKey, label, labelKey, namespace, description, entityApiKey
INSERT INTO p_meta_item (metamodel_api_key, api_key, label, label_key, item_type, data_type, item_order, require_flg, creatable, updatable, enable_package, db_column, description, description_key, delete_flg, created_by, created_at, updated_by, updated_at) VALUES
('pickOption', 'item_api_key',       '所属字段',       'meta.pick.item_api_key',       1, 1, 1, 1, 1, 0, 0, 'dbc_varchar_1',   '所属字段apiKey',         'meta.pick.item_api_key.desc',       0, 1, 1711929600000, 1, 1711929600000),
('pickOption', 'description_key',    '描述Key',        'meta.pick.description_key',    1, 1, 2, 0, 1, 1, 0, 'dbc_varchar_2',   '描述多语言Key',          'meta.pick.description_key.desc',    0, 1, 1711929600000, 1, 1711929600000),
('pickOption', 'option_order',       '排序号',         'meta.pick.option_order',       8, 3, 3, 0, 1, 1, 0, 'dbc_int_1',       '选项排序号',             'meta.pick.option_order.desc',       0, 1, 1711929600000, 1, 1711929600000),
('pickOption', 'default_flg',        '默认选中',       'meta.pick.default_flg',        6, 3, 4, 0, 1, 1, 0, 'dbc_smallint_1',  '是否默认选中',           'meta.pick.default_flg.desc',        0, 1, 1711929600000, 1, 1711929600000),
('pickOption', 'global_flg',         '全局标记',       'meta.pick.global_flg',         6, 3, 5, 0, 1, 1, 0, 'dbc_smallint_2',  '是否全局选项',           'meta.pick.global_flg.desc',         0, 1, 1711929600000, 1, 1711929600000),
('pickOption', 'custom_flg',         '自定义标记',     'meta.pick.custom_flg',         6, 3, 6, 0, 1, 0, 0, 'dbc_smallint_3',  '0=标准 1=自定义',        'meta.pick.custom_flg.desc',         0, 1, 1711929600000, 1, 1711929600000),
('pickOption', 'enable_flg',         '启用标记',       'meta.pick.enable_flg',         6, 3, 7, 0, 1, 1, 0, 'dbc_smallint_4',  '是否启用',               'meta.pick.enable_flg.desc',         0, 1, 1711929600000, 1, 1711929600000);

-- ============================================================
-- Step 7: 插入 entityLink（关联关系）的完整字段映射
-- ============================================================
-- 固定列自动映射：apiKey, label, labelKey, namespace, description, entityApiKey
INSERT INTO p_meta_item (metamodel_api_key, api_key, label, label_key, item_type, data_type, item_order, require_flg, creatable, updatable, enable_package, db_column, description, description_key, delete_flg, created_by, created_at, updated_by, updated_at) VALUES
('entityLink', 'type_property',          '类型属性',       'meta.link.type_property',          1, 1, 1, 0, 1, 1, 0, 'dbc_varchar_1',   '关联类型扩展属性',       'meta.link.type_property.desc',          0, 1, 1711929600000, 1, 1711929600000),
('entityLink', 'parent_entity_api_key',  '父对象',         'meta.link.parent_entity_api_key',  1, 1, 2, 1, 1, 0, 0, 'dbc_varchar_2',   '父对象apiKey',           'meta.link.parent_entity_api_key.desc',  0, 1, 1711929600000, 1, 1711929600000),
('entityLink', 'child_entity_api_key',   '子对象',         'meta.link.child_entity_api_key',   1, 1, 3, 1, 1, 0, 0, 'dbc_varchar_3',   '子对象apiKey',           'meta.link.child_entity_api_key.desc',   0, 1, 1711929600000, 1, 1711929600000),
('entityLink', 'description_key',        '描述Key',        'meta.link.description_key',        1, 1, 4, 0, 1, 1, 0, 'dbc_varchar_4',   '描述多语言Key',          'meta.link.description_key.desc',        0, 1, 1711929600000, 1, 1711929600000),
('entityLink', 'link_type',              '关联类型',       'meta.link.link_type',              6, 3, 5, 1, 1, 0, 0, 'dbc_int_1',       '关联类型(1=LOOKUP等)',   'meta.link.link_type.desc',              0, 1, 1711929600000, 1, 1711929600000),
('entityLink', 'detail_link',            '明细关联',       'meta.link.detail_link',            6, 3, 6, 0, 1, 1, 0, 'dbc_smallint_1',  '是否明细关联',           'meta.link.detail_link.desc',            0, 1, 1711929600000, 1, 1711929600000),
('entityLink', 'cascade_delete',         '级联删除',       'meta.link.cascade_delete',         6, 3, 7, 0, 1, 1, 0, 'dbc_smallint_2',  '是否级联删除',           'meta.link.cascade_delete.desc',         0, 1, 1711929600000, 1, 1711929600000),
('entityLink', 'access_control',         '访问控制',       'meta.link.access_control',         6, 3, 8, 0, 1, 1, 0, 'dbc_smallint_3',  '是否启用访问控制',       'meta.link.access_control.desc',         0, 1, 1711929600000, 1, 1711929600000),
('entityLink', 'enable_flg',             '启用标记',       'meta.link.enable_flg',             6, 3, 9, 0, 1, 1, 0, 'dbc_smallint_4',  '是否启用',               'meta.link.enable_flg.desc',             0, 1, 1711929600000, 1, 1711929600000);

-- ============================================================
-- Step 8: 插入 checkRule（校验规则）的完整字段映射
-- ============================================================
-- 固定列自动映射：apiKey, label, labelKey, namespace, description, entityApiKey
INSERT INTO p_meta_item (metamodel_api_key, api_key, label, label_key, item_type, data_type, item_order, require_flg, creatable, updatable, enable_package, db_column, description, description_key, delete_flg, created_by, created_at, updated_by, updated_at) VALUES
('checkRule', 'description_key',        '描述Key',        'meta.rule.description_key',        1, 1, 1, 0, 1, 1, 0, 'dbc_varchar_1',   '描述多语言Key',          'meta.rule.description_key.desc',        0, 1, 1711929600000, 1, 1711929600000),
('checkRule', 'check_error_msg',        '错误消息',       'meta.rule.check_error_msg',        1, 1, 2, 0, 1, 1, 0, 'dbc_varchar_2',   '校验失败错误消息',       'meta.rule.check_error_msg.desc',        0, 1, 1711929600000, 1, 1711929600000),
('checkRule', 'check_error_msg_key',    '错误消息Key',    'meta.rule.check_error_msg_key',    1, 1, 3, 0, 1, 1, 0, 'dbc_varchar_3',   '错误消息多语言Key',      'meta.rule.check_error_msg_key.desc',    0, 1, 1711929600000, 1, 1711929600000),
('checkRule', 'check_error_item_api_key','错误字段',      'meta.rule.check_error_item_api_key',1,1, 4, 0, 1, 1, 0, 'dbc_varchar_4',   '校验失败关联字段',       'meta.rule.check_error_item_api_key.desc',0,1, 1711929600000, 1, 1711929600000),
('checkRule', 'check_formula',          '校验公式',       'meta.rule.check_formula',          2, 1, 5, 1, 1, 1, 0, 'dbc_textarea_1',  '校验规则公式表达式',     'meta.rule.check_formula.desc',          0, 1, 1711929600000, 1, 1711929600000),
('checkRule', 'active_flg',             '激活标记',       'meta.rule.active_flg',             6, 3, 6, 0, 1, 1, 0, 'dbc_smallint_1',  '是否激活',               'meta.rule.active_flg.desc',             0, 1, 1711929600000, 1, 1711929600000),
('checkRule', 'check_error_location',   '错误位置',       'meta.rule.check_error_location',   6, 3, 7, 0, 1, 1, 0, 'dbc_int_1',       '错误提示位置',           'meta.rule.check_error_location.desc',   0, 1, 1711929600000, 1, 1711929600000),
('checkRule', 'check_all_items_flg',    '全字段校验',     'meta.rule.check_all_items_flg',    6, 3, 8, 0, 1, 1, 0, 'dbc_smallint_2',  '是否校验所有字段',       'meta.rule.check_all_items_flg.desc',    0, 1, 1711929600000, 1, 1711929600000),
('checkRule', 'check_error_way',        '错误方式',       'meta.rule.check_error_way',        6, 3, 9, 0, 1, 1, 0, 'dbc_int_2',       '错误提示方式',           'meta.rule.check_error_way.desc',        0, 1, 1711929600000, 1, 1711929600000);

-- ============================================================
-- Step 9: 插入 referenceFilter（关联过滤条件）的完整字段映射
-- ============================================================
-- 固定列自动映射：apiKey, label, labelKey, namespace, description, entityApiKey
INSERT INTO p_meta_item (metamodel_api_key, api_key, label, label_key, item_type, data_type, item_order, require_flg, creatable, updatable, enable_package, db_column, description, description_key, delete_flg, created_by, created_at, updated_by, updated_at) VALUES
('referenceFilter', 'item_api_key',       '所属字段',       'meta.refer.item_api_key',       1, 1, 1, 1, 1, 0, 0, 'dbc_varchar_1',   '所属字段apiKey',         'meta.refer.item_api_key.desc',       0, 1, 1711929600000, 1, 1711929600000),
('referenceFilter', 'link_api_key',       '关联关系',       'meta.refer.link_api_key',       1, 1, 2, 0, 1, 1, 0, 'dbc_varchar_2',   '关联关系apiKey',         'meta.refer.link_api_key.desc',       0, 1, 1711929600000, 1, 1711929600000),
('referenceFilter', 'filter_field',       '过滤字段',       'meta.refer.filter_field',       1, 1, 3, 1, 1, 1, 0, 'dbc_varchar_3',   '过滤条件字段名',         'meta.refer.filter_field.desc',       0, 1, 1711929600000, 1, 1711929600000),
('referenceFilter', 'filter_operator',    '过滤操作符',     'meta.refer.filter_operator',    1, 1, 4, 1, 1, 1, 0, 'dbc_varchar_4',   '过滤操作符',             'meta.refer.filter_operator.desc',    0, 1, 1711929600000, 1, 1711929600000),
('referenceFilter', 'filter_value',       '过滤值',         'meta.refer.filter_value',       1, 1, 5, 0, 1, 1, 0, 'dbc_varchar_5',   '过滤条件值',             'meta.refer.filter_value.desc',       0, 1, 1711929600000, 1, 1711929600000),
('referenceFilter', 'description_key',    '描述Key',        'meta.refer.description_key',    1, 1, 6, 0, 1, 1, 0, 'dbc_varchar_6',   '描述多语言Key',          'meta.refer.description_key.desc',    0, 1, 1711929600000, 1, 1711929600000),
('referenceFilter', 'filter_order',       '过滤排序',       'meta.refer.filter_order',       8, 3, 7, 0, 1, 1, 0, 'dbc_int_1',       '过滤条件排序号',         'meta.refer.filter_order.desc',       0, 1, 1711929600000, 1, 1711929600000);

-- ============================================================
-- Step 10: 同步更新 p_common_metadata.metamodel_api_key（如果已有数据）
-- ============================================================
UPDATE p_common_metadata SET metamodel_api_key = 'entity'          WHERE metamodel_api_key = 'CustomEntity';
UPDATE p_common_metadata SET metamodel_api_key = 'item'             WHERE metamodel_api_key = 'CustomItem';
UPDATE p_common_metadata SET metamodel_api_key = 'entityLink'       WHERE metamodel_api_key = 'CustomEntityLink';
UPDATE p_common_metadata SET metamodel_api_key = 'checkRule'        WHERE metamodel_api_key = 'CustomCheckRule';
UPDATE p_common_metadata SET metamodel_api_key = 'pickOption'       WHERE metamodel_api_key = 'CustomPickOption';
UPDATE p_common_metadata SET metamodel_api_key = 'referenceFilter'  WHERE metamodel_api_key = 'ReferFilter';

UPDATE p_tenant_metadata SET metamodel_api_key = 'entity'          WHERE metamodel_api_key = 'CustomEntity';
UPDATE p_tenant_metadata SET metamodel_api_key = 'item'             WHERE metamodel_api_key = 'CustomItem';
UPDATE p_tenant_metadata SET metamodel_api_key = 'entityLink'       WHERE metamodel_api_key = 'CustomEntityLink';
UPDATE p_tenant_metadata SET metamodel_api_key = 'checkRule'        WHERE metamodel_api_key = 'CustomCheckRule';
UPDATE p_tenant_metadata SET metamodel_api_key = 'pickOption'       WHERE metamodel_api_key = 'CustomPickOption';
UPDATE p_tenant_metadata SET metamodel_api_key = 'referenceFilter'  WHERE metamodel_api_key = 'ReferFilter';

-- ============================================================
-- 完成
-- ============================================================
