package com.hongyang.platform.metarepo.service.service.metadata.impl;

import com.hongyang.platform.metarepo.service.entity.metadata.EntityLink;
import com.hongyang.platform.metarepo.service.service.metadata.AbstractMetadataServiceImpl;
import com.hongyang.platform.metarepo.service.service.metadata.IEntityLinkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EntityLinkServiceImpl
        extends AbstractMetadataServiceImpl<EntityLink>
        implements IEntityLinkService {

    @Override
    public List<EntityLink> listMerged(String parentEntityApiKey) {
        return super.listMerged().stream()
                .filter(e -> parentEntityApiKey.equals(e.getParentEntityApiKey()))
                .collect(Collectors.toList());
    }

    @Override
    public List<EntityLink> listByParentEntityApiKey(String parentEntityApiKey) {
        return listMerged(parentEntityApiKey);
    }
}
