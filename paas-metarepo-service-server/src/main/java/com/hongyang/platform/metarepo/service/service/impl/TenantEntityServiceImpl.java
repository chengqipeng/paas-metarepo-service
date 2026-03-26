package com.hongyang.platform.metarepo.service.service.impl;

import com.alibaba.fastjson2.JSON;
import com.hongyang.framework.dao.split.CommonTenantServiceImpl;
import com.hongyang.framework.base.exception.BaseKnownException;
import com.hongyang.framework.common.enums.paas.MetaRepoErrorCodeEnum;
import com.hongyang.platform.metarepo.service.entity.metamodel.tenant.TenantEntity;
import com.hongyang.platform.metarepo.service.entity.metamodel.tenant.TenantEntityLink;
import com.hongyang.platform.metarepo.service.entity.metamodel.tenant.TenantItem;
import com.hongyang.platform.metarepo.service.service.ITenantEntityLinkService;
import com.hongyang.platform.metarepo.service.service.ITenantEntityService;
import com.hongyang.platform.metarepo.service.service.ITenantItemService;
import com.hongyang.platform.metarepo.service.service.IMetaLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class TenantEntityServiceImpl
        extends CommonTenantServiceImpl<TenantEntity>
        implements ITenantEntityService {

    private final ITenantItemService customItemService;
    private final ITenantEntityLinkService customEntityLinkService;
    private final IMetaLogService metaLogService;
    private final MetaMetamodelDataServiceHelper metamodelDataHelper;

    public TenantEntityServiceImpl(@Lazy ITenantItemService customItemService,
                                    @Lazy ITenantEntityLinkService customEntityLinkService,
                                    IMetaLogService metaLogService,
                                    MetaMetamodelDataServiceHelper metamodelDataHelper) {
        this.customItemService = customItemService;
        this.customEntityLinkService = customEntityLinkService;
        this.metaLogService = metaLogService;
        this.metamodelDataHelper = metamodelDataHelper;
    }

    @Override
    public TenantEntity getByApiKey(Long tenantId, String apiKey) {
        return getByApiKeyMerged(tenantId, apiKey, TenantEntity::getApiKey);
    }

    @Override
    public List<TenantEntity> listByTenant(Long tenantId) {
        return listMerged(tenantId, TenantEntity::getApiKey).stream()
                .filter(e -> e.getEnableFlg() != null && e.getEnableFlg() == 1)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public boolean existsApiKey(Long tenantId, String apiKey) {
        return lambdaQuery()
                .eq(TenantEntity::getTenantId, tenantId)
                .eq(TenantEntity::getApiKey, apiKey)
                .exists();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TenantEntity createEntity(TenantEntity entity) {
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
    public TenantEntity updateEntity(Long entityId, Long tenantId, TenantEntity updates) {
        TenantEntity entity = getByIdAndTenant(entityId, tenantId);
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

        // 同步更新大宽表
        metamodelDataHelper.syncEntityUpdateToMetamodelData(entity);

        metaLogService.log(tenantId, entityId, entityId, null,
                oldValue, JSON.toJSONString(entity), 2);
        return entity;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteEntityCascade(Long tenantId, Long entityId) {
        TenantEntity entity = getByIdAndTenant(entityId, tenantId);
        if (entity == null) {
            throw new BaseKnownException(MetaRepoErrorCodeEnum.META_NOT_FOUND, String.valueOf(entityId));
        }
        // 标准元数据不可删除
        if (entity.getCustomFlg() != null && entity.getCustomFlg() == 0) {
            throw new BaseKnownException(MetaRepoErrorCodeEnum.META_STANDARD_CANNOT_DELETE, entity.getApiKey());
        }

        String oldValue = JSON.toJSONString(entity);

        // 级联软删除字段
        List<TenantItem> items = customItemService.listByEntityId(tenantId, entityId);
        for (TenantItem item : items) {
            customItemService.softDelete(item.getId(), tenantId);
        }

        // 级联软删除关联关系
        List<TenantEntityLink> links = customEntityLinkService.listByEntityId(tenantId, entityId);
        for (TenantEntityLink link : links) {
            customEntityLinkService.softDelete(link.getId(), tenantId);
        }

        // 软删除对象本身
        removeById(entityId);

        // 同步软删除大宽表记录
        metamodelDataHelper.syncEntityDeleteToMetamodelData(entity);

        metaLogService.log(tenantId, entityId, entityId, null, oldValue, null, 3);
    }
}
