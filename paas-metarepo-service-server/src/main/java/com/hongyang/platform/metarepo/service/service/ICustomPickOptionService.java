package com.hongyang.platform.metarepo.service.service;

import com.hongyang.framework.dao.service.IBaseService;
import com.hongyang.platform.metarepo.service.entity.CustomPickOption;
import java.util.List;

/**
 * 字段选项值 Service 接口
 */
public interface ICustomPickOptionService extends IBaseService<CustomPickOption> {

    /** 查询字段的所有选项值 */
    List<CustomPickOption> listByItemId(Long tenantId, Long itemId);

    /** 批量保存选项值（先删后插） */
    void saveOptions(Long tenantId, Long entityId, Long itemId, List<CustomPickOption> options);
}
