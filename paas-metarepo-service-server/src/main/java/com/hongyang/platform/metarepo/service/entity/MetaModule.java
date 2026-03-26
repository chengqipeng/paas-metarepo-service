package com.hongyang.platform.metarepo.service.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hongyang.framework.dao.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 模块定义（p_meta_module）
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("p_meta_module")
public class MetaModule extends BaseEntity {

    private String name;
    private String apiKey;
    private String label;
    private String version;
    private Integer status;
    private String description;
}
