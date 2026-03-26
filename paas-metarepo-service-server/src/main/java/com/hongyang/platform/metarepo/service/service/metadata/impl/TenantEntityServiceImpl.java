package com.hongyang.platform.metarepo.service.service.metadata.impl;

import com.hongyang.framework.dao.split.CommonTenantServiceImpl;
import com.hongyang.platform.metarepo.service.entity.metadata.tenant.TenantEntity;
import com.hongyang.platform.metarepo.service.service.metadata.ITenantEntityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 自定义对象 Service 实现（Common/Tenant 分表）
 */
@Slf4j
@Service
public class TenantEntityServiceImpl
        extends CommonTenantServiceImpl<TenantEntity>
        implements ITenantEntityService {

    @Override
    public List<TenantEntity> listMerged(Long tenantId) {
        return listMerged(tenantId, TenantEntity::getApiKey);
    }

    @Override
    public TenantEntity getByApiKeyMerged(Long tenantId, String apiKey) {
        return getByApiKeyMerged(tenantId, apiKey, TenantEntity::getApiKey);
    }

    @Override
    public boolean existsApiKey(Long tenantId, String apiKey) {
        return getByApiKeyMerged(tenantId, apiKey) != null;
    }
}
