package com.hongyang.platform.metarepo.core.model.metamodel;

import lombok.Data;
import java.io.Serializable;

/**
 * 校验规则元模型
 */
@Data
public class XCheckRule implements Serializable {
    private String entityApiKey;
    private String apiKey;
    private String label;
    private String labelKey;
    private String namespace;
    private String name;
    private String nameKey;
    private Integer activeFlg;
    private String description;
    private String descriptionKey;
    private String checkFormula;
    private String checkErrorMsg;
    private String checkErrorMsgKey;
    private Integer checkErrorLocation;
    private String checkErrorItemApiKey;
    private Integer checkAllItemsFlg;
    private Integer checkErrorWay;
    private Integer deleteFlg;
    private Long createdAt;
    private Long createdBy;
    private Long updatedAt;
    private Long updatedBy;
}
