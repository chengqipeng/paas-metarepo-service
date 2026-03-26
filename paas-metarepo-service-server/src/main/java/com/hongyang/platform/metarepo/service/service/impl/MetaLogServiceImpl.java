package com.hongyang.platform.metarepo.service.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hongyang.framework.multi.context.context.GlobalContext;
import com.hongyang.platform.metarepo.service.entity.MetaLog;
import com.hongyang.platform.metarepo.service.service.IMetaLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class MetaLogServiceImpl
        extends ServiceImpl<BaseMapper<MetaLog>, MetaLog>
        implements IMetaLogService {

    @Override
    public void log(Long tenantId, Long metadataId, Long entityId, Long metamodelId,
                    String oldValue, String newValue, Integer opType) {
        MetaLog logEntity = new MetaLog();
        logEntity.setTenantId(tenantId);
        logEntity.setMetadataId(metadataId);
        logEntity.setEntityId(entityId);
        logEntity.setMetamodelId(metamodelId);
        logEntity.setOldValue(oldValue);
        logEntity.setNewValue(newValue);
        logEntity.setOpType(opType);
        logEntity.setCreatedAt(System.currentTimeMillis());
        try {
            String traceId = GlobalContext.getSystemContext().getTraceId();
            logEntity.setTraceId(traceId != null ? traceId : "");
            String userId = GlobalContext.getSystemContext().getUserId();
            if (userId != null && !userId.isEmpty()) {
                logEntity.setCreatedBy(Long.parseLong(userId));
            }
        } catch (Exception e) {
            logEntity.setTraceId("");
        }
        save(logEntity);
    }

    @Override
    public List<MetaLog> listByMetadataId(Long tenantId, Long metadataId) {
        return lambdaQuery()
                .eq(MetaLog::getTenantId, tenantId)
                .eq(MetaLog::getMetadataId, metadataId)
                .orderByDesc(MetaLog::getCreatedAt)
                .list();
    }

    @Override
    public List<MetaLog> listByTenant(Long tenantId) {
        return lambdaQuery()
                .eq(MetaLog::getTenantId, tenantId)
                .orderByDesc(MetaLog::getCreatedAt)
                .last("LIMIT 100")
                .list();
    }
}
