package com.hongyang.platform.metarepo.service.service.metamodel.impl;

import com.hongyang.framework.dao.query.MetaQueryCondition;
import com.hongyang.framework.dao.service.MetaServiceImpl;
import com.hongyang.platform.metarepo.service.entity.metamodel.MetaModel;
import com.hongyang.platform.metarepo.service.service.metamodel.IMetaModelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 元模型定义 Service 实现（p_meta_model）
 */
@Slf4j
@Service
public class MetaModelServiceImpl extends MetaServiceImpl<MetaModel> implements IMetaModelService {

    @Override
    public List<MetaModel> listAllEnablePackage() {
        MetaQueryCondition<MetaModel> condition = MetaQueryCondition.<MetaModel>create()
                .eq(MetaModel::getEnablePackage, 1);
        return listByCondition(condition);
    }

    @Override
    public List<MetaModel> listAllEnableApp() {
        MetaQueryCondition<MetaModel> condition = MetaQueryCondition.<MetaModel>create()
                .eq(MetaModel::getEnableApp, 1);
        return listByCondition(condition);
    }
}
