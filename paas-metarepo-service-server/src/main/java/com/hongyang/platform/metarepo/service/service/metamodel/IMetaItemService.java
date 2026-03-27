package com.hongyang.platform.metarepo.service.service.metamodel;

import com.hongyang.framework.dao.service.IMetaService;
import com.hongyang.platform.metarepo.service.entity.metamodel.MetaItem;

import java.util.List;

/**
 * 元模型字段项 Service 接口（p_meta_item）
 */
public interface IMetaItemService extends IMetaService<MetaItem> {

    List<MetaItem> listByMetamodelApiKey(String metamodelApiKey);

    List<MetaItem> listEnablePackageByMetamodelApiKey(String metamodelApiKey);

    MetaItem getByApiKey(String metamodelApiKey, String apiKey);

    boolean deleteByMetamodelApiKey(String metamodelApiKey);
}
