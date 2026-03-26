package com.hongyang.platform.metarepo.service.service.impl;

import com.hongyang.framework.dao.service.SimpleBaseServiceImpl;
import com.hongyang.platform.metarepo.service.entity.CustomPickOption;
import com.hongyang.platform.metarepo.service.service.ICustomPickOptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class CustomPickOptionServiceImpl
        extends SimpleBaseServiceImpl<CustomPickOption>
        implements ICustomPickOptionService {

    @Override
    public List<CustomPickOption> listByItemId(Long tenantId, Long itemId) {
        return lambdaQuery()
                .eq(CustomPickOption::getTenantId, tenantId)
                .eq(CustomPickOption::getItemId, itemId)
                .orderByAsc(CustomPickOption::getOptionOrder)
                .list();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOptions(Long tenantId, Long entityId, Long itemId, List<CustomPickOption> options) {
        // 软删除旧选项
        lambdaUpdate()
                .eq(CustomPickOption::getTenantId, tenantId)
                .eq(CustomPickOption::getItemId, itemId)
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
