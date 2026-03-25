package com.rkhd.platform.metarepo.core.model.req;

import lombok.Data;
import java.io.Serializable;

@Data
public class CreateItemReq implements Serializable {
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
    private Integer customFlg;
    private Integer creatable;
    private Integer updatable;
    private Integer uniqueKeyFlg;
    private Integer hiddenFlg;
    private Long referEntityId;
    private String dbColumn;
    private Integer itemOrder;
    private Long operatorId;
}
