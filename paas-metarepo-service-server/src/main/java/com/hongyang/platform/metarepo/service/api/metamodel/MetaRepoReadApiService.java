package com.hongyang.platform.metarepo.service.api.metamodel;

import com.hongyang.platform.metarepo.core.api.MetaRepoReadApi;
import com.hongyang.platform.metarepo.core.model.metamodel.XCheckRule;
import com.hongyang.platform.metarepo.core.model.metamodel.XItem;
import com.hongyang.platform.metarepo.core.model.metamodel.XLink;
import com.hongyang.platform.metarepo.core.model.metamodel.XMetaModel;
import com.hongyang.platform.metarepo.core.model.metamodel.XPickOption;
import com.hongyang.platform.metarepo.service.common.converter.MetaRepoConverter;
import com.hongyang.platform.metarepo.service.service.ICustomCheckRuleService;
import com.hongyang.platform.metarepo.service.service.ICustomEntityLinkService;
import com.hongyang.platform.metarepo.service.service.ICustomItemService;
import com.hongyang.platform.metarepo.service.service.ICustomPickOptionService;
import com.hongyang.platform.metarepo.service.service.IMetaModelQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/metarepo")
@RequiredArgsConstructor
public class MetaRepoReadApiService implements MetaRepoReadApi {

    private final IMetaModelQueryService metaModelQueryService;
    private final ICustomItemService customItemService;
    private final ICustomPickOptionService customPickOptionService;
    private final ICustomCheckRuleService customCheckRuleService;
    private final ICustomEntityLinkService customEntityLinkService;

    @Override
    @GetMapping("/read/metamodel")
    public XMetaModel getMetaModel(@RequestParam("tenantId") Long tenantId,
                                    @RequestParam("objectApiKey") String objectApiKey) {
        return metaModelQueryService.getMetaModel(tenantId, objectApiKey);
    }

    @Override
    @PostMapping("/read/metamodel/batch")
    public List<XMetaModel> batchGetMetaModel(@RequestParam("tenantId") Long tenantId,
                                               @RequestBody List<String> apiKeys) {
        return metaModelQueryService.batchGetMetaModel(tenantId, apiKeys);
    }

    @Override
    @GetMapping("/read/metamodel/list")
    public List<XMetaModel> listMetaModels(@RequestParam("tenantId") Long tenantId) {
        return metaModelQueryService.listMetaModels(tenantId);
    }

    @Override
    @GetMapping("/read/items")
    public List<XItem> listItems(@RequestParam("tenantId") Long tenantId,
                                  @RequestParam("entityId") Long entityId) {
        return MetaRepoConverter.toXItemList(customItemService.listByEntityId(tenantId, entityId));
    }

    @Override
    @GetMapping("/read/field-meta")
    public List<XItem> getFieldMeta(@RequestParam("tenantId") Long tenantId,
                                     @RequestParam("entityId") Long entityId) {
        return metaModelQueryService.getFieldMeta(tenantId, entityId);
    }

    @Override
    @GetMapping("/read/pick-options")
    public List<XPickOption> listPickOptions(@RequestParam("tenantId") Long tenantId,
                                              @RequestParam("itemId") Long itemId) {
        return MetaRepoConverter.toXPickOptionList(customPickOptionService.listByItemId(tenantId, itemId));
    }

    @Override
    @GetMapping("/read/check-rules")
    public List<XCheckRule> listCheckRules(@RequestParam("tenantId") Long tenantId,
                                            @RequestParam("entityId") Long entityId) {
        return MetaRepoConverter.toXCheckRuleList(customCheckRuleService.listByEntityId(tenantId, entityId));
    }

    @Override
    @GetMapping("/read/entity-links")
    public List<XLink> listEntityLinks(@RequestParam("tenantId") Long tenantId,
                                        @RequestParam("entityId") Long entityId) {
        return MetaRepoConverter.toXLinkList(customEntityLinkService.listByEntityId(tenantId, entityId));
    }
}
