package com.hongyang.platform.metarepo.service.common.converter;

import com.hongyang.platform.metarepo.core.model.metamodel.XCheckRule;
import com.hongyang.platform.metarepo.core.model.metamodel.XEntity;
import com.hongyang.platform.metarepo.core.model.metamodel.XItem;
import com.hongyang.platform.metarepo.core.model.metamodel.XLink;
import com.hongyang.platform.metarepo.core.model.metamodel.XPickOption;
import com.hongyang.platform.metarepo.service.entity.metadata.tenant.TenantCheckRule;
import com.hongyang.platform.metarepo.service.entity.metadata.tenant.TenantEntity;
import com.hongyang.platform.metarepo.service.entity.metadata.tenant.TenantEntityLink;
import com.hongyang.platform.metarepo.service.entity.metadata.tenant.TenantItem;
import com.hongyang.platform.metarepo.service.entity.metadata.tenant.TenantPickOption;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Entity → X 元模型转换工具
 */
public final class MetaRepoConverter {

    private MetaRepoConverter() {}

    // ==================== Entity ====================

    public static XEntity toXEntity(TenantEntity e) {
        if (e == null) return null;
        XEntity x = new XEntity();
        x.setId(e.getId());
        x.setTenantId(e.getTenantId());
        x.setNameSpace(e.getNameSpace());
        x.setEntityId(e.getObjectId());
        x.setName(e.getName());
        x.setNameKey(e.getNameKey());
        x.setApiKey(e.getApiKey());
        x.setLabel(e.getLabel());
        x.setLabelKey(e.getLabelKey());
        x.setObjectType(e.getObjectType());
        x.setSvgId(e.getSvgId());
        x.setSvgColor(e.getSvgColor());
        x.setDescription(e.getDescription());
        x.setDescriptionKey(e.getDescriptionKey());
        x.setTenantEntityseq(e.getTenantEntityseq());
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

    public static XItem toXItem(TenantItem e) {
        if (e == null) return null;
        XItem x = new XItem();
        x.setId(e.getId());
        x.setTenantId(e.getTenantId());
        x.setEntityId(e.getEntityId());
        x.setName(e.getName());
        x.setNameKey(e.getNameKey());
        x.setApiKey(e.getApiKey());
        x.setLabel(e.getLabel());
        x.setLabelKey(e.getLabelKey());
        x.setItemType(e.getItemType());
        x.setDataType(e.getDataType());
        x.setTypeProperty(e.getTypeProperty());
        x.setHelpText(e.getHelpText());
        x.setHelpTextKey(e.getHelpTextKey());
        x.setDescription(e.getDescription());
        x.setDescriptionKey(e.getDescriptionKey());
        x.setTenantItemseq(e.getTenantItemseq());
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

    public static List<XItem> toXItemList(List<TenantItem> list) {
        if (list == null) return Collections.emptyList();
        return list.stream().map(MetaRepoConverter::toXItem).collect(Collectors.toList());
    }

    // ==================== Link ====================

    public static XLink toXLink(TenantEntityLink e) {
        if (e == null) return null;
        XLink x = new XLink();
        x.setId(e.getId());
        x.setTenantId(e.getTenantId());
        x.setName(e.getName());
        x.setNameKey(e.getNameKey());
        x.setApiKey(e.getApiKey());
        x.setLabel(e.getLabel());
        x.setLabelKey(e.getLabelKey());
        x.setLinkType(e.getLinkType());
        x.setParentEntityId(e.getParentEntityId());
        x.setChildEntityId(e.getChildEntityId());
        x.setCascadeDelete(e.getCascadeDelete());
        x.setAccessControl(e.getAccessControl());
        x.setEnableFlg(e.getEnableFlg());
        x.setDescription(e.getDescription());
        x.setDescriptionKey(e.getDescriptionKey());
        x.setCreatedAt(e.getCreatedAt());
        x.setCreatedBy(e.getCreatedBy());
        return x;
    }

    public static List<XLink> toXLinkList(List<TenantEntityLink> list) {
        if (list == null) return Collections.emptyList();
        return list.stream().map(MetaRepoConverter::toXLink).collect(Collectors.toList());
    }

    // ==================== PickOption ====================

    public static XPickOption toXPickOption(TenantPickOption e) {
        if (e == null) return null;
        XPickOption x = new XPickOption();
        x.setId(e.getId());
        x.setTenantId(e.getTenantId());
        x.setEntityId(e.getEntityId());
        x.setItemId(e.getItemId());
        x.setApiKey(e.getApiKey());
        x.setOptionCode(e.getOptionCode());
        x.setLabel(e.getLabel());
        x.setLabelKey(e.getLabelKey());
        x.setOptionOrder(e.getOptionOrder());
        x.setDefaultFlg(e.getDefaultFlg());
        x.setGlobalFlg(e.getGlobalFlg());
        x.setCustomFlg(e.getCustomFlg());
        x.setEnableFlg(e.getEnableFlg());
        x.setDescription(e.getDescription());
        x.setDescriptionKey(e.getDescriptionKey());
        return x;
    }

    public static List<XPickOption> toXPickOptionList(List<TenantPickOption> list) {
        if (list == null) return Collections.emptyList();
        return list.stream().map(MetaRepoConverter::toXPickOption).collect(Collectors.toList());
    }

    /** XPickOption -> TenantPickOption（用于 savePickOptions） */
    public static TenantPickOption fromXPickOption(XPickOption x) {
        if (x == null) return null;
        TenantPickOption e = new TenantPickOption();
        e.setId(x.getId());
        e.setTenantId(x.getTenantId());
        e.setEntityId(x.getEntityId());
        e.setItemId(x.getItemId());
        e.setApiKey(x.getApiKey());
        e.setOptionCode(x.getOptionCode());
        e.setLabel(x.getLabel());
        e.setLabelKey(x.getLabelKey());
        e.setOptionOrder(x.getOptionOrder());
        e.setDefaultFlg(x.getDefaultFlg());
        e.setGlobalFlg(x.getGlobalFlg());
        e.setCustomFlg(x.getCustomFlg());
        e.setEnableFlg(x.getEnableFlg());
        return e;
    }

    public static List<TenantPickOption> fromXPickOptionList(List<XPickOption> list) {
        if (list == null) return Collections.emptyList();
        return list.stream().map(MetaRepoConverter::fromXPickOption).collect(Collectors.toList());
    }

    // ==================== CheckRule ====================

    public static XCheckRule toXCheckRule(TenantCheckRule e) {
        if (e == null) return null;
        XCheckRule x = new XCheckRule();
        x.setId(e.getId());
        x.setTenantId(e.getTenantId());
        x.setEntityId(e.getObjectId());
        x.setName(e.getName());
        x.setNameKey(e.getNameKey());
        x.setApiKey(e.getApiKey());
        x.setLabel(e.getLabel());
        x.setLabelKey(e.getLabelKey());
        x.setActiveFlg(e.getActiveFlg());
        x.setDescription(e.getDescription());
        x.setDescriptionKey(e.getDescriptionKey());
        x.setCheckFormula(e.getCheckFormula());
        x.setCheckErrorMsg(e.getCheckErrorMsg());
        x.setCheckErrorMsgKey(e.getCheckErrorMsgKey());
        x.setCheckErrorLocation(e.getCheckErrorLocation());
        x.setCheckErrorItemId(e.getCheckErrorItemId());
        x.setCheckAllItemsFlg(e.getCheckAllItemsFlg());
        x.setCheckErrorWay(e.getCheckErrorWay());
        x.setCreatedAt(e.getCreatedAt());
        x.setCreatedBy(e.getCreatedBy());
        x.setUpdatedAt(e.getUpdatedAt());
        x.setUpdatedBy(e.getUpdatedBy());
        return x;
    }

    public static List<XCheckRule> toXCheckRuleList(List<TenantCheckRule> list) {
        if (list == null) return Collections.emptyList();
        return list.stream().map(MetaRepoConverter::toXCheckRule).collect(Collectors.toList());
    }

}
