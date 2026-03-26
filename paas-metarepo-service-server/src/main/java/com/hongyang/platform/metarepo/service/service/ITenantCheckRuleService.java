package com.hongyang.platform.metarepo.service.service;

import com.hongyang.framework.dao.service.IBaseService;
import com.hongyang.platform.metarepo.core.model.request.CreateCheckRuleRequest;
import com.hongyang.platform.metarepo.core.model.request.UpdateCheckRuleRequest;
import com.hongyang.platform.metarepo.service.entity.metamodel.tenant.TenantCheckRule;
import java.util.List;

public interface ITenantCheckRuleService extends IBaseService<TenantCheckRule> {

    List<TenantCheckRule> listByEntityId(Long tenantId, Long entityId);

    TenantCheckRule createRule(CreateCheckRuleRequest request);

    TenantCheckRule updateRule(Long ruleId, UpdateCheckRuleRequest request);
}
