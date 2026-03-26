package com.hongyang.platform.metarepo.service.service.metadata.impl;

import com.hongyang.framework.dao.split.CommonTenantServiceImpl;
import com.hongyang.platform.metarepo.service.entity.metadata.Entity;
import com.hongyang.platform.metarepo.service.service.metadata.IEntityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 自定义对象 Service 实现（Common/Tenant 分表）
 */
@Slf4j
@Service
public class EntityServiceImpl
        extends CommonTenantServiceImpl<Entity>
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
