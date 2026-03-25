package com.hongyang.platform.metarepo.service.service.impl;

import com.hongyang.platform.metarepo.core.model.dto.*;
import com.hongyang.platform.metarepo.service.common.converter.MetaRepoConverter;
import com.hongyang.platform.metarepo.service.entity.*;
import com.hongyang.platform.metarepo.service.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 元模型组装与查询实现
 * 将 entity/item/link/pickOption/checkRule 组装为完整的 MetaModelDTO
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MetaModelQueryServiceImpl implements IMetaModelQueryService {

    private final ICustomEntityService customEntityService;
    private final ICustomItemService customItemService;
    private final ICustomEntityLinkService customEntityLinkService;
    private final ICustomPickOptionService customPickOptionService;
    private final ICustomCheckRuleService customCheckRuleService;

    @Override
    public MetaModelDTO getMetaModel(Long tenantId, String objectApiKey) {
        CustomEntityEntity entity = customEntityService.getByApiKey(tenantId, objectApiKey);
        if (entity == null) {
            return null;
        }
        return assembleMetaModel(tenantId, entity);
    }

    @Override
    public List<MetaModelDTO> batchGetMetaModel(Long tenantId, List<String> apiKeys) {
        if (apiKeys == null || apiKeys.isEmpty()) {
            return Collections.emptyList();
        }
        return apiKeys.stream()
                .map(apiKey -> getMetaModel(tenantId, apiKey))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public List<MetaModelDTO> listMetaModels(Long tenantId) {
        List<CustomEntityEntity> entities = customEntityService.listByTenant(tenantId);
        return entities.stream()
                .map(entity -> assembleMetaModel(tenantId, entity))
                .collect(Collectors.toList());
    }

    /**
     * 组装完整元模型
     */
    private MetaModelDTO assembleMetaModel(Long tenantId, CustomEntityEntity entity) {
        Long entityId = entity.getId();

        // 查询字段列表
        List<CustomItemEntity> items = customItemService.listByEntityId(tenantId, entityId);

        // 查询关联关系
        List<CustomEntityLinkEntity> links = customEntityLinkService.listByEntityId(tenantId, entityId);

        // 查询校验规则
        List<CustomCheckRuleEntity> checkRules = customCheckRuleService.listByObjectId(tenantId, entityId);

        // 查询选项值（按 itemId 分组）
        Map<Long, List<PickOptionDTO>> pickOptionsMap = new HashMap<>();
        for (CustomItemEntity item : items) {
            // 仅 PICKLIST 类型字段查询选项值
            if (item.getItemType() != null && isPicklistType(item.getItemType())) {
                List<CustomPickOptionEntity> options = customPickOptionService.listByItemId(tenantId, item.getId());
                if (!options.isEmpty()) {
                    pickOptionsMap.put(item.getId(), MetaRepoConverter.toPickOptionDTOList(options));
                }
            }
        }

        // 组装 DTO
        MetaModelDTO dto = new MetaModelDTO();
        dto.setEntityId(entityId);
        dto.setApiKey(entity.getApiKey());
        dto.setLabel(entity.getLabel());
        dto.setLabelKey(entity.getLabelKey());
        dto.setObjectType(entity.getObjectType());
        dto.setDbTable(entity.getDbTable());
        dto.setCustomFlg(entity.getCustomFlg());
        dto.setEnableFlg(entity.getEnableFlg());
        dto.setEnableSharing(entity.getEnableSharing());
        dto.setTypeProperty(entity.getTypeProperty());
        dto.setDescription(entity.getDescription());
        dto.setItems(MetaRepoConverter.toItemDTOList(items));
        dto.setLinks(MetaRepoConverter.toLinkDTOList(links));
        dto.setCheckRules(MetaRepoConverter.toCheckRuleDTOList(checkRules));
        dto.setPickOptionsMap(pickOptionsMap);

        return dto;
    }

    /** 判断是否为 PICKLIST 类型（itemType=6 或 7 为单选/多选） */
    private boolean isPicklistType(Integer itemType) {
        return itemType == 6 || itemType == 7;
    }
}
