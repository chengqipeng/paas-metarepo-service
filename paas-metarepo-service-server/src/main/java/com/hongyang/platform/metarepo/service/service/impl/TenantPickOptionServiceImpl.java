package com.hongyang.platform.metarepo.service.service.impl;

import com.hongyang.framework.dao.service.SimpleBaseServiceImpl;
import com.hongyang.platform.metarepo.service.entity.metamodel.tenant.TenantPickOption;
import com.hongyang.platform.metarepo.service.service.ITenantPickOptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class TenantPickOptionServiceImpl
        extends SimpleBaseServiceImpl<TenantPickOption>
        implements ITenantPickOptionService {

    @Override
    public List<TenantPickOption> listByItemId(Long tenantId, Long itemId) {
        return lambdaQuery()
                .eq(TenantPickOption::getTenantId, tenantId)
                .eq(TenantPickOption::getItemId, itemId)
                .orderByAsc(TenantPickOption::getOptionOrder)
                .list();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOptions(Long tenantId, Long entityId, Long itemId, List<TenantPickOption> options) {
        // 软删除旧选项
        lambdaUpdate()
                .eq(TenantPickOption::getTenantId, tenantId)
                .eq(TenantPickOption::getItemId, itemId)
                .remove();
        // 批量插入新选项
        if (options != null && !options.isEmpty()) {
            options.forEach(opt -> {
                opt.setTenantId(tenantId);
                opt.setEntityId(entityId);
                opt.setItemId(itemId);
            });
            saveBatch(options);
        }
    }
}
