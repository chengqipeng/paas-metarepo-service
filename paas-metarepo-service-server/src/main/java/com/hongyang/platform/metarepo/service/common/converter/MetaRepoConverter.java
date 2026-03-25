package com.hongyang.platform.metarepo.service.common.converter;

import com.hongyang.platform.metarepo.core.model.dto.*;
import com.hongyang.platform.metarepo.service.entity.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Entity → DTO 转换工具
 */
public final class MetaRepoConverter {

    private MetaRepoConverter() {}

    // ==================== Entity ====================

    public static EntityDTO toEntityDTO(CustomEntityEntity e) {
        if (e == null) return null;
        EntityDTO dto = new EntityDTO();
        dto.setId(e.getId());
        dto.setTenantId(e.getTenantId());
        dto.setNameSpace(e.getNameSpace());
        dto.setObjectId(e.getObjectId());
        dto.setName(e.getName());
        dto.setApiKey(e.getApiKey());
        dto.setLabel(e.getLabel());
        dto.setLabelKey(e.getLabelKey());
        dto.setObjectType(e.getObjectType());
        dto.setSvgId(e.getSvgId());
        dto.setSvgColor(e.getSvgColor());
        dto.setDescription(e.getDescription());
        dto.setCustomEntityseq(e.getCustomEntityseq());
        dto.setDeleteFlg(e.getDeleteFlg());
        dto.setEnableFlg(e.getEnableFlg());
        dto.setCustomFlg(e.getCustomFlg());
        dto.setBusinessCategory(e.getBusinessCategory());
        dto.setTypeProperty(e.getTypeProperty());
        dto.setDbTable(e.getDbTable());
        dto.setDetailFlg(e.getDetailFlg());
        dto.setEnableTeam(e.getEnableTeam());
        dto.setEnableSocial(e.getEnableSocial());
        dto.setEnableConfig(e.getEnableConfig());
        dto.setHiddenFlg(e.getHiddenFlg());
        dto.setSearchable(e.getSearchable());
        dto.setEnableSharing(e.getEnableSharing());
        dto.setEnableScriptTrigger(e.getEnableScriptTrigger());
        dto.setEnableActivity(e.getEnableActivity());
        dto.setEnableHistoryLog(e.getEnableHistoryLog());
        dto.setEnableReport(e.getEnableReport());
        dto.setEnableRefer(e.getEnableRefer());
        dto.setEnableApi(e.getEnableApi());
        dto.setEnableFlow(e.getEnableFlow());
        dto.setEnablePackage(e.getEnablePackage());
        dto.setExtendProperty(e.getExtendProperty());
        dto.setCreatedAt(e.getCreatedAt());
        dto.setCreatedBy(e.getCreatedBy());
        dto.setUpdatedAt(e.getUpdatedAt());
        dto.setUpdatedBy(e.getUpdatedBy());
        return dto;
    }

    // ==================== Item ====================

    public static ItemDTO toItemDTO(CustomItemEntity e) {
        if (e == null) return null;
        ItemDTO dto = new ItemDTO();
        dto.setId(e.getId());
        dto.setTenantId(e.getTenantId());
        dto.setEntityId(e.getEntityId());
        dto.setName(e.getName());
        dto.setApiKey(e.getApiKey());
        dto.setLabel(e.getLabel());
        dto.setLabelKey(e.getLabelKey());
        dto.setItemType(e.getItemType());
        dto.setDataType(e.getDataType());
        dto.setTypeProperty(e.getTypeProperty());
        dto.setHelpText(e.getHelpText());
        dto.setHelpTextKey(e.getHelpTextKey());
        dto.setDescription(e.getDescription());
        dto.setCustomItemseq(e.getCustomItemseq());
        dto.setDefaultValue(e.getDefaultValue());
        dto.setRequireFlg(e.getRequireFlg());
        dto.setDeleteFlg(e.getDeleteFlg());
        dto.setCustomFlg(e.getCustomFlg());
        dto.setEnableFlg(e.getEnableFlg());
        dto.setCreatable(e.getCreatable());
        dto.setUpdatable(e.getUpdatable());
        dto.setUniqueKeyFlg(e.getUniqueKeyFlg());
        dto.setEnableHistoryLog(e.getEnableHistoryLog());
        dto.setEnableConfig(e.getEnableConfig());
        dto.setEnablePackage(e.getEnablePackage());
        dto.setReadonlyStatus(e.getReadonlyStatus());
        dto.setVisibleStatus(e.getVisibleStatus());
        dto.setHiddenFlg(e.getHiddenFlg());
        dto.setReferEntityId(e.getReferEntityId());
        dto.setReferLinkId(e.getReferLinkId());
        dto.setDbColumn(e.getDbColumn());
        dto.setItemOrder(e.getItemOrder());
        dto.setSortFlg(e.getSortFlg());
        dto.setColumnName(e.getColumnName());
        dto.setCreatedAt(e.getCreatedAt());
        dto.setCreatedBy(e.getCreatedBy());
        dto.setUpdatedAt(e.getUpdatedAt());
        dto.setUpdatedBy(e.getUpdatedBy());
        return dto;
    }

    public static List<ItemDTO> toItemDTOList(List<CustomItemEntity> list) {
        if (list == null) return Collections.emptyList();
        return list.stream().map(MetaRepoConverter::toItemDTO).collect(Collectors.toList());
    }

    // ==================== Link ====================

    public static LinkDTO toLinkDTO(CustomEntityLinkEntity e) {
        if (e == null) return null;
        LinkDTO dto = new LinkDTO();
        dto.setId(e.getId());
        dto.setTenantId(e.getTenantId());
        dto.setName(e.getName());
        dto.setApiKey(e.getApiKey());
        dto.setLabel(e.getLabel());
        dto.setLinkType(e.getLinkType());
        dto.setParentEntityId(e.getParentEntityId());
        dto.setChildEntityId(e.getChildEntityId());
        dto.setCascadeDelete(e.getCascadeDelete());
        dto.setAccessControl(e.getAccessControl());
        dto.setEnableFlg(e.getEnableFlg());
        dto.setDescription(e.getDescription());
        dto.setCreatedAt(e.getCreatedAt());
        dto.setCreatedBy(e.getCreatedBy());
        return dto;
    }

    public static List<LinkDTO> toLinkDTOList(List<CustomEntityLinkEntity> list) {
        if (list == null) return Collections.emptyList();
        return list.stream().map(MetaRepoConverter::toLinkDTO).collect(Collectors.toList());
    }

    // ==================== PickOption ====================

    public static PickOptionDTO toPickOptionDTO(CustomPickOptionEntity e) {
        if (e == null) return null;
        PickOptionDTO dto = new PickOptionDTO();
        dto.setId(e.getId());
        dto.setTenantId(e.getTenantId());
        dto.setEntityId(e.getEntityId());
        dto.setItemId(e.getItemId());
        dto.setApiKey(e.getApiKey());
        dto.setOptionCode(e.getOptionCode());
        dto.setOptionLabel(e.getOptionLabel());
        dto.setOptionOrder(e.getOptionOrder());
        dto.setDefaultFlg(e.getDefaultFlg());
        dto.setGlobalFlg(e.getGlobalFlg());
        dto.setEnableFlg(e.getEnableFlg());
        return dto;
    }

    public static List<PickOptionDTO> toPickOptionDTOList(List<CustomPickOptionEntity> list) {
        if (list == null) return Collections.emptyList();
        return list.stream().map(MetaRepoConverter::toPickOptionDTO).collect(Collectors.toList());
    }

    // ==================== CheckRule ====================

    public static CheckRuleDTO toCheckRuleDTO(CustomCheckRuleEntity e) {
        if (e == null) return null;
        CheckRuleDTO dto = new CheckRuleDTO();
        dto.setId(e.getId());
        dto.setTenantId(e.getTenantId());
        dto.setObjectId(e.getObjectId());
        dto.setName(e.getName());
        dto.setApiKey(e.getApiKey());
        dto.setActiveFlg(e.getActiveFlg());
        dto.setCheckFormula(e.getCheckFormula());
        dto.setCheckErrorMsg(e.getCheckErrorMsg());
        dto.setCheckErrorLocation(e.getCheckErrorLocation());
        dto.setCheckErrorItemId(e.getCheckErrorItemId());
        dto.setCreatedAt(e.getCreatedAt());
        dto.setCreatedBy(e.getCreatedBy());
        return dto;
    }

    public static List<CheckRuleDTO> toCheckRuleDTOList(List<CustomCheckRuleEntity> list) {
        if (list == null) return Collections.emptyList();
        return list.stream().map(MetaRepoConverter::toCheckRuleDTO).collect(Collectors.toList());
    }
}
