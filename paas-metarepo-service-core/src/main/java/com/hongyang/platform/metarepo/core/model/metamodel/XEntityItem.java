package com.hongyang.platform.metarepo.core.model.metamodel;

import lombok.Data;
import java.io.Serializable;

/**
 * 自定义字段元模型
 */
@Data
public class XEntityItem implements Serializable {
    private String entityApiKey;
    private String apiKey;
    private String label;
    private String labelKey;
    private String namespace;
    private Integer itemType;
    private Integer dataType;
    private String helpText;
    private String helpTextKey;
    private String description;
    private String descriptionKey;
    private Integer customItemSeq;
    private String defaultValue;
    private Integer requireFlg;
    private Integer deleteFlg;
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
    private Integer compound;
    private Integer maskPrefix;
    private Integer maskSuffix;
    private Integer encrypt;
    private Integer indexOrder;
    private Integer indexType;
    private Integer markdown;
    private Integer maskSymbolType;
    private Integer incrementStrategy;
    private Integer referItemFilterEnable;
    private String isComputeMultiCurrencyUnit;
    private String format;
    /** 字段最大长度 */
    private Integer maxLength;
    /** 字段最小长度 */
    private Integer minLength;
    /** 小数位数 */
    private Integer decimal;
    /** 是否货币字段：0=否，1=是 */
    private Integer isCurrency;
    /** 货币部分：1=本币，2=原币 */
    private Integer currencyPart;
    /** 是否多币种 */
    private Integer isMultiCurrency;
    /** 货币标记 */
    private Integer currencyFlg;
    /** 公式计算结果类型 */
    private Integer computeType;
    /** 是否实时计算 */
    private Integer realTimeCompute;
    /** 是否引用全局选项集 */
    private Integer referGlobal;
    /** 全局选项集 ID */
    private String globalPickItem;
    /** 全局选项集 apiKey */
    private String globalPickItemApikey;
    /** 是否外部选项源 */
    private Integer isExternal;
    /** 日期模式：1=仅日期，2=日期+时间 */
    private Integer dateMode;
    /** 级联删除策略 */
    private Integer cascadeDelete;
    /** 是否明细关联 */
    private Integer isDetail;
    /** 是否可批量创建 */
    private Integer canBatchCreate;
    /** 是否随父记录复制 */
    private Integer isCopyWithParent;
    /** 是否掩码显示 */
    private Integer isMask;
    /** 启用多明细 */
    private Integer enableMultiDetail;
    /** 批量创建模式 */
    private Integer batchCreateMode;
    /** 按业务类型批量创建关联 */
    private Integer batchCreateLinkByBusitype;
    /** 关联字段 ID/apiKey */
    private String joinItem;
    /** 关联对象 ID/apiKey */
    private String joinObject;
    /** 关联关系 ID/apiKey */
    private String joinLink;
    /** 关联标签 */
    private String linkLabel;
    /** 关联实体列表(逗号分隔) */
    private String referEntityIds;
    /** 实体或数据模式 */
    private Integer entityOrData;
    /** 分组 key */
    private String groupKey;
    /** 是否复合子字段 */
    private Integer compoundSub;
    /** 复合字段 apiKey */
    private String compoundApiKey;
    /** 多行文本标记 */
    private Integer multiLineText;
    /** 扫码录入标记 */
    private Integer scanCodeEntryFlg;
    /** 大小写敏感 */
    private Integer caseSensitive;
    /** 富文本显示行数 */
    private Integer showRows;
    /** 水印开关 */
    private Integer watermarkFlg;
    /** 水印时间标记 */
    private Integer watermarkTimeFlg;
    /** 水印登录用户标记 */
    private Integer watermarkLoginUserFlg;
    /** 水印位置标记 */
    private Integer watermarkLocationFlg;
    /** 水印关联字段 */
    private String watermarkJoinField;
    /** 是否外置到扩展表 */
    private Integer extTable;
    private Long createdAt;
    private Long createdBy;
    private Long updatedAt;
    private Long updatedBy;
}
