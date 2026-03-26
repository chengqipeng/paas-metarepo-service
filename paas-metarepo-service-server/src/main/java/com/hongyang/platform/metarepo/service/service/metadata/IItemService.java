package com.hongyang.platform.metarepo.service.service.metadata;

import com.hongyang.framework.dao.service.IBaseService;
import com.hongyang.platform.metarepo.service.entity.metadata.EntityItem;

import java.util.List;

/**
 * 字段定义 Service 接口（Common/Tenant 分表）
 */
public interface IItemService extends IBaseService<EntityItem> {

    List<EntityItem> listMerged(String entityApiKey);

    EntityItem getByApiKeyMerged(String apiKey);

    List<EntityItem> listByEntityApiKey(String entityApiKey);
}
