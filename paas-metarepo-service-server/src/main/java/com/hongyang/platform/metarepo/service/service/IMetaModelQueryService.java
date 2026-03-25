package com.hongyang.platform.metarepo.service.service;

import com.hongyang.platform.metarepo.core.model.metamodel.XItem;
import com.hongyang.platform.metarepo.core.model.metamodel.XMetaModel;
import java.util.List;

public interface IMetaModelQueryService {

    XMetaModel getMetaModel(Long tenantId, String objectApiKey);

    List<XMetaModel> batchGetMetaModel(Long tenantId, List<String> apiKeys);

    List<XMetaModel> listMetaModels(Long tenantId);

    /** 仅查询字段元数据 */
    List<XItem> getFieldMeta(Long tenantId, Long entityId);
}
