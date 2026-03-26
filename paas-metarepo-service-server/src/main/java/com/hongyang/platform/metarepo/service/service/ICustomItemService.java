package com.hongyang.platform.metarepo.service.service;

import com.hongyang.framework.dao.service.IBaseService;
import com.hongyang.platform.metarepo.service.entity.CustomItem;
import java.util.List;

public interface ICustomItemService extends IBaseService<CustomItem> {

    List<CustomItem> listByEntityId(Long tenantId, Long entityId);

    CustomItem getByApiKey(Long tenantId, Long entityId, String apiKey);

    boolean existsApiKey(Long tenantId, Long entityId, String apiKey);

    /** 创建字段（LOOKUP 类型自动创建 entity_link） */
    CustomItem createItem(CustomItem item);

    /** 更新字段 */
    CustomItem updateItem(Long itemId, Long tenantId, CustomItem updates);

    /** 删除字段（检查引用） */
    void deleteItemWithCheck(Long tenantId, Long itemId);
}
