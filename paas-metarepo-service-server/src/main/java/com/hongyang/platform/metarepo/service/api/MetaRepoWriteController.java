package com.hongyang.platform.metarepo.service.api;

import com.hongyang.platform.metarepo.core.api.MetaRepoWriteApi;
import com.hongyang.platform.metarepo.core.model.Result;
import com.hongyang.platform.metarepo.core.model.dto.*;
import com.hongyang.platform.metarepo.core.model.req.*;
import com.hongyang.platform.metarepo.service.common.converter.MetaRepoConverter;
import com.hongyang.platform.metarepo.service.entity.*;
import com.hongyang.platform.metarepo.service.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * MetaRepo 写接口实现
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class MetaRepoWriteController implements MetaRepoWriteApi {

    private final ICustomEntityService customEntityService;
    private final ICustomItemService customItemService;
    private final ICustomEntityLinkService customEntityLinkService;
    private final IMetaLogService metaLogService;

    @Override
    public Result<EntityDTO> createEntity(CreateEntityReq req) {
        // 校验 apiKey 唯一性
        if (customEntityService.existsApiKey(req.getTenantId(), req.getApiKey())) {
            return Result.fail(409, "apiKey 已存在: " + req.getApiKey());
        }

        CustomEntityEntity entity = new CustomEntityEntity();
        entity.setTenantId(req.getTenantId());
        entity.setNameSpace(req.getNameSpace());
        entity.setName(req.getName());
        entity.setApiKey(req.getApiKey());
        entity.setLabel(req.getLabel());
        entity.setLabelKey(req.getLabelKey());
        entity.setObjectType(req.getObjectType());
        entity.setSvgId(req.getSvgId());
        entity.setSvgColor(req.getSvgColor());
        entity.setDescription(req.getDescription());
        entity.setCustomFlg(req.getCustomFlg());
        entity.setBusinessCategory(req.getBusinessCategory());
        entity.setTypeProperty(req.getTypeProperty());
        entity.setDbTable(req.getDbTable());
        entity.setEnableTeam(req.getEnableTeam());
        entity.setEnableSharing(req.getEnableSharing());
        entity.setEnableHistoryLog(req.getEnableHistoryLog());
        entity.setEnableReport(req.getEnableReport());
        entity.setEnableApi(req.getEnableApi());
        entity.setEnablePackage(req.getEnablePackage());
        entity.setEnableFlg(1);

        customEntityService.save(entity);

        // 记录变更日志
        metaLogService.log(req.getTenantId(), entity.getId(), entity.getId(), null,
                null, entity.getApiKey(), 1);

        return Result.success(MetaRepoConverter.toEntityDTO(entity));
    }

    @Override
    public Result<EntityDTO> updateEntity(Long entityId, CreateEntityReq req) {
        CustomEntityEntity entity = customEntityService.getByIdAndTenant(entityId, req.getTenantId());
        if (entity == null) {
            return Result.fail(404, "对象不存在: " + entityId);
        }

        // 更新可修改字段（apiKey 不可修改）
        if (req.getLabel() != null) entity.setLabel(req.getLabel());
        if (req.getDescription() != null) entity.setDescription(req.getDescription());
        if (req.getSvgId() != null) entity.setSvgId(req.getSvgId());
        if (req.getSvgColor() != null) entity.setSvgColor(req.getSvgColor());
        if (req.getEnableTeam() != null) entity.setEnableTeam(req.getEnableTeam());
        if (req.getEnableSharing() != null) entity.setEnableSharing(req.getEnableSharing());
        if (req.getEnableHistoryLog() != null) entity.setEnableHistoryLog(req.getEnableHistoryLog());
        if (req.getEnableReport() != null) entity.setEnableReport(req.getEnableReport());
        if (req.getEnableApi() != null) entity.setEnableApi(req.getEnableApi());

        customEntityService.updateById(entity);

        metaLogService.log(req.getTenantId(), entityId, entityId, null,
                null, entity.getApiKey(), 2);

        return Result.success(MetaRepoConverter.toEntityDTO(entity));
    }

    @Override
    public Result<Void> deleteEntity(Long tenantId, Long entityId) {
        boolean deleted = customEntityService.softDelete(entityId, tenantId);
        if (!deleted) {
            return Result.fail(404, "对象不存在: " + entityId);
        }
        metaLogService.log(tenantId, entityId, entityId, null, null, null, 3);
        return Result.success(null);
    }

    @Override
    public Result<ItemDTO> createItem(CreateItemReq req) {
        if (customItemService.existsApiKey(req.getTenantId(), req.getEntityId(), req.getApiKey())) {
            return Result.fail(409, "apiKey 已存在: " + req.getApiKey());
        }

        CustomItemEntity item = new CustomItemEntity();
        item.setTenantId(req.getTenantId());
        item.setEntityId(req.getEntityId());
        item.setName(req.getName());
        item.setApiKey(req.getApiKey());
        item.setLabel(req.getLabel());
        item.setLabelKey(req.getLabelKey());
        item.setItemType(req.getItemType());
        item.setDataType(req.getDataType());
        item.setTypeProperty(req.getTypeProperty());
        item.setHelpText(req.getHelpText());
        item.setDescription(req.getDescription());
        item.setDefaultValue(req.getDefaultValue());
        item.setRequireFlg(req.getRequireFlg() != null ? req.getRequireFlg() : 0);
        item.setCustomFlg(req.getCustomFlg() != null ? req.getCustomFlg() : 1);
        item.setEnableFlg(1);
        item.setDeleteFlg(0);
        item.setCreatable(req.getCreatable());
        item.setUpdatable(req.getUpdatable());
        item.setUniqueKeyFlg(req.getUniqueKeyFlg());
        item.setHiddenFlg(req.getHiddenFlg());
        item.setReferEntityId(req.getReferEntityId());
        item.setDbColumn(req.getDbColumn());
        item.setItemOrder(req.getItemOrder() != null ? req.getItemOrder() : 0);

        customItemService.save(item);

        metaLogService.log(req.getTenantId(), item.getId(), req.getEntityId(), null,
                null, item.getApiKey(), 1);

        return Result.success(MetaRepoConverter.toItemDTO(item));
    }

    @Override
    public Result<Void> deleteItem(Long tenantId, Long itemId) {
        boolean deleted = customItemService.softDelete(itemId, tenantId);
        if (!deleted) {
            return Result.fail(404, "字段不存在: " + itemId);
        }
        metaLogService.log(tenantId, itemId, null, null, null, null, 3);
        return Result.success(null);
    }

    @Override
    public Result<LinkDTO> createEntityLink(CreateLinkReq req) {
        CustomEntityLinkEntity link = new CustomEntityLinkEntity();
        link.setTenantId(req.getTenantId());
        link.setName(req.getName());
        link.setApiKey(req.getApiKey());
        link.setLabel(req.getLabel());
        link.setLinkType(req.getLinkType());
        link.setParentEntityId(req.getParentEntityId());
        link.setChildEntityId(req.getChildEntityId());
        link.setCascadeDelete(req.getCascadeDelete() != null ? req.getCascadeDelete() : 0);
        link.setAccessControl(req.getAccessControl() != null ? req.getAccessControl() : 0);
        link.setEnableFlg(1);
        link.setDeleteFlg(0);

        customEntityLinkService.save(link);

        metaLogService.log(req.getTenantId(), link.getId(), null, null,
                null, link.getApiKey(), 1);

        return Result.success(MetaRepoConverter.toLinkDTO(link));
    }

    @Override
    public Result<Void> deleteEntityLink(Long tenantId, Long linkId) {
        boolean deleted = customEntityLinkService.softDelete(linkId, tenantId);
        if (!deleted) {
            return Result.fail(404, "关联关系不存在: " + linkId);
        }
        metaLogService.log(tenantId, linkId, null, null, null, null, 3);
        return Result.success(null);
    }
}
