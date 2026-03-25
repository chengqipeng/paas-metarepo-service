package com.hongyang.platform.metarepo.service.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hongyang.framework.dao.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 校验规则（p_custom_check_rule）
 * 注意：该表无 tenant_id/delete_flg 在 BaseEntity 中，但 DDL 中有 tenant_id
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("p_custom_check_rule")
public class CustomCheckRuleEntity extends BaseEntity {

    private Long objectId;
    private String name;
    private String apiKey;
    private String ruleLabel;
    private String ruleLabelKey;
    private Integer activeFlg;
    private String description;
    private String checkFormula;
    private String checkErrorMsg;
    private String checkErrorMsgKey;
    private Integer checkErrorLocation;
    private Long checkErrorItemId;
    private Integer checkAllItemsFlg;
    private Integer checkErrorWay;
}
