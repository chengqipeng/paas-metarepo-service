package com.hongyang.platform.metarepo.service.service.metamodel.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hongyang.framework.dao.service.MetaServiceImpl;
import com.hongyang.platform.metarepo.service.entity.metamodel.MetaModel;
import com.hongyang.platform.metarepo.service.service.metamodel.IMetaModelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 元模型定义 Service 实现（p_meta_model）
 * <p>
 * 对标老系统 MetaModelServiceImpl，使用 framework-dao 的 MetaServiceImpl 替代 sns-dal Dao。
 * 老系统通过 MetaModelCacheProvides 做本地缓存（按 id / apiKey 双索引），
 * 新系统由 MetaServiceImpl 内置的 DaoCacheManager 统一管理对象级缓存和两阶段查询。
 * <p>
 * IMetaService 已提供：getByApiKey / listAll / existsByApiKey / create / updateByApiKey / deleteByApiKey
 * 本类仅扩展老系统特有的过滤查询。
 */
@Slf4j
@Service
public class MetaModelServiceImpl extends MetaServiceImpl<MetaModel> implements IMetaModelService {

    @Override
    public List<MetaModel> listAllEnablePackage() {
        return baseMapper.selectList(
                Wrappers.<MetaModel>lambdaQuery()
                        .eq(MetaModel::getEnablePackage, 1));
    }

    @Override
    public List<MetaModel> listAllEnableApp() {
        return baseMapper.selectList(
                Wrappers.<MetaModel>lambdaQuery()
                        .eq(MetaModel::getEnableApp, 1));
    }
}
