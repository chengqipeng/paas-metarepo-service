package com.hongyang.platform.metarepo.service.service.impl;

import com.hongyang.framework.dao.service.SimpleBaseServiceImpl;
import com.hongyang.platform.metarepo.service.entity.CustomEntityLinkEntity;
import com.hongyang.platform.metarepo.service.service.ICustomEntityLinkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class CustomEntityLinkServiceImpl
        extends SimpleBaseServiceImpl<CustomEntityLinkEntity>
        implements ICustomEntityLinkService {

    @Override
    public List<CustomEntityLinkEntity> listByEntityId(Long tenantId, Long entityId) {
        List<CustomEntityLinkEntity> asParent = listByParentEntityId(tenantId, entityId);
        List<CustomEntityLinkEntity> asChild = listByChildEntityId(tenantId, entityId);
        return Stream.concat(asParent.stream(), asChild.stream())
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<CustomEntityLinkEntity> listByParentEntityId(Long tenantId, Long parentEntityId) {
        return lambdaQuery()
                .eq(CustomEntityLinkEntity::getTenantId, tenantId)
                .eq(CustomEntityLinkEntity::getParentEntityId, parentEntityId)
                .eq(CustomEntityLinkEntity::getEnableFlg, 1)
                .list();
    }

    @Override
    public List<CustomEntityLinkEntity> listByChildEntityId(Long tenantId, Long childEntityId) {
        return lambdaQuery()
                .eq(CustomEntityLinkEntity::getTenantId, tenantId)
                .eq(CustomEntityLinkEntity::getChildEntityId, childEntityId)
                .eq(CustomEntityLinkEntity::getEnableFlg, 1)
                .list();
    }
}
