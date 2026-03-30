package com.hongyang.platform.metarepo.service.entity.metamodel;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hongyang.framework.dao.entity.BaseMetaCommonEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 元模型关联关系（p_meta_link）
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("p_meta_link")
public class MetaLink extends BaseMetaCommonEntity {

    private Integer linkType;
    private String referItemApiKey;
    private String childMetamodelApiKey;
    private String parentMetamodelApiKey;
    private Integer cascadeDelete;
    private String description;
    private String descriptionKey;

}
