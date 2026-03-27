package com.hongyang.platform.metarepo.service.entity.metadata;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hongyang.framework.dao.entity.BaseMetaTenantEntity;
import com.hongyang.platform.metarepo.service.common.annotation.CommonTenantSplit;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 校验规则（Common/Tenant 共用）
 * Tenant 表：p_tenant_check_rule（有 tenant_id）
 * Common 数据：统一存储在 p_common_metadata（metamodel_api_key = "check_rule"）
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("p_tenant_check_rule")
@CommonTenantSplit(metamodelApiKey = "check_rule")
public class CheckRule extends BaseMetaTenantEntity {

    private String entityApiKey;
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
