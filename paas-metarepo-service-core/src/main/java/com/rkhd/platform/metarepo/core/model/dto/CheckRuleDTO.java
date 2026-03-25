package com.rkhd.platform.metarepo.core.model.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class CheckRuleDTO implements Serializable {
    private Long id;
    private Long tenantId;
    private Long objectId;
    private String name;
    private String apiKey;
    private Integer activeFlg;
    private String checkFormula;
    private String checkErrorMsg;
    private Integer checkErrorLocation;
    private Long checkErrorItemId;
    private Long createdAt;
    private Long createdBy;
}
