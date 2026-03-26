package com.hongyang.platform.metarepo.service.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hongyang.framework.dao.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 校验规则（p_custom_check_rule）
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("p_custom_check_rule")
public class CustomCheckRule extends BaseEntity {

    private Long entityId;
    private String name;
    private String nameKey;
    private String apiKey;
    @TableField("rule_label")
    private String label;
    @TableField("rule_label_key")
    private String labelKey;
    private Integer activeFlg;
    private String description;
    private String descriptionKey;
    private String checkFormula;
    private String checkErrorMsg;
    private String checkErrorMsgKey;
    private Integer checkErrorLocation;
    private Long checkErrorItemId;
    private Integer checkAllItemsFlg;
    private Integer checkErrorWay;
}
