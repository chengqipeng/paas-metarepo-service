package com.hongyang.platform.metarepo.service.service.metadata.impl;

import com.hongyang.framework.dao.entity.BaseMetaTenantEntity;
import com.hongyang.framework.dao.split.CommonTenantServiceImpl;
import com.hongyang.platform.metarepo.service.entity.metadata.tenant.TenantPickOption;
import com.hongyang.platform.metarepo.service.service.metadata.ITenantPickOptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 字段选项值 Service 实现（Common/Tenant 分表）
 */
@Slf4j
@Service
public class TenantPickOptionServiceImpl
        extends CommonTenantServiceImpl<TenantPickOption>
        implements ITenantPickOptionService {

    @Override
    public List<TenantPickOption> listMerged(Long tenantId, String itemApiKey) {
        return merge(
            listCommon().stream()
                .filter(e -> itemApiKey.equals(e.getItemApiKey()))
                .collect(java.util.stream.Collectors.toList()),
            listByItemApiKey(tenantId, itemApiKey),
            TenantPickOption::getApiKey
        );
    }

    @Override
    public List<TenantPickOption> listByItemApiKey(Long tenantId, String itemApiKey) {
        return lambdaQuery()
            .eq(BaseMetaTenantEntity::getTenantId, tenantId)
            .eq(TenantPickOption::getItemApiKey, itemApiKey)
            .list();
    }
}
