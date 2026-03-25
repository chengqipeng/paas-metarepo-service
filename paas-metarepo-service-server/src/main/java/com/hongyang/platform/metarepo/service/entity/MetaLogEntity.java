package com.hongyang.platform.metarepo.service.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;

/**
 * 元数据变更日志（p_meta_log）
 * 该表无 delete_flg，不继承 BaseEntity，独立定义
 */
@Data
@TableName("p_meta_log")
public class MetaLogEntity implements Serializable {

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
