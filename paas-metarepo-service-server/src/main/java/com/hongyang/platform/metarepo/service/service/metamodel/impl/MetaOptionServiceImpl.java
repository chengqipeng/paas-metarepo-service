package com.hongyang.platform.metarepo.service.service.metamodel.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hongyang.framework.dao.service.MetaServiceImpl;
import com.hongyang.platform.metarepo.service.entity.metamodel.MetaOption;
import com.hongyang.platform.metarepo.service.service.metamodel.IMetaOptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 元模型选项值 Service 实现（p_meta_option）
 * <p>
 * 对标老系统 MetaOptionServiceImpl，使用 framework-dao 的 MetaServiceImpl 替代 sns-dal Dao。
 * 老系统通过 MetaOptionCacheProvides 按 itemId 缓存选项列表，
 * 新系统由 MetaServiceImpl 内置的 DaoCacheManager 统一管理。
 * <p>
 * IMetaService 已提供：getByApiKey / listAll / create / updateByApiKey / deleteByApiKey 等。
 * 本类扩展按 metamodelApiKey + itemApiKey 维度的查询、删除和校验。
 */
@Slf4j
@Service
public class MetaOptionServiceImpl extends MetaServiceImpl<MetaOption> implements IMetaOptionService {

    @Override
    public List<MetaOption> listByItemApiKey(String metamodelApiKey, String itemApiKey) {
        return baseMapper.selectList(
                Wrappers.<MetaOption>lambdaQuery()
                        .eq(MetaOption::getMetamodelApiKey, metamodelApiKey)
                        .eq(MetaOption::getItemApiKey, itemApiKey)
                        .orderByAsc(MetaOption::getOptionOrder));
    }

    @Override
    public boolean deleteByItemApiKey(String metamodelApiKey, String itemApiKey) {
        return baseMapper.delete(
                Wrappers.<MetaOption>lambdaQuery()
                        .eq(MetaOption::getMetamodelApiKey, metamodelApiKey)
                        .eq(MetaOption::getItemApiKey, itemApiKey)) > 0;
    }

    @Override
    public boolean isValidCode(String metamodelApiKey, String itemApiKey, Integer optionCode) {
        return baseMapper.selectCount(
                Wrappers.<MetaOption>lambdaQuery()
                        .eq(MetaOption::getMetamodelApiKey, metamodelApiKey)
                        .eq(MetaOption::getItemApiKey, itemApiKey)
                        .eq(MetaOption::getOptionCode, optionCode)
                        .eq(MetaOption::getEnableFlg, 1)) > 0;
    }
}
