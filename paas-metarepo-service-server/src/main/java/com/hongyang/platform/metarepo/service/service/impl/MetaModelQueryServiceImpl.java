package com.hongyang.platform.metarepo.service.service.impl;

import com.hongyang.platform.metarepo.core.model.metamodel.XItem;
import com.hongyang.platform.metarepo.core.model.metamodel.XMetaModel;
import com.hongyang.platform.metarepo.core.model.metamodel.XPickOption;
import com.hongyang.platform.metarepo.service.common.converter.MetaRepoConverter;
import com.hongyang.platform.metarepo.service.entity.metamodel.tenant.TenantCheckRule;
import com.hongyang.platform.metarepo.service.entity.metamodel.tenant.TenantEntity;
import com.hongyang.platform.metarepo.service.entity.metamodel.tenant.TenantEntityLink;
import com.hongyang.platform.metarepo.service.entity.metamodel.tenant.TenantItem;
import com.hongyang.platform.metarepo.service.entity.metamodel.tenant.TenantPickOption;
import com.hongyang.platform.metarepo.service.service.ITenantCheckRuleService;
import com.hongyang.platform.metarepo.service.service.ITenantEntityLinkService;
import com.hongyang.platform.metarepo.service.service.ITenantEntityService;
import com.hongyang.platform.metarepo.service.service.ITenantItemService;
import com.hongyang.platform.metarepo.service.service.ITenantPickOptionService;
import com.hongyang.platform.metarepo.service.service.IMetaModelQueryService;
import lombok.RequiredArgsConstructor;
import java.util.Map;
import java.util.HashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
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

    private final ITenantEntityService customEntityService;
    private final ITenantItemService customItemService;
    private final ITenantEntityLinkService customEntityLinkService;
    private final ITenantPickOptionService customPickOptionService;
    private final ITenantCheckRuleService customCheckRuleService;

    @Override
    public XMetaModel getMetaModel(Long tenantId, String objectApiKey) {
        TenantEntity entity = customEntityService.getByApiKey(tenantId, objectApiKey);
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
        List<TenantEntity> entities = customEntityService.listByTenant(tenantId);
        return entities.stream()
                .map(entity -> assembleMetaModel(tenantId, entity))
                .collect(Collectors.toList());
    }

    @Override
    public List<XItem> getFieldMeta(Long tenantId, Long entityId) {
        List<TenantItem> items = customItemService.listByEntityId(tenantId, entityId);
        return MetaRepoConverter.toXItemList(items);
    }

    private XMetaModel assembleMetaModel(Long tenantId, TenantEntity entity) {
        Long entityId = entity.getId();

        List<TenantItem> items = customItemService.listByEntityId(tenantId, entityId);
        List<TenantEntityLink> links = customEntityLinkService.listByEntityId(tenantId, entityId);
        List<TenantCheckRule> checkRules = customCheckRuleService.listByEntityId(tenantId, entityId);

        Map<Long, List<XPickOption>> pickOptionsMap = new HashMap<>();
        for (TenantItem item : items) {
            if (item.getItemType() != null && isPicklistType(item.getItemType())) {
                List<TenantPickOption> options = customPickOptionService.listByItemId(tenantId, item.getId());
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
