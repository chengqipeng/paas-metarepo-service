package com.hongyang.platform.metarepo.service.api.metamodel;

import com.hongyang.framework.common.constants.paas.MetamodelApiKey;
import com.hongyang.platform.metarepo.core.api.MetaRepoWriteApi;
import com.hongyang.platform.metarepo.core.model.metamodel.XCheckRule;
import com.hongyang.platform.metarepo.core.model.metamodel.XEntity;
import com.hongyang.platform.metarepo.core.model.metamodel.XEntityItem;
import com.hongyang.platform.metarepo.core.model.metamodel.XLink;
import com.hongyang.platform.metarepo.core.model.request.CreateCheckRuleRequest;
import com.hongyang.platform.metarepo.core.model.request.CreateEntityRequest;
import com.hongyang.platform.metarepo.core.model.request.CreateItemRequest;
import com.hongyang.platform.metarepo.core.model.request.CreateLinkRequest;
import com.hongyang.platform.metarepo.core.model.request.SavePickOptionRequest;
import com.hongyang.platform.metarepo.core.model.request.UpdateCheckRuleRequest;
import com.hongyang.platform.metarepo.core.model.request.UpdateEntityRequest;
import com.hongyang.platform.metarepo.core.model.request.UpdateItemRequest;
import com.hongyang.platform.metarepo.service.common.converter.MetaRepoConverter;
import com.hongyang.platform.metarepo.service.entity.metadata.CheckRule;
import com.hongyang.platform.metarepo.service.entity.metadata.Entity;
import com.hongyang.platform.metarepo.service.entity.metadata.EntityItem;
import com.hongyang.platform.metarepo.service.entity.metadata.EntityLink;
import com.hongyang.platform.metarepo.service.entity.metadata.PickOption;
import com.hongyang.platform.metarepo.service.service.metadata.IMetadataMergeWriteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/metarepo")
@RequiredArgsConstructor
public class MetaRepoWriteApiService implements MetaRepoWriteApi {

    private final IMetadataMergeWriteService metadataMergeWriteService;

    @Override
    @PostMapping("/write/entity")
    public XEntity createEntity(@RequestBody CreateEntityRequest request) {
        Entity entity = MetaRepoConverter.toEntity(request);
        Entity created = metadataMergeWriteService.create(
                MetamodelApiKey.ENTITY, entity, request.getOperatorId());
        return MetaRepoConverter.toXEntity(created);
    }

    @Override
    @PutMapping("/write/entity")
    public XEntity updateEntity(@RequestBody UpdateEntityRequest request) {
        Entity entity = MetaRepoConverter.toEntity(request);
        Entity updated = metadataMergeWriteService.update(
                MetamodelApiKey.ENTITY, entity, request.getOperatorId());
        return MetaRepoConverter.toXEntity(updated);
    }

    @Override
    @DeleteMapping("/write/entity")
    public void deleteEntity(@RequestParam("apiKey") String apiKey) {
        metadataMergeWriteService.delete(
                MetamodelApiKey.ENTITY, apiKey, Entity.class, null);
    }

    @Override
    @PostMapping("/write/item")
    public XEntityItem createItem(@RequestBody CreateItemRequest request) {
        EntityItem entity = MetaRepoConverter.toEntityItem(request);
        EntityItem created = metadataMergeWriteService.create(
                MetamodelApiKey.ITEM, entity, request.getOperatorId());
        return MetaRepoConverter.toXEntityItem(created);
    }

    @Override
    @PutMapping("/write/item")
    public XEntityItem updateItem(@RequestBody UpdateItemRequest request) {
        EntityItem entity = MetaRepoConverter.toEntityItem(request);
        EntityItem updated = metadataMergeWriteService.update(
                MetamodelApiKey.ITEM, entity, request.getOperatorId());
        return MetaRepoConverter.toXEntityItem(updated);
    }

    @Override
    @DeleteMapping("/write/item")
    public void deleteItem(@RequestParam("apiKey") String apiKey) {
        metadataMergeWriteService.delete(
                MetamodelApiKey.ITEM, apiKey, EntityItem.class, null);
    }

    @Override
    @PostMapping("/write/pick-options")
    public void savePickOptions(@RequestBody SavePickOptionRequest request) {
        // TODO: 批量保存选项值逻辑
        throw new UnsupportedOperationException("savePickOptions 待实现");
    }

    @Override
    @PostMapping("/write/entity-link")
    public XLink createEntityLink(@RequestBody CreateLinkRequest request) {
        EntityLink entity = MetaRepoConverter.toEntityLink(request);
        EntityLink created = metadataMergeWriteService.create(
                MetamodelApiKey.ENTITY_LINK, entity, request.getOperatorId());
        return MetaRepoConverter.toXLink(created);
    }

    @Override
    @DeleteMapping("/write/entity-link")
    public void deleteEntityLink(@RequestParam("apiKey") String apiKey) {
        metadataMergeWriteService.delete(
                MetamodelApiKey.ENTITY_LINK, apiKey, EntityLink.class, null);
    }

    @Override
    @PostMapping("/write/check-rule")
    public XCheckRule createCheckRule(@RequestBody CreateCheckRuleRequest request) {
        CheckRule entity = MetaRepoConverter.toCheckRule(request);
        CheckRule created = metadataMergeWriteService.create(
                MetamodelApiKey.CHECK_RULE, entity, request.getOperatorId());
        return MetaRepoConverter.toXCheckRule(created);
    }

    @Override
    @PutMapping("/write/check-rule")
    public XCheckRule updateCheckRule(@RequestBody UpdateCheckRuleRequest request) {
        CheckRule entity = MetaRepoConverter.toCheckRule(request);
        CheckRule updated = metadataMergeWriteService.update(
                MetamodelApiKey.CHECK_RULE, entity, request.getOperatorId());
        return MetaRepoConverter.toXCheckRule(updated);
    }
}
