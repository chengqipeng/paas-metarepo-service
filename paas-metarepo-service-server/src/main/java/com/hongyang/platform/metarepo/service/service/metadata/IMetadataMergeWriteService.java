package com.hongyang.platform.metarepo.service.service.metadata;

import com.hongyang.framework.dao.entity.BaseMetaTenantEntity;

/**
 * 租户级元数据写入 Service 接口。
 * 所有写操作（create/update/delete）均写入 p_tenant_metadata。
 * entityClass 通过 MetamodelApiKeyEnum 自动解析，调用方无需传入。
 */
public interface IMetadataMergeWriteService {

    /**
     * 创建租户级元数据。
     * 将业务 Entity 转换为 TenantMetadata 写入大宽表，并记录变更日志。
     *
     * @param metamodelApiKey 元模型 api_key
     * @param entity          业务 Entity（如 Entity、EntityItem 等）
     * @param operatorId      操作人 ID
     * @return 写入后的业务 Entity（含生成的 id 等）
     */
    <T extends BaseMetaTenantEntity> T create(String metamodelApiKey, T entity, Long operatorId);

    /**
     * 更新租户级元数据。
     * 按 apiKey 定位记录，将业务 Entity 转换为 TenantMetadata 更新大宽表，并记录变更日志。
     *
     * @param metamodelApiKey 元模型 api_key
     * @param entity          业务 Entity（必须包含 apiKey）
     * @param operatorId      操作人 ID
     * @return 更新后的业务 Entity
     */
    <T extends BaseMetaTenantEntity> T update(String metamodelApiKey, T entity, Long operatorId);

    /**
     * 软删除租户级元数据。
     * 按 apiKey 定位记录，设置 delete_flg=1，并记录变更日志。
     * 如果是 Common 级数据的遮蔽删除，会在 Tenant 表中插入一条 delete_flg=1 的记录。
     * entityClass 通过 MetamodelApiKeyEnum 自动解析。
     *
     * @param metamodelApiKey 元模型 api_key
     * @param apiKey          元数据 api_key
     * @param operatorId      操作人 ID
     */
    void delete(String metamodelApiKey, String apiKey, Long operatorId);
}
