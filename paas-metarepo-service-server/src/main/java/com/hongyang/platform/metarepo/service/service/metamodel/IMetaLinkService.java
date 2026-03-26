package com.hongyang.platform.metarepo.service.service.metamodel;

import com.hongyang.framework.dao.service.IBaseService;
import com.hongyang.platform.metarepo.service.entity.metamodel.MetaLink;

import java.util.List;

/**
 * 元模型关联关系 Service 接口（p_meta_link）
 */
public interface IMetaLinkService extends IBaseService<MetaLink> {

    /**
     * 根据引用字段 apiKey 查询关联
     */
    List<MetaLink> listByReferItemApiKey(Long referItemApiKey);

    /**
     * 根据引用字段 apiKey 删除关联
     */
    boolean deleteByReferItemApiKey(Long referItemApiKey);

    /**
     * 根据子元模型 apiKey 查询所有父关联
     */
    List<MetaLink> listAllParentLink(Long childMetamodelApiKey);

    /**
     * 根据父元模型 apiKey 查询所有子关联
     */
    List<MetaLink> listAllChildLink(Long parentMetamodelApiKey);

    /**
     * 根据元模型 apiKey 删除所有关联（作为子模型的关联）
     */
    boolean deleteByChildMetamodelApiKey(Long childMetamodelApiKey);
}
