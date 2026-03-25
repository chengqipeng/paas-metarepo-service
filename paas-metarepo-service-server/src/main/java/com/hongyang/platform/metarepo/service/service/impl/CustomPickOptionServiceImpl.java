package com.hongyang.platform.metarepo.service.service.impl;

import com.hongyang.framework.dao.service.SimpleBaseServiceImpl;
import com.hongyang.platform.metarepo.service.entity.CustomPickOptionEntity;
import com.hongyang.platform.metarepo.service.service.ICustomPickOptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class CustomPickOptionServiceImpl
        extends SimpleBaseServiceImpl<CustomPickOptionEntity>
        implements ICustomPickOptionService {

    @Override
    public List<CustomPickOptionEntity> listByItemId(Long tenantId, Long itemId) {
        return lambdaQuery()
                .eq(CustomPickOptionEntity::getTenantId, tenantId)
                .eq(CustomPickOptionEntity::getItemId, itemId)
                .orderByAsc(CustomPickOptionEntity::getOptionOrder)
                .list();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOptions(Long tenantId, Long entityId, Long itemId, List<CustomPickOptionEntity> options) {
        // 软删除旧选项
        lambdaUpdate()
                .eq(CustomPickOptionEntity::getTenantId, tenantId)
                .eq(CustomPickOptionEntity::getItemId, itemId)
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
