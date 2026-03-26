package com.hongyang.platform.metarepo.service.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hongyang.framework.dao.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 自定义字段定义（p_custom_item）
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("p_custom_item")
public class CustomItem extends BaseEntity {

    private Long entityId;
    private String name;
    private String nameKey;
    private String apiKey;
    private String label;
    private String labelKey;
    private Integer itemType;
    private Integer dataType;
    private String typeProperty;
    private String helpText;
    private String helpTextKey;
    private String description;
    private String descriptionKey;
    private Integer customItemseq;
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
    private Long referEntityId;
    private Long referLinkId;
    private String dbColumn;
    private Integer itemOrder;
    private Integer sortFlg;
    private String columnName;
}
