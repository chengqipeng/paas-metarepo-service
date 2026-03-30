-- ============================================================
-- 补充 p_meta_option 和 p_meta_link 数据
-- 为 6 种实体相关元模型建立完整的取值约束和关联关系
-- ============================================================

-- ============================================================
-- Part 1: p_meta_link — 元模型间关联关系
-- ============================================================
-- 关联关系描述元模型之间的父子层级：
--   entity 是顶层，item/entityLink/checkRule 是 entity 的子元模型
--   pickOption 是 item 的子元模型
--   referenceFilter 是 item 的子元模型

INSERT INTO p_meta_link (id, api_key, namespace, label, label_key,
  parent_metamodel_api_key, child_metamodel_api_key, refer_item_api_key,
  link_type, cascade_delete, delete_flg, created_by, created_at, updated_by, updated_at)
VALUES
-- entity → item（对象包含字段，级联删除）
(900001, 'entity_to_item', 'system', '对象-字段', 'link.entity_to_item',
 'entity', 'item', 'entityApiKey', 1, 1, 0, 1, 1711929600000, 1, 1711929600000),

-- entity → entityLink（对象包含关联关系，级联删除）
(900002, 'entity_to_link', 'system', '对象-关联', 'link.entity_to_link',
 'entity', 'entityLink', 'entityApiKey', 1, 1, 0, 1, 1711929600000, 1, 1711929600000),

-- entity → checkRule（对象包含校验规则，级联删除）
(900003, 'entity_to_checkrule', 'system', '对象-校验规则', 'link.entity_to_checkrule',
 'entity', 'checkRule', 'entityApiKey', 1, 1, 0, 1, 1711929600000, 1, 1711929600000),

-- item → pickOption（字段包含选项值，级联删除）
(900004, 'item_to_pickoption', 'system', '字段-选项值', 'link.item_to_pickoption',
 'item', 'pickOption', 'itemApiKey', 1, 1, 0, 1, 1711929600000, 1, 1711929600000),

-- item → referenceFilter（字段包含关联过滤，级联删除）
(900005, 'item_to_reffilter', 'system', '字段-关联过滤', 'link.item_to_reffilter',
 'item', 'referenceFilter', 'itemApiKey', 1, 1, 0, 1, 1711929600000, 1, 1711929600000);

-- ============================================================
-- Part 2: p_meta_option — 元模型字段取值范围
-- ============================================================

-- --------------------------------------------------------
-- 2.1 entity 元模型的枚举字段
-- --------------------------------------------------------

-- entity.entity_type（对象类型）
INSERT INTO p_meta_option (id, metamodel_api_key, item_api_key, namespace, option_code, option_key, option_label, option_label_key, option_order, default_flg, enable_flg, delete_flg, created_by, created_at, updated_by, updated_at) VALUES
(800001, 'entity', 'entity_type', 'system', 0, 'STANDARD',  '标准对象',   'option.entity_type.standard',  1, 0, 1, 0, 1, 1711929600000, 1, 1711929600000),
(800002, 'entity', 'entity_type', 'system', 1, 'CUSTOM',    '自定义对象', 'option.entity_type.custom',     2, 1, 1, 0, 1, 1711929600000, 1, 1711929600000),
(800003, 'entity', 'entity_type', 'system', 2, 'SYSTEM',    '系统对象',   'option.entity_type.system',     3, 0, 1, 0, 1, 1711929600000, 1, 1711929600000),
(800004, 'entity', 'entity_type', 'system', 3, 'VIRTUAL',   '虚拟对象',   'option.entity_type.virtual',    4, 0, 1, 0, 1, 1711929600000, 1, 1711929600000);

-- entity 布尔开关字段（enable_flg, custom_flg, enable_history_log 等）统一 0/1
-- 所有 item_type=6(选择) 且语义为布尔的字段共享同一组选项
INSERT INTO p_meta_option (id, metamodel_api_key, item_api_key, namespace, option_code, option_key, option_label, option_label_key, option_order, default_flg, enable_flg, delete_flg, created_by, created_at, updated_by, updated_at) VALUES
(800010, 'entity', 'enable_flg',         'system', 0, 'NO',  '否', 'option.bool.no',  1, 0, 1, 0, 1, 1711929600000, 1, 1711929600000),
(800011, 'entity', 'enable_flg',         'system', 1, 'YES', '是', 'option.bool.yes', 2, 1, 1, 0, 1, 1711929600000, 1, 1711929600000),
(800012, 'entity', 'custom_flg',         'system', 0, 'STANDARD', '标准', 'option.custom_flg.standard', 1, 1, 1, 0, 1, 1711929600000, 1, 1711929600000),
(800013, 'entity', 'custom_flg',         'system', 1, 'CUSTOM',   '自定义', 'option.custom_flg.custom', 2, 0, 1, 0, 1, 1711929600000, 1, 1711929600000),
(800014, 'entity', 'enable_history_log', 'system', 0, 'NO',  '否', 'option.bool.no',  1, 0, 1, 0, 1, 1711929600000, 1, 1711929600000),
(800015, 'entity', 'enable_history_log', 'system', 1, 'YES', '是', 'option.bool.yes', 2, 1, 1, 0, 1, 1711929600000, 1, 1711929600000),
(800016, 'entity', 'enable_busitype',    'system', 0, 'NO',  '否', 'option.bool.no',  1, 0, 1, 0, 1, 1711929600000, 1, 1711929600000),
(800017, 'entity', 'enable_busitype',    'system', 1, 'YES', '是', 'option.bool.yes', 2, 0, 1, 0, 1, 1711929600000, 1, 1711929600000),
(800018, 'entity', 'enable_checkrule',   'system', 0, 'NO',  '否', 'option.bool.no',  1, 0, 1, 0, 1, 1711929600000, 1, 1711929600000),
(800019, 'entity', 'enable_checkrule',   'system', 1, 'YES', '是', 'option.bool.yes', 2, 0, 1, 0, 1, 1711929600000, 1, 1711929600000);

-- --------------------------------------------------------
-- 2.2 item 元模型的枚举字段
-- --------------------------------------------------------

-- item.itemType（字段UI类型，对应 ItemTypeEnum）
INSERT INTO p_meta_option (id, metamodel_api_key, item_api_key, namespace, option_code, option_key, option_label, option_label_key, option_order, default_flg, enable_flg, delete_flg, created_by, created_at, updated_by, updated_at) VALUES
(801001, 'item', 'itemType', 'system', 1,  'TEXT',           '文本',     'option.item_type.text',           1,  1, 1, 0, 1, 1711929600000, 1, 1711929600000),
(801002, 'item', 'itemType', 'system', 2,  'NUMBER',         '数字',     'option.item_type.number',         2,  0, 1, 0, 1, 1711929600000, 1, 1711929600000),
(801003, 'item', 'itemType', 'system', 3,  'DATE',           '日期',     'option.item_type.date',           3,  0, 1, 0, 1, 1711929600000, 1, 1711929600000),
(801004, 'item', 'itemType', 'system', 4,  'PICKLIST',       '选项',     'option.item_type.picklist',       4,  0, 1, 0, 1, 1711929600000, 1, 1711929600000),
(801005, 'item', 'itemType', 'system', 5,  'LOOKUP',         '查找',     'option.item_type.lookup',         5,  0, 1, 0, 1, 1711929600000, 1, 1711929600000),
(801006, 'item', 'itemType', 'system', 6,  'FORMULA',        '公式',     'option.item_type.formula',        6,  0, 1, 0, 1, 1711929600000, 1, 1711929600000),
(801007, 'item', 'itemType', 'system', 7,  'ROLLUP',         '汇总',     'option.item_type.rollup',         7,  0, 1, 0, 1, 1711929600000, 1, 1711929600000),
(801008, 'item', 'itemType', 'system', 8,  'TEXTAREA',       '长文本',   'option.item_type.textarea',       8,  0, 1, 0, 1, 1711929600000, 1, 1711929600000),
(801009, 'item', 'itemType', 'system', 9,  'BOOLEAN',        '布尔',     'option.item_type.boolean',        9,  0, 1, 0, 1, 1711929600000, 1, 1711929600000),
(801010, 'item', 'itemType', 'system', 10, 'CURRENCY',       '货币',     'option.item_type.currency',       10, 0, 1, 0, 1, 1711929600000, 1, 1711929600000),
(801011, 'item', 'itemType', 'system', 11, 'PERCENT',        '百分比',   'option.item_type.percent',        11, 0, 1, 0, 1, 1711929600000, 1, 1711929600000),
(801012, 'item', 'itemType', 'system', 12, 'EMAIL',          '邮箱',     'option.item_type.email',          12, 0, 1, 0, 1, 1711929600000, 1, 1711929600000),
(801013, 'item', 'itemType', 'system', 13, 'PHONE',          '电话',     'option.item_type.phone',          13, 0, 1, 0, 1, 1711929600000, 1, 1711929600000),
(801014, 'item', 'itemType', 'system', 14, 'URL',            'URL',      'option.item_type.url',            14, 0, 1, 0, 1, 1711929600000, 1, 1711929600000),
(801015, 'item', 'itemType', 'system', 15, 'DATETIME',       '日期时间', 'option.item_type.datetime',       15, 0, 1, 0, 1, 1711929600000, 1, 1711929600000),
(801016, 'item', 'itemType', 'system', 16, 'MULTIPICKLIST',  '多选',     'option.item_type.multipicklist',  16, 0, 1, 0, 1, 1711929600000, 1, 1711929600000),
(801017, 'item', 'itemType', 'system', 17, 'MASTER_DETAIL',  '主从',     'option.item_type.master_detail',  17, 0, 1, 0, 1, 1711929600000, 1, 1711929600000),
(801018, 'item', 'itemType', 'system', 18, 'GEOLOCATION',    '地理位置', 'option.item_type.geolocation',    18, 0, 1, 0, 1, 1711929600000, 1, 1711929600000),
(801019, 'item', 'itemType', 'system', 19, 'IMAGE',          '图片',     'option.item_type.image',          19, 0, 1, 0, 1, 1711929600000, 1, 1711929600000),
(801020, 'item', 'itemType', 'system', 20, 'AUTONUMBER',     '自动编号', 'option.item_type.autonumber',     20, 0, 1, 0, 1, 1711929600000, 1, 1711929600000),
(801021, 'item', 'itemType', 'system', 21, 'JOIN',           '引用',     'option.item_type.join',           21, 0, 1, 0, 1, 1711929600000, 1, 1711929600000),
(801022, 'item', 'itemType', 'system', 22, 'AUDIO',          '语音',     'option.item_type.audio',          22, 0, 1, 0, 1, 1711929600000, 1, 1711929600000);

-- item.cascadeDelete（级联删除策略）
INSERT INTO p_meta_option (id, metamodel_api_key, item_api_key, namespace, option_code, option_key, option_label, option_label_key, option_order, default_flg, enable_flg, delete_flg, created_by, created_at, updated_by, updated_at) VALUES
(801030, 'item', 'cascadeDelete', 'system', 0, 'NONE',    '不级联', 'option.cascade.none',    1, 1, 1, 0, 1, 1711929600000, 1, 1711929600000),
(801031, 'item', 'cascadeDelete', 'system', 1, 'CASCADE', '级联删除', 'option.cascade.cascade', 2, 0, 1, 0, 1, 1711929600000, 1, 1711929600000),
(801032, 'item', 'cascadeDelete', 'system', 2, 'PREVENT', '阻止删除', 'option.cascade.prevent', 3, 0, 1, 0, 1, 1711929600000, 1, 1711929600000);

-- item.dateMode（日期模式）
INSERT INTO p_meta_option (id, metamodel_api_key, item_api_key, namespace, option_code, option_key, option_label, option_label_key, option_order, default_flg, enable_flg, delete_flg, created_by, created_at, updated_by, updated_at) VALUES
(801040, 'item', 'dateMode', 'system', 1, 'DATE_ONLY',     '仅日期',     'option.date_mode.date_only',     1, 1, 1, 0, 1, 1711929600000, 1, 1711929600000),
(801041, 'item', 'dateMode', 'system', 2, 'DATE_AND_TIME', '日期+时间', 'option.date_mode.date_and_time', 2, 0, 1, 0, 1, 1711929600000, 1, 1711929600000);

-- item.currencyPart（货币部分）
INSERT INTO p_meta_option (id, metamodel_api_key, item_api_key, namespace, option_code, option_key, option_label, option_label_key, option_order, default_flg, enable_flg, delete_flg, created_by, created_at, updated_by, updated_at) VALUES
(801050, 'item', 'currencyPart', 'system', 1, 'LOCAL',    '本币', 'option.currency_part.local',    1, 1, 1, 0, 1, 1711929600000, 1, 1711929600000),
(801051, 'item', 'currencyPart', 'system', 2, 'ORIGINAL', '原币', 'option.currency_part.original', 2, 0, 1, 0, 1, 1711929600000, 1, 1711929600000);

-- --------------------------------------------------------
-- 2.3 entityLink 元模型的枚举字段
-- --------------------------------------------------------

-- entityLink.link_type（关联类型）
INSERT INTO p_meta_option (id, metamodel_api_key, item_api_key, namespace, option_code, option_key, option_label, option_label_key, option_order, default_flg, enable_flg, delete_flg, created_by, created_at, updated_by, updated_at) VALUES
(802001, 'entityLink', 'link_type', 'system', 0, 'LOOKUP',        'LOOKUP',  'option.link_type.lookup',        1, 1, 1, 0, 1, 1711929600000, 1, 1711929600000),
(802002, 'entityLink', 'link_type', 'system', 1, 'MASTER_DETAIL', '主从',    'option.link_type.master_detail', 2, 0, 1, 0, 1, 1711929600000, 1, 1711929600000),
(802003, 'entityLink', 'link_type', 'system', 2, 'MANY_TO_MANY',  '多对多',  'option.link_type.many_to_many',  3, 0, 1, 0, 1, 1711929600000, 1, 1711929600000);

-- entityLink.cascade_delete（级联删除）
INSERT INTO p_meta_option (id, metamodel_api_key, item_api_key, namespace, option_code, option_key, option_label, option_label_key, option_order, default_flg, enable_flg, delete_flg, created_by, created_at, updated_by, updated_at) VALUES
(802010, 'entityLink', 'cascade_delete', 'system', 0, 'NONE',    '不级联',   'option.cascade.none',    1, 1, 1, 0, 1, 1711929600000, 1, 1711929600000),
(802011, 'entityLink', 'cascade_delete', 'system', 1, 'CASCADE', '级联删除', 'option.cascade.cascade', 2, 0, 1, 0, 1, 1711929600000, 1, 1711929600000),
(802012, 'entityLink', 'cascade_delete', 'system', 2, 'PREVENT', '阻止删除', 'option.cascade.prevent', 3, 0, 1, 0, 1, 1711929600000, 1, 1711929600000);

-- entityLink.access_control（访问控制）
INSERT INTO p_meta_option (id, metamodel_api_key, item_api_key, namespace, option_code, option_key, option_label, option_label_key, option_order, default_flg, enable_flg, delete_flg, created_by, created_at, updated_by, updated_at) VALUES
(802020, 'entityLink', 'access_control', 'system', 0, 'NONE',      '无控制',   'option.access.none',      1, 1, 1, 0, 1, 1711929600000, 1, 1711929600000),
(802021, 'entityLink', 'access_control', 'system', 1, 'READ_WRITE','读写控制', 'option.access.read_write', 2, 0, 1, 0, 1, 1711929600000, 1, 1711929600000);

-- entityLink.enable_flg / detail_link（布尔）
INSERT INTO p_meta_option (id, metamodel_api_key, item_api_key, namespace, option_code, option_key, option_label, option_label_key, option_order, default_flg, enable_flg, delete_flg, created_by, created_at, updated_by, updated_at) VALUES
(802030, 'entityLink', 'enable_flg',  'system', 0, 'NO',  '否', 'option.bool.no',  1, 0, 1, 0, 1, 1711929600000, 1, 1711929600000),
(802031, 'entityLink', 'enable_flg',  'system', 1, 'YES', '是', 'option.bool.yes', 2, 1, 1, 0, 1, 1711929600000, 1, 1711929600000),
(802032, 'entityLink', 'detail_link', 'system', 0, 'NO',  '否', 'option.bool.no',  1, 0, 1, 0, 1, 1711929600000, 1, 1711929600000),
(802033, 'entityLink', 'detail_link', 'system', 1, 'YES', '是', 'option.bool.yes', 2, 0, 1, 0, 1, 1711929600000, 1, 1711929600000);

-- --------------------------------------------------------
-- 2.4 checkRule 元模型的枚举字段
-- --------------------------------------------------------

-- checkRule.activeFlg（激活状态）
INSERT INTO p_meta_option (id, metamodel_api_key, item_api_key, namespace, option_code, option_key, option_label, option_label_key, option_order, default_flg, enable_flg, delete_flg, created_by, created_at, updated_by, updated_at) VALUES
(803001, 'checkRule', 'activeFlg', 'system', 0, 'INACTIVE', '未激活', 'option.active.inactive', 1, 0, 1, 0, 1, 1711929600000, 1, 1711929600000),
(803002, 'checkRule', 'activeFlg', 'system', 1, 'ACTIVE',   '已激活', 'option.active.active',   2, 1, 1, 0, 1, 1711929600000, 1, 1711929600000);

-- checkRule.checkAllItemsFlg（全量更新标识）
INSERT INTO p_meta_option (id, metamodel_api_key, item_api_key, namespace, option_code, option_key, option_label, option_label_key, option_order, default_flg, enable_flg, delete_flg, created_by, created_at, updated_by, updated_at) VALUES
(803010, 'checkRule', 'checkAllItemsFlg', 'system', 0, 'INCREMENTAL', '增量更新', 'option.check_all.incremental', 1, 1, 1, 0, 1, 1711929600000, 1, 1711929600000),
(803011, 'checkRule', 'checkAllItemsFlg', 'system', 1, 'FULL',        '全量更新', 'option.check_all.full',        2, 0, 1, 0, 1, 1711929600000, 1, 1711929600000);

-- --------------------------------------------------------
-- 2.5 referenceFilter 元模型的枚举字段
-- --------------------------------------------------------

-- referenceFilter.filterMode（过滤模式）
INSERT INTO p_meta_option (id, metamodel_api_key, item_api_key, namespace, option_code, option_key, option_label, option_label_key, option_order, default_flg, enable_flg, delete_flg, created_by, created_at, updated_by, updated_at) VALUES
(804001, 'referenceFilter', 'filterMode', 'system', 0, 'NONE',    '无过滤',   'option.filter_mode.none',    1, 1, 1, 0, 1, 1711929600000, 1, 1711929600000),
(804002, 'referenceFilter', 'filterMode', 'system', 1, 'SIMPLE',  '简单过滤', 'option.filter_mode.simple',  2, 0, 1, 0, 1, 1711929600000, 1, 1711929600000),
(804003, 'referenceFilter', 'filterMode', 'system', 2, 'FORMULA', '公式过滤', 'option.filter_mode.formula', 3, 0, 1, 0, 1, 1711929600000, 1, 1711929600000);
