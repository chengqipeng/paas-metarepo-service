package com.hongyang.platform.metarepo.service.service.metamodel.impl;

import com.hongyang.framework.dao.entity.BaseMetaCommonEntity;
import com.hongyang.framework.dao.query.MetaQueryCondition;
import com.hongyang.framework.dao.service.MetaServiceImpl;
import com.hongyang.platform.metarepo.service.entity.metamodel.MetaItem;
import com.hongyang.platform.metarepo.service.service.metamodel.IMetaItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 元模型字段项 Service 实现（p_meta_item）
 */
@Slf4j
@Service
public class MetaItemServiceImpl extends MetaServiceImpl<MetaItem> implements IMetaItemService {

    @Override
    public List<MetaItem> listByMetamodelApiKey(String metamodelApiKey) {
        MetaQueryCondition<MetaItem> condition = MetaQueryCondition.<MetaItem>create()
                .eq(MetaItem::getMetamodelApiKey, metamodelApiKey)
                .orderByAsc(MetaItem::getItemOrder);
        return listByCondition(condition);
    }

    @Override
    public List<MetaItem> listEnablePackageByMetamodelApiKey(String metamodelApiKey) {
        MetaQueryCondition<MetaItem> condition = MetaQueryCondition.<MetaItem>create()
                .eq(MetaItem::getMetamodelApiKey, metamodelApiKey)
                .eq(MetaItem::getEnablePackage, 1)
                .orderByAsc(MetaItem::getItemOrder);
        return listByCondition(condition);
    }

    @Override
    public MetaItem getByApiKey(String metamodelApiKey, String apiKey) {
        MetaQueryCondition<MetaItem> condition = MetaQueryCondition.<MetaItem>create()
                .eq(MetaItem::getMetamodelApiKey, metamodelApiKey)
                .eq(BaseMetaCommonEntity::getApiKey, apiKey);
        return getOneByCondition(condition);
    }

    @Override
    public boolean deleteByMetamodelApiKey(String metamodelApiKey) {
        MetaQueryCondition<MetaItem> condition = MetaQueryCondition.<MetaItem>create()
                .eq(MetaItem::getMetamodelApiKey, metamodelApiKey);
        List<MetaItem> items = listByCondition(condition);
        if (items.isEmpty()) return false;
        for (MetaItem item : items) {
            deleteByApiKey(item.getApiKey());
        }
        return true;
    }
}
