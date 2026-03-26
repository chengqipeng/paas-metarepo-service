package com.hongyang.platform.metarepo.service.entity.metadata.common;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hongyang.framework.dao.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 模块包含的元数据清单（p_meta_module_metadata）
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("p_meta_module_metadata")
public class MetaModuleMetadata extends BaseEntity {

    private Long moduleId;
    private Long metadataId;
    private Long metamodelId;
    private String apiKey;
}
