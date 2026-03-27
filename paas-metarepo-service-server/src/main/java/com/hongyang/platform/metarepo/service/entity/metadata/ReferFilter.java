package com.hongyang.platform.metarepo.service.entity.metadata;

import com.hongyang.framework.dao.entity.BaseMetaTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 关联过滤条件
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ReferFilter extends BaseMetaTenantEntity {

    private String entityApiKey;
    private String itemApiKey;
    private String linkApiKey;
    private String filterField;
    private String filterOperator;
    private String filterValue;
    private Integer filterOrder;
    private String description;
    private String descriptionKey;
}