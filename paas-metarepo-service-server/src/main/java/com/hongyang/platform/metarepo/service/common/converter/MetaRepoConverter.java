package com.hongyang.platform.metarepo.service.common.converter;

import com.hongyang.platform.metarepo.core.model.metamodel.*;
import com.hongyang.platform.metarepo.service.entity.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Entity → X 元模型转换工具
 */
public final class MetaRepoConverter {

    private MetaRepoConverter() {}

    // ==================== Entity ====================

    public static XEntity toXEntity(CustomEntityEntity e) {
        if (e == null) return null;
        XEntity x = new XEntity();
        x.setId(e.getId());
        x.setTenantId(e.getTenantId());
        x.setNameSpace(e.getNameSpace());
        x.setObjectId(e.getObjectId());
        x.setName(e.getName());
        x.setApiKey(e.getApiKey());
        x.setLabel(e.getLabel());
        x.setLabelKey(e.getLabelKey());
        x.setObjectType(e.getObjectType());
        x.setSvgId(e.getSvgId());
        x.setSvgColor(e.getSvgColor());
        x.setDescription(e.getDescription());
        x.setCustomEntityseq(e.getCustomEntityseq());
        x.setDeleteFlg(e.getDeleteFlg());
        x.setEnableFlg(e.getEnableFlg());
        x.setCustomFlg(e.getCustomFlg());
        x.setBusinessCategory(e.getBusinessCategory());
        x.setTypeProperty(e.getTypeProperty());
        x.setDbTable(e.getDbTable());
        x.setDetailFlg(e.getDetailFlg());
        x.setEnableTeam(e.getEnableTeam());
        x.setEnableSocial(e.getEnableSocial());
        x.setEnableConfig(e.getEnableConfig());
        x.setHiddenFlg(e.getHiddenFlg());
        x.setSearchable(e.getSearchable());
        x.setEnableSharing(e.getEnableSharing());
        x.setEnableScriptTrigger(e.getEnableScriptTrigger());
        x.setEnableActivity(e.getEnableActivity());
        x.setEnableHistoryLog(e.getEnableHistoryLog());
        x.setEnableReport(e.getEnableReport());
        x.setEnableRefer(e.getEnableRefer());
        x.setEnableApi(e.getEnableApi());
        x.setEnableFlow(e.getEnableFlow());
        x.setEnablePackage(e.getEnablePackage());
        x.setExtendProperty(e.getExtendProperty());
        x.setCreatedAt(e.getCreatedAt());
        x.setCreatedBy(e.getCreatedBy());
        x.setUpdatedAt(e.getUpdatedAt());
        x.setUpdatedBy(e.getUpdatedBy());
        return x;
    }

    // ==================== Item ====================

    public static XItem toXItem(CustomItemEntity e) {
        if (e == null) return null;
        XItem x = new XItem();
        x.setId(e.getId());
        x.setTenantId(e.getTenantId());
        x.setEntityId(e.getEntityId());
        x.setName(e.getName());
        x.setApiKey(e.getApiKey());
        x.setLabel(e.getLabel());
        x.setLabelKey(e.getLabelKey());
        x.setItemType(e.getItemType());
        x.setDataType(e.getDataType());
        x.setTypeProperty(e.getTypeProperty());
        x.setHelpText(e.getHelpText());
        x.setHelpTextKey(e.getHelpTextKey());
        x.setDescription(e.getDescription());
        x.setCustomItemseq(e.getCustomItemseq());
        x.setDefaultValue(e.getDefaultValue());
        x.setRequireFlg(e.getRequireFlg());
        x.setDeleteFlg(e.getDeleteFlg());
        x.setCustomFlg(e.getCustomFlg());
        x.setEnableFlg(e.getEnableFlg());
        x.setCreatable(e.getCreatable());
        x.setUpdatable(e.getUpdatable());
        x.setUniqueKeyFlg(e.getUniqueKeyFlg());
        x.setEnableHistoryLog(e.getEnableHistoryLog());
        x.setEnableConfig(e.getEnableConfig());
        x.setEnablePackage(e.getEnablePackage());
        x.setReadonlyStatus(e.getReadonlyStatus());
        x.setVisibleStatus(e.getVisibleStatus());
        x.setHiddenFlg(e.getHiddenFlg());
        x.setReferEntityId(e.getReferEntityId());
        x.setReferLinkId(e.getReferLinkId());
        x.setDbColumn(e.getDbColumn());
        x.setItemOrder(e.getItemOrder());
        x.setSortFlg(e.getSortFlg());
        x.setColumnName(e.getColumnName());
        x.setCreatedAt(e.getCreatedAt());
        x.setCreatedBy(e.getCreatedBy());
        x.setUpdatedAt(e.getUpdatedAt());
        x.setUpdatedBy(e.getUpdatedBy());
        return x;
    }

    public static List<XItem> toXItemList(List<CustomItemEntity> list) {
        if (list == null) return Collections.emptyList();
        return list.stream().map(MetaRepoConverter::toXItem).collect(Collectors.toList());
    }

    // ==================== Link ====================

    public static XLink toXLink(CustomEntityLinkEntity e) {
        if (e == null) return null;
        XLink x = new XLink();
        x.setId(e.getId());
        x.setTenantId(e.getTenantId());
        x.setName(e.getName());
        x.setApiKey(e.getApiKey());
        x.setLabel(e.getLabel());
        x.setLinkType(e.getLinkType());
        x.setParentEntityId(e.getParentEntityId());
        x.setChildEntityId(e.getChildEntityId());
        x.setCascadeDelete(e.getCascadeDelete());
        x.setAccessControl(e.getAccessControl());
        x.setEnableFlg(e.getEnableFlg());
        x.setDescription(e.getDescription());
        x.setCreatedAt(e.getCreatedAt());
        x.setCreatedBy(e.getCreatedBy());
        return x;
    }

    public static List<XLink> toXLinkList(List<CustomEntityLinkEntity> list) {
        if (list == null) return Collections.emptyList();
        return list.stream().map(MetaRepoConverter::toXLink).collect(Collectors.toList());
    }

    // ==================== PickOption ====================

    public static XPickOption toXPickOption(CustomPickOptionEntity e) {
        if (e == null) return null;
        XPickOption x = new XPickOption();
        x.setId(e.getId());
        x.setTenantId(e.getTenantId());
        x.setEntityId(e.getEntityId());
        x.setItemId(e.getItemId());
        x.setApiKey(e.getApiKey());
        x.setOptionCode(e.getOptionCode());
        x.setOptionLabel(e.getOptionLabel());
        x.setOptionOrder(e.getOptionOrder());
        x.setDefaultFlg(e.getDefaultFlg());
        x.setGlobalFlg(e.getGlobalFlg());
        x.setEnableFlg(e.getEnableFlg());
        return x;
    }

    public static List<XPickOption> toXPickOptionList(List<CustomPickOptionEntity> list) {
        if (list == null) return Collections.emptyList();
        return list.stream().map(MetaRepoConverter::toXPickOption).collect(Collectors.toList());
    }

    /** XPickOption -> CustomPickOptionEntity（用于 savePickOptions） */
    public static CustomPickOptionEntity fromXPickOption(XPickOption x) {
        if (x == null) return null;
        CustomPickOptionEntity e = new CustomPickOptionEntity();
        e.setId(x.getId());
        e.setTenantId(x.getTenantId());
        e.setEntityId(x.getEntityId());
        e.setItemId(x.getItemId());
        e.setApiKey(x.getApiKey());
        e.setOptionCode(x.getOptionCode());
        e.setOptionLabel(x.getOptionLabel());
        e.setOptionOrder(x.getOptionOrder());
        e.setDefaultFlg(x.getDefaultFlg());
        e.setGlobalFlg(x.getGlobalFlg());
        e.setEnableFlg(x.getEnableFlg());
        return e;
    }

    public static List<CustomPickOptionEntity> fromXPickOptionList(List<XPickOption> list) {
        if (list == null) return Collections.emptyList();
        return list.stream().map(MetaRepoConverter::fromXPickOption).collect(Collectors.toList());
    }

    // ==================== CheckRule ====================

    public static XCheckRule toXCheckRule(CustomCheckRuleEntity e) {
        if (e == null) return null;
        XCheckRule x = new XCheckRule();
        x.setId(e.getId());
        x.setTenantId(e.getTenantId());
        x.setObjectId(e.getObjectId());
        x.setName(e.getName());
        x.setApiKey(e.getApiKey());
        x.setActiveFlg(e.getActiveFlg());
        x.setCheckFormula(e.getCheckFormula());
        x.setCheckErrorMsg(e.getCheckErrorMsg());
        x.setCheckErrorLocation(e.getCheckErrorLocation());
        x.setCheckErrorItemId(e.getCheckErrorItemId());
        x.setCreatedAt(e.getCreatedAt());
        x.setCreatedBy(e.getCreatedBy());
        return x;
    }

    public static List<XCheckRule> toXCheckRuleList(List<CustomCheckRuleEntity> list) {
        if (list == null) return Collections.emptyList();
        return list.stream().map(MetaRepoConverter::toXCheckRule).collect(Collectors.toList());
    }
}
