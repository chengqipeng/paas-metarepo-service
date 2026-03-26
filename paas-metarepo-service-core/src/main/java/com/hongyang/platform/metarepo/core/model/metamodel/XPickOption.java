package com.hongyang.platform.metarepo.core.model.metamodel;

import lombok.Data;
import java.io.Serializable;

/**
 * 字段选项值元模型
 */
@Data
public class XPickOption implements Serializable {
    private String entityApiKey;
    private String itemApiKey;
    private String apiKey;
    private String label;
    private String labelKey;
    private String namespace;
    private Integer optionCode;
    private Integer optionOrder;
    private Integer defaultFlg;
    private Integer globalFlg;
    private Integer customFlg;
    private Integer enableFlg;
    private String description;
    private String descriptionKey;
    private Long createdAt;
    private Long createdBy;
    private Long updatedAt;
    private Long updatedBy;
}
