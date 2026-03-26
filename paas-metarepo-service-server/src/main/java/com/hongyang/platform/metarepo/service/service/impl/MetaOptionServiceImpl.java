package com.hongyang.platform.metarepo.service.service.impl;

import com.hongyang.framework.dao.service.SimpleBaseServiceImpl;
import com.hongyang.platform.metarepo.service.entity.meta.MetaOption;
import com.hongyang.platform.metarepo.service.service.IMetaOptionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 元模型选项值 Service 实现
 */
@Service
public class MetaOptionServiceImpl
        extends SimpleBaseServiceImpl<MetaOption>
        implements IMetaOptionService {

    @Override
    public List<MetaOption> listByItemApiKey(String metamodelApiKey, String itemApiKey) {
        return lambdaQuery()
                .eq(MetaOption::getMetamodelApiKey, metamodelApiKey)
                .eq(MetaOption::getItemApiKey, itemApiKey)
                .list();
    }

    @Override
    public Set<Integer> getValidCodes(String metamodelApiKey, String itemApiKey) {
        return listByItemApiKey(metamodelApiKey, itemApiKey).stream()
                .map(MetaOption::getOptionCode)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isValidCode(String metamodelApiKey, String itemApiKey, Integer code) {
        return getValidCodes(metamodelApiKey, itemApiKey).contains(code);
    }
}
