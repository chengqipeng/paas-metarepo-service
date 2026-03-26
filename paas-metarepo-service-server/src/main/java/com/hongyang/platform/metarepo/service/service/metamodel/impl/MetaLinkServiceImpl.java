package com.hongyang.platform.metarepo.service.service.metamodel.impl;

import com.hongyang.framework.dao.query.MetaQueryCondition;
import com.hongyang.framework.dao.service.MetaServiceImpl;
import com.hongyang.platform.metarepo.service.entity.metamodel.MetaLink;
import com.hongyang.platform.metarepo.service.service.metamodel.IMetaLinkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 元模型关联关系 Service 实现（p_meta_link）
 * <p>
 * 对标老系统 MetaLinkServiceImpl，使用 framework-dao 的 MetaServiceImpl 替代 sns-dal Dao。
 * 所有查询走 MetaServiceImpl 的两阶段缓存路径（listByCondition），
 * 不直接使用 baseMapper 以避免绕过缓存层。
 * <p>
 * IMetaService 已提供：getByApiKey / listAll / create / updateByApiKey / deleteByApiKey 等。
 * 本类扩展按 referItemApiKey / childMetamodelApiKey / parentMetamodelApiKey 维度的查询和删除。
 */
@Slf4j
@Service
public class MetaLinkServiceImpl extends MetaServiceImpl<MetaLink> implements IMetaLinkService {

    @Override
    public List<MetaLink> listByReferItemApiKey(Long referItemApiKey) {
        MetaQueryCondition<MetaLink> condition = MetaQueryCondition.<MetaLink>create()
                .eq(MetaLink::getReferItemApiKey, referItemApiKey);
        return listByCondition(condition);
    }

    @Override
    public boolean deleteByReferItemApiKey(Long referItemApiKey) {
        List<MetaLink> links = listByReferItemApiKey(referItemApiKey);
        if (links.isEmpty()) {
            return false;
        }
        for (MetaLink link : links) {
            deleteByApiKey(link.getApiKey());
        }
        return true;
    }

    @Override
    public List<MetaLink> listAllParentLink(Long childMetamodelApiKey) {
        MetaQueryCondition<MetaLink> condition = MetaQueryCondition.<MetaLink>create()
                .eq(MetaLink::getChildMetamodelApiKey, childMetamodelApiKey);
        return listByCondition(condition);
    }

    @Override
    public List<MetaLink> listAllChildLink(Long parentMetamodelApiKey) {
        MetaQueryCondition<MetaLink> condition = MetaQueryCondition.<MetaLink>create()
                .eq(MetaLink::getParentMetamodelApiKey, parentMetamodelApiKey);
        return listByCondition(condition);
    }

    @Override
    public boolean deleteByChildMetamodelApiKey(Long childMetamodelApiKey) {
        List<MetaLink> links = listAllParentLink(childMetamodelApiKey);
        if (links.isEmpty()) {
            return false;
        }
        for (MetaLink link : links) {
            deleteByApiKey(link.getApiKey());
        }
        return true;
    }
}
