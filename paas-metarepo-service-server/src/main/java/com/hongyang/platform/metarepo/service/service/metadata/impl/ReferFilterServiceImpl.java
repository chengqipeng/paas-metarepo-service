package com.hongyang.platform.metarepo.service.service.metadata.impl;

import com.hongyang.platform.metarepo.service.entity.metadata.ReferFilter;
import com.hongyang.platform.metarepo.service.service.metadata.AbstractMetadataServiceImpl;
import com.hongyang.platform.metarepo.service.service.metadata.IReferFilterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ReferFilterServiceImpl
        extends AbstractMetadataServiceImpl<ReferFilter>
        implements IReferFilterService {

    @Override
    public List<ReferFilter> listByItemApiKey(String itemApiKey) {
        return super.listMerged().stream()
                .filter(e -> itemApiKey.equals(e.getItemApiKey()))
                .collect(Collectors.toList());
    }
}
