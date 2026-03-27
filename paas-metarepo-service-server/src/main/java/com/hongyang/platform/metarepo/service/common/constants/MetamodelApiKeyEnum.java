package com.hongyang.platform.metarepo.service.common.constants;

/**
 * 元模型 api_key 枚举，统一管理所有业务元数据类型的 metamodelApiKey。
 * 对应 p_meta_model.api_key。
 * <p>
 * 注解中使用常量：@CommonTenantSplit(metamodelApiKey = MetamodelApiKeyEnum.K_ENTITY)
 * 运行时使用枚举：MetamodelApiKeyEnum.ENTITY.getValue()
 */
public enum MetamodelApiKeyEnum {

    ENTITY("xobject"),
    ITEM("item"),
    PICK_OPTION("pickOption"),
    ENTITY_LINK("xobjectLink"),
    CHECK_RULE("checkRule"),
    REFER_FILTER("referenceFilter"),
    ;

    private final String value;

    MetamodelApiKeyEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    /** 对象定义 */
    public static final String K_ENTITY = "xobject";
    /** 字段定义 */
    public static final String K_ITEM = "item";
    /** 字段选项值 */
    public static final String K_PICK_OPTION = "pickOption";
    /** 对象关联关系 */
    public static final String K_ENTITY_LINK = "xobjectLink";
    /** 校验规则 */
    public static final String K_CHECK_RULE = "checkRule";
    /** 关联过滤条件 */
    public static final String K_REFER_FILTER = "referenceFilter";
}
