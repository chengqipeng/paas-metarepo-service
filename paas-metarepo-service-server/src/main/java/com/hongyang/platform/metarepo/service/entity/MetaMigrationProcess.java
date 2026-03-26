package com.hongyang.platform.metarepo.service.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hongyang.framework.dao.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 迁移任务（p_meta_migration_process）
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("p_meta_migration_process")
public class MetaMigrationProcess extends BaseEntity {

    private String packageName;
    private Integer migrationStage;
    private Long startTime;
    private Long endTime;
    private Integer status;
    private String processContent;
}
