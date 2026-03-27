package com.hongyang.platform.metarepo.service.entity.metadata;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hongyang.framework.dao.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 多语言资源（p_meta_i18n_resource）
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("p_meta_i18n_resource")
public class MetaI18nResource extends BaseEntity {

    private Long metamodelApiKey;
    private Long metadataApiKey;
    private Long entityApiKey;
    private String resourceKey;
    private String langCode;
    private String resourceValue;
    private String description;
}
