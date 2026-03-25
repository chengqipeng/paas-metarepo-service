package com.hongyang.platform.metarepo.service.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hongyang.framework.dao.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 沙箱元数据项（p_sandbox_meta_item）
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("p_sandbox_meta_item")
public class SandboxMetaItemEntity extends BaseEntity {

    private Long metamodelId;
    private String metaItemApiKey;
    private String dataType;
    private String setMethod;
    private String getMethod;
}
