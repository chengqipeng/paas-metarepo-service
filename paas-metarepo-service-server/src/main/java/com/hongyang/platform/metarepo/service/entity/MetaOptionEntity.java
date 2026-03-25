package com.hongyang.platform.metarepo.service.entity;

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
public class MetaOptionEntity extends BaseEntity {

    private Long metamodelId;
    private Long itemId;
    private Integer optionCode;
    private String optionKey;
    private String optionLabel;
    private String optionLabelKey;
    private Integer optionOrder;
    private Integer defaultFlg;
    private Integer enableFlg;
    private String description;
}
