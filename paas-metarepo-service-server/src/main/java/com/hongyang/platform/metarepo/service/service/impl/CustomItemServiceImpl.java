package com.hongyang.platform.metarepo.service.service.impl;

import com.alibaba.fastjson2.JSON;
import com.hongyang.framework.dao.service.SimpleBaseServiceImpl;
import com.hongyang.framework.base.exception.BaseKnownException;
import com.hongyang.framework.common.enums.paas.MetaRepoErrorCodeEnum;
import com.hongyang.platform.metarepo.service.entity.CustomEntityLink;
import com.hongyang.platform.metarepo.service.entity.CustomItem;
import com.hongyang.platform.metarepo.service.entity.CustomPickOption;
import com.hongyang.platform.metarepo.service.service.ICustomEntityLinkService;
import com.hongyang.platform.metarepo.service.service.ICustomItemService;
import com.hongyang.platform.metarepo.service.service.ICustomPickOptionService;
import com.hongyang.platform.metarepo.service.service.IMetaLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/** itemType 常量：LOOKUP */
@Slf4j
@Service
public class CustomItemServiceImpl
        extends SimpleBaseServiceImpl<CustomItem>
        implements ICustomItemService {

    private static final int ITEM_TYPE_LOOKUP = 19;
    private static final int ITEM_TYPE_PICKLIST = 6;
    private static final int ITEM_TYPE_MULTI_PICKLIST = 7;

    private final ICustomEntityLinkService customEntityLinkService;
    private final ICustomPickOptionService customPickOptionService;
    private final IMetaLogService metaLogService;

    public CustomItemServiceImpl(@Lazy ICustomEntityLinkService customEntityLinkService,
                                  @Lazy ICustomPickOptionService customPickOptionService,
                                  IMetaLogService metaLogService) {
        this.customEntityLinkService = customEntityLinkService;
        this.customPickOptionService = customPickOptionService;
        this.metaLogService = metaLogService;
    }

    @Override
    public List<CustomItem> listByEntityId(Long tenantId, Long entityId) {
        return lambdaQuery()
                .eq(CustomItem::getTenantId, tenantId)
                .eq(CustomItem::getEntityId, entityId)
                .orderByAsc(CustomItem::getItemOrder)
                .list();
    }

    @Override
    public CustomItem getByApiKey(Long tenantId, Long entityId, String apiKey) {
        return lambdaQuery()
                .eq(CustomItem::getTenantId, tenantId)
                .eq(CustomItem::getEntityId, entityId)
                .eq(CustomItem::getApiKey, apiKey)
                .one();
    }

    @Override
    public boolean existsApiKey(Long tenantId, Long entityId, String apiKey) {
        return lambdaQuery()
                .eq(CustomItem::getTenantId, tenantId)
                .eq(CustomItem::getEntityId, entityId)
                .eq(CustomItem::getApiKey, apiKey)
                .exists();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CustomItem createItem(CustomItem item) {
        if (existsApiKey(item.getTenantId(), item.getEntityId(), item.getApiKey())) {
            throw new BaseKnownException(MetaRepoErrorCodeEnum.META_APIKEY_DUPLICATE, item.getApiKey());
        }
        item.setEnableFlg(1);
        if (item.getDeleteFlg() == null) item.setDeleteFlg(0);
        if (item.getRequireFlg() == null) item.setRequireFlg(0);
        if (item.getCustomFlg() == null) item.setCustomFlg(1);
        if (item.getItemOrder() == null) item.setItemOrder(0);
        save(item);

        // LOOKUP 类型自动创建 entity_link
        if (item.getItemType() != null && item.getItemType() == ITEM_TYPE_LOOKUP
                && item.getReferEntityId() != null) {
            CustomEntityLink link = new CustomEntityLink();
            link.setTenantId(item.getTenantId());
            link.setName(item.getName() + "_link");
            link.setApiKey(item.getApiKey() + "_link");
            link.setLabel(item.getLabel());
            link.setLinkType(1); // LOOKUP
            link.setParentEntityId(item.getReferEntityId());
            link.setChildEntityId(item.getEntityId());
            link.setCascadeDelete(0);
            link.setAccessControl(0);
            link.setEnableFlg(1);
            link.setDeleteFlg(0);
            customEntityLinkService.save(link);
            item.setReferLinkId(link.getId());
            updateById(item);
        }

        // PICKLIST/MULTI_PICKLIST 类型：从 typeProperty JSON 中解析默认选项值并创建
        if (item.getItemType() != null && isPicklistType(item.getItemType())
                && item.getTypeProperty() != null) {
            createDefaultPickOptions(item);
        }

        metaLogService.log(item.getTenantId(), item.getId(), item.getEntityId(), null,
                null, JSON.toJSONString(item), 1);
        return item;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CustomItem updateItem(Long itemId, Long tenantId, CustomItem updates) {
        CustomItem item = getByIdAndTenant(itemId, tenantId);
        if (item == null) {
            throw new BaseKnownException(MetaRepoErrorCodeEnum.META_NOT_FOUND, String.valueOf(itemId));
        }
        String oldValue = JSON.toJSONString(item);

        if (updates.getLabel() != null) item.setLabel(updates.getLabel());
        if (updates.getDescription() != null) item.setDescription(updates.getDescription());
        if (updates.getHelpText() != null) item.setHelpText(updates.getHelpText());
        if (updates.getDefaultValue() != null) item.setDefaultValue(updates.getDefaultValue());
        if (updates.getRequireFlg() != null) item.setRequireFlg(updates.getRequireFlg());
        if (updates.getHiddenFlg() != null) item.setHiddenFlg(updates.getHiddenFlg());
        if (updates.getItemOrder() != null) item.setItemOrder(updates.getItemOrder());
        if (updates.getTypeProperty() != null) item.setTypeProperty(updates.getTypeProperty());

        updateById(item);

        metaLogService.log(tenantId, itemId, item.getEntityId(), null,
                oldValue, JSON.toJSONString(item), 2);
        return item;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteItemWithCheck(Long tenantId, Long itemId) {
        CustomItem item = getByIdAndTenant(itemId, tenantId);
        if (item == null) {
            throw new BaseKnownException(MetaRepoErrorCodeEnum.META_NOT_FOUND, String.valueOf(itemId));
        }
        if (item.getCustomFlg() != null && item.getCustomFlg() == 0) {
            throw new BaseKnownException(MetaRepoErrorCodeEnum.META_STANDARD_CANNOT_DELETE, item.getApiKey());
        }

        // 检查是否有关联关系引用此字段
        if (item.getReferLinkId() != null) {
            customEntityLinkService.softDelete(item.getReferLinkId(), tenantId);
        }

        String oldValue = JSON.toJSONString(item);
        removeById(itemId);

        metaLogService.log(tenantId, itemId, item.getEntityId(), null, oldValue, null, 3);
    }

    private boolean isPicklistType(Integer itemType) {
        return itemType == ITEM_TYPE_PICKLIST || itemType == ITEM_TYPE_MULTI_PICKLIST;
    }

    /**
     * 从 typeProperty JSON 解析选项值并批量创建
     * typeProperty 格式示例：{"options":[{"label":"选项1","code":1},{"label":"选项2","code":2}]}
     */
    private void createDefaultPickOptions(CustomItem item) {
        try {
            com.alibaba.fastjson2.JSONObject tp = com.alibaba.fastjson2.JSON.parseObject(item.getTypeProperty());
            if (tp == null || !tp.containsKey("options")) return;
            com.alibaba.fastjson2.JSONArray optionsArr = tp.getJSONArray("options");
            if (optionsArr == null || optionsArr.isEmpty()) return;

            List<CustomPickOption> options = new java.util.ArrayList<>();
            for (int i = 0; i < optionsArr.size(); i++) {
                com.alibaba.fastjson2.JSONObject opt = optionsArr.getJSONObject(i);
                CustomPickOption po = new CustomPickOption();
                po.setTenantId(item.getTenantId());
                po.setEntityId(item.getEntityId());
                po.setItemId(item.getId());
                po.setLabel(opt.getString("label"));
                po.setOptionCode(opt.getIntValue("code", i + 1));
                po.setOptionOrder(i);
                po.setDefaultFlg(opt.getIntValue("defaultFlg", 0));
                po.setCustomFlg(item.getCustomFlg());
                po.setEnableFlg(1);
                options.add(po);
            }
            if (!options.isEmpty()) {
                customPickOptionService.saveBatch(options);
            }
        } catch (Exception e) {
            log.warn("解析 typeProperty 创建选项值失败, itemId={}: {}", item.getId(), e.getMessage());
        }
    }
}
