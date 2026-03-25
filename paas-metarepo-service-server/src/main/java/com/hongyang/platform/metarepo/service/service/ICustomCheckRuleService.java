package com.hongyang.platform.metarepo.service.service;

import com.hongyang.framework.dao.service.IBaseService;
import com.hongyang.platform.metarepo.core.model.request.CreateCheckRuleRequest;
import com.hongyang.platform.metarepo.core.model.request.UpdateCheckRuleRequest;
import com.hongyang.platform.metarepo.service.entity.CustomCheckRuleEntity;
import java.util.List;

public interface ICustomCheckRuleService extends IBaseService<CustomCheckRuleEntity> {

    List<CustomCheckRuleEntity> listByObjectId(Long tenantId, Long objectId);

    CustomCheckRuleEntity createRule(CreateCheckRuleRequest request);

    CustomCheckRuleEntity updateRule(Long ruleId, UpdateCheckRuleRequest request);
}
