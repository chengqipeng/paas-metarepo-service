package com.hongyang.platform.metarepo.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hongyang.platform.metarepo.service.entity.MetaLog;
import java.util.List;

public interface IMetaLogService extends IService<MetaLog> {

    /** 记录变更日志（含 old_value 快照） */
    void log(Long tenantId, Long metadataId, Long entityId, Long metamodelId,
             String oldValue, String newValue, Integer opType);

    List<MetaLog> listByMetadataId(Long tenantId, Long metadataId);

    List<MetaLog> listByTenant(Long tenantId);
}
