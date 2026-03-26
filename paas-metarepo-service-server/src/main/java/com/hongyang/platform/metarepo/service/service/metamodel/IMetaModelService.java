package com.hongyang.platform.metarepo.service.service.metamodel;

import com.hongyang.framework.dao.service.IBaseService;
import com.hongyang.platform.metarepo.service.entity.metamodel.MetaModel;

import java.util.List;

/**
 * 元模型定义 Service 接口（p_meta_model）
 */
public interface IMetaModelService extends IBaseService<MetaModel> {

    /**
     * 根据 apiKey 查询元模型
     */
    MetaModel getByApiKey(String apiKey);

    /**
     * 查询所有元模型
     */
    List<MetaModel> listAll();

    /**
     * 查询所有启用 Package 的元模型
     */
    List<MetaModel> listAllEnablePackage();

    /**
     * 查询所有启用 App 的元模型
     */
    List<MetaModel> listAllEnableApp();

    /**
     * 根据 apiKey 判断是否存在
     */
    boolean existsByApiKey(String apiKey);
}
