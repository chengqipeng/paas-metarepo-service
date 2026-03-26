-- ============================================================
-- 统一元模型表的 label/label_key 列名
-- 兼容 MySQL 和 PostgreSQL
-- ============================================================

-- 1. p_custom_check_rule: rule_label -> label, rule_label_key -> label_key
ALTER TABLE p_custom_check_rule RENAME COLUMN rule_label TO label;
ALTER TABLE p_custom_check_rule RENAME COLUMN rule_label_key TO label_key;

-- 2. p_custom_pickoption: option_label -> label, option_label_key -> label_key
ALTER TABLE p_custom_pickoption RENAME COLUMN option_label TO label;
ALTER TABLE p_custom_pickoption RENAME COLUMN option_label_key TO label_key;

-- 3. p_common_check_rule（如果存在）: rule_label -> label, rule_label_key -> label_key
-- ALTER TABLE p_common_check_rule RENAME COLUMN rule_label TO label;
-- ALTER TABLE p_common_check_rule RENAME COLUMN rule_label_key TO label_key;

-- 4. p_common_pickoption（如果存在）: option_label -> label, option_label_key -> label_key
-- ALTER TABLE p_common_pickoption RENAME COLUMN option_label TO label;
-- ALTER TABLE p_common_pickoption RENAME COLUMN option_label_key TO label_key;

-- 5. p_meta_option: option_label -> label, option_label_key -> label_key
ALTER TABLE p_meta_option RENAME COLUMN option_label TO label;
ALTER TABLE p_meta_option RENAME COLUMN option_label_key TO label_key;
