package com.hongyang.platform.metarepo.service.entity.metadata;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hongyang.framework.dao.entity.BaseMetaTenantEntity;
import com.hongyang.platform.metarepo.service.common.annotation.CommonTenantSplit;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 关联过滤条件
 * Tenant 表：p_tenant_refer_filter（有 tenant_id）
 * Common 数据：统一存储在 p_common_metadata（metamodel_api_key = "refer_filter"）
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("p_tenant_refer_filter")
@CommonTenantSplit(metamodelApiKey = "refer_filter")
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