package com.hongyang.platform.metarepo.service.service.metadata;

import com.hongyang.framework.dao.service.IBaseService;
import com.hongyang.platform.metarepo.service.entity.metadata.CheckRule;

import java.util.List;

/**
 * 校验规则 Service 接口（Common/Tenant 分表）
 */
public interface ICheckRuleService {

    List<CheckRule> listMerged(String entityApiKey);

    List<CheckRule> listByEntityApiKey(String entityApiKey);
}
