-- ============================================================
-- 为 p_meta_item (metamodel_api_key='item') 中 db_column 为 NULL 的行
-- 分配 dbc 列映射
--
-- 执行库：paas_metarepo_common
-- ============================================================

-- Step 1: 扩展大宽表 dbc_smallint 列（15 → 50）
ALTER TABLE p_common_metadata ADD COLUMN dbc_smallint16 SMALLINT;
ALTER TABLE p_common_metadata ADD COLUMN dbc_smallint17 SMALLINT;
ALTER TABLE p_common_metadata ADD COLUMN dbc_smallint18 SMALLINT;
ALTER TABLE p_common_metadata ADD COLUMN dbc_smallint19 SMALLINT;
ALTER TABLE p_common_metadata ADD COLUMN dbc_smallint20 SMALLINT;
ALTER TABLE p_common_metadata ADD COLUMN dbc_smallint21 SMALLINT;
ALTER TABLE p_common_metadata ADD COLUMN dbc_smallint22 SMALLINT;
ALTER TABLE p_common_metadata ADD COLUMN dbc_smallint23 SMALLINT;
ALTER TABLE p_common_metadata ADD COLUMN dbc_smallint24 SMALLINT;
ALTER TABLE p_common_metadata ADD COLUMN dbc_smallint25 SMALLINT;
ALTER TABLE p_common_metadata ADD COLUMN dbc_smallint26 SMALLINT;
ALTER TABLE p_common_metadata ADD COLUMN dbc_smallint27 SMALLINT;
ALTER TABLE p_common_metadata ADD COLUMN dbc_smallint28 SMALLINT;
ALTER TABLE p_common_metadata ADD COLUMN dbc_smallint29 SMALLINT;
ALTER TABLE p_common_metadata ADD COLUMN dbc_smallint30 SMALLINT;
ALTER TABLE p_common_metadata ADD COLUMN dbc_smallint31 SMALLINT;
ALTER TABLE p_common_metadata ADD COLUMN dbc_smallint32 SMALLINT;
ALTER TABLE p_common_metadata ADD COLUMN dbc_smallint33 SMALLINT;
ALTER TABLE p_common_metadata ADD COLUMN dbc_smallint34 SMALLINT;
ALTER TABLE p_common_metadata ADD COLUMN dbc_smallint35 SMALLINT;
ALTER TABLE p_common_metadata ADD COLUMN dbc_smallint36 SMALLINT;
ALTER TABLE p_common_metadata ADD COLUMN dbc_smallint37 SMALLINT;
ALTER TABLE p_common_metadata ADD COLUMN dbc_smallint38 SMALLINT;
ALTER TABLE p_common_metadata ADD COLUMN dbc_smallint39 SMALLINT;
ALTER TABLE p_common_metadata ADD COLUMN dbc_smallint40 SMALLINT;
ALTER TABLE p_common_metadata ADD COLUMN dbc_smallint41 SMALLINT;
ALTER TABLE p_common_metadata ADD COLUMN dbc_smallint42 SMALLINT;
ALTER TABLE p_common_metadata ADD COLUMN dbc_smallint43 SMALLINT;
ALTER TABLE p_common_metadata ADD COLUMN dbc_smallint44 SMALLINT;
ALTER TABLE p_common_metadata ADD COLUMN dbc_smallint45 SMALLINT;
ALTER TABLE p_common_metadata ADD COLUMN dbc_smallint46 SMALLINT;
ALTER TABLE p_common_metadata ADD COLUMN dbc_smallint47 SMALLINT;
ALTER TABLE p_common_metadata ADD COLUMN dbc_smallint48 SMALLINT;
ALTER TABLE p_common_metadata ADD COLUMN dbc_smallint49 SMALLINT;
ALTER TABLE p_common_metadata ADD COLUMN dbc_smallint50 SMALLINT;

-- Step 2: 为 db_column=NULL 的 item 字段分配 dbc 列
-- 注意：列名格式与老数据一致，使用 dbc_smallintN（无下划线分隔数字）

-- Integer 类型 → dbc_int（已用 1~12，从 13 开始）
UPDATE p_meta_item SET db_column = 'dbc_int13'      WHERE metamodel_api_key = 'item' AND api_key = 'maxLength'       AND (db_column IS NULL OR db_column = '');
UPDATE p_meta_item SET db_column = 'dbc_int14'      WHERE metamodel_api_key = 'item' AND api_key = 'minLength'       AND (db_column IS NULL OR db_column = '');
UPDATE p_meta_item SET db_column = 'dbc_int15'      WHERE metamodel_api_key = 'item' AND api_key = 'decimal'         AND (db_column IS NULL OR db_column = '');

-- Smallint 布尔/开关类 → dbc_smallint（已用 1~12，从 13 开始）
UPDATE p_meta_item SET db_column = 'dbc_smallint13' WHERE metamodel_api_key = 'item' AND api_key = 'isCurrency'      AND (db_column IS NULL OR db_column = '');
UPDATE p_meta_item SET db_column = 'dbc_smallint14' WHERE metamodel_api_key = 'item' AND api_key = 'currencyPart'    AND (db_column IS NULL OR db_column = '');
UPDATE p_meta_item SET db_column = 'dbc_smallint15' WHERE metamodel_api_key = 'item' AND api_key = 'isMultiCurrency' AND (db_column IS NULL OR db_column = '');
UPDATE p_meta_item SET db_column = 'dbc_smallint16' WHERE metamodel_api_key = 'item' AND api_key = 'currencyFlg'     AND (db_column IS NULL OR db_column = '');
UPDATE p_meta_item SET db_column = 'dbc_smallint17' WHERE metamodel_api_key = 'item' AND api_key = 'computeType'     AND (db_column IS NULL OR db_column = '');
UPDATE p_meta_item SET db_column = 'dbc_smallint18' WHERE metamodel_api_key = 'item' AND api_key = 'realTimeCompute' AND (db_column IS NULL OR db_column = '');
UPDATE p_meta_item SET db_column = 'dbc_smallint19' WHERE metamodel_api_key = 'item' AND api_key = 'referGlobal'     AND (db_column IS NULL OR db_column = '');
UPDATE p_meta_item SET db_column = 'dbc_smallint20' WHERE metamodel_api_key = 'item' AND api_key = 'isExternal'      AND (db_column IS NULL OR db_column = '');
UPDATE p_meta_item SET db_column = 'dbc_smallint21' WHERE metamodel_api_key = 'item' AND api_key = 'dateMode'        AND (db_column IS NULL OR db_column = '');
UPDATE p_meta_item SET db_column = 'dbc_smallint22' WHERE metamodel_api_key = 'item' AND api_key = 'cascadeDelete'   AND (db_column IS NULL OR db_column = '');
UPDATE p_meta_item SET db_column = 'dbc_smallint23' WHERE metamodel_api_key = 'item' AND api_key = 'isDetail'        AND (db_column IS NULL OR db_column = '');
UPDATE p_meta_item SET db_column = 'dbc_smallint24' WHERE metamodel_api_key = 'item' AND api_key = 'canBatchCreate'  AND (db_column IS NULL OR db_column = '');
UPDATE p_meta_item SET db_column = 'dbc_smallint25' WHERE metamodel_api_key = 'item' AND api_key = 'isCopyWithParent' AND (db_column IS NULL OR db_column = '');
UPDATE p_meta_item SET db_column = 'dbc_smallint26' WHERE metamodel_api_key = 'item' AND api_key = 'isMask'          AND (db_column IS NULL OR db_column = '');
UPDATE p_meta_item SET db_column = 'dbc_smallint27' WHERE metamodel_api_key = 'item' AND api_key = 'entityOrData'    AND (db_column IS NULL OR db_column = '');
UPDATE p_meta_item SET db_column = 'dbc_smallint28' WHERE metamodel_api_key = 'item' AND api_key = 'compoundSub'     AND (db_column IS NULL OR db_column = '');
UPDATE p_meta_item SET db_column = 'dbc_smallint29' WHERE metamodel_api_key = 'item' AND api_key = 'multiLineText'   AND (db_column IS NULL OR db_column = '');
UPDATE p_meta_item SET db_column = 'dbc_smallint30' WHERE metamodel_api_key = 'item' AND api_key = 'watermarkFlg'    AND (db_column IS NULL OR db_column = '');
UPDATE p_meta_item SET db_column = 'dbc_smallint31' WHERE metamodel_api_key = 'item' AND api_key = 'enableWatermarkTime' AND (db_column IS NULL OR db_column = '');
UPDATE p_meta_item SET db_column = 'dbc_smallint32' WHERE metamodel_api_key = 'item' AND api_key = 'enableWatermarkLoginUser' AND (db_column IS NULL OR db_column = '');
UPDATE p_meta_item SET db_column = 'dbc_smallint33' WHERE metamodel_api_key = 'item' AND api_key = 'enableWatermarkLocation' AND (db_column IS NULL OR db_column = '');
UPDATE p_meta_item SET db_column = 'dbc_smallint34' WHERE metamodel_api_key = 'item' AND api_key = 'extTable'        AND (db_column IS NULL OR db_column = '');
UPDATE p_meta_item SET db_column = 'dbc_smallint35' WHERE metamodel_api_key = 'item' AND api_key = 'scanCodeEntryFlg' AND (db_column IS NULL OR db_column = '');
UPDATE p_meta_item SET db_column = 'dbc_smallint36' WHERE metamodel_api_key = 'item' AND api_key = 'isRebuild'       AND (db_column IS NULL OR db_column = '');
UPDATE p_meta_item SET db_column = 'dbc_smallint37' WHERE metamodel_api_key = 'item' AND api_key = 'accessControl'   AND (db_column IS NULL OR db_column = '');
UPDATE p_meta_item SET db_column = 'dbc_smallint38' WHERE metamodel_api_key = 'item' AND api_key = 'uploadFlg'       AND (db_column IS NULL OR db_column = '');
UPDATE p_meta_item SET db_column = 'dbc_smallint39' WHERE metamodel_api_key = 'item' AND api_key = 'globalSearch'    AND (db_column IS NULL OR db_column = '');
UPDATE p_meta_item SET db_column = 'dbc_smallint40' WHERE metamodel_api_key = 'item' AND api_key = 'maskType'        AND (db_column IS NULL OR db_column = '');
UPDATE p_meta_item SET db_column = 'dbc_smallint41' WHERE metamodel_api_key = 'item' AND api_key = 'textFormat'      AND (db_column IS NULL OR db_column = '');
UPDATE p_meta_item SET db_column = 'dbc_smallint42' WHERE metamodel_api_key = 'item' AND api_key = 'showRows'        AND (db_column IS NULL OR db_column = '');
UPDATE p_meta_item SET db_column = 'dbc_smallint43' WHERE metamodel_api_key = 'item' AND api_key = 'caseSensitive'   AND (db_column IS NULL OR db_column = '');
UPDATE p_meta_item SET db_column = 'dbc_smallint44' WHERE metamodel_api_key = 'item' AND api_key = 'batchCreateMode' AND (db_column IS NULL OR db_column = '');
UPDATE p_meta_item SET db_column = 'dbc_smallint45' WHERE metamodel_api_key = 'item' AND api_key = 'enableMultiDetail' AND (db_column IS NULL OR db_column = '');
UPDATE p_meta_item SET db_column = 'dbc_smallint46' WHERE metamodel_api_key = 'item' AND api_key = 'batchCreateBaseLink' AND (db_column IS NULL OR db_column = '');

-- String 类型 → dbc_varchar（已用 1~9，从 10 开始）
UPDATE p_meta_item SET db_column = 'dbc_varchar10'  WHERE metamodel_api_key = 'item' AND api_key = 'globalPickItem'  AND (db_column IS NULL OR db_column = '');
UPDATE p_meta_item SET db_column = 'dbc_varchar11'  WHERE metamodel_api_key = 'item' AND api_key = 'globalPickItemApiKey' AND (db_column IS NULL OR db_column = '');
UPDATE p_meta_item SET db_column = 'dbc_varchar12'  WHERE metamodel_api_key = 'item' AND api_key = 'joinItem'        AND (db_column IS NULL OR db_column = '');
UPDATE p_meta_item SET db_column = 'dbc_varchar13'  WHERE metamodel_api_key = 'item' AND api_key = 'joinObject'      AND (db_column IS NULL OR db_column = '');
UPDATE p_meta_item SET db_column = 'dbc_varchar14'  WHERE metamodel_api_key = 'item' AND api_key = 'joinLink'        AND (db_column IS NULL OR db_column = '');
UPDATE p_meta_item SET db_column = 'dbc_varchar15'  WHERE metamodel_api_key = 'item' AND api_key = 'linkLabel'       AND (db_column IS NULL OR db_column = '');
UPDATE p_meta_item SET db_column = 'dbc_varchar16'  WHERE metamodel_api_key = 'item' AND api_key = 'referEntityIds'  AND (db_column IS NULL OR db_column = '');
UPDATE p_meta_item SET db_column = 'dbc_varchar17'  WHERE metamodel_api_key = 'item' AND api_key = 'groupKey'        AND (db_column IS NULL OR db_column = '');
UPDATE p_meta_item SET db_column = 'dbc_varchar18'  WHERE metamodel_api_key = 'item' AND api_key = 'compoundApiKey'  AND (db_column IS NULL OR db_column = '');
UPDATE p_meta_item SET db_column = 'dbc_varchar19'  WHERE metamodel_api_key = 'item' AND api_key = 'watermarkJoinField' AND (db_column IS NULL OR db_column = '');
UPDATE p_meta_item SET db_column = 'dbc_varchar20'  WHERE metamodel_api_key = 'item' AND api_key = 'enableWatermarkJoinField' AND (db_column IS NULL OR db_column = '');
UPDATE p_meta_item SET db_column = 'dbc_varchar21'  WHERE metamodel_api_key = 'item' AND api_key = 'joinItemKey'     AND (db_column IS NULL OR db_column = '');
UPDATE p_meta_item SET db_column = 'dbc_varchar22'  WHERE metamodel_api_key = 'item' AND api_key = 'dataFormat'      AND (db_column IS NULL OR db_column = '');
UPDATE p_meta_item SET db_column = 'dbc_varchar23'  WHERE metamodel_api_key = 'item' AND api_key = 'startNumber'     AND (db_column IS NULL OR db_column = '');
UPDATE p_meta_item SET db_column = 'dbc_varchar24'  WHERE metamodel_api_key = 'item' AND api_key = 'displayFormat'   AND (db_column IS NULL OR db_column = '');
UPDATE p_meta_item SET db_column = 'dbc_varchar25'  WHERE metamodel_api_key = 'item' AND api_key = 'locationType'    AND (db_column IS NULL OR db_column = '');
UPDATE p_meta_item SET db_column = 'dbc_varchar26'  WHERE metamodel_api_key = 'item' AND api_key = 'isComputeMultiCurrency' AND (db_column IS NULL OR db_column = '');
UPDATE p_meta_item SET db_column = 'dbc_varchar27'  WHERE metamodel_api_key = 'item' AND api_key = 'syncMask'        AND (db_column IS NULL OR db_column = '');
UPDATE p_meta_item SET db_column = 'dbc_varchar28'  WHERE metamodel_api_key = 'item' AND api_key = 'unique'          AND (db_column IS NULL OR db_column = '');
UPDATE p_meta_item SET db_column = 'dbc_varchar29'  WHERE metamodel_api_key = 'item' AND api_key = 'aggregateComputeType' AND (db_column IS NULL OR db_column = '');
