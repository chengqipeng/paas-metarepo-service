package com.hongyang.platform.metarepo.service.service.metadata.impl;

import com.hongyang.platform.metarepo.service.entity.metadata.CheckRule;
import com.hongyang.platform.metarepo.service.service.metadata.AbstractMetadataServiceImpl;
import com.hongyang.platform.metarepo.service.service.metadata.ICheckRuleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 校验规则 Service 实现（Common/Tenant 分表）
 * 列映射从 p_meta_item 动态加载。
 */
@Slf4j
@Service
public class CheckRuleServiceImpl
        extends AbstractMetadataServiceImpl<CheckRule>
        implements ICheckRuleService {

    @Override
    public List<CheckRule> listMerged(String entityApiKey) {
        return merge(
            listCommon().stream()
                .filter(e -> entityApiKey.equals(e.getEntityApiKey()))
                .collect(Collectors.toList()),
            listByEntityApiKey(entityApiKey),
            CheckRule::getApiKey
        );
    }

    @Override
    public List<CheckRule> listByEntityApiKey(String entityApiKey) {
        // TODO: 待改造为大宽表查询 + 列映射转换
        return Collections.emptyList();
    }
}
