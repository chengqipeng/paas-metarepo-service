package com.hongyang.platform.metarepo.service.service.metamodel.impl;

import com.hongyang.framework.dao.service.SimpleBaseServiceImpl;
import com.hongyang.platform.metarepo.service.entity.metamodel.MetaOption;
import com.hongyang.platform.metarepo.service.service.metamodel.IMetaOptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 元模型选项值 Service 实现（p_meta_option）
 * <p>
 * 对标老系统 MetaOptionServiceImpl，使用 framework-dao 的 SimpleBaseServiceImpl 替代 sns-dal Dao。
 * 老系统通过 MetaOptionCacheProvides 按 itemId 缓存选项列表，
 * 新系统由 SimpleBaseServiceImpl 内置的 DaoCacheManager 统一管理。
 */
@Slf4j
@Service
public class MetaOptionServiceImpl extends SimpleBaseServiceImpl<MetaOption> implements IMetaOptionService {

    @Override
    public List<MetaOption> listByItemApiKey(String metamodelApiKey, String itemApiKey) {
        return lambdaQuery()
                .eq(MetaOption::getMetamodelApiKey, metamodelApiKey)
                .eq(MetaOption::getItemApiKey, itemApiKey)
                .orderByAsc(MetaOption::getOptionOrder)
                .list();
    }

    @Override
    public boolean deleteByItemApiKey(String metamodelApiKey, String itemApiKey) {
        return lambdaUpdate()
                .eq(MetaOption::getMetamodelApiKey, metamodelApiKey)
                .eq(MetaOption::getItemApiKey, itemApiKey)
                .remove();
    }

    @Override
    public boolean isValidCode(String metamodelApiKey, String itemApiKey, Integer optionCode) {
        return lambdaQuery()
                .eq(MetaOption::getMetamodelApiKey, metamodelApiKey)
                .eq(MetaOption::getItemApiKey, itemApiKey)
                .eq(MetaOption::getOptionCode, optionCode)
                .eq(MetaOption::getEnableFlg, 1)
                .exists();
    }
}
