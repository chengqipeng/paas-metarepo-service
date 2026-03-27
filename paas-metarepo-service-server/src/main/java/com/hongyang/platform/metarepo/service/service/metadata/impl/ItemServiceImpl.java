package com.hongyang.platform.metarepo.service.service.metadata.impl;

import com.hongyang.platform.metarepo.service.entity.metadata.EntityItem;
import com.hongyang.platform.metarepo.service.service.metadata.AbstractMetadataServiceImpl;
import com.hongyang.platform.metarepo.service.service.metadata.IItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ItemServiceImpl
        extends AbstractMetadataServiceImpl<EntityItem>
        implements IItemService {

    @Override
    public List<EntityItem> listMerged(String entityApiKey) {
        return super.listMerged().stream()
                .filter(e -> entityApiKey.equals(e.getEntityApiKey()))
                .collect(Collectors.toList());
    }

    @Override
    public EntityItem getByApiKeyMerged(String apiKey) {
        return super.getByApiKeyMerged(apiKey);
    }

    @Override
    public List<EntityItem> listByEntityApiKey(String entityApiKey) {
        return listMerged(entityApiKey);
    }
}
