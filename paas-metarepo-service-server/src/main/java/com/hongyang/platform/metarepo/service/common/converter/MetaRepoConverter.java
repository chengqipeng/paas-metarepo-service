package com.hongyang.platform.metarepo.service.common.converter;

import com.hongyang.platform.metarepo.core.model.metamodel.XCheckRule;
import com.hongyang.platform.metarepo.core.model.metamodel.XEntity;
import com.hongyang.platform.metarepo.core.model.metamodel.XEntityItem;
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

    public static XEntityItem toXEntityItem(EntityItem e) {
        if (e == null) return null;
        XEntityItem x = new XEntityItem();
        x.setEntityApiKey(e.getEntityApiKey());
        x.setApiKey(e.getApiKey());
        x.setLabel(e.getLabel());
        x.setLabelKey(e.getLabelKey());
        x.setNamespace(e.getNamespace());
        x.setItemType(e.getItemType());
        x.setDataType(e.getDataType());
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
        x.setEnableDeactive(e.getEnableDeactive());
        x.setCompound(e.getCompound());
        x.setMaskPrefix(e.getMaskPrefix());
        x.setMaskSuffix(e.getMaskSuffix());
        x.setEncrypt(e.getEncrypt());
        x.setIndexOrder(e.getIndexOrder());
        x.setIndexType(e.getIndexType());
        x.setMarkdown(e.getMarkdown());
        x.setMaskSymbolType(e.getMaskSymbolType());
        x.setIncrementStrategy(e.getIncrementStrategy());
        x.setReferItemFilterEnable(e.getReferItemFilterEnable());
        x.setIsComputeMultiCurrencyUnit(e.getIsComputeMultiCurrencyUnit());
        x.setFormat(e.getFormat());
        // 原 typeProperty JSON 字段
        x.setMaxLength(e.getMaxLength());
        x.setMinLength(e.getMinLength());
        x.setDecimal(e.getDecimal());
        x.setIsCurrency(e.getIsCurrency());
        x.setCurrencyPart(e.getCurrencyPart());
        x.setIsMultiCurrency(e.getIsMultiCurrency());
        x.setCurrencyFlg(e.getCurrencyFlg());
        x.setComputeType(e.getComputeType());
        x.setRealTimeCompute(e.getRealTimeCompute());
        x.setReferGlobal(e.getReferGlobal());
        x.setGlobalPickItem(e.getGlobalPickItem());
        x.setGlobalPickItemApikey(e.getGlobalPickItemApikey());
        x.setIsExternal(e.getIsExternal());
        x.setDateMode(e.getDateMode());
        x.setCascadeDelete(e.getCascadeDelete());
        x.setIsDetail(e.getIsDetail());
        x.setCanBatchCreate(e.getCanBatchCreate());
        x.setIsCopyWithParent(e.getIsCopyWithParent());
        x.setIsMask(e.getIsMask());
        x.setEnableMultiDetail(e.getEnableMultiDetail());
        x.setBatchCreateMode(e.getBatchCreateMode());
        x.setBatchCreateLinkByBusitype(e.getBatchCreateLinkByBusitype());
        x.setJoinItem(e.getJoinItem());
        x.setJoinObject(e.getJoinObject());
        x.setJoinLink(e.getJoinLink());
        x.setLinkLabel(e.getLinkLabel());
        x.setReferEntityApiKeys(e.getReferEntityApiKeys());
        x.setEntityOrData(e.getEntityOrData());
        x.setGroupKey(e.getGroupKey());
        x.setCompoundSub(e.getCompoundSub());
        x.setCompoundApiKey(e.getCompoundApiKey());
        x.setMultiLineText(e.getMultiLineText());
        x.setScanCodeEntryFlg(e.getScanCodeEntryFlg());
        x.setCaseSensitive(e.getCaseSensitive());
        x.setShowRows(e.getShowRows());
        x.setWatermarkFlg(e.getWatermarkFlg());
        x.setWatermarkTimeFlg(e.getWatermarkTimeFlg());
        x.setWatermarkLoginUserFlg(e.getWatermarkLoginUserFlg());
        x.setWatermarkLocationFlg(e.getWatermarkLocationFlg());
        x.setWatermarkJoinField(e.getWatermarkJoinField());
        x.setDeleteFlg(e.getDeleteFlg());
        x.setCreatedAt(e.getCreatedAt());
        x.setCreatedBy(e.getCreatedBy());
        x.setUpdatedAt(e.getUpdatedAt());
        x.setUpdatedBy(e.getUpdatedBy());
        return x;
    }

    public static List<XEntityItem> toXEntityItemList(List<EntityItem> list) {
        if (list == null) return Collections.emptyList();
        return list.stream().map(MetaRepoConverter::toXEntityItem).collect(Collectors.toList());
    }

    // ==================== Link ====================

    public static XLink toXLink(EntityLink e) {
        if (e == null) return null;
        XLink x = new XLink();
        x.setApiKey(e.getApiKey());
        x.setLabel(e.getLabel());
        x.setLabelKey(e.getLabelKey());
        x.setNamespace(e.getNamespace());
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

    // ==================== X → Entity 反向转换 ====================

    public static Entity toEntity(XEntity x) {
        if (x == null) return null;
        Entity e = new Entity();
        e.setApiKey(x.getApiKey());
        e.setLabel(x.getLabel());
        e.setLabelKey(x.getLabelKey());
        e.setNamespace(x.getNamespace());
        e.setEntityType(x.getEntityType());
        e.setSvgApiKey(x.getSvgApiKey());
        e.setSvgColor(x.getSvgColor());
        e.setDescription(x.getDescription());
        e.setDescriptionKey(x.getDescriptionKey());
        e.setCustomEntitySeq(x.getCustomEntitySeq());
        e.setEnableFlg(x.getEnableFlg());
        e.setCustomFlg(x.getCustomFlg());
        e.setBusinessCategory(x.getBusinessCategory());
        e.setTypeProperty(x.getTypeProperty());
        e.setDbTable(x.getDbTable());
        e.setDetailFlg(x.getDetailFlg());
        e.setEnableTeam(x.getEnableTeam());
        e.setEnableSocial(x.getEnableSocial());
        e.setEnableConfig(x.getEnableConfig());
        e.setHiddenFlg(x.getHiddenFlg());
        e.setSearchable(x.getSearchable());
        e.setEnableSharing(x.getEnableSharing());
        e.setEnableScriptTrigger(x.getEnableScriptTrigger());
        e.setEnableActivity(x.getEnableActivity());
        e.setEnableHistoryLog(x.getEnableHistoryLog());
        e.setEnableReport(x.getEnableReport());
        e.setEnableRefer(x.getEnableRefer());
        e.setEnableApi(x.getEnableApi());
        e.setEnableFlow(x.getEnableFlow());
        e.setEnablePackage(x.getEnablePackage());
        e.setExtendProperty(x.getExtendProperty());
        e.setDeleteFlg(x.getDeleteFlg());
        return e;
    }

    public static EntityItem toEntityItem(XEntityItem x) {
        if (x == null) return null;
        EntityItem e = new EntityItem();
        e.setEntityApiKey(x.getEntityApiKey());
        e.setApiKey(x.getApiKey());
        e.setLabel(x.getLabel());
        e.setLabelKey(x.getLabelKey());
        e.setNamespace(x.getNamespace());
        e.setItemType(x.getItemType());
        e.setDataType(x.getDataType());
        e.setHelpText(x.getHelpText());
        e.setHelpTextKey(x.getHelpTextKey());
        e.setDescription(x.getDescription());
        e.setDescriptionKey(x.getDescriptionKey());
        e.setCustomItemSeq(x.getCustomItemSeq());
        e.setDefaultValue(x.getDefaultValue());
        e.setRequireFlg(x.getRequireFlg());
        e.setCustomFlg(x.getCustomFlg());
        e.setEnableFlg(x.getEnableFlg());
        e.setCreatable(x.getCreatable());
        e.setUpdatable(x.getUpdatable());
        e.setUniqueKeyFlg(x.getUniqueKeyFlg());
        e.setEnableHistoryLog(x.getEnableHistoryLog());
        e.setEnableConfig(x.getEnableConfig());
        e.setEnablePackage(x.getEnablePackage());
        e.setReadonlyStatus(x.getReadonlyStatus());
        e.setVisibleStatus(x.getVisibleStatus());
        e.setHiddenFlg(x.getHiddenFlg());
        e.setReferEntityApiKey(x.getReferEntityApiKey());
        e.setReferLinkApiKey(x.getReferLinkApiKey());
        e.setDbColumn(x.getDbColumn());
        e.setItemOrder(x.getItemOrder());
        e.setSortFlg(x.getSortFlg());
        e.setColumnName(x.getColumnName());
        e.setEnableDeactive(x.getEnableDeactive());
        e.setCompound(x.getCompound());
        e.setMaskPrefix(x.getMaskPrefix());
        e.setMaskSuffix(x.getMaskSuffix());
        e.setEncrypt(x.getEncrypt());
        e.setIndexOrder(x.getIndexOrder());
        e.setIndexType(x.getIndexType());
        e.setMarkdown(x.getMarkdown());
        e.setMaskSymbolType(x.getMaskSymbolType());
        e.setIncrementStrategy(x.getIncrementStrategy());
        e.setReferItemFilterEnable(x.getReferItemFilterEnable());
        e.setIsComputeMultiCurrencyUnit(x.getIsComputeMultiCurrencyUnit());
        e.setFormat(x.getFormat());
        e.setMaxLength(x.getMaxLength());
        e.setMinLength(x.getMinLength());
        e.setDecimal(x.getDecimal());
        e.setIsCurrency(x.getIsCurrency());
        e.setCurrencyPart(x.getCurrencyPart());
        e.setIsMultiCurrency(x.getIsMultiCurrency());
        e.setCurrencyFlg(x.getCurrencyFlg());
        e.setComputeType(x.getComputeType());
        e.setRealTimeCompute(x.getRealTimeCompute());
        e.setReferGlobal(x.getReferGlobal());
        e.setGlobalPickItem(x.getGlobalPickItem());
        e.setGlobalPickItemApikey(x.getGlobalPickItemApikey());
        e.setIsExternal(x.getIsExternal());
        e.setDateMode(x.getDateMode());
        e.setCascadeDelete(x.getCascadeDelete());
        e.setIsDetail(x.getIsDetail());
        e.setCanBatchCreate(x.getCanBatchCreate());
        e.setIsCopyWithParent(x.getIsCopyWithParent());
        e.setIsMask(x.getIsMask());
        e.setEnableMultiDetail(x.getEnableMultiDetail());
        e.setBatchCreateMode(x.getBatchCreateMode());
        e.setBatchCreateLinkByBusitype(x.getBatchCreateLinkByBusitype());
        e.setJoinItem(x.getJoinItem());
        e.setJoinObject(x.getJoinObject());
        e.setJoinLink(x.getJoinLink());
        e.setLinkLabel(x.getLinkLabel());
        e.setReferEntityApiKeys(x.getReferEntityApiKeys());
        e.setEntityOrData(x.getEntityOrData());
        e.setGroupKey(x.getGroupKey());
        e.setCompoundSub(x.getCompoundSub());
        e.setCompoundApiKey(x.getCompoundApiKey());
        e.setMultiLineText(x.getMultiLineText());
        e.setScanCodeEntryFlg(x.getScanCodeEntryFlg());
        e.setCaseSensitive(x.getCaseSensitive());
        e.setShowRows(x.getShowRows());
        e.setWatermarkFlg(x.getWatermarkFlg());
        e.setWatermarkTimeFlg(x.getWatermarkTimeFlg());
        e.setWatermarkLoginUserFlg(x.getWatermarkLoginUserFlg());
        e.setWatermarkLocationFlg(x.getWatermarkLocationFlg());
        e.setWatermarkJoinField(x.getWatermarkJoinField());
        e.setDeleteFlg(x.getDeleteFlg());
        return e;
    }

    public static EntityLink toEntityLink(XLink x) {
        if (x == null) return null;
        EntityLink e = new EntityLink();
        e.setApiKey(x.getApiKey());
        e.setLabel(x.getLabel());
        e.setLabelKey(x.getLabelKey());
        e.setNamespace(x.getNamespace());
        e.setTypeProperty(x.getTypeProperty());
        e.setLinkType(x.getLinkType());
        e.setParentEntityApiKey(x.getParentEntityApiKey());
        e.setChildEntityApiKey(x.getChildEntityApiKey());
        e.setDetailLink(x.getDetailLink());
        e.setCascadeDelete(x.getCascadeDelete());
        e.setAccessControl(x.getAccessControl());
        e.setEnableFlg(x.getEnableFlg());
        e.setDescription(x.getDescription());
        e.setDescriptionKey(x.getDescriptionKey());
        e.setDeleteFlg(x.getDeleteFlg());
        return e;
    }

    public static CheckRule toCheckRule(XCheckRule x) {
        if (x == null) return null;
        CheckRule e = new CheckRule();
        e.setEntityApiKey(x.getEntityApiKey());
        e.setApiKey(x.getApiKey());
        e.setLabel(x.getLabel());
        e.setLabelKey(x.getLabelKey());
        e.setNamespace(x.getNamespace());
        e.setActiveFlg(x.getActiveFlg());
        e.setDescription(x.getDescription());
        e.setDescriptionKey(x.getDescriptionKey());
        e.setCheckFormula(x.getCheckFormula());
        e.setCheckErrorMsg(x.getCheckErrorMsg());
        e.setCheckErrorMsgKey(x.getCheckErrorMsgKey());
        e.setCheckErrorLocation(x.getCheckErrorLocation());
        e.setCheckErrorItemApiKey(x.getCheckErrorItemApiKey());
        e.setCheckAllItemsFlg(x.getCheckAllItemsFlg());
        e.setCheckErrorWay(x.getCheckErrorWay());
        e.setDeleteFlg(x.getDeleteFlg());
        return e;
    }
}