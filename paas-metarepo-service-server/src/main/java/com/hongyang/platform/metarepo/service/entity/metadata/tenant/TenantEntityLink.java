package com.hongyang.platform.metarepo.service.entity.metadata.tenant;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hongyang.framework.dao.entity.BaseMetaTenantEntity;
import com.hongyang.framework.dao.split.CommonTenantSplit;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 对象关联关系（Common/Tenant 共用）
 * Tenant 表：p_custom_entity_link（有 tenant_id）
 * Common 表：p_common_entity_link（无 tenant_id）
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("p_custom_entity_link")
@CommonTenantSplit(commonTable = "p_common_entity_link")
public class TenantEntityLink extends BaseMetaTenantEntity {

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
}
