package com.hongyang.platform.metarepo.service.service.metadata;

import com.hongyang.framework.dao.entity.BaseMetaTenantEntity;
import com.hongyang.framework.dao.query.PageResult;
import com.hongyang.platform.metarepo.service.common.constants.MetamodelApiKeyEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * 元数据 Service 基类。
 * 通过 MetamodelApiKeyEnum 从 Entity Class 反查 metamodelApiKey，
 * 委托 IMetadataMergeService 执行 Common + Tenant 合并查询。
 */
@Slf4j
public abstract class AbstractMetadataServiceImpl<T extends BaseMetaTenantEntity> {

    @Autowired
    private IMetadataMergeService metadataMergeService;

    private String metamodelApiKey;

    protected String getMetamodelApiKey() {
        if (metamodelApiKey == null) {
            metamodelApiKey = MetamodelApiKeyEnum.getKeyByEntityClass(getEntityClass());
            if (metamodelApiKey == null) {
                throw new IllegalStateException(
                    getEntityClass().getSimpleName() + " 未在 MetamodelApiKeyEnum 中注册");
            }
        }
        return metamodelApiKey;
    }

    @SuppressWarnings("unchecked")
    public Class<T> getEntityClass() {
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];
    }

    protected List<T> listMerged() {
        return metadataMergeService.listMerged(getMetamodelApiKey(), getEntityClass());
    }

    protected T getByApiKeyMerged(String apiKey) {
        return metadataMergeService.getByApiKeyMerged(getMetamodelApiKey(), apiKey, getEntityClass());
    }

    protected PageResult<T> pageMerged(int page, int size) {
        return metadataMergeService.pageMerged(getMetamodelApiKey(), getEntityClass(), page, size);
    }
}
