package com.hongyang.platform.metarepo.service.service;

import com.hongyang.framework.dao.service.IBaseService;
import com.hongyang.platform.metarepo.service.entity.CustomItemEntity;
import java.util.List;

public interface ICustomItemService extends IBaseService<CustomItemEntity> {

    List<CustomItemEntity> listByEntityId(Long tenantId, Long entityId);

    CustomItemEntity getByApiKey(Long tenantId, Long entityId, String apiKey);

    boolean existsApiKey(Long tenantId, Long entityId, String apiKey);

    /** 创建字段（LOOKUP 类型自动创建 entity_link） */
    CustomItemEntity createItem(CustomItemEntity item);

    /** 更新字段 */
    CustomItemEntity updateItem(Long itemId, Long tenantId, CustomItemEntity updates);

    /** 删除字段（检查引用） */
    void deleteItemWithCheck(Long tenantId, Long itemId);
}
