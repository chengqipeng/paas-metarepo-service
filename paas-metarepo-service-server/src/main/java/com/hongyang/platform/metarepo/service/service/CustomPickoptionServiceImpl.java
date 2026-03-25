package com.hongyang.platform.metarepo.service.service;

import com.hongyang.framework.dao.service.BaseServiceImpl;
import com.hongyang.platform.metarepo.service.entity.CustomPickoption;
import com.hongyang.platform.metarepo.service.mapper.CustomPickoptionMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomPickoptionServiceImpl
        extends BaseServiceImpl<CustomPickoptionMapper, CustomPickoption> {

    public List<CustomPickoption> listByItemId(Long tenantId, Long itemId) {
        return lambdaQuery()
            .eq(CustomPickoption::getTenantId, tenantId)
            .eq(CustomPickoption::getItemId, itemId)
            .eq(CustomPickoption::getDeleteFlg, 0)
            .orderByAsc(CustomPickoption::getOptionOrder)
            .list();
    }
}
