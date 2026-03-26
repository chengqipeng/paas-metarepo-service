package com.hongyang.platform.metarepo.service.service.metadata;

import com.hongyang.framework.dao.service.IBaseService;
import com.hongyang.platform.metarepo.service.entity.metadata.tenant.TenantReferFilter;

import java.util.List;

/**
 * 关联过滤条件 Service 接口
 */
public interface ITenantReferFilterService extends IBaseService<TenantReferFilter> {

    List<TenantReferFilter> listByItemApiKey(Long tenantId, String itemApiKey);
}
