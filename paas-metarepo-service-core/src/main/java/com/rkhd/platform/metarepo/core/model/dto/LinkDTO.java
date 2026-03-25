package com.rkhd.platform.metarepo.core.model.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * 关联关系响应 DTO
 */
@Data
public class LinkDTO implements Serializable {
    private Long id;
    private Long tenantId;
    private String name;
    private String apiKey;
    private String label;
    private Integer linkType;
    private Long parentEntityId;
    private Long childEntityId;
    private Integer cascadeDelete;
    private Integer accessControl;
    private Integer enableFlg;
    private String description;
    private Long createdAt;
    private Long createdBy;
}
