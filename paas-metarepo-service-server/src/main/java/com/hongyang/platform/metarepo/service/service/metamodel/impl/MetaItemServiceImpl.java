package com.hongyang.platform.metarepo.service.service.metamodel.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hongyang.framework.dao.entity.BaseMetaCommonEntity;
import com.hongyang.framework.dao.service.MetaServiceImpl;
import com.hongyang.platform.metarepo.service.entity.metamodel.MetaItem;
import com.hongyang.platform.metarepo.service.service.metamodel.IMetaItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 元模型字段项 Service 实现（p_meta_item）
 * <p>
 * 对标老系统 MetaItemServiceImpl，使用 framework-dao 的 MetaServiceImpl 替代 sns-dal Dao。
 * 老系统通过 MetaItemCacheProvides 按 metaModelId 缓存字段列表，
 * 新系统由 MetaServiceImpl 内置的 DaoCacheManager 统一管理。
 * <p>
 * IMetaService 已提供：getByApiKey / listAll / create / updateByApiKey / deleteByApiKey 等。
 * 本类扩展按 metamodelApiKey 维度的查询和删除。
 */
@Slf4j
@Service
public class MetaItemServiceImpl extends MetaServiceImpl<MetaItem> implements IMetaItemService {

    @Override
    public List<MetaItem> listByMetamodelApiKey(String metamodelApiKey) {
        return baseMapper.selectList(
                Wrappers.<MetaItem>lambdaQuery()
                        .eq(MetaItem::getMetamodelApiKey, metamodelApiKey)
                        .orderByAsc(MetaItem::getItemOrder));
    }

    @Override
    public List<MetaItem> listEnablePackageByMetamodelApiKey(String metamodelApiKey) {
        return baseMapper.selectList(
                Wrappers.<MetaItem>lambdaQuery()
                        .eq(MetaItem::getMetamodelApiKey, metamodelApiKey)
                        .eq(MetaItem::getEnablePackage, 1)
                        .orderByAsc(MetaItem::getItemOrder));
    }

    @Override
    public MetaItem getByApiKey(String metamodelApiKey, String apiKey) {
        return baseMapper.selectOne(
                Wrappers.<MetaItem>lambdaQuery()
                        .eq(MetaItem::getMetamodelApiKey, metamodelApiKey)
                        .eq(BaseMetaCommonEntity::getApiKey, apiKey));
    }

    @Override
    public boolean deleteByMetamodelApiKey(String metamodelApiKey) {
        return baseMapper.delete(
                Wrappers.<MetaItem>lambdaQuery()
                        .eq(MetaItem::getMetamodelApiKey, metamodelApiKey)) > 0;
    }
}
