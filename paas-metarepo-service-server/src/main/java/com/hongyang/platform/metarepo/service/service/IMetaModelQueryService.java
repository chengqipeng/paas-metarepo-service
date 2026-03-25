package com.hongyang.platform.metarepo.service.service;

import com.hongyang.platform.metarepo.core.model.metamodel.XMetaModel;
import java.util.List;

/**
 * 元模型组装与查询 Service 接口
 * 核心读取服务，将多张表组装为完整的 XMetaModel
 */
public interface IMetaModelQueryService {

    /** 组装完整元模型（对象+字段+关系+选项值+校验规则） */
    XMetaModel getMetaModel(Long tenantId, String objectApiKey);

    /** 批量组装元模型 */
    List<XMetaModel> batchGetMetaModel(Long tenantId, List<String> apiKeys);

    /** 查询租户下所有对象的元模型列表 */
    List<XMetaModel> listMetaModels(Long tenantId);
}
