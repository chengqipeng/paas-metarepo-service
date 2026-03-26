package com.hongyang.platform.metarepo.service.repository;

import com.alibaba.fastjson2.JSON;
import com.hongyang.framework.dao.service.SimpleBaseServiceImpl;
import com.hongyang.platform.metarepo.core.model.request.CreateCheckRuleRequest;
import com.hongyang.platform.metarepo.core.model.request.UpdateCheckRuleRequest;
import com.hongyang.framework.base.exception.BaseKnownException;
import com.hongyang.framework.common.enums.paas.MetaRepoErrorCodeEnum;
import com.hongyang.platform.metarepo.service.entity.CustomCheckRule;
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
        extends SimpleBaseServiceImpl<CustomCheckRule>
        implements ICustomCheckRuleService {

    private final IMetaLogService metaLogService;

    @Override
    public List<CustomCheckRule> listByEntityId(Long tenantId, Long entityId) {
        return lambdaQuery()
                .eq(CustomCheckRule::getTenantId, tenantId)
                .eq(CustomCheckRule::getEntityId, entityId)
                .list();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CustomCheckRule createRule(CreateCheckRuleRequest request) {
        CustomCheckRule rule = new CustomCheckRule();
        rule.setTenantId(request.getTenantId());
        rule.setEntityId(request.getEntityId());
        rule.setName(request.getName());
        rule.setApiKey(request.getApiKey());
        rule.setLabel(request.getLabel());
        rule.setLabelKey(request.getLabelKey());
        rule.setActiveFlg(request.getActiveFlg() != null ? request.getActiveFlg() : 1);
        rule.setCheckFormula(request.getCheckFormula());
        rule.setCheckErrorMsg(request.getCheckErrorMsg());
        rule.setCheckErrorLocation(request.getCheckErrorLocation());
        rule.setCheckErrorItemId(request.getCheckErrorItemId());
        save(rule);

        metaLogService.log(request.getTenantId(), rule.getId(), request.getEntityId(), null,
                null, JSON.toJSONString(rule), 1);
        return rule;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CustomCheckRule updateRule(Long ruleId, UpdateCheckRuleRequest request) {
        CustomCheckRule rule = getByIdAndTenant(ruleId, request.getTenantId());
        if (rule == null) {
            throw new BaseKnownException(MetaRepoErrorCodeEnum.META_NOT_FOUND, String.valueOf(ruleId));
        }
        String oldValue = JSON.toJSONString(rule);

        if (request.getName() != null) rule.setName(request.getName());
        if (request.getLabel() != null) rule.setLabel(request.getLabel());
        if (request.getActiveFlg() != null) rule.setActiveFlg(request.getActiveFlg());
        if (request.getCheckFormula() != null) rule.setCheckFormula(request.getCheckFormula());
        if (request.getCheckErrorMsg() != null) rule.setCheckErrorMsg(request.getCheckErrorMsg());
        if (request.getCheckErrorLocation() != null) rule.setCheckErrorLocation(request.getCheckErrorLocation());
        if (request.getCheckErrorItemId() != null) rule.setCheckErrorItemId(request.getCheckErrorItemId());
        updateById(rule);

        metaLogService.log(request.getTenantId(), ruleId, rule.getEntityId(), null,
                oldValue, JSON.toJSONString(rule), 2);
        return rule;
    }
}
