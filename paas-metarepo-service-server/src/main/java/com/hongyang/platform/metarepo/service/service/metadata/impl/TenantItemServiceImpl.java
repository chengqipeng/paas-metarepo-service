package com.hongyang.platform.metarepo.service.service.metadata.impl;

import com.hongyang.framework.dao.entity.BaseMetaTenantEntity;
import com.hongyang.framework.dao.split.CommonTenantServiceImpl;
import com.hongyang.platform.metarepo.service.entity.metadata.tenant.TenantItem;
import com.hongyang.platform.metarepo.service.service.metadata.ITenantItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 字段定义 Service 实现（Common/Tenant 分表）
 */
@Slf4j
@Service
public class TenantItemServiceImpl
        extends CommonTenantServiceImpl<TenantItem>
        implements ITenantItemService {

    @Override
    public List<TenantItem> listMerged(Long tenantId, String entityApiKey) {
        return merge(
            listCommon().stream()
                .filter(e -> entityApiKey.equals(e.getEntityApiKey()))
                .collect(java.util.stream.Collectors.toList()),
            listByEntityApiKey(tenantId, entityApiKey),
            TenantItem::getApiKey
        );
    }

    @Override
    public TenantItem getByApiKeyMerged(Long tenantId, String apiKey) {
        return getByApiKeyMerged(tenantId, apiKey, TenantItem::getApiKey);
    }

    @Override
    public List<TenantItem> listByEntityApiKey(Long tenantId, String entityApiKey) {
        return lambdaQuery()
            .eq(BaseMetaTenantEntity::getTenantId, tenantId)
            .eq(TenantItem::getEntityApiKey, entityApiKey)
            .list();
    }
}
