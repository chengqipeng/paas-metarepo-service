package com.hongyang.platform.metarepo.service.api.metamodel;

import com.hongyang.platform.metarepo.core.api.MetaRepoReadApi;
import com.hongyang.platform.metarepo.core.model.metamodel.*;
import com.hongyang.platform.metarepo.service.common.converter.MetaRepoConverter;
import com.hongyang.platform.metarepo.service.entity.*;
import com.hongyang.platform.metarepo.service.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * MetaRepo 读接口实现
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class MetaRepoReadApiService implements MetaRepoReadApi {

    private final IMetaModelQueryService metaModelQueryService;
    private final ICustomItemService customItemService;
    private final ICustomPickOptionService customPickOptionService;
    private final ICustomCheckRuleService customCheckRuleService;
    private final ICustomEntityLinkService customEntityLinkService;

    @Override
    public XMetaModel getMetaModel(Long tenantId, String objectApiKey) {
        return metaModelQueryService.getMetaModel(tenantId, objectApiKey);
    }

    @Override
    public List<XMetaModel> batchGetMetaModel(Long tenantId, List<String> apiKeys) {
        return metaModelQueryService.batchGetMetaModel(tenantId, apiKeys);
    }

    @Override
    public List<XMetaModel> listMetaModels(Long tenantId) {
        return metaModelQueryService.listMetaModels(tenantId);
    }

    @Override
    public List<XItem> listItems(Long tenantId, Long entityId) {
        List<CustomItemEntity> items = customItemService.listByEntityId(tenantId, entityId);
        return MetaRepoConverter.toXItemList(items);
    }

    @Override
    public List<XPickOption> listPickOptions(Long tenantId, Long itemId) {
        List<CustomPickOptionEntity> options = customPickOptionService.listByItemId(tenantId, itemId);
        return MetaRepoConverter.toXPickOptionList(options);
    }

    @Override
    public List<XCheckRule> listCheckRules(Long tenantId, Long entityId) {
        List<CustomCheckRuleEntity> rules = customCheckRuleService.listByObjectId(tenantId, entityId);
        return MetaRepoConverter.toXCheckRuleList(rules);
    }

    @Override
    public List<XLink> listEntityLinks(Long tenantId, Long entityId) {
        List<CustomEntityLinkEntity> links = customEntityLinkService.listByEntityId(tenantId, entityId);
        return MetaRepoConverter.toXLinkList(links);
    }
}
