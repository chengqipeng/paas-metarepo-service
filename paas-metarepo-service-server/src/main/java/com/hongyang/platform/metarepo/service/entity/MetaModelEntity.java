package com.hongyang.platform.metarepo.service.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hongyang.framework.dao.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 元模型定义（p_meta_model）
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("p_meta_model")
public class MetaModelEntity extends BaseEntity {

    private String apiKey;
    private String label;
    private String labelKey;
    private Integer metamodelType;
    private Integer enablePackage;
    private Integer enableApp;
    private Integer enableDeprecation;
    private Integer enableDeactivation;
    private Integer enableDelta;
    private Integer enableLog;
    private Integer deltaScope;
    private Integer deltaMode;
    private Integer enableModuleControl;
    private String dbTable;
    private String description;
    private Integer xobjectDependency;
    private Integer visible;
}
