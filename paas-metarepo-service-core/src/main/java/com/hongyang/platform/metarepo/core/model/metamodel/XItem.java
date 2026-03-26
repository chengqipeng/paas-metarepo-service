package com.hongyang.platform.metarepo.core.model.metamodel;

import lombok.Data;
import java.io.Serializable;

/**
 * 自定义字段元模型
 */
@Data
public class XItem implements Serializable {
    private Long id;
    private Long tenantId;
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
    private Long referEntityId;
    private Long referLinkId;
    private String dbColumn;
    private Integer itemOrder;
    private Integer sortFlg;
    private String columnName;
    private Long createdAt;
    private Long createdBy;
    private Long updatedAt;
    private Long updatedBy;
}
