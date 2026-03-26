package com.hongyang.platform.metarepo.service.service.metamodel.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
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
 * 老系统通过 SQL 名称（getMetaLinkByReferItemId / getMetaLinkAllParentLink / getMetaLinkAllChildLink）
 * 查询 id 列表再加载对象，新系统使用 baseMapper + Wrappers 实现等价逻辑。
 * <p>
 * IMetaService 已提供：getByApiKey / listAll / create / updateByApiKey / deleteByApiKey 等。
 * 本类扩展按 referItemApiKey / childMetamodelApiKey / parentMetamodelApiKey 维度的查询和删除。
 */
@Slf4j
@Service
public class MetaLinkServiceImpl extends MetaServiceImpl<MetaLink> implements IMetaLinkService {

    @Override
    public List<MetaLink> listByReferItemApiKey(Long referItemApiKey) {
        return baseMapper.selectList(
                Wrappers.<MetaLink>lambdaQuery()
                        .eq(MetaLink::getReferItemApiKey, referItemApiKey));
    }

    @Override
    public boolean deleteByReferItemApiKey(Long referItemApiKey) {
        return baseMapper.delete(
                Wrappers.<MetaLink>lambdaQuery()
                        .eq(MetaLink::getReferItemApiKey, referItemApiKey)) > 0;
    }

    @Override
    public List<MetaLink> listAllParentLink(Long childMetamodelApiKey) {
        return baseMapper.selectList(
                Wrappers.<MetaLink>lambdaQuery()
                        .eq(MetaLink::getChildMetamodelApiKey, childMetamodelApiKey));
    }

    @Override
    public List<MetaLink> listAllChildLink(Long parentMetamodelApiKey) {
        return baseMapper.selectList(
                Wrappers.<MetaLink>lambdaQuery()
                        .eq(MetaLink::getParentMetamodelApiKey, parentMetamodelApiKey));
    }

    @Override
    public boolean deleteByChildMetamodelApiKey(Long childMetamodelApiKey) {
        return baseMapper.delete(
                Wrappers.<MetaLink>lambdaQuery()
                        .eq(MetaLink::getChildMetamodelApiKey, childMetamodelApiKey)) > 0;
    }
}
