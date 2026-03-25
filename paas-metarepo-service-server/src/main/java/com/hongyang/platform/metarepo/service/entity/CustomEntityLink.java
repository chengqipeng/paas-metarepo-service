package com.hongyang.platform.metarepo.service.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hongyang.framework.dao.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("p_custom_entity_link")
public class CustomEntityLink extends BaseEntity {
    private String name;
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
}
