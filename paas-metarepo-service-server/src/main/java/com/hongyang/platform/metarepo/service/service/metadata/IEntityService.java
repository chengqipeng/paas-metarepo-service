package com.hongyang.platform.metarepo.service.service.metadata;

import com.hongyang.platform.metarepo.service.entity.metadata.Entity;

import java.util.List;

/**
 * 自定义对象 Service 接口（Common/Tenant 分表合并查询）
 */
public interface IEntityService {

    List<Entity> listMerged();

    Entity getByApiKeyMerged(String apiKey);

    boolean existsApiKey(String apiKey);
}
