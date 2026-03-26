package com.hongyang.platform.metarepo.service.service.impl;

import com.hongyang.platform.metarepo.core.model.metamodel.XEntity;
import com.hongyang.platform.metarepo.core.model.metamodel.XItem;
import com.hongyang.platform.metarepo.service.common.converter.MetaRepoConverter;
import com.hongyang.platform.metarepo.service.entity.metamodel.tenant.TenantEntity;
import com.hongyang.platform.metarepo.service.entity.metamodel.common.CommonEntity;
import com.hongyang.platform.metarepo.service.entity.metamodel.tenant.TenantItem;
import com.hongyang.platform.metarepo.service.service.ICommonTenantMergeService;
import com.hongyang.platform.metarepo.service.service.ITenantEntityService;
import com.hongyang.platform.metarepo.service.service.ITenantItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Common/Tenant 合并查询实现
 * 规则：
 * - Common 有、Tenant 无 → 使用 Common
 * - Common 有、Tenant 有（同 api_key）→ Tenant 覆盖 Common
 * - Common 无、Tenant 有 → 纯租户自定义
 * - Tenant 中 delete_flg=1 → 对此租户隐藏
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CommonTenantMergeServiceImpl implements ICommonTenantMergeService {

    private final CommonEntityService commonEntityService;
    private final ITenantEntityService tenantEntityService;
    private final CommonItemService commonItemService;
    private final ITenantItemService tenantItemService;

    @Override
    public List<XEntity> listMergedEntities(Long tenantId) {
        // 查 Common 层
        List<CommonEntity> commonList = commonEntityService.lambdaQuery()
                .eq(CommonEntity::getEnableFlg, 1)
                .list();

        // 查 Tenant 层
        List<TenantEntity> tenantList = tenantEntityService.listByTenant(tenantId);

        return mergeEntityLists(commonList, tenantList);
    }

    @Override
    public List<XEntity> listMergedEntitiesPage(Long tenantId, int page, int size) {
        List<XEntity> all = listMergedEntities(tenantId);
        int from = (page - 1) * size;
        if (from >= all.size()) return Collections.emptyList();
        int to = Math.min(from + size, all.size());
        return all.subList(from, to);
    }

    @Override
    public long countMergedEntities(Long tenantId) {
        return listMergedEntities(tenantId).size();
    }

    @Override
    public List<XItem> listMergedItems(Long tenantId, Long entityId) {
        List<TenantItem> tenantItems = tenantItemService.listByEntityId(tenantId, entityId);
        // Common 级字段通过 commonItemService 查询
        List<TenantItem> commonItems = commonItemService.listByEntityId(entityId);

        return mergeItemLists(commonItems, tenantItems);
    }

    @Override
    public XEntity getMergedEntity(Long tenantId, String apiKey) {
        CommonEntity common = commonEntityService.lambdaQuery()
                .eq(CommonEntity::getApiKey, apiKey)
                .one();
        TenantEntity tenant = tenantEntityService.getByApiKey(tenantId, apiKey);

        if (tenant != null && tenant.getDeleteFlg() != null && tenant.getDeleteFlg() == 1) {
            return null; // 租户隐藏
        }
        if (common == null && tenant == null) return null;
        if (tenant != null) return MetaRepoConverter.toXEntity(tenant);
        return MetaRepoConverter.commonToXEntity(common);
    }

    /**
     * 合并 Entity 列表
     */
    private List<XEntity> mergeEntityLists(List<CommonEntity> commonList, List<TenantEntity> tenantList) {
        Map<String, TenantEntity> tenantMap = tenantList.stream()
                .filter(e -> e.getApiKey() != null)
                .collect(Collectors.toMap(TenantEntity::getApiKey, e -> e, (a, b) -> b));

        List<XEntity> result = new ArrayList<>();

        for (CommonEntity common : commonList) {
            TenantEntity tenant = tenantMap.remove(common.getApiKey());
            if (tenant != null) {
                if (tenant.getDeleteFlg() != null && tenant.getDeleteFlg() == 1) continue;
                result.add(MetaRepoConverter.toXEntity(tenant));
            } else {
                result.add(MetaRepoConverter.commonToXEntity(common));
            }
        }

        // 纯租户自定义
        for (TenantEntity tenant : tenantMap.values()) {
            if (tenant.getDeleteFlg() == null || tenant.getDeleteFlg() != 1) {
                result.add(MetaRepoConverter.toXEntity(tenant));
            }
        }

        return result;
    }

    /**
     * 合并 Item 列表
     */
    private List<XItem> mergeItemLists(List<TenantItem> commonItems, List<TenantItem> tenantItems) {
        Map<String, TenantItem> tenantMap = tenantItems.stream()
                .filter(e -> e.getApiKey() != null)
                .collect(Collectors.toMap(TenantItem::getApiKey, e -> e, (a, b) -> b));

        List<XItem> result = new ArrayList<>();

        for (TenantItem common : commonItems) {
            TenantItem tenant = tenantMap.remove(common.getApiKey());
            if (tenant != null) {
                if (tenant.getDeleteFlg() != null && tenant.getDeleteFlg() == 1) continue;
                result.add(MetaRepoConverter.toXItem(tenant));
            } else {
                result.add(MetaRepoConverter.toXItem(common));
            }
        }

        for (TenantItem tenant : tenantMap.values()) {
            if (tenant.getDeleteFlg() == null || tenant.getDeleteFlg() != 1) {
                result.add(MetaRepoConverter.toXItem(tenant));
            }
        }

        result.sort(Comparator.comparingInt(i -> i.getItemOrder() != null ? i.getItemOrder() : Integer.MAX_VALUE));
        return result;
    }
}
