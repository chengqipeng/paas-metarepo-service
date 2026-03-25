package com.hongyang.platform.metarepo.service.service.impl;

import com.hongyang.framework.dao.service.SimpleBaseServiceImpl;
import com.hongyang.platform.metarepo.service.entity.CustomItemEntity;
import com.hongyang.platform.metarepo.service.service.ICustomItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CustomItemServiceImpl
        extends SimpleBaseServiceImpl<CustomItemEntity>
        implements ICustomItemService {

    @Override
    public List<CustomItemEntity> listByEntityId(Long tenantId, Long entityId) {
        return lambdaQuery()
                .eq(CustomItemEntity::getTenantId, tenantId)
                .eq(CustomItemEntity::getEntityId, entityId)
                .orderByAsc(CustomItemEntity::getItemOrder)
                .list();
    }

    @Override
    public CustomItemEntity getByApiKey(Long tenantId, Long entityId, String apiKey) {
        return lambdaQuery()
                .eq(CustomItemEntity::getTenantId, tenantId)
                .eq(CustomItemEntity::getEntityId, entityId)
                .eq(CustomItemEntity::getApiKey, apiKey)
                .one();
    }

    @Override
    public boolean existsApiKey(Long tenantId, Long entityId, String apiKey) {
        return lambdaQuery()
                .eq(CustomItemEntity::getTenantId, tenantId)
                .eq(CustomItemEntity::getEntityId, entityId)
                .eq(CustomItemEntity::getApiKey, apiKey)
                .exists();
    }
}
