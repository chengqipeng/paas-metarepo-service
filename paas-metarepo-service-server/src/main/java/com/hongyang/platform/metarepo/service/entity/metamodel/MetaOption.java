package com.hongyang.platform.metarepo.service.entity.metamodel;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hongyang.framework.dao.entity.BaseMetaCommonEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 元模型选项值定义（p_meta_option）
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("p_meta_option")
public class MetaOption extends BaseMetaCommonEntity {

    private String metamodelApiKey;
    private String itemApiKey;
    private String optionKey;
    private Integer optionOrder;
    private Integer defaultFlg;
    private Integer enableFlg;
    private String description;
    private String descriptionKey;

}
