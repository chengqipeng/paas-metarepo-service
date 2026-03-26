package com.hongyang.platform.metarepo.service.service.impl;

import com.hongyang.platform.metarepo.core.model.metamodel.XCheckRule;
import com.hongyang.platform.metarepo.core.model.metamodel.XItem;
import com.hongyang.platform.metarepo.core.model.metamodel.XLink;
import com.hongyang.platform.metarepo.core.model.metamodel.XMetaModel;
import com.hongyang.platform.metarepo.core.model.metamodel.XPickOption;
import com.hongyang.platform.metarepo.service.common.converter.MetaRepoConverter;
import com.hongyang.platform.metarepo.service.entity.CustomCheckRule;
import com.hongyang.platform.metarepo.service.entity.CustomEntity;
import com.hongyang.platform.metarepo.service.entity.CustomEntityLink;
import com.hongyang.platform.metarepo.service.entity.CustomItem;
import com.hongyang.platform.metarepo.service.entity.CustomPickOption;
import com.hongyang.platform.metarepo.service.service.ICustomCheckRuleService;
import com.hongyang.platform.metarepo.service.service.ICustomEntityLinkService;
import com.hongyang.platform.metarepo.service.service.ICustomEntityService;
import com.hongyang.platform.metarepo.service.service.ICustomItemService;
import com.hongyang.platform.metarepo.service.service.ICustomPickOptionService;
import com.hongyang.platform.metarepo.service.service.IMetaModelQueryService;
import lombok.RequiredArgsConstructor;
import java.util.Map;
import java.util.HashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 元模型组装与查询实现
 * 将 entity/item/link/pickOption/checkRule 组装为完整的 XMetaModel
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
    public XMetaModel getMetaModel(Long tenantId, String objectApiKey) {
        CustomEntity entity = customEntityService.getByApiKey(tenantId, objectApiKey);
        if (entity == null) {
            return null;
        }
        return assembleMetaModel(tenantId, entity);
    }

    @Override
    public List<XMetaModel> batchGetMetaModel(Long tenantId, List<String> apiKeys) {
        if (apiKeys == null || apiKeys.isEmpty()) {
            return Collections.emptyList();
        }
        return apiKeys.stream()
                .map(apiKey -> getMetaModel(tenantId, apiKey))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public List<XMetaModel> listMetaModels(Long tenantId) {
        List<CustomEntity> entities = customEntityService.listByTenant(tenantId);
        return entities.stream()
                .map(entity -> assembleMetaModel(tenantId, entity))
                .collect(Collectors.toList());
    }

    @Override
    public List<XItem> getFieldMeta(Long tenantId, Long entityId) {
        List<CustomItem> items = customItemService.listByEntityId(tenantId, entityId);
        return MetaRepoConverter.toXItemList(items);
    }

    private XMetaModel assembleMetaModel(Long tenantId, CustomEntity entity) {
        Long entityId = entity.getId();

        List<CustomItem> items = customItemService.listByEntityId(tenantId, entityId);
        List<CustomEntityLink> links = customEntityLinkService.listByEntityId(tenantId, entityId);
        List<CustomCheckRule> checkRules = customCheckRuleService.listByEntityId(tenantId, entityId);

        Map<Long, List<XPickOption>> pickOptionsMap = new HashMap<>();
        for (CustomItem item : items) {
            if (item.getItemType() != null && isPicklistType(item.getItemType())) {
                List<CustomPickOption> options = customPickOptionService.listByItemId(tenantId, item.getId());
                if (!options.isEmpty()) {
                    pickOptionsMap.put(item.getId(), MetaRepoConverter.toXPickOptionList(options));
                }
            }
        }

        XMetaModel model = new XMetaModel();
        model.setEntityId(entityId);
        model.setApiKey(entity.getApiKey());
        model.setLabel(entity.getLabel());
        model.setLabelKey(entity.getLabelKey());
        model.setObjectType(entity.getObjectType());
        model.setDbTable(entity.getDbTable());
        model.setCustomFlg(entity.getCustomFlg());
        model.setEnableFlg(entity.getEnableFlg());
        model.setEnableSharing(entity.getEnableSharing());
        model.setTypeProperty(entity.getTypeProperty());
        model.setDescription(entity.getDescription());
        model.setItems(MetaRepoConverter.toXItemList(items));
        model.setLinks(MetaRepoConverter.toXLinkList(links));
        model.setCheckRules(MetaRepoConverter.toXCheckRuleList(checkRules));
        model.setPickOptionsMap(pickOptionsMap);

        return model;
    }

    private boolean isPicklistType(Integer itemType) {
        return itemType == 6 || itemType == 7;
    }
}
