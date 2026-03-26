package com.hongyang.platform.metarepo.service.service.metadata.impl;

import com.hongyang.framework.dao.split.CommonTenantServiceImpl;
import com.hongyang.platform.metarepo.service.entity.metadata.CheckRule;
import com.hongyang.platform.metarepo.service.service.metadata.ICheckRuleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 校验规则 Service 实现（Common/Tenant 分表）
 */
@Slf4j
@Service
public class CheckRuleServiceImpl
        extends CommonTenantServiceImpl<CheckRule>
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
        return lambdaQuery()
            .eq(CheckRule::getEntityApiKey, entityApiKey)
            .list();
    }
}
