package com.hongyang.platform.metarepo.service.common.constants;

import com.hongyang.framework.common.constants.paas.MetamodelApiKey;
import com.hongyang.framework.dao.entity.BaseMetaTenantEntity;
import com.hongyang.platform.metarepo.service.entity.metadata.CheckRule;
import com.hongyang.platform.metarepo.service.entity.metadata.Entity;
import com.hongyang.platform.metarepo.service.entity.metadata.EntityItem;
import com.hongyang.platform.metarepo.service.entity.metadata.EntityLink;
import com.hongyang.platform.metarepo.service.entity.metadata.PickOption;
import com.hongyang.platform.metarepo.service.entity.metadata.ReferFilter;

/**
 * 元模型 api_key 枚举，管理 metamodelApiKey 与对应 Entity 类的映射。
 * api_key 常量定义在 framework-common 的 {@link MetamodelApiKey} 中，供所有业务服务引用。
 */
public enum MetamodelApiKeyEnum {

    ENTITY(MetamodelApiKey.ENTITY, Entity.class),
    ITEM(MetamodelApiKey.ITEM, EntityItem.class),
    PICK_OPTION(MetamodelApiKey.PICK_OPTION, PickOption.class),
    ENTITY_LINK(MetamodelApiKey.ENTITY_LINK, EntityLink.class),
    CHECK_RULE(MetamodelApiKey.CHECK_RULE, CheckRule.class),
    REFER_FILTER(MetamodelApiKey.REFER_FILTER, ReferFilter.class),
    ;

    private final String value;
    private final Class<? extends BaseMetaTenantEntity> entityClass;

    MetamodelApiKeyEnum(String value, Class<? extends BaseMetaTenantEntity> entityClass) {
        this.value = value;
        this.entityClass = entityClass;
    }

    public String getValue() {
        return value;
    }

    public Class<? extends BaseMetaTenantEntity> getEntityClass() {
        return entityClass;
    }

    public static MetamodelApiKeyEnum fromValue(String value) {
        if (value == null) return null;
        for (MetamodelApiKeyEnum e : values()) {
            if (e.value.equals(value)) return e;
        }
        return null;
    }

    public static Class<? extends BaseMetaTenantEntity> getEntityClassByKey(String metamodelApiKey) {
        MetamodelApiKeyEnum e = fromValue(metamodelApiKey);
        return e != null ? e.entityClass : null;
    }

    /** @deprecated 使用 {@link MetamodelApiKey#ENTITY} 替代 */
    public static final String K_ENTITY = MetamodelApiKey.ENTITY;
    /** @deprecated 使用 {@link MetamodelApiKey#ITEM} 替代 */
    public static final String K_ITEM = MetamodelApiKey.ITEM;
    /** @deprecated 使用 {@link MetamodelApiKey#PICK_OPTION} 替代 */
    public static final String K_PICK_OPTION = MetamodelApiKey.PICK_OPTION;
    /** @deprecated 使用 {@link MetamodelApiKey#ENTITY_LINK} 替代 */
    public static final String K_ENTITY_LINK = MetamodelApiKey.ENTITY_LINK;
    /** @deprecated 使用 {@link MetamodelApiKey#CHECK_RULE} 替代 */
    public static final String K_CHECK_RULE = MetamodelApiKey.CHECK_RULE;
    /** @deprecated 使用 {@link MetamodelApiKey#REFER_FILTER} 替代 */
    public static final String K_REFER_FILTER = MetamodelApiKey.REFER_FILTER;
}
