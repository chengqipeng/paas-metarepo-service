package com.hongyang.platform.metarepo.service.service.metamodel.impl;

import com.hongyang.framework.dao.entity.BaseMetaCommonEntity;
import com.hongyang.framework.dao.query.MetaQueryCondition;
import com.hongyang.framework.dao.service.MetaServiceImpl;
import com.hongyang.platform.metarepo.service.entity.metamodel.CommonMetadata;
import com.hongyang.platform.metarepo.service.service.metamodel.ICommonMetadataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Common 级通用元数据 Service 实现（p_common_metadata）。
 * 继承 MetaServiceImpl，走两阶段缓存（Caffeine L1 + Redis L2）。
 */
@Slf4j
@Service
public class CommonMetadataServiceImpl
        extends MetaServiceImpl<CommonMetadata>
        implements ICommonMetadataService {

    @Override
    public List<CommonMetadata> listByMetamodelApiKey(String metamodelApiKey) {
        MetaQueryCondition<CommonMetadata> condition = MetaQueryCondition.<CommonMetadata>create()
                .eq(CommonMetadata::getMetamodelApiKey, metamodelApiKey);
        return listByCondition(condition);
    }

    @Override
    public CommonMetadata getByMetamodelApiKeyAndApiKey(String metamodelApiKey, String apiKey) {
        MetaQueryCondition<CommonMetadata> condition = MetaQueryCondition.<CommonMetadata>create()
                .eq(CommonMetadata::getMetamodelApiKey, metamodelApiKey)
                .eq(BaseMetaCommonEntity::getApiKey, apiKey);
        return getOneByCondition(condition);
    }
}
