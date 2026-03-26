package com.hongyang.platform.metarepo.service.service;

import com.hongyang.framework.dao.service.IBaseService;
import com.hongyang.platform.metarepo.service.entity.metamodel.tenant.TenantItem;
import java.util.List;

public interface ITenantItemService extends IBaseService<TenantItem> {

    List<TenantItem> listByEntityId(Long tenantId, Long entityId);

    TenantItem getByApiKey(Long tenantId, Long entityId, String apiKey);

    boolean existsApiKey(Long tenantId, Long entityId, String apiKey);

    /** 创建字段（LOOKUP 类型自动创建 entity_link） */
    TenantItem createItem(TenantItem item);

    /** 更新字段 */
    TenantItem updateItem(Long itemId, Long tenantId, TenantItem updates);

    /** 删除字段（检查引用） */
    void deleteItemWithCheck(Long tenantId, Long itemId);
}
