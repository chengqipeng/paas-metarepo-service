package com.hongyang.platform.metarepo.service.service.metadata.impl;

import com.hongyang.platform.metarepo.service.entity.metadata.EntityItem;
import com.hongyang.platform.metarepo.service.service.metadata.AbstractMetadataServiceImpl;
import com.hongyang.platform.metarepo.service.service.metadata.IItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 字段定义 Service 实现（Common/Tenant 分表）
 * 列映射从 p_meta_item 动态加载。
 */
@Slf4j
@Service
public class ItemServiceImpl
        extends AbstractMetadataServiceImpl<EntityItem>
        implements IItemService {

    @Override
    public List<EntityItem> listMerged(String entityApiKey) {
        return merge(
            listCommon().stream()
                .filter(e -> entityApiKey.equals(e.getEntityApiKey()))
                .collect(Collectors.toList()),
            Collections.emptyList(), // TODO: 待改造为大宽表查询 + 列映射转换
            EntityItem::getApiKey
        );
    }

    @Override
    public EntityItem getByApiKeyMerged(String apiKey) {
        return getByApiKeyMerged(apiKey, EntityItem::getApiKey);
    }

    @Override
    public List<EntityItem> listByEntityApiKey(String entityApiKey) {
        // TODO: 待改造为大宽表查询 + 列映射转换
        return Collections.emptyList();
    }
}
