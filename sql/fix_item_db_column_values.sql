-- ============================================================
-- 修复 p_common_metadata 中所有 item 数据的 item_type 和 db_column
--
-- 老→新 item_type 映射：
--   1(TEXT)→1, 2(TEXTAREA)→8, 3(SELECT)→4, 4(MULTISELECT)→16,
--   5(INTEGER)→2, 6(REAL)→10, 7(DATE)→3, 10(RELATION)→5,
--   21(DATETIME)→15, 22(PHONE)→13, 26(JOIN)→21, 27(FORMULA)→6,
--   31(BOOLEAN)→9, 33(PERCENTAGE)→11
--
-- p_meta_item 映射：itemType→dbc_int1, dbColumn→dbc_varchar3
-- ============================================================

-- Step 1: 用 CASE 一次性更新 item_type，避免顺序依赖问题
UPDATE p_common_metadata
SET dbc_int1 = CASE dbc_int1
    WHEN 1  THEN 1    -- TEXT → TEXT
    WHEN 2  THEN 8    -- TEXTAREA → TEXTAREA
    WHEN 3  THEN 4    -- SELECT → PICKLIST
    WHEN 4  THEN 16   -- MULTISELECT → MULTIPICKLIST
    WHEN 5  THEN 2    -- INTEGER → NUMBER
    WHEN 6  THEN 10   -- REAL → CURRENCY
    WHEN 7  THEN 3    -- DATE → DATE
    WHEN 10 THEN 5    -- RELATION → LOOKUP
    WHEN 21 THEN 15   -- DATETIME → DATETIME
    WHEN 22 THEN 13   -- PHONE → PHONE
    WHEN 26 THEN 21   -- JOIN → JOIN
    WHEN 27 THEN 6    -- FORMULA → FORMULA
    WHEN 31 THEN 9    -- BOOLEAN → BOOLEAN
    WHEN 33 THEN 11   -- PERCENTAGE → PERCENT
    ELSE dbc_int1     -- 99 等保留
END
WHERE metamodel_api_key = 'item'
  AND dbc_int1 IN (1,2,3,4,5,6,7,10,21,22,26,27,31,33);

-- Step 2: 根据新 item_type 分配 db_column（dbc_varchar3）
-- 使用 ItemTypeEnum.dbColumnPrefix + 按 entity 内同类型递增编号
-- FORMULA(6), ROLLUP(7), JOIN(21) 不占物理列，db_column 设为 NULL

-- 清空不占物理列的类型
UPDATE p_common_metadata
SET dbc_varchar3 = NULL
WHERE metamodel_api_key = 'item'
  AND dbc_int1 IN (6, 7, 21);  -- FORMULA, ROLLUP, JOIN

-- 为每个 entity 的每种列前缀类型分配递增编号
-- 使用存储过程批量处理
DROP PROCEDURE IF EXISTS assign_db_columns;

DELIMITER //
CREATE PROCEDURE assign_db_columns()
BEGIN
    DECLARE done INT DEFAULT FALSE;
    DECLARE v_id BIGINT;
    DECLARE v_entity VARCHAR(255);
    DECLARE v_item_type INT;
    DECLARE v_prefix VARCHAR(50);
    DECLARE v_last_entity VARCHAR(255) DEFAULT '';
    DECLARE v_counters TEXT DEFAULT '';

    -- 按 entity + item_order 排序遍历所有 item
    DECLARE cur CURSOR FOR
        SELECT id, entity_api_key, dbc_int1
        FROM p_common_metadata
        WHERE metamodel_api_key = 'item'
          AND dbc_int1 IS NOT NULL
          AND dbc_int1 NOT IN (6, 7, 21)  -- 排除无物理列的类型
        ORDER BY entity_api_key, dbc_int3;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

    -- 临时表存储每个 entity+prefix 的计数器
    DROP TEMPORARY TABLE IF EXISTS tmp_prefix_counter;
    CREATE TEMPORARY TABLE tmp_prefix_counter (
        entity_key VARCHAR(255),
        col_prefix VARCHAR(50),
        counter INT DEFAULT 0,
        PRIMARY KEY (entity_key, col_prefix)
    );

    OPEN cur;
    read_loop: LOOP
        FETCH cur INTO v_id, v_entity, v_item_type;
        IF done THEN LEAVE read_loop; END IF;

        -- 根据 item_type 确定列前缀
        SET v_prefix = CASE v_item_type
            WHEN 1  THEN 'dbc_varchar'    -- TEXT
            WHEN 2  THEN 'dbc_bigint'     -- NUMBER
            WHEN 3  THEN 'dbc_bigint'     -- DATE
            WHEN 4  THEN 'dbc_int'        -- PICKLIST
            WHEN 5  THEN 'dbc_bigint'     -- LOOKUP
            WHEN 8  THEN 'dbc_textarea'   -- TEXTAREA
            WHEN 9  THEN 'dbc_smallint'   -- BOOLEAN
            WHEN 10 THEN 'dbc_decimal'    -- CURRENCY
            WHEN 11 THEN 'dbc_decimal'    -- PERCENT
            WHEN 13 THEN 'dbc_varchar'    -- PHONE
            WHEN 14 THEN 'dbc_varchar'    -- URL
            WHEN 15 THEN 'dbc_bigint'     -- DATETIME
            WHEN 16 THEN 'dbc_varchar'    -- MULTIPICKLIST
            WHEN 17 THEN 'dbc_bigint'     -- MASTER_DETAIL
            WHEN 18 THEN 'dbc_varchar'    -- GEOLOCATION
            WHEN 19 THEN 'dbc_varchar'    -- IMAGE
            WHEN 20 THEN 'dbc_varchar'    -- AUTONUMBER
            WHEN 22 THEN 'dbc_varchar'    -- AUDIO
            ELSE NULL
        END;

        IF v_prefix IS NOT NULL THEN
            -- 获取或初始化计数器
            INSERT INTO tmp_prefix_counter (entity_key, col_prefix, counter)
            VALUES (v_entity, v_prefix, 0)
            ON DUPLICATE KEY UPDATE counter = counter;

            -- 递增计数器
            UPDATE tmp_prefix_counter
            SET counter = counter + 1
            WHERE entity_key = v_entity AND col_prefix = v_prefix;

            -- 读取当前计数
            SELECT counter INTO @cur_idx FROM tmp_prefix_counter
            WHERE entity_key = v_entity AND col_prefix = v_prefix;

            -- 更新 db_column
            UPDATE p_common_metadata
            SET dbc_varchar3 = CONCAT(v_prefix, @cur_idx)
            WHERE id = v_id;
        END IF;
    END LOOP;
    CLOSE cur;

    DROP TEMPORARY TABLE IF EXISTS tmp_prefix_counter;
END //
DELIMITER ;

CALL assign_db_columns();
DROP PROCEDURE IF EXISTS assign_db_columns;

-- 验证结果
SELECT entity_api_key, api_key, dbc_int1 AS new_item_type, dbc_varchar3 AS new_db_column, dbc_int3 AS item_order
FROM p_common_metadata
WHERE metamodel_api_key = 'item' AND entity_api_key = 'account'
ORDER BY dbc_int3
LIMIT 30;