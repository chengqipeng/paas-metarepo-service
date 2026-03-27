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
    private String typeProperty;
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
    /** 自动编号格式 */
    private String format;
}
