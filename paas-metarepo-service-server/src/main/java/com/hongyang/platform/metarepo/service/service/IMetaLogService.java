package com.hongyang.platform.metarepo.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hongyang.platform.metarepo.service.entity.MetaLogEntity;
import java.util.List;

/**
 * 元数据变更日志 Service 接口
 * MetaLogEntity 不继承 BaseEntity，使用 IService
 */
public interface IMetaLogService extends IService<MetaLogEntity> {

    /** 记录变更日志 */
    void log(Long tenantId, Long metadataId, Long objectId, Long metamodelId,
             String oldValue, String newValue, Integer opType);

    /** 查询元数据的变更历史 */
    List<MetaLogEntity> listByMetadataId(Long tenantId, Long metadataId);
}
