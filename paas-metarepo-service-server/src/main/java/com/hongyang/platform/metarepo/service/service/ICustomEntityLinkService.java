package com.hongyang.platform.metarepo.service.service;

import com.hongyang.framework.dao.service.IBaseService;
import com.hongyang.platform.metarepo.service.entity.CustomEntityLink;
import java.util.List;

/**
 * 对象关联关系 Service 接口
 */
public interface ICustomEntityLinkService extends IBaseService<CustomEntityLink> {

    /** 查询对象的所有关联关系（作为父或子） */
    List<CustomEntityLink> listByEntityId(Long tenantId, Long entityId);

    /** 查询对象作为父方的关联关系 */
    List<CustomEntityLink> listByParentEntityId(Long tenantId, Long parentEntityId);

    /** 查询对象作为子方的关联关系 */
    List<CustomEntityLink> listByChildEntityId(Long tenantId, Long childEntityId);
}
