package com.hongyang.platform.metarepo.service.service.impl;

import com.hongyang.framework.dao.service.SimpleBaseServiceImpl;
import com.hongyang.platform.metarepo.service.entity.CustomCheckRuleEntity;
import com.hongyang.platform.metarepo.service.service.ICustomCheckRuleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CustomCheckRuleServiceImpl
        extends SimpleBaseServiceImpl<CustomCheckRuleEntity>
        implements ICustomCheckRuleService {

    @Override
    public List<CustomCheckRuleEntity> listByObjectId(Long tenantId, Long objectId) {
        return lambdaQuery()
                .eq(CustomCheckRuleEntity::getTenantId, tenantId)
                .eq(CustomCheckRuleEntity::getObjectId, objectId)
                .list();
    }
}
