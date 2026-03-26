package com.hongyang.platform.metarepo.service.service.metadata;

import com.hongyang.framework.dao.service.IBaseService;
import com.hongyang.platform.metarepo.service.entity.metadata.tenant.TenantPickOption;

import java.util.List;

/**
 * 字段选项值 Service 接口（Common/Tenant 分表）
 */
public interface ITenantPickOptionService extends IBaseService<TenantPickOption> {

    List<TenantPickOption> listMerged(Long tenantId, String itemApiKey);

    List<TenantPickOption> listByItemApiKey(Long tenantId, String itemApiKey);
}
