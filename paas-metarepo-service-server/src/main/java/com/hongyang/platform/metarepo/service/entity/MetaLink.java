package com.hongyang.platform.metarepo.service.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hongyang.framework.dao.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 元模型关联关系（p_meta_link）
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("p_meta_link")
public class MetaLink extends BaseEntity {

    private String apiKey;
    private String label;
    private String labelKey;
    private String name;
    private String nameKey;
    private Integer linkType;
    private Long referItemId;
    private Long childMetamodelId;
    private Long parentMetamodelId;
    private Integer cascadeDelete;
    private String description;
    private String descriptionKey;
}
