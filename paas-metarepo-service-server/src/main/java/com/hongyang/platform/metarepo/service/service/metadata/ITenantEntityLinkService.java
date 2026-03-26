package com.hongyang.platform.metarepo.service.service.metadata;

import com.hongyang.framework.dao.service.IBaseService;
import com.hongyang.platform.metarepo.service.entity.metadata.tenant.TenantEntityLink;

import java.util.List;

/**
 * 对象关联关系 Service 接口（Common/Tenant 分表）
 */
public interface ITenantEntityLinkService extends IBaseService<TenantEntityLink> {

    List<TenantEntityLink> listMerged(Long tenantId, Long parentEntityId);

    List<TenantEntityLink> listByParentEntityId(Long tenantId, Long parentEntityId);
}
