package com.hongyang.platform.metarepo.service.service;

import com.hongyang.platform.metarepo.core.model.metamodel.XEntity;
import com.hongyang.platform.metarepo.core.model.metamodel.XItem;

import java.util.List;

/**
 * Common/Tenant 合并查询 Service
 * Common 级表无 tenant_id，Tenant 级表有 tenant_id
 * 查询时合并两层数据，Tenant 覆盖 Common（按 api_key 匹配）
 */
public interface ICommonTenantMergeService {

    /** 合并查询对象列表（Common + Tenant，Tenant 覆盖 Common） */
    List<XEntity> listMergedEntities(Long tenantId);

    /** 合并分页查询对象列表 */
    List<XEntity> listMergedEntitiesPage(Long tenantId, int page, int size);

    /** 合并查询对象总数 */
    long countMergedEntities(Long tenantId);

    /** 合并查询字段列表（Common + Tenant） */
    List<XItem> listMergedItems(Long tenantId, Long entityId);

    /** 按 apiKey 合并查询单个对象 */
    XEntity getMergedEntity(Long tenantId, String apiKey);
}
