package com.hongyang.platform.metarepo.service.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记 Entity 支持 Common/Tenant 分表。
 * Common 级元数据统一存储在 p_common_metadata 大宽表中，
 * 通过 metamodelApiKey 区分不同类型的元数据。
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CommonTenantSplit {

    /** 该 Entity 对应的元模型 api_key（p_meta_model.api_key） */
    String metamodelApiKey();
}
