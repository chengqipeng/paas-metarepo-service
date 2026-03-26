package com.hongyang.platform.metarepo.service.entity.meta;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hongyang.framework.dao.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 元模型字段项定义（p_meta_item）
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("p_meta_item")
public class MetaItem extends BaseEntity {

    private String apiKey;
    private String label;
    private String labelKey;
    private String metaModelApiKey;
    private Integer itemType;
    private Integer dataType;
    private Integer itemOrder;
    private Integer requireFlg;
    private Integer uniqueKeyFlg;
    private Integer creatable;
    private Integer updatable;
    private Integer enablePackage;
    private Integer enableDelta;
    private Integer enableLog;
    private String dbColumn;
    private String description;
    private String descriptionKey;
    private Integer minLength;
    private Integer maxLength;
    private Integer textFormat;
    private String jsonSchema;
    private Integer nameField;

}
