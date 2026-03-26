package com.hongyang.platform.metarepo.service.service;

import com.hongyang.framework.dao.service.IBaseService;
import com.hongyang.platform.metarepo.service.entity.CustomEntity;
import java.util.List;

public interface ICustomEntityService extends IBaseService<CustomEntity> {

    CustomEntity getByApiKey(Long tenantId, String apiKey);

    List<CustomEntity> listByTenant(Long tenantId);

    boolean existsApiKey(Long tenantId, String apiKey);

    /** 创建对象（同步写大宽表） */
    CustomEntity createEntity(CustomEntity entity);

    /** 更新对象 */
    CustomEntity updateEntity(Long entityId, Long tenantId, CustomEntity updates);

    /** 删除对象（级联处理字段和关系） */
    void deleteEntityCascade(Long tenantId, Long entityId);
}
