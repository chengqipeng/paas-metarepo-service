package com.hongyang.platform.metarepo.service.service.impl;

import com.alibaba.fastjson2.JSON;
import com.hongyang.framework.dao.service.SimpleBaseServiceImpl;
import com.hongyang.framework.base.exception.BaseKnownException;
import com.hongyang.framework.common.enums.paas.MetaRepoErrorCodeEnum;
import com.hongyang.platform.metarepo.service.entity.CustomEntityLinkEntity;
import com.hongyang.platform.metarepo.service.entity.CustomItemEntity;
import com.hongyang.platform.metarepo.service.service.ICustomEntityLinkService;
import com.hongyang.platform.metarepo.service.service.ICustomItemService;
import com.hongyang.platform.metarepo.service.service.IMetaLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/** itemType 常量：LOOKUP */
@Slf4j
@Service
public class CustomItemServiceImpl
        extends SimpleBaseServiceImpl<CustomItemEntity>
        implements ICustomItemService {

    private static final int ITEM_TYPE_LOOKUP = 19;

    private final ICustomEntityLinkService customEntityLinkService;
    private final IMetaLogService metaLogService;

    public CustomItemServiceImpl(@Lazy ICustomEntityLinkService customEntityLinkService,
                                  IMetaLogService metaLogService) {
        this.customEntityLinkService = customEntityLinkService;
        this.metaLogService = metaLogService;
    }

    @Override
    public List<CustomItemEntity> listByEntityId(Long tenantId, Long entityId) {
        return lambdaQuery()
                .eq(CustomItemEntity::getTenantId, tenantId)
                .eq(CustomItemEntity::getEntityId, entityId)
                .orderByAsc(CustomItemEntity::getItemOrder)
                .list();
    }

    @Override
    public CustomItemEntity getByApiKey(Long tenantId, Long entityId, String apiKey) {
        return lambdaQuery()
                .eq(CustomItemEntity::getTenantId, tenantId)
                .eq(CustomItemEntity::getEntityId, entityId)
                .eq(CustomItemEntity::getApiKey, apiKey)
                .one();
    }

    @Override
    public boolean existsApiKey(Long tenantId, Long entityId, String apiKey) {
        return lambdaQuery()
                .eq(CustomItemEntity::getTenantId, tenantId)
                .eq(CustomItemEntity::getEntityId, entityId)
                .eq(CustomItemEntity::getApiKey, apiKey)
                .exists();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CustomItemEntity createItem(CustomItemEntity item) {
        if (existsApiKey(item.getTenantId(), item.getEntityId(), item.getApiKey())) {
            throw new BaseKnownException(MetaRepoErrorCodeEnum.META_APIKEY_DUPLICATE, item.getApiKey());
        }
        item.setEnableFlg(1);
        if (item.getDeleteFlg() == null) item.setDeleteFlg(0);
        if (item.getRequireFlg() == null) item.setRequireFlg(0);
        if (item.getCustomFlg() == null) item.setCustomFlg(1);
        if (item.getItemOrder() == null) item.setItemOrder(0);
        save(item);

        // LOOKUP 类型自动创建 entity_link
        if (item.getItemType() != null && item.getItemType() == ITEM_TYPE_LOOKUP
                && item.getReferEntityId() != null) {
            CustomEntityLinkEntity link = new CustomEntityLinkEntity();
            link.setTenantId(item.getTenantId());
            link.setName(item.getName() + "_link");
            link.setApiKey(item.getApiKey() + "_link");
            link.setLabel(item.getLabel());
            link.setLinkType(1); // LOOKUP
            link.setParentEntityId(item.getReferEntityId());
            link.setChildEntityId(item.getEntityId());
            link.setCascadeDelete(0);
            link.setAccessControl(0);
            link.setEnableFlg(1);
            link.setDeleteFlg(0);
            customEntityLinkService.save(link);
            item.setReferLinkId(link.getId());
            updateById(item);
        }

        metaLogService.log(item.getTenantId(), item.getId(), item.getEntityId(), null,
                null, JSON.toJSONString(item), 1);
        return item;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CustomItemEntity updateItem(Long itemId, Long tenantId, CustomItemEntity updates) {
        CustomItemEntity item = getByIdAndTenant(itemId, tenantId);
        if (item == null) {
            throw new BaseKnownException(MetaRepoErrorCodeEnum.META_NOT_FOUND, String.valueOf(itemId));
        }
        String oldValue = JSON.toJSONString(item);

        if (updates.getLabel() != null) item.setLabel(updates.getLabel());
        if (updates.getDescription() != null) item.setDescription(updates.getDescription());
        if (updates.getHelpText() != null) item.setHelpText(updates.getHelpText());
        if (updates.getDefaultValue() != null) item.setDefaultValue(updates.getDefaultValue());
        if (updates.getRequireFlg() != null) item.setRequireFlg(updates.getRequireFlg());
        if (updates.getHiddenFlg() != null) item.setHiddenFlg(updates.getHiddenFlg());
        if (updates.getItemOrder() != null) item.setItemOrder(updates.getItemOrder());
        if (updates.getTypeProperty() != null) item.setTypeProperty(updates.getTypeProperty());

        updateById(item);

        metaLogService.log(tenantId, itemId, item.getEntityId(), null,
                oldValue, JSON.toJSONString(item), 2);
        return item;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteItemWithCheck(Long tenantId, Long itemId) {
        CustomItemEntity item = getByIdAndTenant(itemId, tenantId);
        if (item == null) {
            throw new BaseKnownException(MetaRepoErrorCodeEnum.META_NOT_FOUND, String.valueOf(itemId));
        }
        if (item.getCustomFlg() != null && item.getCustomFlg() == 0) {
            throw new BaseKnownException(MetaRepoErrorCodeEnum.META_STANDARD_CANNOT_DELETE, item.getApiKey());
        }

        // 检查是否有关联关系引用此字段
        if (item.getReferLinkId() != null) {
            customEntityLinkService.softDelete(item.getReferLinkId(), tenantId);
        }

        String oldValue = JSON.toJSONString(item);
        removeById(itemId);

        metaLogService.log(tenantId, itemId, item.getEntityId(), null, oldValue, null, 3);
    }
}
