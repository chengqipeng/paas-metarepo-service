package com.hongyang.platform.metarepo.service.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hongyang.framework.dao.entity.BaseEntity;
import com.hongyang.framework.dao.split.CommonTenantSplit;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 对象关联关系
 * Tenant 表：p_custom_entity_link（有 tenant_id）
 * Common 表：p_common_entity_link（无 tenant_id）
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("p_custom_entity_link")
@CommonTenantSplit(commonTable = "p_common_entity_link")
public class CustomEntityLink extends BaseEntity {

    private String name;
    private String nameKey;
    private String apiKey;
    private String label;
    private String labelKey;
    private String typeProperty;
    private Integer linkType;
    private Long parentEntityId;
    private Long childEntityId;
    private Integer detailLink;
    private Integer cascadeDelete;
    private Integer accessControl;
    private Integer enableFlg;
    private String description;
    private String descriptionKey;
}
