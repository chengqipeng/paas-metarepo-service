package com.hongyang.platform.metarepo.service.service;

import com.hongyang.framework.dao.service.BaseServiceImpl;
import com.hongyang.platform.metarepo.service.entity.CustomItem;
import com.hongyang.platform.metarepo.service.mapper.CustomItemMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CustomItemServiceImpl
        extends BaseServiceImpl<CustomItemMapper, CustomItem> {

    public List<CustomItem> listByEntityId(Long tenantId, Long entityId) {
        return lambdaQuery()
            .eq(CustomItem::getTenantId, tenantId)
            .eq(CustomItem::getEntityId, entityId)
            .eq(CustomItem::getDeleteFlg, 0)
            .orderByAsc(CustomItem::getItemOrder)
            .list();
    }

    public boolean existsByApiKey(Long tenantId, Long entityId, String apiKey) {
        return lambdaQuery()
            .eq(CustomItem::getTenantId, tenantId)
            .eq(CustomItem::getEntityId, entityId)
            .eq(CustomItem::getApiKey, apiKey)
            .eq(CustomItem::getDeleteFlg, 0)
            .exists();
    }

    public List<CustomItem> listByReferEntityId(Long tenantId, Long referEntityId) {
        return lambdaQuery()
            .eq(CustomItem::getTenantId, tenantId)
            .eq(CustomItem::getReferEntityId, referEntityId)
            .eq(CustomItem::getDeleteFlg, 0)
            .list();
    }
}
