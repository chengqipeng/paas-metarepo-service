package com.hongyang.platform.metarepo.service.service.metamodel;

import com.hongyang.framework.dao.service.IBaseService;
import com.hongyang.platform.metarepo.service.entity.metamodel.MetaItem;

import java.util.List;

/**
 * 元模型字段项 Service 接口（p_meta_item）
 */
public interface IMetaItemService extends IBaseService<MetaItem> {

    /**
     * 根据元模型 apiKey 查询所有字段项
     */
    List<MetaItem> listByMetamodelApiKey(String metamodelApiKey);

    /**
     * 根据元模型 apiKey 查询所有启用 Package 的字段项
     */
    List<MetaItem> listEnablePackageByMetamodelApiKey(String metamodelApiKey);

    /**
     * 根据元模型 apiKey + 字段 apiKey 查询
     */
    MetaItem getByApiKey(String metamodelApiKey, String apiKey);

    /**
     * 根据元模型 apiKey 删除所有字段项
     */
    boolean deleteByMetamodelApiKey(String metamodelApiKey);
}
