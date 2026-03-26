package com.hongyang.platform.metarepo.service.common.converter;

import com.hongyang.platform.metarepo.core.model.metamodel.XCheckRule;
import com.hongyang.platform.metarepo.core.model.metamodel.XEntity;
import com.hongyang.platform.metarepo.core.model.metamodel.XItem;
import com.hongyang.platform.metarepo.core.model.metamodel.XLink;
import com.hongyang.platform.metarepo.core.model.metamodel.XPickOption;
import com.hongyang.platform.metarepo.service.entity.metadata.CheckRule;
import com.hongyang.platform.metarepo.service.entity.metadata.Entity;
import com.hongyang.platform.metarepo.service.entity.metadata.EntityLink;
import com.hongyang.platform.metarepo.service.entity.metadata.EntityItem;
import com.hongyang.platform.metarepo.service.entity.metadata.PickOption;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Entity → X 元模型转换工具
 */
public final class MetaRepoConverter {

    private MetaRepoConverter() {}

    // ==================== Entity ====================

    public static XEntity toXEntity(Entity e) {
        if (e == null) return null;
        XEntity x = new XEntity();
        x.setApiKey(e.getApiKey());
        x.setLabel(e.getLabel());
        x.setLabelKey(e.getLabelKey());
        x.setNamespace(e.getNamespace());
        x.setEntityType(e.getEntityType());
        x.setSvgApiKey(e.getSvgApiKey());
        x.setSvgColor(e.getSvgColor());
        x.setDescription(e.getDescription());
        x.setDescriptionKey(e.getDescriptionKey());
        x.setCustomEntitySeq(e.getCustomEntitySeq());
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
        x.setDeleteFlg(e.getDeleteFlg());
        x.setCreatedAt(e.getCreatedAt());
        x.setCreatedBy(e.getCreatedBy());
        x.setUpdatedAt(e.getUpdatedAt());
        x.setUpdatedBy(e.getUpdatedBy());
        return x;
    }

    public static List<XEntity> toXEntityList(List<Entity> list) {
        if (list == null) return Collections.emptyList();
        return list.stream().map(MetaRepoConverter::toXEntity).collect(Collectors.toList());
    }

    // ==================== Item ====================

    public static XItem toXItem(EntityItem e) {
        if (e == null) return null;
        XItem x = new XItem();
        x.setEntityApiKey(e.getEntityApiKey());
        x.setApiKey(e.getApiKey());
        x.setLabel(e.getLabel());
        x.setLabelKey(e.getLabelKey());
        x.setNamespace(e.getNamespace());
        x.setName(e.getName());
        x.setNameKey(e.getNameKey());
        x.setItemType(e.getItemType());
        x.setDataType(e.getDataType());
        x.setTypeProperty(e.getTypeProperty());
        x.setHelpText(e.getHelpText());
        x.setHelpTextKey(e.getHelpTextKey());
        x.setDescription(e.getDescription());
        x.setDescriptionKey(e.getDescriptionKey());
        x.setCustomItemSeq(e.getCustomItemSeq());
        x.setDefaultValue(e.getDefaultValue());
        x.setRequireFlg(e.getRequireFlg());
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
        x.setReferEntityApiKey(e.getReferEntityApiKey());
        x.setReferLinkApiKey(e.getReferLinkApiKey());
        x.setDbColumn(e.getDbColumn());
        x.setItemOrder(e.getItemOrder());
        x.setSortFlg(e.getSortFlg());
        x.setColumnName(e.getColumnName());
        x.setDeleteFlg(e.getDeleteFlg());
        x.setCreatedAt(e.getCreatedAt());
        x.setCreatedBy(e.getCreatedBy());
        x.setUpdatedAt(e.getUpdatedAt());
        x.setUpdatedBy(e.getUpdatedBy());
        return x;
    }

    public static List<XItem> toXItemList(List<EntityItem> list) {
        if (list == null) return Collections.emptyList();
        return list.stream().map(MetaRepoConverter::toXItem).collect(Collectors.toList());
    }

    // ==================== Link ====================

    public static XLink toXLink(EntityLink e) {
        if (e == null) return null;
        XLink x = new XLink();
        x.setApiKey(e.getApiKey());
        x.setLabel(e.getLabel());
        x.setLabelKey(e.getLabelKey());
        x.setNamespace(e.getNamespace());
        x.setName(e.getName());
        x.setNameKey(e.getNameKey());
        x.setTypeProperty(e.getTypeProperty());
        x.setLinkType(e.getLinkType());
        x.setParentEntityApiKey(e.getParentEntityApiKey());
        x.setChildEntityApiKey(e.getChildEntityApiKey());
        x.setDetailLink(e.getDetailLink());
        x.setCascadeDelete(e.getCascadeDelete());
        x.setAccessControl(e.getAccessControl());
        x.setEnableFlg(e.getEnableFlg());
        x.setDescription(e.getDescription());
        x.setDescriptionKey(e.getDescriptionKey());
        x.setDeleteFlg(e.getDeleteFlg());
        x.setCreatedAt(e.getCreatedAt());
        x.setCreatedBy(e.getCreatedBy());
        return x;
    }

    public static List<XLink> toXLinkList(List<EntityLink> list) {
        if (list == null) return Collections.emptyList();
        return list.stream().map(MetaRepoConverter::toXLink).collect(Collectors.toList());
    }

    // ==================== PickOption ====================

    public static XPickOption toXPickOption(PickOption e) {
        if (e == null) return null;
        XPickOption x = new XPickOption();
        x.setEntityApiKey(e.getEntityApiKey());
        x.setItemApiKey(e.getItemApiKey());
        x.setApiKey(e.getApiKey());
        x.setLabel(e.getLabel());
        x.setLabelKey(e.getLabelKey());
        x.setNamespace(e.getNamespace());
        x.setOptionCode(e.getOptionCode());
        x.setOptionOrder(e.getOptionOrder());
        x.setDefaultFlg(e.getDefaultFlg());
        x.setGlobalFlg(e.getGlobalFlg());
        x.setCustomFlg(e.getCustomFlg());
        x.setEnableFlg(e.getEnableFlg());
        x.setDescription(e.getDescription());
        x.setDescriptionKey(e.getDescriptionKey());
        return x;
    }

    public static List<XPickOption> toXPickOptionList(List<PickOption> list) {
        if (list == null) return Collections.emptyList();
        return list.stream().map(MetaRepoConverter::toXPickOption).collect(Collectors.toList());
    }

    // ==================== CheckRule ====================

    public static XCheckRule toXCheckRule(CheckRule e) {
        if (e == null) return null;
        XCheckRule x = new XCheckRule();
        x.setEntityApiKey(e.getEntityApiKey());
        x.setApiKey(e.getApiKey());
        x.setLabel(e.getLabel());
        x.setLabelKey(e.getLabelKey());
        x.setNamespace(e.getNamespace());
        x.setName(e.getName());
        x.setNameKey(e.getNameKey());
        x.setActiveFlg(e.getActiveFlg());
        x.setDescription(e.getDescription());
        x.setDescriptionKey(e.getDescriptionKey());
        x.setCheckFormula(e.getCheckFormula());
        x.setCheckErrorMsg(e.getCheckErrorMsg());
        x.setCheckErrorMsgKey(e.getCheckErrorMsgKey());
        x.setCheckErrorLocation(e.getCheckErrorLocation());
        x.setCheckErrorItemApiKey(e.getCheckErrorItemApiKey());
        x.setCheckAllItemsFlg(e.getCheckAllItemsFlg());
        x.setCheckErrorWay(e.getCheckErrorWay());
        x.setDeleteFlg(e.getDeleteFlg());
        x.setCreatedAt(e.getCreatedAt());
        x.setCreatedBy(e.getCreatedBy());
        x.setUpdatedAt(e.getUpdatedAt());
        x.setUpdatedBy(e.getUpdatedBy());
        return x;
    }

    public static List<XCheckRule> toXCheckRuleList(List<CheckRule> list) {
        if (list == null) return Collections.emptyList();
        return list.stream().map(MetaRepoConverter::toXCheckRule).collect(Collectors.toList());
    }
}