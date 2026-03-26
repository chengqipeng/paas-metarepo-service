package com.hongyang.platform.metarepo.service.service.metadata;

import com.hongyang.framework.dao.service.IBaseService;
import com.hongyang.platform.metarepo.service.entity.metadata.PickOption;

import java.util.List;

/**
 * 字段选项值 Service 接口（Common/Tenant 分表）
 */
public interface IPickOptionService extends IBaseService<PickOption> {

    List<PickOption> listMerged(String itemApiKey);

    List<PickOption> listByItemApiKey(String itemApiKey);
}
