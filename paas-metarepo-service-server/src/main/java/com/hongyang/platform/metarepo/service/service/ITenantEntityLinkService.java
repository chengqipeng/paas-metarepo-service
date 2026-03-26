package com.hongyang.platform.metarepo.service.service;

import com.hongyang.framework.dao.service.IBaseService;
import com.hongyang.platform.metarepo.service.entity.metamodel.tenant.TenantEntityLink;
import java.util.List;

/**
 * 对象关联关系 Service 接口
 */
public interface ITenantEntityLinkService extends IBaseService<TenantEntityLink> {

    /** 查询对象的所有关联关系（作为父或子） */
    List<TenantEntityLink> listByEntityId(Long tenantId, Long entityId);

    /** 查询对象作为父方的关联关系 */
    List<TenantEntityLink> listByParentEntityId(Long tenantId, Long parentEntityId);

    /** 查询对象作为子方的关联关系 */
    List<TenantEntityLink> listByChildEntityId(Long tenantId, Long childEntityId);
}
