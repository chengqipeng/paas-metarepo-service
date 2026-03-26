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
    private Long entityId;
    private String name;
    private String nameKey;
    private String apiKey;
    private String label;
    private String labelKey;
    private Integer activeFlg;
    private String description;
    private String descriptionKey;
    private String checkFormula;
    private String checkErrorMsg;
    private String checkErrorMsgKey;
    private Integer checkErrorLocation;
    private Long checkErrorItemId;
    private Integer checkAllItemsFlg;
    private Integer checkErrorWay;
    private Long createdAt;
    private Long createdBy;
    private Long updatedAt;
    private Long updatedBy;
}
