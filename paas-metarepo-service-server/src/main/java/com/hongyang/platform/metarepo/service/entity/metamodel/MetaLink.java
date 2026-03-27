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
@TableName("paas_metarepo_common.p_meta_link")
public class MetaLink extends BaseMetaCommonEntity {

    private Integer linkType;
    private Long referItemApiKey;
    private Long childMetamodelApiKey;
    private Long parentMetamodelApiKey;
    private Integer cascadeDelete;
    private String description;
    private String descriptionKey;

}
