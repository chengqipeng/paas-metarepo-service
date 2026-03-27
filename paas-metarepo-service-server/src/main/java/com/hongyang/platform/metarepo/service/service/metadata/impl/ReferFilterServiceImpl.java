package com.hongyang.platform.metarepo.service.service.metadata.impl;

import com.hongyang.platform.metarepo.service.entity.metadata.ReferFilter;
import com.hongyang.platform.metarepo.service.service.metadata.AbstractMetadataServiceImpl;
import com.hongyang.platform.metarepo.service.service.metadata.IReferFilterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 关联过滤条件 Service 实现（Common/Tenant 分表）
 * 列映射从 p_meta_item 动态加载。
 */
@Slf4j
@Service
public class ReferFilterServiceImpl
        extends AbstractMetadataServiceImpl<ReferFilter>
        implements IReferFilterService {

    @Override
    public List<ReferFilter> listByItemApiKey(String itemApiKey) {
        return lambdaQuery()
            .eq(ReferFilter::getItemApiKey, itemApiKey)
            .eq(ReferFilter::getDeleteFlg, 0)
            .list();
    }
}
