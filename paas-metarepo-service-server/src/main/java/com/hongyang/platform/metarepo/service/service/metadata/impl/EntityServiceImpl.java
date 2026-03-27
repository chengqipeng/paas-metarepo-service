package com.hongyang.platform.metarepo.service.service.metadata.impl;

import com.hongyang.platform.metarepo.service.entity.metadata.Entity;
import com.hongyang.platform.metarepo.service.service.metadata.AbstractMetadataServiceImpl;
import com.hongyang.platform.metarepo.service.service.metadata.IEntityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class EntityServiceImpl
        extends AbstractMetadataServiceImpl<Entity>
        implements IEntityService {

    @Override
    public List<Entity> listMerged() {
        return super.listMerged();
    }

    @Override
    public Entity getByApiKeyMerged(String apiKey) {
        return super.getByApiKeyMerged(apiKey);
    }

    @Override
    public boolean existsApiKey(String apiKey) {
        return getByApiKeyMerged(apiKey) != null;
    }
}
