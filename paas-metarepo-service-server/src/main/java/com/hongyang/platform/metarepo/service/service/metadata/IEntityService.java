package com.hongyang.platform.metarepo.service.service.metadata;

import com.hongyang.framework.dao.service.IBaseService;
import com.hongyang.platform.metarepo.service.entity.metadata.Entity;

import java.util.List;

/**
 * 自定义对象 Service 接口（Common/Tenant 分表）
 */
public interface IEntityService extends IBaseService<Entity> {

    List<Entity> listMerged();

    Entity getByApiKeyMerged(String apiKey);

    boolean existsApiKey(String apiKey);
}
