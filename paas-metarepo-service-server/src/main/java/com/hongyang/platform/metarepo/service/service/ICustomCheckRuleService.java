package com.hongyang.platform.metarepo.service.service;

import com.hongyang.framework.dao.service.IBaseService;
import com.hongyang.platform.metarepo.core.model.request.CreateCheckRuleRequest;
import com.hongyang.platform.metarepo.core.model.request.UpdateCheckRuleRequest;
import com.hongyang.platform.metarepo.service.entity.CustomCheckRule;
import java.util.List;

public interface ICustomCheckRuleService extends IBaseService<CustomCheckRule> {

    List<CustomCheckRule> listByEntityId(Long tenantId, Long entityId);

    CustomCheckRule createRule(CreateCheckRuleRequest request);

    CustomCheckRule updateRule(Long ruleId, UpdateCheckRuleRequest request);
}
