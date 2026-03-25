package com.hongyang.platform.metarepo.service.service;

import com.hongyang.framework.dao.service.BaseServiceImpl;
import com.hongyang.platform.metarepo.service.entity.CustomEntityLink;
import com.hongyang.platform.metarepo.service.mapper.CustomEntityLinkMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CustomEntityLinkServiceImpl
        extends BaseServiceImpl<CustomEntityLinkMapper, CustomEntityLink> {

    public List<CustomEntityLink> listByParentEntityId(Long tenantId, Long parentEntityId) {
        return lambdaQuery()
            .eq(CustomEntityLink::getTenantId, tenantId)
            .eq(CustomEntityLink::getParentEntityId, parentEntityId)
            .eq(CustomEntityLink::getDeleteFlg, 0)
            .list();
    }

    public List<CustomEntityLink> listByChildEntityId(Long tenantId, Long childEntityId) {
        return lambdaQuery()
            .eq(CustomEntityLink::getTenantId, tenantId)
            .eq(CustomEntityLink::getChildEntityId, childEntityId)
            .eq(CustomEntityLink::getDeleteFlg, 0)
            .list();
    }

    public boolean existsMasterLink(Long tenantId, Long childEntityId) {
        return lambdaQuery()
            .eq(CustomEntityLink::getTenantId, tenantId)
            .eq(CustomEntityLink::getChildEntityId, childEntityId)
            .eq(CustomEntityLink::getLinkType, 1)
            .eq(CustomEntityLink::getDeleteFlg, 0)
            .exists();
    }
}
