package com.hongyang.platform.metarepo.service.service.metadata.impl;

import com.hongyang.framework.dao.split.CommonTenantServiceImpl;
import com.hongyang.platform.metarepo.service.entity.metadata.EntityLink;
import com.hongyang.platform.metarepo.service.service.metadata.IEntityLinkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 对象关联关系 Service 实现（Common/Tenant 分表）
 */
@Slf4j
@Service
public class EntityLinkServiceImpl
        extends CommonTenantServiceImpl<EntityLink>
        implements IEntityLinkService {

    @Override
    public List<EntityLink> listMerged(String parentEntityApiKey) {
        return merge(
            listCommon().stream()
                .filter(e -> parentEntityApiKey.equals(e.getParentEntityApiKey()))
                .collect(Collectors.toList()),
            listByParentEntityApiKey(parentEntityApiKey),
            EntityLink::getApiKey
        );
    }

    @Override
    public List<EntityLink> listByParentEntityApiKey(String parentEntityApiKey) {
        return lambdaQuery()
            .eq(EntityLink::getParentEntityApiKey, parentEntityApiKey)
            .list();
    }
}
