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
        return MetaRepoConverter.toXEntityList(
                metadataMergeReadService.listMerged(MetamodelApiKey.ENTITY, Entity.class));
    }

    @Override
    @GetMapping("/read/entity")
    public XEntity getEntity(@RequestParam("apiKey") String apiKey) {
        return MetaRepoConverter.toXEntity(
                metadataMergeReadService.getByApiKeyMerged(MetamodelApiKey.ENTITY, apiKey, Entity.class));
    }

    @Override
    @GetMapping("/read/items")
    public List<XEntityItem> listItems(@RequestParam("entityApiKey") String entityApiKey) {
        return MetaRepoConverter.toXEntityItemList(
                metadataMergeReadService.listMerged(MetamodelApiKey.ITEM, EntityItem.class).stream()
                        .filter(e -> entityApiKey.equals(e.getEntityApiKey()))
                        .collect(Collectors.toList()));
    }

    @Override
    @GetMapping("/read/pick-options")
    public List<XPickOption> listPickOptions(@RequestParam("itemApiKey") String itemApiKey) {
        return MetaRepoConverter.toXPickOptionList(
                metadataMergeReadService.listMerged(MetamodelApiKey.PICK_OPTION, PickOption.class).stream()
                        .filter(e -> itemApiKey.equals(e.getItemApiKey()))
                        .collect(Collectors.toList()));
    }

    @Override
    @GetMapping("/read/check-rules")
    public List<XCheckRule> listCheckRules(@RequestParam("entityApiKey") String entityApiKey) {
        return MetaRepoConverter.toXCheckRuleList(
                metadataMergeReadService.listMerged(MetamodelApiKey.CHECK_RULE, CheckRule.class).stream()
                        .filter(e -> entityApiKey.equals(e.getEntityApiKey()))
                        .collect(Collectors.toList()));
    }

    @Override
    @GetMapping("/read/entity-links")
    public List<XLink> listEntityLinks(@RequestParam("entityApiKey") String entityApiKey) {
        return MetaRepoConverter.toXLinkList(
                metadataMergeReadService.listMerged(MetamodelApiKey.ENTITY_LINK, EntityLink.class).stream()
                        .filter(e -> entityApiKey.equals(e.getParentEntityApiKey()))
                        .collect(Collectors.toList()));
    }
}
