package com.hongyang.platform.metarepo.service.api.metamodel;

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
import com.hongyang.platform.metarepo.service.entity.metadata.Entity;
import com.hongyang.platform.metarepo.service.entity.metadata.EntityLink;
import com.hongyang.platform.metarepo.service.entity.metadata.EntityItem;
import com.hongyang.platform.metarepo.service.service.metadata.IEntityLinkService;
import com.hongyang.platform.metarepo.service.service.metadata.IEntityService;
import com.hongyang.platform.metarepo.service.service.metadata.IItemService;
import com.hongyang.framework.base.exception.BaseKnownException;
import com.hongyang.framework.common.enums.paas.MetaRepoErrorCodeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * MetaRepo 写接口实现
 */
@Slf4j
@RestController
@RequestMapping("/metarepo")
@RequiredArgsConstructor
public class MetaRepoWriteApiService implements MetaRepoWriteApi {

    private final IEntityService customEntityService;
    private final IItemService customItemService;
    private final IEntityLinkService customEntityLinkService;

    @Override
    @PostMapping("/write/entity")
    public XEntity createEntity(@RequestBody CreateEntityRequest request) {
        if (customEntityService.existsApiKey(request.getApiKey())) {
            throw new BaseKnownException(MetaRepoErrorCodeEnum.META_APIKEY_DUPLICATE, request.getApiKey());
        }
        Entity entity = new Entity();
        entity.setApiKey(request.getApiKey());
        entity.setLabel(request.getLabel());
        entity.setLabelKey(request.getLabelKey());
        entity.setNamespace(request.getNamespace());
        entity.setDescription(request.getDescription());
        entity.setEntityType(request.getEntityType());
        entity.setSvgApiKey(request.getSvgApiKey());
        entity.setSvgColor(request.getSvgColor());
        entity.setCustomFlg(request.getCustomFlg());
        entity.setBusinessCategory(request.getBusinessCategory());
        entity.setTypeProperty(request.getTypeProperty());
        entity.setDbTable(request.getDbTable());
        entity.setEnableFlg(1);
        // TODO: 写操作需要改造为写入 Tenant 快捷表（大宽表结构）
        throw new UnsupportedOperationException("createEntity 待改造为大宽表写入");
    }

    @Override
    @PutMapping("/write/entity")
    public XEntity updateEntity(@RequestBody UpdateEntityRequest request) {
        Entity entity = customEntityService.getByApiKeyMerged(request.getApiKey());
        if (entity == null) {
            throw new BaseKnownException(MetaRepoErrorCodeEnum.META_NOT_FOUND, request.getApiKey());
        }
        if (request.getLabel() != null) entity.setLabel(request.getLabel());
        if (request.getDescription() != null) entity.setDescription(request.getDescription());
        if (request.getSvgApiKey() != null) entity.setSvgApiKey(request.getSvgApiKey());
        if (request.getSvgColor() != null) entity.setSvgColor(request.getSvgColor());
        // TODO: 写操作需要改造为写入 Tenant 快捷表（大宽表结构）
        throw new UnsupportedOperationException("updateEntity 待改造为大宽表写入");
    }

    @Override
    @DeleteMapping("/write/entity")
    public void deleteEntity(@RequestParam("apiKey") String apiKey) {
        Entity entity = customEntityService.getByApiKeyMerged(apiKey);
        if (entity == null) {
            throw new BaseKnownException(MetaRepoErrorCodeEnum.META_NOT_FOUND, apiKey);
        }
        // TODO: 写操作需要改造为写入 Tenant 快捷表（大宽表结构）
        throw new UnsupportedOperationException("deleteEntity 待改造");
    }

    @Override
    @PostMapping("/write/item")
    public XEntityItem createItem(@RequestBody CreateItemRequest request) {
        EntityItem item = new EntityItem();
        item.setEntityApiKey(request.getEntityApiKey());
        item.setApiKey(request.getApiKey());
        item.setLabel(request.getLabel());
        item.setLabelKey(request.getLabelKey());
        item.setItemType(request.getItemType());
        item.setDataType(request.getDataType());
        item.setTypeProperty(request.getTypeProperty());
        item.setHelpText(request.getHelpText());
        item.setDescription(request.getDescription());
        item.setDefaultValue(request.getDefaultValue());
        item.setRequireFlg(request.getRequireFlg() != null ? request.getRequireFlg() : 0);
        item.setCustomFlg(request.getCustomFlg() != null ? request.getCustomFlg() : 1);
        item.setEnableFlg(1);
        item.setDbColumn(request.getDbColumn());
        item.setItemOrder(request.getItemOrder() != null ? request.getItemOrder() : 0);
        // TODO: 写操作需要改造为写入 Tenant 快捷表（大宽表结构）
        throw new UnsupportedOperationException("createItem 待改造为大宽表写入");
    }

    @Override
    @PutMapping("/write/item")
    public XEntityItem updateItem(@RequestBody UpdateItemRequest request) {
        // TODO: 实现字段更新
        throw new UnsupportedOperationException("updateItem 待实现");
    }

    @Override
    @DeleteMapping("/write/item")
    public void deleteItem(@RequestParam("apiKey") String apiKey) {
        // TODO: 写操作需要改造
        throw new UnsupportedOperationException("deleteItem 待改造");
    }

    @Override
    @PostMapping("/write/pick-options")
    public void savePickOptions(@RequestBody SavePickOptionRequest request) {
        // TODO: 实现选项保存
        throw new UnsupportedOperationException("savePickOptions 待实现");
    }

    @Override
    @PostMapping("/write/entity-link")
    public XLink createEntityLink(@RequestBody CreateLinkRequest request) {
        EntityLink link = new EntityLink();
        link.setApiKey(request.getApiKey());
        link.setLabel(request.getLabel());
        link.setLinkType(request.getLinkType());
        link.setParentEntityApiKey(request.getParentEntityApiKey());
        link.setChildEntityApiKey(request.getChildEntityApiKey());
        link.setCascadeDelete(request.getCascadeDelete() != null ? request.getCascadeDelete() : 0);
        link.setAccessControl(request.getAccessControl() != null ? request.getAccessControl() : 0);
        link.setEnableFlg(1);
        // TODO: 写操作需要改造
        throw new UnsupportedOperationException("createEntityLink 待改造");
    }

    @Override
    @DeleteMapping("/write/entity-link")
    public void deleteEntityLink(@RequestParam("apiKey") String apiKey) {
        // TODO: 按 apiKey 查找并删除
        throw new UnsupportedOperationException("deleteEntityLink 待实现");
    }

    @Override
    @PostMapping("/write/check-rule")
    public XCheckRule createCheckRule(@RequestBody CreateCheckRuleRequest request) {
        // TODO: 实现校验规则创建
        throw new UnsupportedOperationException("createCheckRule 待实现");
    }

    @Override
    @PutMapping("/write/check-rule")
    public XCheckRule updateCheckRule(@RequestBody UpdateCheckRuleRequest request) {
        // TODO: 实现校验规则更新
        throw new UnsupportedOperationException("updateCheckRule 待实现");
    }
}