package com.hongyang.platform.metarepo.service.service.metamodel.impl;

import com.hongyang.framework.dao.service.SimpleBaseServiceImpl;
import com.hongyang.platform.metarepo.service.entity.metamodel.MetaLink;
import com.hongyang.platform.metarepo.service.service.metamodel.IMetaLinkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 元模型关联关系 Service 实现（p_meta_link）
 * <p>
 * 对标老系统 MetaLinkServiceImpl，使用 framework-dao 的 SimpleBaseServiceImpl 替代 sns-dal Dao。
 * 老系统通过 SQL 名称（getMetaLinkByReferItemId / getMetaLinkAllParentLink / getMetaLinkAllChildLink）
 * 查询 id 列表再加载对象，新系统使用 MyBatis-Plus lambdaQuery 实现等价逻辑。
 */
@Slf4j
@Service
public class MetaLinkServiceImpl extends SimpleBaseServiceImpl<MetaLink> implements IMetaLinkService {

    @Override
    public List<MetaLink> listByReferItemApiKey(Long referItemApiKey) {
        return lambdaQuery()
                .eq(MetaLink::getReferItemApiKey, referItemApiKey)
                .list();
    }

    @Override
    public boolean deleteByReferItemApiKey(Long referItemApiKey) {
        return lambdaUpdate()
                .eq(MetaLink::getReferItemApiKey, referItemApiKey)
                .remove();
    }

    @Override
    public List<MetaLink> listAllParentLink(Long childMetamodelApiKey) {
        return lambdaQuery()
                .eq(MetaLink::getChildMetamodelApiKey, childMetamodelApiKey)
                .list();
    }

    @Override
    public List<MetaLink> listAllChildLink(Long parentMetamodelApiKey) {
        return lambdaQuery()
                .eq(MetaLink::getParentMetamodelApiKey, parentMetamodelApiKey)
                .list();
    }

    @Override
    public boolean deleteByChildMetamodelApiKey(Long childMetamodelApiKey) {
        return lambdaUpdate()
                .eq(MetaLink::getChildMetamodelApiKey, childMetamodelApiKey)
                .remove();
    }
}
