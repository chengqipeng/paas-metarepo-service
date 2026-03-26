package com.hongyang.platform.metarepo.core.model.metamodel;

import lombok.Data;
import java.io.Serializable;

/**
 * 关联关系元模型
 */
@Data
public class XLink implements Serializable {
    private String apiKey;
    private String label;
    private String labelKey;
    private String namespace;
    private String name;
    private String nameKey;
    private String typeProperty;
    private Integer linkType;
    private String parentEntityApiKey;
    private String childEntityApiKey;
    private Integer detailLink;
    private Integer cascadeDelete;
    private Integer accessControl;
    private Integer enableFlg;
    private String description;
    private String descriptionKey;
    private Integer deleteFlg;
    private Long createdAt;
    private Long createdBy;
    private Long updatedAt;
    private Long updatedBy;
}
