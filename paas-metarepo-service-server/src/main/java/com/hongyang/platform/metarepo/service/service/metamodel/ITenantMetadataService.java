package com.hongyang.platform.metarepo.service.service.metamodel;

import com.hongyang.framework.dao.service.IBaseService;
import com.hongyang.platform.metarepo.service.entity.metamodel.TenantMetadata;

import java.util.List;

/**
 * Tenant 级通用元数据 Service 接口（p_tenant_metadata）。
 */
public interface ITenantMetadataService extends IBaseService<TenantMetadata> {

    /** 按 metamodelApiKey 查询当前租户的所有元数据（含 delete_flg=1 用于合并遮蔽） */
    List<TenantMetadata> listByMetamodelApiKey(String metamodelApiKey);

    /** 按 metamodelApiKey + apiKey 查询单条 */
    TenantMetadata getByMetamodelApiKeyAndApiKey(String metamodelApiKey, String apiKey);
}
