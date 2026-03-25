package com.hongyang.platform.metarepo.service.service;

import com.hongyang.framework.dao.service.IBaseService;
import com.hongyang.platform.metarepo.service.entity.CustomPickOptionEntity;
import java.util.List;

/**
 * 字段选项值 Service 接口
 */
public interface ICustomPickOptionService extends IBaseService<CustomPickOptionEntity> {

    /** 查询字段的所有选项值 */
    List<CustomPickOptionEntity> listByItemId(Long tenantId, Long itemId);

    /** 批量保存选项值（先删后插） */
    void saveOptions(Long tenantId, Long entityId, Long itemId, List<CustomPickOptionEntity> options);
}
