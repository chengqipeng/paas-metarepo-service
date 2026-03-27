package com.hongyang.platform.metarepo.service.service.metadata;

import com.hongyang.framework.dao.entity.BaseMetaTenantEntity;
import com.hongyang.framework.dao.query.PageResult;
import com.hongyang.platform.metarepo.service.entity.metadata.GenericMetadata;

import java.util.List;

/**
 * 元数据合并查询 Service 接口。
 * 统一处理 Common（p_common_metadata）+ Tenant（p_tenant_metadata）的合并逻辑。
 * <p>
 * entityClass 通过 MetamodelApiKeyEnum 自动解析，调用方无需传入。
 * 已注册的 metamodelApiKey 返回对应 Entity 类型，未注册的返回 GenericMetadata。
 */
public interface IMetadataMergeReadService {

    /**
     * 查询指定元模型的合并后元数据。
     * 通过 MetamodelApiKeyEnum 自动解析 entityClass。
     */
    <T extends BaseMetaTenantEntity> List<T> listMerged(String metamodelApiKey);

    /**
     * 按 apiKey 查询单条合并后元数据。
     */
    <T extends BaseMetaTenantEntity> T getByApiKeyMerged(String metamodelApiKey, String apiKey);

    /**
     * 分页查询合并后元数据。
     */
    <T extends BaseMetaTenantEntity> PageResult<T> pageMerged(String metamodelApiKey, int page, int size);

    /**
     * 查询指定元模型的合并后元数据，使用通用模型 GenericMetadata。
     * 用于没有定义专用 Entity 类的通用元模型类型。
     */
    List<GenericMetadata> listMergedGeneric(String metamodelApiKey);

    /**
     * 根据 metamodelApiKey 自动判断：
     * 如果是已注册的模型（entity/item 等）→ 转为对应 Entity 类
     * 如果是通用模型 → 转为 GenericMetadata
     */
    List<?> listMergedAuto(String metamodelApiKey);

    /**
     * 按 entityApiKey 过滤查询合并后元数据（用于 item/pickOption/checkRule/entityLink 等子表）。
     * 直接在 SQL 层按 entity_api_key 过滤，避免加载全量数据后内存过滤。
     */
    <T extends BaseMetaTenantEntity> List<T> listMergedByEntityApiKey(
            String metamodelApiKey, String entityApiKey);
}
