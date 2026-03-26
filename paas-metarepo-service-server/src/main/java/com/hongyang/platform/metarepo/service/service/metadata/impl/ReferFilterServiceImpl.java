package com.hongyang.platform.metarepo.service.service.metadata.impl;

import com.hongyang.framework.dao.service.DataBaseServiceImpl;
import com.hongyang.platform.metarepo.service.entity.metadata.ReferFilter;
import com.hongyang.platform.metarepo.service.service.metadata.IReferFilterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 关联过滤条件 Service 实现（仅 Tenant 表，无 Common 分表）
 * TenantInterceptor 自动注入 tenant_id 条件。
 */
@Slf4j
@Service
public class ReferFilterServiceImpl
        extends DataBaseServiceImpl<ReferFilter>
        implements IReferFilterService {

    @Override
    public List<ReferFilter> listByItemApiKey(String itemApiKey) {
        return lambdaQuery()
            .eq(ReferFilter::getItemApiKey, itemApiKey)
            .list();
    }
}
