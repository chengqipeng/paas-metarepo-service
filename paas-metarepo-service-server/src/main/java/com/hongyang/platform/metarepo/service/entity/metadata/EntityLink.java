package com.hongyang.platform.metarepo.service.entity.metadata;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hongyang.framework.dao.entity.BaseMetaTenantEntity;
import com.hongyang.platform.metarepo.service.common.annotation.CommonTenantSplit;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 对象关联关系（Common/Tenant 共用）
 * Tenant 表：p_tenant_entity_link（有 tenant_id）
 * Common 数据：统一存储在 p_common_metadata（metamodel_api_key = "entity_link"）
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("p_tenant_entity_link")
@CommonTenantSplit(metamodelApiKey = "entity_link")
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
