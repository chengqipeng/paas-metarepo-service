package com.hongyang.platform.metarepo.service.service;

import com.hongyang.framework.dao.service.IBaseService;
import com.hongyang.platform.metarepo.service.entity.CustomCheckRuleEntity;
import java.util.List;

/**
 * 校验规则 Service 接口
 */
public interface ICustomCheckRuleService extends IBaseService<CustomCheckRuleEntity> {

    /** 查询对象下所有校验规则 */
    List<CustomCheckRuleEntity> listByObjectId(Long tenantId, Long objectId);
}
