package com.hongyang.platform.metarepo.service.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hongyang.framework.dao.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 迁移任务单元（p_meta_migration_process_unit）
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("p_meta_migration_process_unit")
public class MetaMigrationProcessUnit extends BaseEntity {

    private Long pid;
    private Long processId;
    private Integer migrationStage;
    private String unitName;
    private Integer rollbackUnitFlg;
    private Long startTime;
    private Long endTime;
    private Long timeConsuming;
    private String execException;
    private String rollbackException;
    private Integer status;
}
