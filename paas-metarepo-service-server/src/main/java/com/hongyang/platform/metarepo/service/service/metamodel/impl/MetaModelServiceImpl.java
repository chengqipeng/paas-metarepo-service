package com.hongyang.platform.metarepo.service.service.metamodel.impl;

import com.hongyang.framework.dao.service.SimpleBaseServiceImpl;
import com.hongyang.platform.metarepo.service.entity.metamodel.MetaModel;
import com.hongyang.platform.metarepo.service.service.metamodel.IMetaModelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 元模型定义 Service 实现（p_meta_model）
 * <p>
 * 对标老系统 MetaModelServiceImpl，使用 framework-dao 的 SimpleBaseServiceImpl 替代 sns-dal Dao。
 * 老系统通过 MetaModelCacheProvides 做本地缓存，新系统由 SimpleBaseServiceImpl 内置的
 * DaoCacheManager 统一管理对象级缓存。
 */
@Slf4j
@Service
public class MetaModelServiceImpl extends SimpleBaseServiceImpl<MetaModel> implements IMetaModelService {

    @Override
    public MetaModel getByApiKey(String apiKey) {
        return lambdaQuery()
                .eq(MetaModel::getApiKey, apiKey)
                .one();
    }

    @Override
    public List<MetaModel> listAll() {
        return lambdaQuery().list();
    }

    @Override
    public List<MetaModel> listAllEnablePackage() {
        return lambdaQuery()
                .eq(MetaModel::getEnablePackage, 1)
                .list();
    }

    @Override
    public List<MetaModel> listAllEnableApp() {
        return lambdaQuery()
                .eq(MetaModel::getEnableApp, 1)
                .list();
    }

    @Override
    public boolean existsByApiKey(String apiKey) {
        return lambdaQuery()
                .eq(MetaModel::getApiKey, apiKey)
                .exists();
    }
}
