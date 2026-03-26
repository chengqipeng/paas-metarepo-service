package com.hongyang.platform.metarepo.service.service.metamodel.impl;

import com.hongyang.framework.dao.query.MetaQueryCondition;
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
 * 所有查询走 MetaServiceImpl 的两阶段缓存路径（listByCondition / countByCondition），
 * 不直接使用 baseMapper 以避免绕过缓存层。
 * <p>
 * IMetaService 已提供：getByApiKey / listAll / create / updateByApiKey / deleteByApiKey 等。
 * 本类扩展按 metamodelApiKey + itemApiKey 维度的查询、删除和校验。
 */
@Slf4j
@Service
public class MetaOptionServiceImpl extends MetaServiceImpl<MetaOption> implements IMetaOptionService {

    @Override
    public List<MetaOption> listByItemApiKey(String metamodelApiKey, String itemApiKey) {
        MetaQueryCondition<MetaOption> condition = MetaQueryCondition.<MetaOption>create()
                .eq(MetaOption::getMetamodelApiKey, metamodelApiKey)
                .eq(MetaOption::getItemApiKey, itemApiKey)
                .orderByAsc(MetaOption::getOptionOrder);
        return listByCondition(condition);
    }

    @Override
    public boolean deleteByItemApiKey(String metamodelApiKey, String itemApiKey) {
        MetaQueryCondition<MetaOption> condition = MetaQueryCondition.<MetaOption>create()
                .eq(MetaOption::getMetamodelApiKey, metamodelApiKey)
                .eq(MetaOption::getItemApiKey, itemApiKey);
        List<MetaOption> options = listByCondition(condition);
        if (options.isEmpty()) {
            return false;
        }
        for (MetaOption option : options) {
            deleteByApiKey(option.getApiKey());
        }
        return true;
    }

    @Override
    public boolean isValidCode(String metamodelApiKey, String itemApiKey, Integer optionCode) {
        MetaQueryCondition<MetaOption> condition = MetaQueryCondition.<MetaOption>create()
                .eq(MetaOption::getMetamodelApiKey, metamodelApiKey)
                .eq(MetaOption::getItemApiKey, itemApiKey)
                .eq(MetaOption::getEnableFlg, 1);
        return countByCondition(condition) > 0;
    }
}
