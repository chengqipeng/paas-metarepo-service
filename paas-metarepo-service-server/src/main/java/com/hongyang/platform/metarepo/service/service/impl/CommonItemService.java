package com.hongyang.platform.metarepo.service.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hongyang.platform.metarepo.service.entity.metamodel.tenant.TenantItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Common 级字段 Service
 * 注意：TenantItem 表同时用于 Common 和 Tenant，这里查 Common 级（entity_id 关联 Common 对象）
 */
@Slf4j
@Service
public class CommonItemService extends ServiceImpl<BaseMapper<TenantItem>, TenantItem> {

    /** 查询 Common 级字段（通过 p_custom_item_common 表） */
    public List<TenantItem> listByEntityId(Long entityId) {
        // TODO: 当 p_custom_item_common 表独立后，改为查 Common 表
        // 目前 Common 数据在 p_custom_item 中 tenant_id <= 0
        return lambdaQuery()
                .le(TenantItem::getTenantId, 0L)
                .eq(TenantItem::getEntityId, entityId)
                .orderByAsc(TenantItem::getItemOrder)
                .list();
    }
}
