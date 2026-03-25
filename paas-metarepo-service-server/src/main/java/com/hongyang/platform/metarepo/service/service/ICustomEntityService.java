package com.hongyang.platform.metarepo.service.service;

import com.hongyang.framework.dao.service.IBaseService;
import com.hongyang.platform.metarepo.service.entity.CustomEntityEntity;
import java.util.List;

/**
 * 自定义对象 Service 接口
 */
public interface ICustomEntityService extends IBaseService<CustomEntityEntity> {

    /** 按租户+apiKey 查询唯一对象 */
    CustomEntityEntity getByApiKey(Long tenantId, String apiKey);

    /** 查询租户下所有启用的对象 */
    List<CustomEntityEntity> listByTenant(Long tenantId);

    /** 检查 apiKey 是否已存在 */
    boolean existsApiKey(Long tenantId, String apiKey);
}
