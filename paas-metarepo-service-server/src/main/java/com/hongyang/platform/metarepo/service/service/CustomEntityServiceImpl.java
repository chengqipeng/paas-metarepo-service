package com.hongyang.platform.metarepo.service.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hongyang.framework.dao.cache.CacheableDao;
import com.hongyang.framework.dao.cache.HashCacheableDao;
import com.hongyang.framework.dao.service.BaseServiceImpl;
import com.hongyang.platform.metarepo.service.entity.CustomEntity;
import com.hongyang.platform.metarepo.service.mapper.CustomEntityMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CustomEntityServiceImpl
        extends BaseServiceImpl<CustomEntityMapper, CustomEntity> {

    @HashCacheableDao(prefix = "meta:entity")
    public CustomEntity getByApiKey(Long tenantId, String apiKey) {
        return lambdaQuery()
            .eq(CustomEntity::getTenantId, tenantId)
            .eq(CustomEntity::getApiKey, apiKey)
            .eq(CustomEntity::getDeleteFlg, 0)
            .one();
    }

    public boolean existsByApiKey(Long tenantId, String apiKey) {
        return lambdaQuery()
            .eq(CustomEntity::getTenantId, tenantId)
            .eq(CustomEntity::getApiKey, apiKey)
            .eq(CustomEntity::getDeleteFlg, 0)
            .exists();
    }

    @HashCacheableDao(prefix = "meta:entity", op = CacheableDao.CacheOp.EVICT)
    public boolean softDeleteEntity(Long tenantId, Long entityId) {
        return softDelete(entityId, tenantId);
    }
}
