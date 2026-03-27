package com.hongyang.platform.metarepo.service.service.metadata.impl;

import com.hongyang.platform.metarepo.service.entity.metadata.PickOption;
import com.hongyang.platform.metarepo.service.service.metadata.AbstractMetadataServiceImpl;
import com.hongyang.platform.metarepo.service.service.metadata.IPickOptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PickOptionServiceImpl
        extends AbstractMetadataServiceImpl<PickOption>
        implements IPickOptionService {

    @Override
    public List<PickOption> listMerged(String itemApiKey) {
        return super.listMerged().stream()
                .filter(e -> itemApiKey.equals(e.getItemApiKey()))
                .collect(Collectors.toList());
    }

    @Override
    public List<PickOption> listByItemApiKey(String itemApiKey) {
        return listMerged(itemApiKey);
    }
}
