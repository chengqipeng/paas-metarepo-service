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
 * 对标老系统 EventHibernateRepoListener + ProduceLogEventService 的异步 MQ 日志，
 * 简化为同步写入 p_meta_log 表。
 * <p>
 * p_meta_log 在 TenantInterceptor.IGNORE_TABLES 中，不自动注入 tenant_id，
 * 查询时需手动指定 tenantId（运维接口支持跨租户查询）。
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
            metaLog.setTenantId(resolveTenantId());
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
    public List<MetaLog> listByTenant(Long tenantId) {
        return lambdaQuery()
            .eq(MetaLog::getTenantId, tenantId)
            .orderByDesc(MetaLog::getCreatedAt)
            .list();
    }

    @Override
    public List<MetaLog> listByMetadataApiKey(Long tenantId, String metadataApiKey) {
        return lambdaQuery()
            .eq(MetaLog::getTenantId, tenantId)
            .eq(MetaLog::getMetadataApiKey, metadataApiKey)
            .orderByDesc(MetaLog::getCreatedAt)
            .list();
    }

    private Long resolveTenantId() {
        String s = GlobalContext.getSystemContext().getTenantId();
        return (s != null && !s.isEmpty()) ? Long.parseLong(s) : null;
    }
}
