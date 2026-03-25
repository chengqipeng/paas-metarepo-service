package com.hongyang.platform.metarepo.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hongyang.platform.metarepo.service.entity.MetaLogEntity;
import java.util.List;

public interface IMetaLogService extends IService<MetaLogEntity> {

    /** 记录变更日志（含 old_value 快照） */
    void log(Long tenantId, Long metadataId, Long objectId, Long metamodelId,
             String oldValue, String newValue, Integer opType);

    List<MetaLogEntity> listByMetadataId(Long tenantId, Long metadataId);

    List<MetaLogEntity> listByTenant(Long tenantId);
}
