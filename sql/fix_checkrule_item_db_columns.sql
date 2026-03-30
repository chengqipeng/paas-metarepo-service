-- ============================================================
-- 修复 checkRule 和 item(name) 的 p_meta_item.db_column
--
-- 问题：checkRule 的 8 个字段 db_column 写的是不存在的固定列名
--       （如 active_flg, check_formula 等），大宽表中没有这些列。
--       item 的 name 字段同理。
--
-- 修复：改为 dbc_xxxN 格式。
-- 执行库：paas_metarepo_common
-- ============================================================

-- ============================================================
-- checkRule 元模型修复（8 个字段）
-- 已有固定列映射（无需修改）：namespace, entity_api_key, api_key, description, label, label_key, created_by/at, updated_by/at
-- 需要改为 dbc 列的：active_flg, check_formula, check_error_msg, check_error_msg_key,
--                    check_error_location, check_error_way, check_error_item_api_key, check_all_items_flg
-- ============================================================

-- activeFlg → dbc_smallint1（布尔 0/1）
UPDATE p_meta_item SET db_column = 'dbc_smallint1'
WHERE metamodel_api_key = 'checkRule' AND api_key = 'activeFlg';

-- checkFormula → dbc_textarea1（长文本，公式表达式）
UPDATE p_meta_item SET db_column = 'dbc_textarea1'
WHERE metamodel_api_key = 'checkRule' AND api_key = 'checkFormula';

-- checkErrorMsg → dbc_varchar1（错误提示文本）
UPDATE p_meta_item SET db_column = 'dbc_varchar1'
WHERE metamodel_api_key = 'checkRule' AND api_key = 'checkErrorMsg';

-- checkErrorMsgKey → dbc_varchar2（错误提示多语言Key）
UPDATE p_meta_item SET db_column = 'dbc_varchar2'
WHERE metamodel_api_key = 'checkRule' AND api_key = 'checkErrorMsgKey';

-- checkErrorLocation → dbc_smallint2（整数，错误显示位置）
UPDATE p_meta_item SET db_column = 'dbc_smallint2'
WHERE metamodel_api_key = 'checkRule' AND api_key = 'checkErrorLocation';

-- checkErrorWay → dbc_smallint3（整数，错误提示方式）
UPDATE p_meta_item SET db_column = 'dbc_smallint3'
WHERE metamodel_api_key = 'checkRule' AND api_key = 'checkErrorWay';

-- checkErrorItemApiKey → dbc_varchar3（关联字段apiKey）
UPDATE p_meta_item SET db_column = 'dbc_varchar3'
WHERE metamodel_api_key = 'checkRule' AND api_key = 'checkErrorItemApiKey';

-- checkAllItemsFlg → dbc_smallint4（布尔 0/1）
UPDATE p_meta_item SET db_column = 'dbc_smallint4'
WHERE metamodel_api_key = 'checkRule' AND api_key = 'checkAllItemsFlg';

-- ============================================================
-- item 元模型修复（1 个字段）
-- name 不是大宽表固定列，改为 dbc_varchar 列
-- 注意：item 的 dbc_varchar 已用到 29，从 30 开始
-- ============================================================

UPDATE p_meta_item SET db_column = 'dbc_varchar30'
WHERE metamodel_api_key = 'item' AND api_key = 'name';

-- ============================================================
-- 验证
-- ============================================================
SELECT metamodel_api_key, api_key, db_column,
    CASE
        WHEN db_column IN ('namespace','api_key','label','label_key','entity_api_key',
                           'parent_metadata_api_key','custom_flg','metadata_order',
                           'description','meta_version','delete_flg','metadata_api_key',
                           'created_at','created_by','updated_at','updated_by') THEN 'OK-固定列'
        WHEN db_column LIKE 'dbc_%' THEN 'OK-dbc列'
        WHEN db_column IS NULL THEN 'NULL'
        ELSE 'ERROR-不存在'
    END AS status
FROM p_meta_item
WHERE metamodel_api_key IN ('checkRule', 'item')
  AND delete_flg = 0
  AND (
    (metamodel_api_key = 'checkRule')
    OR (metamodel_api_key = 'item' AND api_key = 'name')
  )
ORDER BY metamodel_api_key, item_order;
