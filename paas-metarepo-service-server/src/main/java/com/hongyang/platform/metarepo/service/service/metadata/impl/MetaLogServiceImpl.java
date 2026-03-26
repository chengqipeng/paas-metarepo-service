package com.hongyang.platform.metarepo.service.service.metadata.impl;

import com.alibaba.fastjson2.JSON;
import com.hongyang.framework.dao.service.DataBaseServiceImpl;
import com.hongyang.framework.multi.context.context.GlobalContext;
import com.hongyang.platform.metarepo.service.entity.metadata.MetaLog;
import com.hongyang.platform.metarepo.service.service.metadata.IMetaLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 元数据变更日志 Service 实现。
 * <p>
 * p_tenant_meta_log 由 TenantInterceptor 自动注入 tenant_id，
 * 业务代码无需手动处理租户过滤。
 */
@Slf4j
@Service
public class MetaLogServiceImpl
        extends DataBaseServiceImpl<MetaLog>
        implements IMetaLogService {

    @Override
    public void log(String entityApiKey, String metamodelApiKey,
                    Object oldValue, Object newValue, int opType) {
        try {
            MetaLog metaLog = new MetaLog();
            metaLog.setMetadataApiKey(entityApiKey);
            metaLog.setMetamodelApiKey(metamodelApiKey);
            metaLog.setOldValue(oldValue != null ? JSON.toJSONString(oldValue) : null);
            metaLog.setNewValue(newValue != null ? JSON.toJSONString(newValue) : null);
            metaLog.setOpType(opType);
            metaLog.setTraceId(GlobalContext.getSystemContext().getTraceId());
            save(metaLog);
        } catch (Exception e) {
            log.warn("[MetaLog] 记录变更日志失败: entityApiKey={}, opType={}",
                    entityApiKey, opType, e);
        }
    }

    @Override
    public List<MetaLog> listAll() {
        return lambdaQuery()
            .orderByDesc(MetaLog::getCreatedAt)
            .list();
    }

    @Override
    public List<MetaLog> listByMetadataApiKey(String metadataApiKey) {
        return lambdaQuery()
            .eq(MetaLog::getMetadataApiKey, metadataApiKey)
            .orderByDesc(MetaLog::getCreatedAt)
            .list();
    }
}
