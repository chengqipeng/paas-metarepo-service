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
 * - metamodel#1 (TenantEntity): object_type(103), enable_sharing(105),
 *   enable_team(106), searchable(107), enable_report(108), enable_api(109)
 * - metamodel#2 (TenantItem): item_type(203), data_type(204), require_flg(205)
 */
@Component
@RequiredArgsConstructor
public class MetaOptionValidator {

    private final IMetaOptionService metaOptionService;

    // ==================== metamodel#1 字段项 ID ====================
    private static final long METAMODEL_ENTITY = 1L;
    private static final long ITEM_OBJECT_TYPE = 103L;
    private static final long ITEM_ENABLE_SHARING = 105L;
    private static final long ITEM_ENABLE_TEAM = 106L;
    private static final long ITEM_SEARCHABLE = 107L;
    private static final long ITEM_ENABLE_REPORT = 108L;
    private static final long ITEM_ENABLE_API = 109L;

    // ==================== metamodel#2 字段项 ID ====================
    private static final long METAMODEL_ITEM = 2L;
    private static final long ITEM_ITEM_TYPE = 203L;
    private static final long ITEM_DATA_TYPE = 204L;
    private static final long ITEM_REQUIRE_FLG = 205L;

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
    private void validateField(Long metamodelId, Long itemId, Integer value, String fieldName) {
        if (value == null) {
            return;
        }
        if (!metaOptionService.isValidCode(metamodelId, itemId, value)) {
            throw new BaseKnownException(
                    MetaRepoErrorCodeEnum.META_OPTION_INVALID,
                    fieldName + "=" + value
            );
        }
    }
}
