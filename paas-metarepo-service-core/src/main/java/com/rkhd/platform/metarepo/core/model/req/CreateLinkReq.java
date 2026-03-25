package com.rkhd.platform.metarepo.core.model.req;

import lombok.Data;
import java.io.Serializable;

@Data
public class CreateLinkReq implements Serializable {
    private Long tenantId;
    private String name;
    private String apiKey;
    private String label;
    private Integer linkType;
    private Long parentEntityId;
    private Long childEntityId;
    private Integer cascadeDelete;
    private Integer accessControl;
    private String description;
    private Long operatorId;
}
