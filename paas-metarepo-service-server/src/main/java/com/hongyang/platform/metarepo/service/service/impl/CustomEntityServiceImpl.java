package com.hongyang.platform.metarepo.service.service.impl;

import com.alibaba.fastjson2.JSON;
import com.hongyang.framework.dao.service.SimpleBaseServiceImpl;
import com.hongyang.framework.base.exception.BaseKnownException;
import com.hongyang.framework.common.enums.paas.MetaRepoErrorCodeEnum;
import com.hongyang.platform.metarepo.service.entity.CustomEntityEntity;
import com.hongyang.platform.metarepo.service.entity.CustomEntityLinkEntity;
import com.hongyang.platform.metarepo.service.entity.CustomItemEntity;
import com.hongyang.platform.metarepo.service.entity.MetaMetamodelDataEntity;
import com.hongyang.platform.metarepo.service.service.ICustomEntityLinkService;
import com.hongyang.platform.metarepo.service.service.ICustomEntityService;
import com.hongyang.platform.metarepo.service.service.ICustomItemService;
import com.hongyang.platform.metarepo.service.service.IMetaLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class CustomEntityServiceImpl
        extends SimpleBaseServiceImpl<CustomEntityEntity>
        implements ICustomEntityService {

    private final ICustomItemService customItemService;
    private final ICustomEntityLinkService customEntityLinkService;
    private final IMetaLogService metaLogService;
    private final MetaMetamodelDataServiceHelper metamodelDataHelper;

    public CustomEntityServiceImpl(@Lazy ICustomItemService customItemService,
                                    @Lazy ICustomEntityLinkService customEntityLinkService,
                                    IMetaLogService metaLogService,
                                    MetaMetamodelDataServiceHelper metamodelDataHelper) {
        this.customItemService = customItemService;
        this.customEntityLinkService = customEntityLinkService;
        this.metaLogService = metaLogService;
        this.metamodelDataHelper = metamodelDataHelper;
    }

    @Override
    public CustomEntityEntity getByApiKey(Long tenantId, String apiKey) {
        return lambdaQuery()
                .eq(CustomEntityEntity::getTenantId, tenantId)
                .eq(CustomEntityEntity::getApiKey, apiKey)
                .one();
    }

    @Override
    public List<CustomEntityEntity> listByTenant(Long tenantId) {
        return lambdaQuery()
                .eq(CustomEntityEntity::getTenantId, tenantId)
                .eq(CustomEntityEntity::getEnableFlg, 1)
                .list();
    }

    @Override
    public boolean existsApiKey(Long tenantId, String apiKey) {
        return lambdaQuery()
                .eq(CustomEntityEntity::getTenantId, tenantId)
                .eq(CustomEntityEntity::getApiKey, apiKey)
                .exists();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CustomEntityEntity createEntity(CustomEntityEntity entity) {
        if (existsApiKey(entity.getTenantId(), entity.getApiKey())) {
            throw new BaseKnownException(MetaRepoErrorCodeEnum.META_APIKEY_DUPLICATE, entity.getApiKey());
        }
        entity.setEnableFlg(1);
        save(entity);

        // 同步写大宽表 p_meta_metamodel_data
        metamodelDataHelper.syncEntityToMetamodelData(entity);

        metaLogService.log(entity.getTenantId(), entity.getId(), entity.getId(), null,
                null, JSON.toJSONString(entity), 1);
        return entity;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CustomEntityEntity updateEntity(Long entityId, Long tenantId, CustomEntityEntity updates) {
        CustomEntityEntity entity = getByIdAndTenant(entityId, tenantId);
        if (entity == null) {
            throw new BaseKnownException(MetaRepoErrorCodeEnum.META_NOT_FOUND, String.valueOf(entityId));
        }
        String oldValue = JSON.toJSONString(entity);

        if (updates.getLabel() != null) entity.setLabel(updates.getLabel());
        if (updates.getDescription() != null) entity.setDescription(updates.getDescription());
        if (updates.getSvgId() != null) entity.setSvgId(updates.getSvgId());
        if (updates.getSvgColor() != null) entity.setSvgColor(updates.getSvgColor());
        if (updates.getEnableTeam() != null) entity.setEnableTeam(updates.getEnableTeam());
        if (updates.getEnableSharing() != null) entity.setEnableSharing(updates.getEnableSharing());
        if (updates.getEnableHistoryLog() != null) entity.setEnableHistoryLog(updates.getEnableHistoryLog());
        if (updates.getEnableReport() != null) entity.setEnableReport(updates.getEnableReport());
        if (updates.getEnableApi() != null) entity.setEnableApi(updates.getEnableApi());

        updateById(entity);

        metaLogService.log(tenantId, entityId, entityId, null,
                oldValue, JSON.toJSONString(entity), 2);
        return entity;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteEntityCascade(Long tenantId, Long entityId) {
        CustomEntityEntity entity = getByIdAndTenant(entityId, tenantId);
        if (entity == null) {
            throw new BaseKnownException(MetaRepoErrorCodeEnum.META_NOT_FOUND, String.valueOf(entityId));
        }
        // 标准元数据不可删除
        if (entity.getCustomFlg() != null && entity.getCustomFlg() == 0) {
            throw new BaseKnownException(MetaRepoErrorCodeEnum.META_STANDARD_CANNOT_DELETE, entity.getApiKey());
        }

        String oldValue = JSON.toJSONString(entity);

        // 级联软删除字段
        List<CustomItemEntity> items = customItemService.listByEntityId(tenantId, entityId);
        for (CustomItemEntity item : items) {
            customItemService.softDelete(item.getId(), tenantId);
        }

        // 级联软删除关联关系
        List<CustomEntityLinkEntity> links = customEntityLinkService.listByEntityId(tenantId, entityId);
        for (CustomEntityLinkEntity link : links) {
            customEntityLinkService.softDelete(link.getId(), tenantId);
        }

        // 软删除对象本身
        removeById(entityId);

        metaLogService.log(tenantId, entityId, entityId, null, oldValue, null, 3);
    }
}
