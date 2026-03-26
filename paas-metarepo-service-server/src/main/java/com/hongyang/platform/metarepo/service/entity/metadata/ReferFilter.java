package com.hongyang.platform.metarepo.service.entity.metadata;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hongyang.framework.dao.entity.BaseMetaTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 关联过滤条件（p_custom_refer_filter）
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("p_custom_refer_filter")
public class ReferFilter extends BaseMetaTenantEntity {

    private String entityApiKey;
    private String itemApiKey;
    private String linkApiKey;
    private String filterField;
    private String filterOperator;
    private String filterValue;
    private Integer filterOrder;
    private String description;
}