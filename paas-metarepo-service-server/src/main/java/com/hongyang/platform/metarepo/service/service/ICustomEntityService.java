package com.hongyang.platform.metarepo.service.service;

import com.hongyang.framework.dao.service.IBaseService;
import com.hongyang.platform.metarepo.service.entity.CustomEntityEntity;
import java.util.List;

public interface ICustomEntityService extends IBaseService<CustomEntityEntity> {

    CustomEntityEntity getByApiKey(Long tenantId, String apiKey);

    List<CustomEntityEntity> listByTenant(Long tenantId);

    boolean existsApiKey(Long tenantId, String apiKey);

    /** 创建对象（同步写大宽表） */
    CustomEntityEntity createEntity(CustomEntityEntity entity);

    /** 更新对象 */
    CustomEntityEntity updateEntity(Long entityId, Long tenantId, CustomEntityEntity updates);

    /** 删除对象（级联处理字段和关系） */
    void deleteEntityCascade(Long tenantId, Long entityId);
}
