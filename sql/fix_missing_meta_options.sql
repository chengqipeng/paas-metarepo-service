-- ============================================================
-- 修复缺失的 MetaOption 种子数据
-- 补充 6 个 item_type=6 的字段缺少的选项值定义
-- ============================================================

-- 1. metamodel#1, item#105: enable_sharing（启用共享）
INSERT INTO p_meta_option (id, tenant_id, metamodel_id, item_id, option_code, option_key, option_label, option_label_key, option_order, default_flg, enable_flg, description, delete_flg, created_by, created_at, updated_by, updated_at, description_key)
VALUES
(351, 0, 1, 105, 0, 'DISABLED', '否', 'opt.enable_sharing.disabled', 1, 1, 1, '不启用共享', 0, 1, 1711929600000, 1, 1711929600000, 'opt.enable_sharing.disabled.desc'),
(352, 0, 1, 105, 1, 'ENABLED',  '是', 'opt.enable_sharing.enabled',  2, 0, 1, '启用共享',   0, 1, 1711929600000, 1, 1711929600000, 'opt.enable_sharing.enabled.desc');

-- 2. metamodel#1, item#106: enable_team（启用团队）
INSERT INTO p_meta_option (id, tenant_id, metamodel_id, item_id, option_code, option_key, option_label, option_label_key, option_order, default_flg, enable_flg, description, delete_flg, created_by, created_at, updated_by, updated_at, description_key)
VALUES
(353, 0, 1, 106, 0, 'DISABLED', '否', 'opt.enable_team.disabled', 1, 1, 1, '不启用团队', 0, 1, 1711929600000, 1, 1711929600000, 'opt.enable_team.disabled.desc'),
(354, 0, 1, 106, 1, 'ENABLED',  '是', 'opt.enable_team.enabled',  2, 0, 1, '启用团队',   0, 1, 1711929600000, 1, 1711929600000, 'opt.enable_team.enabled.desc');

-- 3. metamodel#1, item#107: searchable（可搜索）
INSERT INTO p_meta_option (id, tenant_id, metamodel_id, item_id, option_code, option_key, option_label, option_label_key, option_order, default_flg, enable_flg, description, delete_flg, created_by, created_at, updated_by, updated_at, description_key)
VALUES
(355, 0, 1, 107, 0, 'DISABLED', '否', 'opt.searchable.disabled', 1, 0, 1, '不可搜索', 0, 1, 1711929600000, 1, 1711929600000, 'opt.searchable.disabled.desc'),
(356, 0, 1, 107, 1, 'ENABLED',  '是', 'opt.searchable.enabled',  2, 1, 1, '可搜索',   0, 1, 1711929600000, 1, 1711929600000, 'opt.searchable.enabled.desc');

-- 4. metamodel#1, item#108: enable_report（启用报表）
INSERT INTO p_meta_option (id, tenant_id, metamodel_id, item_id, option_code, option_key, option_label, option_label_key, option_order, default_flg, enable_flg, description, delete_flg, created_by, created_at, updated_by, updated_at, description_key)
VALUES
(357, 0, 1, 108, 0, 'DISABLED', '否', 'opt.enable_report.disabled', 1, 0, 1, '不启用报表', 0, 1, 1711929600000, 1, 1711929600000, 'opt.enable_report.disabled.desc'),
(358, 0, 1, 108, 1, 'ENABLED',  '是', 'opt.enable_report.enabled',  2, 1, 1, '启用报表',   0, 1, 1711929600000, 1, 1711929600000, 'opt.enable_report.enabled.desc');

-- 5. metamodel#1, item#109: enable_api（启用API）
INSERT INTO p_meta_option (id, tenant_id, metamodel_id, item_id, option_code, option_key, option_label, option_label_key, option_order, default_flg, enable_flg, description, delete_flg, created_by, created_at, updated_by, updated_at, description_key)
VALUES
(359, 0, 1, 109, 0, 'DISABLED', '否', 'opt.enable_api.disabled', 1, 0, 1, '不启用API', 0, 1, 1711929600000, 1, 1711929600000, 'opt.enable_api.disabled.desc'),
(360, 0, 1, 109, 1, 'ENABLED',  '是', 'opt.enable_api.enabled',  2, 1, 1, '启用API',   0, 1, 1711929600000, 1, 1711929600000, 'opt.enable_api.enabled.desc');

-- 6. metamodel#2, item#205: require_flg（是否必填）
INSERT INTO p_meta_option (id, tenant_id, metamodel_id, item_id, option_code, option_key, option_label, option_label_key, option_order, default_flg, enable_flg, description, delete_flg, created_by, created_at, updated_by, updated_at, description_key)
VALUES
(361, 0, 2, 205, 0, 'NOT_REQUIRED', '否', 'opt.require_flg.not_required', 1, 1, 1, '非必填', 0, 1, 1711929600000, 1, 1711929600000, 'opt.require_flg.not_required.desc'),
(362, 0, 2, 205, 1, 'REQUIRED',     '是', 'opt.require_flg.required',     2, 0, 1, '必填',   0, 1, 1711929600000, 1, 1711929600000, 'opt.require_flg.required.desc');
