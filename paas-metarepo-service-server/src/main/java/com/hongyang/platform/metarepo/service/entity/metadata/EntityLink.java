package com.hongyang.platform.metarepo.service.entity.metadata;

import com.hongyang.framework.dao.entity.BaseMetaTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 对象关联关系（Common/Tenant 共用）
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class EntityLink extends BaseMetaTenantEntity {

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
}
