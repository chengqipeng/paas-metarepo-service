package com.hongyang.platform.metarepo.service.service.metamodel.impl;

import com.hongyang.framework.dao.service.SimpleBaseServiceImpl;
import com.hongyang.platform.metarepo.service.entity.metamodel.MetaItem;
import com.hongyang.platform.metarepo.service.service.metamodel.IMetaItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 元模型字段项 Service 实现（p_meta_item）
 * <p>
 * 对标老系统 MetaItemServiceImpl，使用 framework-dao 的 SimpleBaseServiceImpl 替代 sns-dal Dao。
 * 老系统通过 MetaItemCacheProvides 做本地缓存（按 metaModelId 缓存字段列表），
 * 新系统由 SimpleBaseServiceImpl 内置的 DaoCacheManager 统一管理。
 */
@Slf4j
@Service
public class MetaItemServiceImpl extends SimpleBaseServiceImpl<MetaItem> implements IMetaItemService {

    @Override
    public List<MetaItem> listByMetamodelApiKey(String metamodelApiKey) {
        return lambdaQuery()
                .eq(MetaItem::getMetamodelApiKey, metamodelApiKey)
                .orderByAsc(MetaItem::getItemOrder)
                .list();
    }

    @Override
    public List<MetaItem> listEnablePackageByMetamodelApiKey(String metamodelApiKey) {
        return lambdaQuery()
                .eq(MetaItem::getMetamodelApiKey, metamodelApiKey)
                .eq(MetaItem::getEnablePackage, 1)
                .orderByAsc(MetaItem::getItemOrder)
                .list();
    }

    @Override
    public MetaItem getByApiKey(String metamodelApiKey, String apiKey) {
        return lambdaQuery()
                .eq(MetaItem::getMetamodelApiKey, metamodelApiKey)
                .eq(MetaItem::getApiKey, apiKey)
                .one();
    }

    @Override
    public boolean deleteByMetamodelApiKey(String metamodelApiKey) {
        return lambdaUpdate()
                .eq(MetaItem::getMetamodelApiKey, metamodelApiKey)
                .remove();
    }
}
