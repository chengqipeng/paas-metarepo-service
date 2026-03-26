package com.hongyang.platform.metarepo.service.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hongyang.framework.dao.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 沙箱元数据关联 Entity
 * 对应表 p_sandbox_meta_relation
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("p_sandbox_meta_relation")
public class SandboxMetaRelationEntity extends BaseEntity implements Serializable {

    /** 沙箱ID */
    private Long sandboxId;

    /** 元数据类型（entity/item/link 等） */
    private String metaType;

    /** 元数据ID */
    private Long metaId;

    /** 操作类型（create/update/delete） */
    private String operationType;
}
