package com.hongyang.platform.metarepo.service.service.metamodel;

import com.hongyang.framework.dao.service.IMetaService;
import com.hongyang.platform.metarepo.service.entity.metamodel.MetaModel;

import java.util.List;

/**
 * 元模型定义 Service 接口（p_meta_model）
 */
public interface IMetaModelService extends IMetaService<MetaModel> {

    /**
     * 查询所有启用 Package 的元模型
     */
    List<MetaModel> listAllEnablePackage();

    /**
     * 查询所有启用 App 的元模型
     */
    List<MetaModel> listAllEnableApp();
}
