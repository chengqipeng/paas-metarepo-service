package com.hongyang.platform.metarepo.service.service.metamodel.impl;

import com.hongyang.framework.dao.entity.BaseMetaCommonEntity;
import com.hongyang.framework.dao.query.MetaQueryCondition;
import com.hongyang.framework.dao.service.MetaServiceImpl;
import com.hongyang.platform.metarepo.service.entity.metamodel.TenantMetadata;
import com.hongyang.platform.metarepo.service.service.metamodel.ITenantMetadataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Tenant 级通用元数据 Service 实现（p_tenant_metadata）。
 * 继承 MetaServiceImpl，走元数据缓存链路（按 apiKey + 条件摘要缓存）。
 * TenantInterceptor 自动注入 tenant_id 条件。
 */
@Slf4j
@Service
public class TenantMetadataServiceImpl
        extends MetaServiceImpl<TenantMetadata>
        implements ITenantMetadataService {

    @Override
    public List<TenantMetadata> listByMetamodelApiKey(String metamodelApiKey) {
        MetaQueryCondition<TenantMetadata> condition = MetaQueryCondition.<TenantMetadata>create()
                .eq(TenantMetadata::getMetamodelApiKey, metamodelApiKey);
        return listByCondition(condition);
    }

    @Override
    public TenantMetadata getByMetamodelApiKeyAndApiKey(String metamodelApiKey, String apiKey) {
        MetaQueryCondition<TenantMetadata> condition = MetaQueryCondition.<TenantMetadata>create()
                .eq(TenantMetadata::getMetamodelApiKey, metamodelApiKey)
                .eq(BaseMetaCommonEntity::getApiKey, apiKey);
        return getOneByCondition(condition);
    }

    @Override
    public TenantMetadata createMetadata(TenantMetadata row) {
        return create(row);
    }

    @Override
    public TenantMetadata updateMetadata(TenantMetadata row) {
        return updateByApiKey(row.getApiKey(), row);
    }

    @Override
    public void softDeleteMetadata(TenantMetadata row, Long operatorId) {
        row.setDeleteFlg(1);
        row.setUpdatedAt(System.currentTimeMillis());
        row.setUpdatedBy(operatorId);
        updateByApiKey(row.getApiKey(), row);
    }
}
