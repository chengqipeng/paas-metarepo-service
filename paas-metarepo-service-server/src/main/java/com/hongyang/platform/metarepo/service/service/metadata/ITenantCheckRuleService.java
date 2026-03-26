package com.hongyang.platform.metarepo.service.service.metadata;

import com.hongyang.framework.dao.service.IBaseService;
import com.hongyang.platform.metarepo.service.entity.metadata.tenant.TenantCheckRule;

import java.util.List;

/**
 * 校验规则 Service 接口（Common/Tenant 分表）
 */
public interface ITenantCheckRuleService extends IBaseService<TenantCheckRule> {

    List<TenantCheckRule> listMerged(Long tenantId, Long objectId);

    List<TenantCheckRule> listByObjectId(Long tenantId, Long objectId);
}
