-- ============================================================
-- 修复 pickOption 和 referenceFilter 的 p_meta_item.db_column
--
-- 问题：这两个元模型的多个字段 db_column 写的是不存在的固定列名
--       （如 item_api_key, option_order, default_flg 等），
--       但大宽表 p_common_metadata 中没有这些固定列，
--       导致 CommonMetadataConverter 无法映射，字段值永远为 NULL。
--
-- 修复：将非固定列的 db_column 改为 dbc_xxxN 格式。
--       每个元模型的 dbc 列序号独立分配，互不冲突。
--
-- 执行库：paas_metarepo_common
-- ============================================================

-- ============================================================
-- pickOption 元模型修复
-- 固定列（无需修改）：namespace, entity_api_key, api_key, label, label_key, custom_flg, delete_flg, description, created_by/at, updated_by/at
-- 需要改为 dbc 列的：item_api_key, option_order, default_flg, global_flg, enable_flg, special_flg
-- ============================================================

-- item_api_key → 子元数据关联父字段，应使用 parent_metadata_api_key 固定列
-- 但 pickOption 的 itemApiKey 语义是"所属字段"，对应大宽表的 parent_metadata_api_key
-- 方案：改用 dbc_varchar1 存储 itemApiKey
UPDATE p_meta_item SET db_column = 'dbc_varchar1'
WHERE metamodel_api_key = 'pickOption' AND api_key = 'itemApiKey';

-- optionOrder → dbc_int1（整数排序号）
UPDATE p_meta_item SET db_column = 'dbc_int1'
WHERE metamodel_api_key = 'pickOption' AND api_key = 'optionOrder';

-- isDefault → dbc_smallint1（布尔 0/1）
UPDATE p_meta_item SET db_column = 'dbc_smallint1'
WHERE metamodel_api_key = 'pickOption' AND api_key = 'isDefault';

-- isGlobal → dbc_smallint2（布尔 0/1）
UPDATE p_meta_item SET db_column = 'dbc_smallint2'
WHERE metamodel_api_key = 'pickOption' AND api_key = 'isGlobal';

-- isActive → dbc_smallint3（布尔 0/1）
UPDATE p_meta_item SET db_column = 'dbc_smallint3'
WHERE metamodel_api_key = 'pickOption' AND api_key = 'isActive';

-- specialFlg → dbc_smallint4（整数标记）
UPDATE p_meta_item SET db_column = 'dbc_smallint4'
WHERE metamodel_api_key = 'pickOption' AND api_key = 'specialFlg';

-- optionType → dbc_int2（整数类型，原来是 NULL，现在分配列）
UPDATE p_meta_item SET db_column = 'dbc_int2'
WHERE metamodel_api_key = 'pickOption' AND api_key = 'optionType';

-- ============================================================
-- referenceFilter 元模型修复
-- 固定列（无需修改）：namespace, entity_api_key, delete_flg, created_by/at, updated_by/at
-- 需要改为 dbc 列的：item_api_key, filter_mode, filter_formula, active_flg, andor
-- ============================================================

-- itemApiKey → dbc_varchar1
UPDATE p_meta_item SET db_column = 'dbc_varchar1'
WHERE metamodel_api_key = 'referenceFilter' AND api_key = 'itemApiKey';

-- filterMode → dbc_smallint1（选择值：0=无过滤, 1=简单过滤, 2=公式过滤）
UPDATE p_meta_item SET db_column = 'dbc_smallint1'
WHERE metamodel_api_key = 'referenceFilter' AND api_key = 'filterMode';

-- filterFormula → dbc_textarea1（长文本，过滤表达式）
UPDATE p_meta_item SET db_column = 'dbc_textarea1'
WHERE metamodel_api_key = 'referenceFilter' AND api_key = 'filterFormula';

-- isActive → dbc_smallint2（布尔 0/1）
UPDATE p_meta_item SET db_column = 'dbc_smallint2'
WHERE metamodel_api_key = 'referenceFilter' AND api_key = 'isActive';

-- andor → dbc_smallint3（整数：0=AND, 1=OR）
UPDATE p_meta_item SET db_column = 'dbc_smallint3'
WHERE metamodel_api_key = 'referenceFilter' AND api_key = 'andor';

-- ============================================================
-- 验证
-- ============================================================
SELECT metamodel_api_key, api_key, db_column,
    CASE
        WHEN db_column IN ('namespace','api_key','label','label_key','entity_api_key',
                           'parent_metadata_api_key','custom_flg','metadata_order',
                           'description','meta_version','delete_flg',
                           'created_at','created_by','updated_at','updated_by',
                           'metadata_api_key','name') THEN 'OK-固定列'
        WHEN db_column LIKE 'dbc_%' THEN 'OK-dbc列'
        WHEN db_column IS NULL THEN 'NULL-无物理列'
        ELSE 'ERROR-不存在的列'
    END AS status
FROM p_meta_item
WHERE metamodel_api_key IN ('pickOption', 'referenceFilter')
  AND delete_flg = 0
ORDER BY metamodel_api_key, item_order;
