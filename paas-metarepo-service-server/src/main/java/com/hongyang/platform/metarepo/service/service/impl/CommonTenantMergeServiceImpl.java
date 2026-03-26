package com.hongyang.platform.metarepo.service.service.impl;

import com.hongyang.framework.dao.service.SimpleBaseServiceImpl;
import com.hongyang.platform.metarepo.core.model.metamodel.XEntity;
import com.hongyang.platform.metarepo.core.model.metamodel.XItem;
import com.hongyang.platform.metarepo.service.common.converter.MetaRepoConverter;
import com.hongyang.platform.metarepo.service.entity.CustomEntity;
import com.hongyang.platform.metarepo.service.entity.CustomEntityCommon;
import com.hongyang.platform.metarepo.service.entity.CustomItem;
import com.hongyang.platform.metarepo.service.service.ICommonTenantMergeService;
import com.hongyang.platform.metarepo.service.service.ICustomEntityService;
import com.hongyang.platform.metarepo.service.service.ICustomItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
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
    private final ICustomEntityService tenantEntityService;
    private final CommonItemService commonItemService;
    private final ICustomItemService tenantItemService;

    @Override
    public List<XEntity> listMergedEntities(Long tenantId) {
        // 查 Common 层
        List<CustomEntityCommon> commonList = commonEntityService.lambdaQuery()
                .eq(CustomEntityCommon::getEnableFlg, 1)
                .list();

        // 查 Tenant 层
        List<CustomEntity> tenantList = tenantEntityService.listByTenant(tenantId);

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
        List<CustomItem> tenantItems = tenantItemService.listByEntityId(tenantId, entityId);
        // Common 级字段通过 commonItemService 查询
        List<CustomItem> commonItems = commonItemService.listByEntityId(entityId);

        return mergeItemLists(commonItems, tenantItems);
    }

    @Override
    public XEntity getMergedEntity(Long tenantId, String apiKey) {
        CustomEntityCommon common = commonEntityService.lambdaQuery()
                .eq(CustomEntityCommon::getApiKey, apiKey)
                .one();
        CustomEntity tenant = tenantEntityService.getByApiKey(tenantId, apiKey);

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
    private List<XEntity> mergeEntityLists(List<CustomEntityCommon> commonList, List<CustomEntity> tenantList) {
        Map<String, CustomEntity> tenantMap = tenantList.stream()
                .filter(e -> e.getApiKey() != null)
                .collect(Collectors.toMap(CustomEntity::getApiKey, e -> e, (a, b) -> b));

        List<XEntity> result = new ArrayList<>();

        for (CustomEntityCommon common : commonList) {
            CustomEntity tenant = tenantMap.remove(common.getApiKey());
            if (tenant != null) {
                if (tenant.getDeleteFlg() != null && tenant.getDeleteFlg() == 1) continue;
                result.add(MetaRepoConverter.toXEntity(tenant));
            } else {
                result.add(MetaRepoConverter.commonToXEntity(common));
            }
        }

        // 纯租户自定义
        for (CustomEntity tenant : tenantMap.values()) {
            if (tenant.getDeleteFlg() == null || tenant.getDeleteFlg() != 1) {
                result.add(MetaRepoConverter.toXEntity(tenant));
            }
        }

        return result;
    }

    /**
     * 合并 Item 列表
     */
    private List<XItem> mergeItemLists(List<CustomItem> commonItems, List<CustomItem> tenantItems) {
        Map<String, CustomItem> tenantMap = tenantItems.stream()
                .filter(e -> e.getApiKey() != null)
                .collect(Collectors.toMap(CustomItem::getApiKey, e -> e, (a, b) -> b));

        List<XItem> result = new ArrayList<>();

        for (CustomItem common : commonItems) {
            CustomItem tenant = tenantMap.remove(common.getApiKey());
            if (tenant != null) {
                if (tenant.getDeleteFlg() != null && tenant.getDeleteFlg() == 1) continue;
                result.add(MetaRepoConverter.toXItem(tenant));
            } else {
                result.add(MetaRepoConverter.toXItem(common));
            }
        }

        for (CustomItem tenant : tenantMap.values()) {
            if (tenant.getDeleteFlg() == null || tenant.getDeleteFlg() != 1) {
                result.add(MetaRepoConverter.toXItem(tenant));
            }
        }

        result.sort(Comparator.comparingInt(i -> i.getItemOrder() != null ? i.getItemOrder() : Integer.MAX_VALUE));
        return result;
    }
}
