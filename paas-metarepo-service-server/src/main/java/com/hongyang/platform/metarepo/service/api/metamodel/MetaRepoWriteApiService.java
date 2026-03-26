package com.hongyang.platform.metarepo.service.api.metamodel;

import com.hongyang.platform.metarepo.core.api.MetaRepoWriteApi;
import com.hongyang.platform.metarepo.core.model.metamodel.XCheckRule;
import com.hongyang.platform.metarepo.core.model.metamodel.XEntity;
import com.hongyang.platform.metarepo.core.model.metamodel.XItem;
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
import com.hongyang.platform.metarepo.service.entity.metadata.tenant.TenantEntity;
import com.hongyang.platform.metarepo.service.entity.metadata.tenant.TenantEntityLink;
import com.hongyang.platform.metarepo.service.entity.metadata.tenant.TenantItem;
import com.hongyang.platform.metarepo.service.service.metadata.ITenantEntityLinkService;
import com.hongyang.platform.metarepo.service.service.metadata.ITenantEntityService;
import com.hongyang.platform.metarepo.service.service.metadata.ITenantItemService;
import com.hongyang.platform.metarepo.service.service.IMetaLogService;
import com.hongyang.framework.base.exception.BaseKnownException;
import com.hongyang.framework.common.enums.paas.MetaRepoErrorCodeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    private final ITenantEntityService customEntityService;
    private final ITenantItemService customItemService;
    private final ITenantEntityLinkService customEntityLinkService;
    private final IMetaLogService metaLogService;

    @Override
    @PostMapping("/write/entity")
    public XEntity createEntity(@RequestBody CreateEntityRequest request) {
        if (customEntityService.existsApiKey(request.getTenantId(), request.getApiKey())) {
            throw new BaseKnownException(MetaRepoErrorCodeEnum.META_APIKEY_DUPLICATE, request.getApiKey());
        }
        TenantEntity entity = new TenantEntity();
        entity.setTenantId(request.getTenantId());
        entity.setNameSpace(request.getNameSpace());
        entity.setName(request.getName());
        entity.setApiKey(request.getApiKey());
        entity.setLabel(request.getLabel());
        entity.setLabelKey(request.getLabelKey());
        entity.setObjectType(request.getObjectType());
        entity.setSvgId(request.getSvgId());
        entity.setSvgColor(request.getSvgColor());
        entity.setDescription(request.getDescription());
        entity.setCustomFlg(request.getCustomFlg());
        entity.setBusinessCategory(request.getBusinessCategory());
        entity.setTypeProperty(request.getTypeProperty());
        entity.setDbTable(request.getDbTable());
        entity.setEnableTeam(request.getEnableTeam());
        entity.setEnableSharing(request.getEnableSharing());
        entity.setEnableHistoryLog(request.getEnableHistoryLog());
        entity.setEnableReport(request.getEnableReport());
        entity.setEnableApi(request.getEnableApi());
        entity.setEnablePackage(request.getEnablePackage());
        entity.setEnableFlg(1);
        customEntityService.save(entity);
        metaLogService.log(request.getTenantId(), entity.getId(), entity.getId(), null,
                null, entity.getApiKey(), 1);
        return MetaRepoConverter.toXEntity(entity);
    }

    @Override
    @PutMapping("/write/entity/{entityId}")
    public XEntity updateEntity(@PathVariable("entityId") Long entityId,
                                 @RequestBody UpdateEntityRequest request) {
        TenantEntity entity = customEntityService.getByIdAndTenant(entityId, request.getTenantId());
        if (entity == null) {
            throw new BaseKnownException(MetaRepoErrorCodeEnum.META_NOT_FOUND, entityId);
        }
        if (request.getLabel() != null) entity.setLabel(request.getLabel());
        if (request.getDescription() != null) entity.setDescription(request.getDescription());
        if (request.getSvgId() != null) entity.setSvgId(request.getSvgId());
        if (request.getSvgColor() != null) entity.setSvgColor(request.getSvgColor());
        if (request.getEnableTeam() != null) entity.setEnableTeam(request.getEnableTeam());
        if (request.getEnableSharing() != null) entity.setEnableSharing(request.getEnableSharing());
        if (request.getEnableHistoryLog() != null) entity.setEnableHistoryLog(request.getEnableHistoryLog());
        if (request.getEnableReport() != null) entity.setEnableReport(request.getEnableReport());
        if (request.getEnableApi() != null) entity.setEnableApi(request.getEnableApi());
        customEntityService.updateById(entity);
        metaLogService.log(request.getTenantId(), entityId, entityId, null,
                null, entity.getApiKey(), 2);
        return MetaRepoConverter.toXEntity(entity);
    }

    @Override
    @DeleteMapping("/write/entity")
    public void deleteEntity(@RequestParam("tenantId") Long tenantId,
                              @RequestParam("entityId") Long entityId) {
        boolean deleted = customEntityService.softDelete(entityId, tenantId);
        if (!deleted) {
            throw new BaseKnownException(MetaRepoErrorCodeEnum.META_NOT_FOUND, entityId);
        }
        metaLogService.log(tenantId, entityId, entityId, null, null, null, 3);
    }

    @Override
    @PostMapping("/write/item")
    public XItem createItem(@RequestBody CreateItemRequest request) {
        if (customItemService.existsApiKey(request.getTenantId(), request.getEntityId(), request.getApiKey())) {
            throw new BaseKnownException(MetaRepoErrorCodeEnum.META_APIKEY_DUPLICATE, request.getApiKey());
        }
        TenantItem item = new TenantItem();
        item.setTenantId(request.getTenantId());
        item.setEntityId(request.getEntityId());
        item.setName(request.getName());
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
        item.setDeleteFlg(0);
        item.setCreatable(request.getCreatable());
        item.setUpdatable(request.getUpdatable());
        item.setUniqueKeyFlg(request.getUniqueKeyFlg());
        item.setHiddenFlg(request.getHiddenFlg());
        item.setReferEntityId(request.getReferEntityId());
        item.setDbColumn(request.getDbColumn());
        item.setItemOrder(request.getItemOrder() != null ? request.getItemOrder() : 0);
        customItemService.save(item);
        metaLogService.log(request.getTenantId(), item.getId(), request.getEntityId(), null,
                null, item.getApiKey(), 1);
        return MetaRepoConverter.toXItem(item);
    }

    @Override
    @PutMapping("/write/item/{itemId}")
    public XItem updateItem(@PathVariable("itemId") Long itemId,
                             @RequestBody UpdateItemRequest request) {
        // TODO: 实现字段更新
        throw new UnsupportedOperationException("updateItem 待实现");
    }

    @Override
    @DeleteMapping("/write/item")
    public void deleteItem(@RequestParam("tenantId") Long tenantId,
                            @RequestParam("itemId") Long itemId) {
        boolean deleted = customItemService.softDelete(itemId, tenantId);
        if (!deleted) {
            throw new BaseKnownException(MetaRepoErrorCodeEnum.META_NOT_FOUND, itemId);
        }
        metaLogService.log(tenantId, itemId, null, null, null, null, 3);
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
        TenantEntityLink link = new TenantEntityLink();
        link.setTenantId(request.getTenantId());
        link.setName(request.getName());
        link.setApiKey(request.getApiKey());
        link.setLabel(request.getLabel());
        link.setLinkType(request.getLinkType());
        link.setParentEntityId(request.getParentEntityId());
        link.setChildEntityId(request.getChildEntityId());
        link.setCascadeDelete(request.getCascadeDelete() != null ? request.getCascadeDelete() : 0);
        link.setAccessControl(request.getAccessControl() != null ? request.getAccessControl() : 0);
        link.setEnableFlg(1);
        link.setDeleteFlg(0);
        customEntityLinkService.save(link);
        metaLogService.log(request.getTenantId(), link.getId(), null, null,
                null, link.getApiKey(), 1);
        return MetaRepoConverter.toXLink(link);
    }

    @Override
    @DeleteMapping("/write/entity-link")
    public void deleteEntityLink(@RequestParam("tenantId") Long tenantId,
                                  @RequestParam("linkId") Long linkId) {
        boolean deleted = customEntityLinkService.softDelete(linkId, tenantId);
        if (!deleted) {
            throw new BaseKnownException(MetaRepoErrorCodeEnum.META_NOT_FOUND, linkId);
        }
        metaLogService.log(tenantId, linkId, null, null, null, null, 3);
    }

    @Override
    @PostMapping("/write/check-rule")
    public XCheckRule createCheckRule(@RequestBody CreateCheckRuleRequest request) {
        // TODO: 实现校验规则创建
        throw new UnsupportedOperationException("createCheckRule 待实现");
    }

    @Override
    @PutMapping("/write/check-rule/{ruleId}")
    public XCheckRule updateCheckRule(@PathVariable("ruleId") Long ruleId,
                                      @RequestBody UpdateCheckRuleRequest request) {
        // TODO: 实现校验规则更新
        throw new UnsupportedOperationException("updateCheckRule 待实现");
    }
}
