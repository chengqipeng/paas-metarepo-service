package com.hongyang.platform.metarepo.service.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hongyang.framework.multi.context.context.GlobalContext;
import com.hongyang.platform.metarepo.service.entity.MetaLogEntity;
import com.hongyang.platform.metarepo.service.service.IMetaLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class MetaLogServiceImpl
        extends ServiceImpl<BaseMapper<MetaLogEntity>, MetaLogEntity>
        implements IMetaLogService {

    @Override
    public void log(Long tenantId, Long metadataId, Long objectId, Long metamodelId,
                    String oldValue, String newValue, Integer opType) {
        MetaLogEntity logEntity = new MetaLogEntity();
        logEntity.setTenantId(tenantId);
        logEntity.setMetadataId(metadataId);
        logEntity.setObjectId(objectId);
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
    public List<MetaLogEntity> listByMetadataId(Long tenantId, Long metadataId) {
        return lambdaQuery()
                .eq(MetaLogEntity::getTenantId, tenantId)
                .eq(MetaLogEntity::getMetadataId, metadataId)
                .orderByDesc(MetaLogEntity::getCreatedAt)
                .list();
    }

    @Override
    public List<MetaLogEntity> listByTenant(Long tenantId) {
        return lambdaQuery()
                .eq(MetaLogEntity::getTenantId, tenantId)
                .orderByDesc(MetaLogEntity::getCreatedAt)
                .last("LIMIT 100")
                .list();
    }
}
