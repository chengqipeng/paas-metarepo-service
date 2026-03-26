package com.hongyang.platform.metarepo.service.service.metamodel.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
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
 * <p>
 * 对标老系统 MetaItemServiceImpl，使用 framework-dao 的 MetaServiceImpl 替代 sns-dal Dao。
 * 所有查询走 MetaServiceImpl 的两阶段缓存路径（listByCondition / getOneByCondition），
 * 不直接使用 baseMapper 以避免绕过缓存层。
 * <p>
 * 写操作（deleteByMetamodelApiKey）直接操作 baseMapper，因为 MetaServiceImpl 的
 * beforeWrite/afterWrite 是 private 的，这里手动调用 deleteByApiKey 或直接操作后
 * 缓存会在下次读取时自然过期。对于批量删除场景，直接操作 DB 是合理的。
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
        // 批量删除：先查出所有 apiKey，逐个调用 deleteByApiKey 以走缓存失效逻辑
        MetaQueryCondition<MetaItem> condition = MetaQueryCondition.<MetaItem>create()
                .eq(MetaItem::getMetamodelApiKey, metamodelApiKey);
        List<MetaItem> items = listByCondition(condition);
        if (items.isEmpty()) {
            return false;
        }
        for (MetaItem item : items) {
            deleteByApiKey(item.getApiKey());
        }
        return true;
    }
}
