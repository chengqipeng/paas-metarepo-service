package com.hongyang.platform.metarepo.service.entity.metamodel;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hongyang.framework.dao.entity.BaseMetaCommonEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 元模型定义（p_meta_model）
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("paas_metarepo_common.p_meta_model")
public class MetaModel extends BaseMetaCommonEntity {

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
    private Integer enableCommon;
    private Integer enableTenant;
    private String dbTable;
    private String description;
    private String descriptionKey;
    private Integer entityDependency;
    private Integer visible;

}
