package com.hongyang.platform.metarepo.service.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hongyang.framework.dao.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 元数据变更日志
 * 继承 BaseEntity 统一获得 id/tenantId/deleteFlg/审计字段
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("p_meta_log")
public class MetaLog extends BaseEntity {
    private Long metadataId;
    private String traceId;
    private Long objectId;
    private Long metamodelId;
    private String oldValue;
    private String newValue;
    private Integer opType;
    private Integer fromType;
    private Long parentMetamodelId;
    private Long parentMetadataId;
    private Integer sync;
    private Long entrustTenantId;
    private Long originTenantId;
}
