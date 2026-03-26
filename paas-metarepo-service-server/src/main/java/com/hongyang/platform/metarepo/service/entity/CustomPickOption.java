package com.hongyang.platform.metarepo.service.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hongyang.framework.dao.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 字段选项值（p_custom_pickoption）
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("p_custom_pickoption")
public class CustomPickOption extends BaseEntity {

    private Long entityId;
    private Long itemId;
    private String apiKey;
    private Integer optionCode;
    /** 对应 DDL 中的 option_label 列 */
    @TableField("option_label")
    private String label;
    /** 对应 DDL 中的 option_label_key 列 */
    @TableField("option_label_key")
    private String labelKey;
    private Integer optionOrder;
    private Integer defaultFlg;
    private Integer globalFlg;
    private Integer customFlg;
    private Integer enableFlg;
    private String description;
    private String descriptionKey;
}
