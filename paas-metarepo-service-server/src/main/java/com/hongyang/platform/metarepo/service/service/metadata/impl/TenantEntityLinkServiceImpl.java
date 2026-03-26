package com.hongyang.platform.metarepo.service.service.metadata.impl;

import com.hongyang.framework.dao.entity.BaseMetaTenantEntity;
import com.hongyang.framework.dao.split.CommonTenantServiceImpl;
import com.hongyang.platform.metarepo.service.entity.metadata.tenant.TenantEntityLink;
import com.hongyang.platform.metarepo.service.service.metadata.ITenantEntityLinkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 对象关联关系 Service 实现（Common/Tenant 分表）
 */
@Slf4j
@Service
public class TenantEntityLinkServiceImpl
        extends CommonTenantServiceImpl<TenantEntityLink>
        implements ITenantEntityLinkService {

    @Override
    public List<TenantEntityLink> listMerged(Long tenantId, Long parentEntityId) {
        return merge(
            listCommon().stream()
                .filter(e -> parentEntityId.equals(e.getParentEntityId()))
                .collect(java.util.stream.Collectors.toList()),
            listByParentEntityId(tenantId, parentEntityId),
            TenantEntityLink::getApiKey
        );
    }

    @Override
    public List<TenantEntityLink> listByParentEntityId(Long tenantId, Long parentEntityId) {
        return lambdaQuery()
            .eq(BaseMetaTenantEntity::getTenantId, tenantId)
            .eq(TenantEntityLink::getParentEntityId, parentEntityId)
            .list();
    }
}
