package com.hongyang.platform.metarepo.service.entity.metadata;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hongyang.framework.dao.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 元数据变更日志（p_meta_log）。
 * <p>
 * 继承 BaseEntity 以复用 AutoMapperRegistrar 自动注册 Mapper，无需手动创建 Mapper。
 * p_meta_log 在 TenantInterceptor.IGNORE_TABLES 中，不自动注入 tenant_id（运维接口需跨租户查询）。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("p_meta_log")
public class MetaLog extends BaseEntity {

    private Long tenantId;
    private String metadataApiKey;
    private String metamodelApiKey;
    private String traceId;
    private String oldValue;
    private String newValue;
    private Integer opType;
    private Integer fromType;
    private Integer sync;
}
