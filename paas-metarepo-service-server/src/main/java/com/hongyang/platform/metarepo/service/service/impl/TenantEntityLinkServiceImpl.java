package com.hongyang.platform.metarepo.service.service.impl;

import com.hongyang.framework.dao.service.SimpleBaseServiceImpl;
import com.hongyang.platform.metarepo.service.entity.metamodel.tenant.TenantEntityLink;
import com.hongyang.platform.metarepo.service.service.ITenantEntityLinkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class TenantEntityLinkServiceImpl
        extends SimpleBaseServiceImpl<TenantEntityLink>
        implements ITenantEntityLinkService {

    @Override
    public List<TenantEntityLink> listByEntityId(Long tenantId, Long entityId) {
        List<TenantEntityLink> asParent = listByParentEntityId(tenantId, entityId);
        List<TenantEntityLink> asChild = listByChildEntityId(tenantId, entityId);
        return Stream.concat(asParent.stream(), asChild.stream())
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<TenantEntityLink> listByParentEntityId(Long tenantId, Long parentEntityId) {
        return lambdaQuery()
                .eq(TenantEntityLink::getTenantId, tenantId)
                .eq(TenantEntityLink::getParentEntityId, parentEntityId)
                .eq(TenantEntityLink::getEnableFlg, 1)
                .list();
    }

    @Override
    public List<TenantEntityLink> listByChildEntityId(Long tenantId, Long childEntityId) {
        return lambdaQuery()
                .eq(TenantEntityLink::getTenantId, tenantId)
                .eq(TenantEntityLink::getChildEntityId, childEntityId)
                .eq(TenantEntityLink::getEnableFlg, 1)
                .list();
    }
}
