package com.hongyang.platform.metarepo.service.service.metadata.impl;

import com.hongyang.platform.metarepo.service.entity.metadata.Entity;
import com.hongyang.platform.metarepo.service.service.metadata.AbstractMetadataServiceImpl;
import com.hongyang.platform.metarepo.service.service.metadata.IEntityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 自定义对象 Service 实现（Common/Tenant 分表）
 * Common 数据从 p_common_metadata WHERE metamodel_api_key = 'entity' 读取，
 * 列映射从 p_meta_item 动态加载。
 */
@Slf4j
@Service
public class EntityServiceImpl
        extends AbstractMetadataServiceImpl<Entity>
        implements IEntityService {

    @Override
    public List<Entity> listMerged() {
        return listMerged(Entity::getApiKey);
    }

    @Override
    public Entity getByApiKeyMerged(String apiKey) {
        return getByApiKeyMerged(apiKey, Entity::getApiKey);
    }

    @Override
    public boolean existsApiKey(String apiKey) {
        return getByApiKeyMerged(apiKey) != null;
    }
}
