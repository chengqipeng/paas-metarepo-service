package com.hongyang.platform.metarepo.service.service.metadata;

import com.hongyang.framework.dao.entity.BaseMetaTenantEntity;
import com.hongyang.framework.dao.query.PageResult;
import com.hongyang.platform.metarepo.service.entity.metadata.GenericMetadata;

import java.util.List;

/**
 * 元数据合并查询 Service 接口。
 * 统一处理 Common（p_common_metadata）+ Tenant（p_tenant_metadata）的合并逻辑。
 */
public interface IMetadataMergeService {

    /**
     * 查询指定元模型的合并后元数据，转换为指定的 Entity 类。
     * 用于已定义专用 Entity 类的模型（entity/item/pickOption 等）。
     */
    <T extends BaseMetaTenantEntity> List<T> listMerged(String metamodelApiKey, Class<T> entityClass);

    /**
     * 按 apiKey 查询单条合并后元数据，转换为指定的 Entity 类。
     */
    <T extends BaseMetaTenantEntity> T getByApiKeyMerged(String metamodelApiKey, String apiKey, Class<T> entityClass);

    /**
     * 分页查询合并后元数据。
     */
    <T extends BaseMetaTenantEntity> PageResult<T> pageMerged(String metamodelApiKey, Class<T> entityClass, int page, int size);

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
}
