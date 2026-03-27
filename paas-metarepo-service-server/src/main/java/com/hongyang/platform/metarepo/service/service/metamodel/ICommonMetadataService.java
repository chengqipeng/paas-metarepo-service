package com.hongyang.platform.metarepo.service.service.metamodel;

import com.hongyang.framework.dao.service.IMetaService;
import com.hongyang.platform.metarepo.service.entity.metamodel.CommonMetadata;

import java.util.List;

/**
 * Common 级通用元数据 Service 接口（p_common_metadata）。
 */
public interface ICommonMetadataService extends IMetaService<CommonMetadata> {

    List<CommonMetadata> listByMetamodelApiKey(String metamodelApiKey);

    CommonMetadata getByMetamodelApiKeyAndApiKey(String metamodelApiKey, String apiKey);

    /** 按 metamodelApiKey + entityApiKey 查询（用于 item/pickOption 等子表） */
    List<CommonMetadata> listByMetamodelApiKeyAndEntityApiKey(String metamodelApiKey, String entityApiKey);
}
