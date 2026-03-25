package com.hongyang.platform.metarepo.core.model.metamodel;

import lombok.Data;
import java.io.Serializable;

/**
 * 校验规则元模型
 */
@Data
public class XCheckRule implements Serializable {
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
