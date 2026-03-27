package com.hongyang.platform.metarepo.service.service.metamodel.impl;

import com.hongyang.framework.dao.entity.BaseMetaCommonEntity;
import com.hongyang.framework.dao.service.DataBaseServiceImpl;
import com.hongyang.platform.metarepo.service.entity.metamodel.TenantMetadata;
import com.hongyang.platform.metarepo.service.service.metamodel.ITenantMetadataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Tenant 级通用元数据 Service 实现（p_tenant_metadata）。
 * 继承 DataBaseServiceImpl，TenantInterceptor 自动注入 tenant_id 条件。
 */
@Slf4j
@Service
public class TenantMetadataServiceImpl
        extends DataBaseServiceImpl<TenantMetadata>
        implements ITenantMetadataService {

    @Override
    public List<TenantMetadata> listByMetamodelApiKey(String metamodelApiKey) {
        return lambdaQuery()
                .eq(TenantMetadata::getMetamodelApiKey, metamodelApiKey)
                .list();
    }

    @Override
    public TenantMetadata getByMetamodelApiKeyAndApiKey(String metamodelApiKey, String apiKey) {
        return lambdaQuery()
                .eq(TenantMetadata::getMetamodelApiKey, metamodelApiKey)
                .eq(BaseMetaCommonEntity::getApiKey, apiKey)
                .one();
    }
}
