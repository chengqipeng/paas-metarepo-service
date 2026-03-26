package com.hongyang.platform.metarepo.service.entity.meta;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hongyang.framework.dao.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 元模型选项值定义（p_meta_option）
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("p_meta_option")
public class MetaOption extends BaseEntity {

    private String apiKey;
    private String label;
    private String labelKey;
    private String entityApiKey;
    private String itemApiKey;
    private String metaModelApiKey;
    private Integer optionOrder;
    private Integer defaultFlg;
    private Integer enableFlg;
    private String description;
    private String descriptionKey;

}
