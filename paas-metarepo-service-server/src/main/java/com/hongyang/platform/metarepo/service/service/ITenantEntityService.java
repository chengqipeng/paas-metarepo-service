package com.hongyang.platform.metarepo.service.service;

import com.hongyang.framework.dao.service.IBaseService;
import com.hongyang.platform.metarepo.service.entity.metamodel.tenant.TenantEntity;
import java.util.List;

public interface ITenantEntityService extends IBaseService<TenantEntity> {

    TenantEntity getByApiKey(Long tenantId, String apiKey);

    List<TenantEntity> listByTenant(Long tenantId);

    boolean existsApiKey(Long tenantId, String apiKey);

    /** 创建对象（同步写大宽表） */
    TenantEntity createEntity(TenantEntity entity);

    /** 更新对象 */
    TenantEntity updateEntity(Long entityId, Long tenantId, TenantEntity updates);

    /** 删除对象（级联处理字段和关系） */
    void deleteEntityCascade(Long tenantId, Long entityId);
}
