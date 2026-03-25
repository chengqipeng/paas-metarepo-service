package com.hongyang.platform.metarepo.service.service.impl;

import com.alibaba.fastjson2.JSON;
import com.hongyang.framework.dao.service.SimpleBaseServiceImpl;
import com.hongyang.platform.metarepo.core.model.request.CreateCheckRuleRequest;
import com.hongyang.platform.metarepo.core.model.request.UpdateCheckRuleRequest;
import com.hongyang.framework.base.exception.BaseKnownException;
import com.hongyang.framework.common.enums.paas.MetaRepoErrorCodeEnum;
import com.hongyang.platform.metarepo.service.entity.CustomCheckRuleEntity;
import com.hongyang.platform.metarepo.service.service.ICustomCheckRuleService;
import com.hongyang.platform.metarepo.service.service.IMetaLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomCheckRuleServiceImpl
        extends SimpleBaseServiceImpl<CustomCheckRuleEntity>
        implements ICustomCheckRuleService {

    private final IMetaLogService metaLogService;

    @Override
    public List<CustomCheckRuleEntity> listByObjectId(Long tenantId, Long objectId) {
        return lambdaQuery()
                .eq(CustomCheckRuleEntity::getTenantId, tenantId)
                .eq(CustomCheckRuleEntity::getObjectId, objectId)
                .list();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CustomCheckRuleEntity createRule(CreateCheckRuleRequest request) {
        CustomCheckRuleEntity rule = new CustomCheckRuleEntity();
        rule.setTenantId(request.getTenantId());
        rule.setObjectId(request.getObjectId());
        rule.setName(request.getName());
        rule.setApiKey(request.getApiKey());
        rule.setActiveFlg(request.getActiveFlg() != null ? request.getActiveFlg() : 1);
        rule.setCheckFormula(request.getCheckFormula());
        rule.setCheckErrorMsg(request.getCheckErrorMsg());
        rule.setCheckErrorLocation(request.getCheckErrorLocation());
        rule.setCheckErrorItemId(request.getCheckErrorItemId());
        save(rule);

        metaLogService.log(request.getTenantId(), rule.getId(), request.getObjectId(), null,
                null, JSON.toJSONString(rule), 1);
        return rule;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CustomCheckRuleEntity updateRule(Long ruleId, UpdateCheckRuleRequest request) {
        CustomCheckRuleEntity rule = getByIdAndTenant(ruleId, request.getTenantId());
        if (rule == null) {
            throw new BaseKnownException(MetaRepoErrorCodeEnum.META_NOT_FOUND, String.valueOf(ruleId));
        }
        String oldValue = JSON.toJSONString(rule);

        if (request.getName() != null) rule.setName(request.getName());
        if (request.getActiveFlg() != null) rule.setActiveFlg(request.getActiveFlg());
        if (request.getCheckFormula() != null) rule.setCheckFormula(request.getCheckFormula());
        if (request.getCheckErrorMsg() != null) rule.setCheckErrorMsg(request.getCheckErrorMsg());
        if (request.getCheckErrorLocation() != null) rule.setCheckErrorLocation(request.getCheckErrorLocation());
        if (request.getCheckErrorItemId() != null) rule.setCheckErrorItemId(request.getCheckErrorItemId());
        updateById(rule);

        metaLogService.log(request.getTenantId(), ruleId, rule.getObjectId(), null,
                oldValue, JSON.toJSONString(rule), 2);
        return rule;
    }
}
