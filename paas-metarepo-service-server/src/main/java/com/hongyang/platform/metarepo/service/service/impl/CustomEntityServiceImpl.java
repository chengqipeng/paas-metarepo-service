package com.hongyang.platform.metarepo.service.service.impl;

import com.hongyang.framework.dao.service.SimpleBaseServiceImpl;
import com.hongyang.platform.metarepo.service.entity.CustomEntityEntity;
import com.hongyang.platform.metarepo.service.service.ICustomEntityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CustomEntityServiceImpl
        extends SimpleBaseServiceImpl<CustomEntityEntity>
        implements ICustomEntityService {

    @Override
    public CustomEntityEntity getByApiKey(Long tenantId, String apiKey) {
        return lambdaQuery()
                .eq(CustomEntityEntity::getTenantId, tenantId)
                .eq(CustomEntityEntity::getApiKey, apiKey)
                .one();
    }

    @Override
    public List<CustomEntityEntity> listByTenant(Long tenantId) {
        return lambdaQuery()
                .eq(CustomEntityEntity::getTenantId, tenantId)
                .eq(CustomEntityEntity::getEnableFlg, 1)
                .list();
    }

    @Override
    public boolean existsApiKey(Long tenantId, String apiKey) {
        return lambdaQuery()
                .eq(CustomEntityEntity::getTenantId, tenantId)
                .eq(CustomEntityEntity::getApiKey, apiKey)
                .exists();
    }
}
