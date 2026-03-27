package com.hongyang.platform.metarepo.service.entity.metadata;

import com.hongyang.framework.dao.entity.BaseMetaTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 校验规则（Common/Tenant 共用）
 */
@Data
@EqualsAndHashCode(callSuper = true)
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
