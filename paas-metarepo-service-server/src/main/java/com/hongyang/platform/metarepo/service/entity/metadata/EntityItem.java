package com.hongyang.platform.metarepo.service.entity.metadata;

import com.hongyang.framework.dao.entity.BaseMetaTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 字段定义（Common/Tenant 共用）
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class EntityItem extends BaseMetaTenantEntity {

    private String entityApiKey;
    private Integer itemType;
    private Integer dataType;
    private String helpText;
    private String helpTextKey;
    private String description;
    private String descriptionKey;
    private Integer customItemSeq;
    private String defaultValue;
    private Integer requireFlg;
    private Integer customFlg;
    private Integer enableFlg;
    private Integer creatable;
    private Integer updatable;
    private Integer uniqueKeyFlg;
    private Integer enableHistoryLog;
    private Long enableConfig;
    private Long enablePackage;
    private Integer readonlyStatus;
    private Integer visibleStatus;
    private Integer hiddenFlg;
    private String referEntityApiKey;
    private String referLinkApiKey;
    private String dbColumn;
    private Integer itemOrder;
    private Integer sortFlg;
    private String columnName;
    private Integer enableDeactive;
    /** 是否复合字段 */
    private Integer compound;
    /** 掩码前缀位数 */
    private Integer maskPrefix;
    /** 掩码后缀位数 */
    private Integer maskSuffix;
    /** 是否加密存储 */
    private Integer encrypt;
    /** 索引顺序 */
    private Integer indexOrder;
    /** 索引类型 */
    private Integer indexType;
    /** 是否 Markdown 编辑器 */
    private Integer markdown;
    /** 掩码符号类型 */
    private Integer maskSymbolType;
    /** 自动编号递增策略 */
    private Integer incrementStrategy;
    /** 关联字段过滤开关 */
    private Integer referItemFilterEnable;
    /** 汇总字段币种单位 */
    private String isComputeMultiCurrencyUnit;
    /** 自动编号格式，如 "ORD-{0000}" */
    private String format;

    // ==================== 原 typeProperty JSON 中的字段（已独立化） ====================

    // --- 通用长度/精度 ---
    /** 字段最大长度（TEXT/INTEGER/REAL/PERCENTAGE/TEXTAREA/URL/EMAIL/PHONE/IMAGE 通用） */
    private Integer maxLength;
    /** 字段最小长度（TEXT/TEXTAREA） */
    private Integer minLength;
    /** 小数位数（REAL/PERCENTAGE） */
    private Integer decimal;

    // --- 货币相关（REAL） ---
    /** 是否货币字段：0=否，1=是 */
    private Integer isCurrency;
    /** 货币部分：1=本币，2=原币 */
    private Integer currencyPart;
    /** 是否多币种：0=单币种，1=多币种 */
    private Integer isMultiCurrency;
    /** 货币标记（冗余标记，与 isCurrency 配合使用） */
    private Integer currencyFlg;

    // --- 公式相关（FORMULA） ---
    /** 公式计算结果类型（对应 itemType 枚举值，如 6=REAL、7=PERCENTAGE） */
    private Integer computeType;
    /** 是否实时计算：0=否，1=是（公式字段在记录保存时还是实时计算） */
    private Integer realTimeCompute;

    // --- 选项集相关（SELECT/CHECK） ---
    /** 是否引用全局选项集：0=否，1=是 */
    private Integer referGlobal;
    /** 全局选项集 apiKey */
    private String globalPickItem;
    /** 全局选项集 apiKey（冗余字段，与 globalPickItem 相同） */
    private String globalPickItemApikey;
    /** 是否外部选项源：0=否，1=是 */
    private Integer isExternal;

    // --- 日期相关（DATE） ---
    /** 日期模式：1=仅日期，2=日期+时间 */
    private Integer dateMode;

    // --- 关联/LOOKUP 相关（REFER） ---
    /** 级联删除策略：0=不级联，1=级联删除，2=阻止删除（REFER/MULTIREF 通用） */
    private Integer cascadeDelete;
    /** 是否明细关联：0=否，1=是（主子关系中的子对象字段） */
    private Integer isDetail;
    /** 是否可批量创建关联记录：0=否，1=是 */
    private Integer canBatchCreate;
    /** 是否随父记录复制：0=否，1=是 */
    private Integer isCopyWithParent;
    /** 是否掩码显示（REFER/TEXT 通用）：0=否，1=是 */
    private Integer isMask;
    /** 启用多明细：0=否，1=是 */
    private Integer enableMultiDetail;
    /** 批量创建模式 */
    private Integer batchCreateMode;
    /** 按业务类型批量创建关联 */
    private Integer batchCreateLinkByBusitype;

    // --- JOIN/LOOKUP 字段（JOIN） ---
    /** 关联字段 ID/apiKey（LOOKUP 类型字段关联的目标字段） */
    private String joinItem;
    /** 关联对象 ID/apiKey（LOOKUP 类型字段关联的目标对象） */
    private String joinObject;
    /** 关联关系 ID/apiKey（LOOKUP 类型字段使用的 EntityLink） */
    private String joinLink;
    /** 关联标签（REFER/MULTIREF 在 UI 上显示的关联名称） */
    private String linkLabel;

    // --- 多关联相关（MULTIREF） ---
    /** 关联实体 apiKey 列表（逗号分隔，MULTIREF 支持的目标对象范围） */
    private String referEntityApiKeys;
    /** 实体或数据模式：1=实体关联，2=数据关联 */
    private Integer entityOrData;
    /** 分组 key（MULTIREF 数据分组标识） */
    private String groupKey;
    /** 是否复合子字段：0=否，1=是（复合字段的子字段标记） */
    private Integer compoundSub;
    /** 复合字段 apiKey（子字段指向的父复合字段） */
    private String compoundApiKey;

    // --- 文本相关（TEXT/TEXTAREA） ---
    /** 多行文本标记："0"=单行，"1"=多行 */
    private Integer multiLineText;
    /** 扫码录入标记：0=否，1=是 */
    private Integer scanCodeEntryFlg;
    /** 大小写敏感：0=不敏感，1=敏感（TEXT/TEXTAREA 唯一性校验时） */
    private Integer caseSensitive;

    // --- 富文本相关（RICH_TEXT） ---
    /** 富文本显示行数 */
    private Integer showRows;

    // --- 图片水印相关（IMAGE） ---
    /** 水印开关：0=关闭，1=开启 */
    private Integer watermarkFlg;
    /** 水印时间标记：0=不显示，1=显示拍摄时间 */
    private Integer watermarkTimeFlg;
    /** 水印登录用户标记：0=不显示，1=显示当前登录用户 */
    private Integer watermarkLoginUserFlg;
    /** 水印位置标记：0=不显示，1=显示拍摄位置 */
    private Integer watermarkLocationFlg;
    /** 水印关联字段（显示指定字段值作为水印内容） */
    private String watermarkJoinField;
}
