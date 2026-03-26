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
    public List<MetaOption> listByItemId(Long metamodelId, Long itemId) {
        return lambdaQuery()
                .eq(MetaOption::getMetamodelId, metamodelId)
                .eq(MetaOption::getItemId, itemId)
                .list();
    }

    @Override
    public Set<Integer> getValidCodes(Long metamodelId, Long itemId) {
        return listByItemId(metamodelId, itemId).stream()
                .map(MetaOption::getOptionCode)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isValidCode(Long metamodelId, Long itemId, Integer code) {
        return getValidCodes(metamodelId, itemId).contains(code);
    }
}
