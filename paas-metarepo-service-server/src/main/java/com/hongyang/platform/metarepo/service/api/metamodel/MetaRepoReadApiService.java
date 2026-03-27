package com.hongyang.platform.metarepo.service.api.metamodel;

import com.hongyang.platform.metarepo.core.api.MetaRepoReadApi;
import com.hongyang.platform.metarepo.core.model.metamodel.XCheckRule;
import com.hongyang.platform.metarepo.core.model.metamodel.XEntity;
import com.hongyang.platform.metarepo.core.model.metamodel.XEntityItem;
import com.hongyang.platform.metarepo.core.model.metamodel.XLink;
import com.hongyang.platform.metarepo.core.model.metamodel.XPickOption;
import com.hongyang.platform.metarepo.service.common.converter.MetaRepoConverter;
import com.hongyang.platform.metarepo.service.service.metadata.ICheckRuleService;
import com.hongyang.platform.metarepo.service.service.metadata.IEntityLinkService;
import com.hongyang.platform.metarepo.service.service.metadata.IEntityService;
import com.hongyang.platform.metarepo.service.service.metadata.IItemService;
import com.hongyang.platform.metarepo.service.service.metadata.IPickOptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/metarepo")
@RequiredArgsConstructor
public class MetaRepoReadApiService implements MetaRepoReadApi {

    private final IEntityService customEntityService;
    private final IItemService customItemService;
    private final IPickOptionService customPickOptionService;
    private final ICheckRuleService customCheckRuleService;
    private final IEntityLinkService customEntityLinkService;

    @Override
    @GetMapping("/read/entities")
    public List<XEntity> listEntities() {
        return MetaRepoConverter.toXEntityList(customEntityService.listMerged());
    }

    @Override
    @GetMapping("/read/entity")
    public XEntity getEntity(@RequestParam("apiKey") String apiKey) {
        return MetaRepoConverter.toXEntity(customEntityService.getByApiKeyMerged(apiKey));
    }

    @Override
    @GetMapping("/read/items")
    public List<XEntityItem> listItems(@RequestParam("entityApiKey") String entityApiKey) {
        return MetaRepoConverter.toXEntityItemList(customItemService.listMerged(entityApiKey));
    }

    @Override
    @GetMapping("/read/pick-options")
    public List<XPickOption> listPickOptions(@RequestParam("itemApiKey") String itemApiKey) {
        return MetaRepoConverter.toXPickOptionList(customPickOptionService.listMerged(itemApiKey));
    }

    @Override
    @GetMapping("/read/check-rules")
    public List<XCheckRule> listCheckRules(@RequestParam("entityApiKey") String entityApiKey) {
        return MetaRepoConverter.toXCheckRuleList(customCheckRuleService.listMerged(entityApiKey));
    }

    @Override
    @GetMapping("/read/entity-links")
    public List<XLink> listEntityLinks(@RequestParam("entityApiKey") String entityApiKey) {
        return MetaRepoConverter.toXLinkList(customEntityLinkService.listMerged(entityApiKey));
    }
}