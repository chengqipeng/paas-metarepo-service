package com.hongyang.platform.metarepo.service.entity.metadata;

import com.hongyang.framework.dao.entity.BaseMetaTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 通用元数据模型，用于没有定义专用 Entity 类的元模型类型。
 * 固定列通过基类字段访问，dbc 列的业务值存储在 fields Map 中。
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GenericMetadata extends BaseMetaTenantEntity {

    /** 所属元模型 api_key */
    private String metamodelApiKey;

    /** 所属对象 api_key */
    private String entityApiKey;

    /** 描述 */
    private String description;

    /** 业务字段（dbc 列映射后的 fieldName → value） */
    private Map<String, Object> fields = new LinkedHashMap<>();
}
