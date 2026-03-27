package com.hongyang.platform.metarepo.service.service.metadata;

import com.hongyang.framework.dao.service.IBaseService;
import com.hongyang.platform.metarepo.service.entity.metadata.EntityLink;

import java.util.List;

/**
 * 对象关联关系 Service 接口（Common/Tenant 分表）
 */
public interface IEntityLinkService {

    List<EntityLink> listMerged(String parentEntityApiKey);

    List<EntityLink> listByParentEntityApiKey(String parentEntityApiKey);
}
