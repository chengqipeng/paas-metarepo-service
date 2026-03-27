package com.hongyang.platform.metarepo.service.service.metamodel;

import com.hongyang.framework.dao.service.IMetaService;
import com.hongyang.platform.metarepo.service.entity.metamodel.CommonMetadata;

import java.util.List;

/**
 * Common 级通用元数据 Service 接口（p_common_metadata）。
 * 走 MetaServiceImpl 的两阶段缓存。
 */
public interface ICommonMetadataService extends IMetaService<CommonMetadata> {

    /** 按 metamodelApiKey 查询所有 Common 元数据 */
    List<CommonMetadata> listByMetamodelApiKey(String metamodelApiKey);

    /** 按 metamodelApiKey + apiKey 查询单条 */
    CommonMetadata getByMetamodelApiKeyAndApiKey(String metamodelApiKey, String apiKey);
}
