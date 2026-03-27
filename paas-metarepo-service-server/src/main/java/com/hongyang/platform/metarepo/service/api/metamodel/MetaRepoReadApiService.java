package com.hongyang.platform.metarepo.service.api.metamodel;

import com.hongyang.framework.common.constants.paas.MetamodelApiKey;
import com.hongyang.platform.metarepo.core.api.MetaRepoReadApi;
import com.hongyang.platform.metarepo.core.model.metamodel.XCheckRule;
import com.hongyang.platform.metarepo.core.model.metamodel.XEntity;
import com.hongyang.platform.metarepo.core.model.metamodel.XEntityItem;
import com.hongyang.platform.metarepo.core.model.metamodel.XLink;
import com.hongyang.platform.metarepo.core.model.metamodel.XPickOption;
import com.hongyang.platform.metarepo.service.common.converter.MetaRepoConverter;
import com.hongyang.platform.metarepo.service.entity.metadata.CheckRule;
import com.hongyang.platform.metarepo.service.entity.metadata.Entity;
import com.hongyang.platform.metarepo.service.entity.metadata.EntityItem;
import com.hongyang.platform.metarepo.service.entity.metadata.EntityLink;
import com.hongyang.platform.metarepo.service.entity.metadata.PickOption;
import com.hongyang.platform.metarepo.service.service.metadata.IMetadataMergeReadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/metarepo")
@RequiredArgsConstructor
public class MetaRepoReadApiService implements MetaRepoReadApi {

    private final IMetadataMergeReadService metadataMergeReadService;

    @Override
    @GetMapping("/read/entities")
    public List<XEntity> listEntities() {
        List<Entity> entities = metadataMergeReadService.listMerged(MetamodelApiKey.ENTITY);
        return MetaRepoConverter.toXEntityList(entities);
    }

    @Override
    @GetMapping("/read/entity")
    public XEntity getEntity(@RequestParam("apiKey") String apiKey) {
        Entity entity = metadataMergeReadService.getByApiKeyMerged(MetamodelApiKey.ENTITY, apiKey);
        return MetaRepoConverter.toXEntity(entity);
    }

    @Override
    @GetMapping("/read/items")
    public List<XEntityItem> listItems(@RequestParam("entityApiKey") String entityApiKey) {
        List<EntityItem> items = metadataMergeReadService.listMergedByEntityApiKey(MetamodelApiKey.ITEM, entityApiKey);
        return MetaRepoConverter.toXEntityItemList(items);
    }

    @Override
    @GetMapping("/read/pick-options")
    public List<XPickOption> listPickOptions(@RequestParam("itemApiKey") String itemApiKey) {
        // pickOption 没有直接的 entityApiKey 过滤，仍用全量 + 内存过滤
        List<PickOption> allOptions = metadataMergeReadService.listMerged(MetamodelApiKey.PICK_OPTION);
        return MetaRepoConverter.toXPickOptionList(
                allOptions.stream()
                        .filter(e -> itemApiKey.equals(e.getItemApiKey()))
                        .collect(Collectors.toList()));
    }

    @Override
    @GetMapping("/read/check-rules")
    public List<XCheckRule> listCheckRules(@RequestParam("entityApiKey") String entityApiKey) {
        List<CheckRule> rules = metadataMergeReadService.listMergedByEntityApiKey(MetamodelApiKey.CHECK_RULE, entityApiKey);
        return MetaRepoConverter.toXCheckRuleList(rules);
    }

    @Override
    @GetMapping("/read/entity-links")
    public List<XLink> listEntityLinks(@RequestParam("entityApiKey") String entityApiKey) {
        // entityLink 的 entity_api_key 存的是 parentEntityApiKey
        List<EntityLink> links = metadataMergeReadService.listMergedByEntityApiKey(MetamodelApiKey.ENTITY_LINK, entityApiKey);
        return MetaRepoConverter.toXLinkList(links);
    }
}
