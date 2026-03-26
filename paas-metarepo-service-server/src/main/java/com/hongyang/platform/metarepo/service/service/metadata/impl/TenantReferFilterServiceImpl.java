package com.hongyang.platform.metarepo.service.service.metadata.impl;

import com.hongyang.framework.dao.entity.BaseMetaTenantEntity;
import com.hongyang.framework.dao.service.DataBaseServiceImpl;
import com.hongyang.platform.metarepo.service.entity.metadata.tenant.TenantReferFilter;
import com.hongyang.platform.metarepo.service.service.metadata.ITenantReferFilterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 关联过滤条件 Service 实现（仅 Tenant 表，无 Common 分表）
 */
@Slf4j
@Service
public class TenantReferFilterServiceImpl
        extends DataBaseServiceImpl<TenantReferFilter>
        implements ITenantReferFilterService {

    @Override
    public List<TenantReferFilter> listByItemApiKey(Long tenantId, String itemApiKey) {
        return lambdaQuery()
            .eq(BaseMetaTenantEntity::getTenantId, tenantId)
            .eq(TenantReferFilter::getItemApiKey, itemApiKey)
            .list();
    }
}
