package com.hongyang.platform.metarepo.service.service.metadata;

import com.hongyang.framework.dao.service.IBaseService;
import com.hongyang.platform.metarepo.service.entity.metadata.tenant.TenantItem;

import java.util.List;

/**
 * 字段定义 Service 接口（Common/Tenant 分表）
 */
public interface ITenantItemService extends IBaseService<TenantItem> {

    List<TenantItem> listMerged(Long tenantId, String entityApiKey);

    TenantItem getByApiKeyMerged(Long tenantId, String apiKey);

    List<TenantItem> listByEntityApiKey(Long tenantId, String entityApiKey);
}
