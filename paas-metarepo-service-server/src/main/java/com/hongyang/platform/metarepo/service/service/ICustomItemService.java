package com.hongyang.platform.metarepo.service.service;

import com.hongyang.framework.dao.service.IBaseService;
import com.hongyang.platform.metarepo.service.entity.CustomItemEntity;
import java.util.List;

/**
 * 自定义字段 Service 接口
 */
public interface ICustomItemService extends IBaseService<CustomItemEntity> {

    /** 查询对象下所有字段 */
    List<CustomItemEntity> listByEntityId(Long tenantId, Long entityId);

    /** 按租户+对象+apiKey 查询唯一字段 */
    CustomItemEntity getByApiKey(Long tenantId, Long entityId, String apiKey);

    /** 检查 apiKey 是否已存在 */
    boolean existsApiKey(Long tenantId, Long entityId, String apiKey);
}
