package com.hongyang.platform.metarepo.service.common.utils;

import com.hongyang.framework.base.exception.BaseKnownException;
import com.hongyang.framework.common.enums.paas.MetaRepoErrorCodeEnum;
import com.hongyang.platform.metarepo.service.service.IMetaOptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * MetaOption 校验器
 * 校验写入字段值是否在 p_meta_option 定义的合法范围内
 *
 * p_meta_item 中 item_type=6 的字段（select 类型）必须通过 MetaOption 管控取值：
 * - CustomEntity: object_type, enable_sharing, enable_team, searchable, enable_report, enable_api
 * - CustomItem: item_type, data_type, require_flg
 */
@Component
@RequiredArgsConstructor
public class MetaOptionValidator {

    private final IMetaOptionService metaOptionService;

    // ==================== CustomEntity 元模型字段项 apiKey ====================
    private static final String METAMODEL_ENTITY = "CustomEntity";
    private static final String ITEM_OBJECT_TYPE = "object_type";
    private static final String ITEM_ENABLE_SHARING = "enable_sharing";
    private static final String ITEM_ENABLE_TEAM = "enable_team";
    private static final String ITEM_SEARCHABLE = "searchable";
    private static final String ITEM_ENABLE_REPORT = "enable_report";
    private static final String ITEM_ENABLE_API = "enable_api";

    // ==================== CustomItem 元模型字段项 apiKey ====================
    private static final String METAMODEL_ITEM = "CustomItem";
    private static final String ITEM_ITEM_TYPE = "item_type";
    private static final String ITEM_DATA_TYPE = "data_type";
    private static final String ITEM_REQUIRE_FLG = "require_flg";

    /**
     * 校验 createEntity 请求中的 select 类型字段
     */
    public void validateEntityFields(Integer objectType, Integer enableSharing,
                                     Integer enableTeam, Integer searchable,
                                     Integer enableReport, Integer enableApi) {
        validateField(METAMODEL_ENTITY, ITEM_OBJECT_TYPE, objectType, "objectType");
        validateField(METAMODEL_ENTITY, ITEM_ENABLE_SHARING, enableSharing, "enableSharing");
        validateField(METAMODEL_ENTITY, ITEM_ENABLE_TEAM, enableTeam, "enableTeam");
        validateField(METAMODEL_ENTITY, ITEM_SEARCHABLE, searchable, "searchable");
        validateField(METAMODEL_ENTITY, ITEM_ENABLE_REPORT, enableReport, "enableReport");
        validateField(METAMODEL_ENTITY, ITEM_ENABLE_API, enableApi, "enableApi");
    }

    /**
     * 校验 createItem 请求中的 select 类型字段
     */
    public void validateItemFields(Integer itemType, Integer dataType, Integer requireFlg) {
        validateField(METAMODEL_ITEM, ITEM_ITEM_TYPE, itemType, "itemType");
        validateField(METAMODEL_ITEM, ITEM_DATA_TYPE, dataType, "dataType");
        validateField(METAMODEL_ITEM, ITEM_REQUIRE_FLG, requireFlg, "requireFlg");
    }

    /**
     * 通用校验：值是否在 MetaOption 定义的合法 option_code 范围内
     */
    private void validateField(String metamodelApiKey, String itemApiKey, Integer value, String fieldName) {
        if (value == null) {
            return;
        }
        if (!metaOptionService.isValidCode(metamodelApiKey, itemApiKey, value)) {
            throw new BaseKnownException(
                    MetaRepoErrorCodeEnum.META_OPTION_INVALID,
                    fieldName + "=" + value
            );
        }
    }
}
