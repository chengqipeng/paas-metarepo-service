package com.hongyang.platform.metarepo.service.service.metadata.impl;

import com.hongyang.framework.dao.split.CommonTenantServiceImpl;
import com.hongyang.platform.metarepo.service.entity.metadata.PickOption;
import com.hongyang.platform.metarepo.service.service.metadata.IPickOptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 字段选项值 Service 实现（Common/Tenant 分表）
 */
@Slf4j
@Service
public class PickOptionServiceImpl
        extends CommonTenantServiceImpl<PickOption>
        implements IPickOptionService {

    @Override
    public List<PickOption> listMerged(String itemApiKey) {
        return merge(
            listCommon().stream()
                .filter(e -> itemApiKey.equals(e.getItemApiKey()))
                .collect(Collectors.toList()),
            listByItemApiKey(itemApiKey),
            PickOption::getApiKey
        );
    }

    @Override
    public List<PickOption> listByItemApiKey(String itemApiKey) {
        return lambdaQuery()
            .eq(PickOption::getItemApiKey, itemApiKey)
            .list();
    }
}
