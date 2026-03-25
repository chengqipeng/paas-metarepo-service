package com.hongyang.platform.metarepo.service.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;
import java.io.Serializable;

/**
 * 元数据变更日志（不继承 BaseEntity，因为字段结构不同）
 */
@Data
@TableName("p_meta_log")
public class MetaLog implements Serializable {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private Long tenantId;
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
    private Long createdBy;
    private Long createdAt;
    private Long entrustTenantId;
    private Long originTenantId;
}
