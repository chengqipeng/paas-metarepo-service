package com.hongyang.platform.metarepo.service.service.metadata.impl;

import com.hongyang.platform.metarepo.service.entity.metadata.CheckRule;
import com.hongyang.platform.metarepo.service.service.metadata.AbstractMetadataServiceImpl;
import com.hongyang.platform.metarepo.service.service.metadata.ICheckRuleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CheckRuleServiceImpl
        extends AbstractMetadataServiceImpl<CheckRule>
        implements ICheckRuleService {

    @Override
    public List<CheckRule> listMerged(String entityApiKey) {
        return super.listMerged().stream()
                .filter(e -> entityApiKey.equals(e.getEntityApiKey()))
                .collect(Collectors.toList());
    }

    @Override
    public List<CheckRule> listByEntityApiKey(String entityApiKey) {
        return listMerged(entityApiKey);
    }
}
