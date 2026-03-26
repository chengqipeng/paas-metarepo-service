package com.hongyang.platform.metarepo.service.service.impl;

import com.hongyang.framework.dao.service.SimpleBaseServiceImpl;
import com.hongyang.platform.metarepo.service.entity.CustomEntityLink;
import com.hongyang.platform.metarepo.service.service.ICustomEntityLinkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class CustomEntityLinkServiceImpl
        extends SimpleBaseServiceImpl<CustomEntityLink>
        implements ICustomEntityLinkService {

    @Override
    public List<CustomEntityLink> listByEntityId(Long tenantId, Long entityId) {
        List<CustomEntityLink> asParent = listByParentEntityId(tenantId, entityId);
        List<CustomEntityLink> asChild = listByChildEntityId(tenantId, entityId);
        return Stream.concat(asParent.stream(), asChild.stream())
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<CustomEntityLink> listByParentEntityId(Long tenantId, Long parentEntityId) {
        return lambdaQuery()
                .eq(CustomEntityLink::getTenantId, tenantId)
                .eq(CustomEntityLink::getParentEntityId, parentEntityId)
                .eq(CustomEntityLink::getEnableFlg, 1)
                .list();
    }

    @Override
    public List<CustomEntityLink> listByChildEntityId(Long tenantId, Long childEntityId) {
        return lambdaQuery()
                .eq(CustomEntityLink::getTenantId, tenantId)
                .eq(CustomEntityLink::getChildEntityId, childEntityId)
                .eq(CustomEntityLink::getEnableFlg, 1)
                .list();
    }
}
