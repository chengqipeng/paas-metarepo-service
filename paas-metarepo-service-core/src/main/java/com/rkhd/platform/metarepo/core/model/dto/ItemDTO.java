package com.rkhd.platform.metarepo.core.model.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * 自定义字段响应 DTO
 */
@Data
public class ItemDTO implements Serializable {
    private Long id;
    private Long tenantId;
    private Long entityId;
    private String name;
    private String apiKey;
    private String label;
    private String labelKey;
    private Integer itemType;
    private Integer dataType;
    private String typeProperty;
    private String helpText;
    private String description;
    private String defaultValue;
    private Integer requireFlg;
    private Integer deleteFlg;
    private Integer customFlg;
    private Integer enableFlg;
    private Integer creatable;
    private Integer updatable;
    private Integer uniqueKeyFlg;
    private Integer hiddenFlg;
    private Long referEntityId;
    private Long referLinkId;
    private String dbColumn;
    private Integer itemOrder;
    private Integer sortFlg;
    private Long createdAt;
    private Long createdBy;
    private Long updatedAt;
    private Long updatedBy;
}
