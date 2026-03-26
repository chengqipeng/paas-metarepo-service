package com.hongyang.platform.metarepo.service.entity.metadata;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hongyang.framework.dao.entity.BaseMetaTenantEntity;
import com.hongyang.framework.dao.split.CommonTenantSplit;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 校验规则（Common/Tenant 共用）
 * Tenant 表：p_custom_check_rule（有 tenant_id）
 * Common 表：p_common_check_rule（无 tenant_id）
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("p_tenant_check_rule")
@CommonTenantSplit(commonTable = "p_common_check_rule")
public class CheckRule extends BaseMetaTenantEntity {

    private String entityApiKey;
    private String name;
    private String nameKey;
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
    private String checkErrorItemApiKey;
    private Integer checkAllItemsFlg;
    private Integer checkErrorWay;
}
