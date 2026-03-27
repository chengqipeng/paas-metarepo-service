package com.hongyang.platform.metarepo.service.service.metamodel;

import com.hongyang.framework.dao.service.IMetaService;
import com.hongyang.platform.metarepo.service.entity.metamodel.TenantMetadata;

import java.util.List;

/**
 * Tenant 级通用元数据 Service 接口（p_tenant_metadata）。
 */
public interface ITenantMetadataService extends IMetaService<TenantMetadata> {

    /** 按 metamodelApiKey 查询当前租户的所有元数据（含 delete_flg=1 用于合并遮蔽） */
    List<TenantMetadata> listByMetamodelApiKey(String metamodelApiKey);

    /** 按 metamodelApiKey + apiKey 查询单条 */
    TenantMetadata getByMetamodelApiKeyAndApiKey(String metamodelApiKey, String apiKey);

    /** 按 metamodelApiKey + entityApiKey 查询（用于 item/pickOption 等子表） */
    List<TenantMetadata> listByMetamodelApiKeyAndEntityApiKey(String metamodelApiKey, String entityApiKey);

    /**
     * 创建租户级元数据。走 dao 层 create() 保证缓存一致性。
     */
    TenantMetadata createMetadata(TenantMetadata row);

    /**
     * 更新租户级元数据。走 dao 层 updateByApiKey() 保证缓存一致性。
     */
    TenantMetadata updateMetadata(TenantMetadata row);

    /**
     * 软删除租户级元数据。设置 delete_flg=1 后走 dao 层更新。
     */
    void softDeleteMetadata(TenantMetadata row, Long operatorId);
}
