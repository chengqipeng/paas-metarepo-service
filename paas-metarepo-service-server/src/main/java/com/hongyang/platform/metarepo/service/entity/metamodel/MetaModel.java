package com.hongyang.platform.metarepo.service.entity.metamodel;

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
public class MetaModel extends BaseEntity {

    private String apiKey;
    private String namespace;
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
    private String descriptionKey;
    private Integer xobjectDependency;
    private Integer visible;

}
