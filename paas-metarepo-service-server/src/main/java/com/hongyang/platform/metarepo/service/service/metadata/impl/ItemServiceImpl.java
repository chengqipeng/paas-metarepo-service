package com.hongyang.platform.metarepo.service.service.metadata.impl;

import com.hongyang.framework.dao.split.CommonTenantServiceImpl;
import com.hongyang.platform.metarepo.service.entity.metadata.EntityItem;
import com.hongyang.platform.metarepo.service.service.metadata.IItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 字段定义 Service 实现（Common/Tenant 分表）
 */
@Slf4j
@Service
public class ItemServiceImpl
        extends CommonTenantServiceImpl<EntityItem>
        implements IItemService {

    @Override
    public List<EntityItem> listMerged(String entityApiKey) {
        return merge(
            listCommon().stream()
                .filter(e -> entityApiKey.equals(e.getEntityApiKey()))
                .collect(Collectors.toList()),
            lambdaQuery().eq(EntityItem::getEntityApiKey, entityApiKey).list(),
            EntityItem::getApiKey
        );
    }

    @Override
    public EntityItem getByApiKeyMerged(String apiKey) {
        return getByApiKeyMerged(apiKey, EntityItem::getApiKey);
    }

    @Override
    public List<EntityItem> listByEntityApiKey(String entityApiKey) {
        return lambdaQuery()
            .eq(EntityItem::getEntityApiKey, entityApiKey)
            .list();
    }
}
