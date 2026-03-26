package com.hongyang.platform.metarepo.service.service.metadata;

import com.hongyang.framework.dao.service.IBaseService;
import com.hongyang.platform.metarepo.service.entity.metadata.tenant.TenantEntity;

import java.util.List;

/**
 * 自定义对象 Service 接口（Common/Tenant 分表）
 */
public interface ITenantEntityService extends IBaseService<TenantEntity> {

    List<TenantEntity> listMerged(Long tenantId);

    TenantEntity getByApiKeyMerged(Long tenantId, String apiKey);

    boolean existsApiKey(Long tenantId, String apiKey);
}
