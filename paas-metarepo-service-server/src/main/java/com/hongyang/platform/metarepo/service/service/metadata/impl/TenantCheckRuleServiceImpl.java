package com.hongyang.platform.metarepo.service.service.metadata.impl;

import com.hongyang.framework.dao.entity.BaseMetaTenantEntity;
import com.hongyang.framework.dao.split.CommonTenantServiceImpl;
import com.hongyang.platform.metarepo.service.entity.metadata.tenant.TenantCheckRule;
import com.hongyang.platform.metarepo.service.service.metadata.ITenantCheckRuleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 校验规则 Service 实现（Common/Tenant 分表）
 */
@Slf4j
@Service
public class TenantCheckRuleServiceImpl
        extends CommonTenantServiceImpl<TenantCheckRule>
        implements ITenantCheckRuleService {

    @Override
    public List<TenantCheckRule> listMerged(Long tenantId, Long objectId) {
        return merge(
            listCommon().stream()
                .filter(e -> objectId.equals(e.getObjectId()))
                .collect(java.util.stream.Collectors.toList()),
            listByObjectId(tenantId, objectId),
            TenantCheckRule::getApiKey
        );
    }

    @Override
    public List<TenantCheckRule> listByObjectId(Long tenantId, Long objectId) {
        return lambdaQuery()
            .eq(BaseMetaTenantEntity::getTenantId, tenantId)
            .eq(TenantCheckRule::getObjectId, objectId)
            .list();
    }
}
