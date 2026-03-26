package com.hongyang.platform.metarepo.service.service.metadata;

import com.hongyang.framework.dao.service.IBaseService;
import com.hongyang.platform.metarepo.service.entity.metadata.ReferFilter;

import java.util.List;

/**
 * 关联过滤条件 Service 接口
 */
public interface IReferFilterService extends IBaseService<ReferFilter> {

    List<ReferFilter> listByItemApiKey(String itemApiKey);
}
