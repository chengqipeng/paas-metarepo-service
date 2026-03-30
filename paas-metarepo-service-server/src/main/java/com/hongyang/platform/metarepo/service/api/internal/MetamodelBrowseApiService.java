package com.hongyang.platform.metarepo.service.api.internal;

import com.hongyang.framework.common.constants.paas.MetamodelApiKey;
import com.hongyang.framework.common.enums.paas.ItemTypeEnum;
import com.hongyang.platform.metarepo.core.model.metamodel.XCheckRule;
import com.hongyang.platform.metarepo.core.model.metamodel.XEntity;
import com.hongyang.platform.metarepo.core.model.metamodel.XEntityItem;
import com.hongyang.platform.metarepo.core.model.metamodel.XLink;
import com.hongyang.platform.metarepo.core.model.metamodel.XPickOption;
import com.hongyang.platform.metarepo.service.common.converter.MetaRepoConverter;
import com.hongyang.platform.metarepo.service.entity.metadata.CheckRule;
import com.hongyang.platform.metarepo.service.entity.metadata.Entity;
import com.hongyang.platform.metarepo.service.entity.metadata.EntityItem;
import com.hongyang.platform.metarepo.service.entity.metadata.EntityLink;
import com.hongyang.platform.metarepo.service.entity.metadata.PickOption;
import com.hongyang.platform.metarepo.service.entity.metamodel.MetaItem;
import com.hongyang.platform.metarepo.service.entity.metamodel.MetaModel;
import com.hongyang.platform.metarepo.service.service.metadata.IMetadataMergeReadService;
import com.hongyang.platform.metarepo.service.service.metamodel.IMetaItemService;
import com.hongyang.platform.metarepo.service.service.metamodel.IMetaModelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * 元模型浏览接口。所有元数据接口返回 Common + Tenant merge 后的结果。
 */
@Slf4j
@RestController
@RequestMapping("/metarepo/internal")
@RequiredArgsConstructor
public class MetamodelBrowseApiService {

    private final IMetaModelService metaModelService;
    private final IMetaItemService metaItemService;
    private final IMetadataMergeReadService metadataMergeReadService;

    @GetMapping("/metamodels")
    public List<MetaModel> listMetaModels() {
        return metaModelService.listAll();
    }

    @GetMapping("/meta-items")
    public List<MetaItem> listMetaItems(@RequestParam("metamodelApiKey") String metamodelApiKey) {
        return metaItemService.listByMetamodelApiKey(metamodelApiKey);
    }

    @GetMapping("/column-mapping")
    public Map<String, String> getColumnMapping(@RequestParam("metamodelApiKey") String metamodelApiKey) {
        List<MetaItem> items = metaItemService.listByMetamodelApiKey(metamodelApiKey);
        Map<String, String> map = new LinkedHashMap<>();
        for (MetaItem item : items) {
            if (item.getDbColumn() != null && item.getApiKey() != null) {
                map.put(item.getDbColumn(), item.getApiKey());
            }
        }
        return map;
    }

    @GetMapping("/metadata/entities")
    public List<XEntity> listEntities() {
        List<Entity> entities = metadataMergeReadService.listMerged(MetamodelApiKey.ENTITY);
        return MetaRepoConverter.toXEntityList(entities);
    }

    @GetMapping("/metadata/items")
    public List<XEntityItem> listItems(@RequestParam("entityApiKey") String entityApiKey) {
        List<EntityItem> items = metadataMergeReadService.listMergedByEntityApiKey(MetamodelApiKey.ITEM, entityApiKey);
        return MetaRepoConverter.toXEntityItemList(items);
    }

    @GetMapping("/metadata/pick-options")
    public List<XPickOption> listPickOptions(@RequestParam("itemApiKey") String itemApiKey) {
        List<PickOption> allOptions = metadataMergeReadService.listMerged(MetamodelApiKey.PICK_OPTION);
        return MetaRepoConverter.toXPickOptionList(
                allOptions.stream()
                        .filter(e -> itemApiKey.equals(e.getItemApiKey()))
                        .collect(Collectors.toList()));
    }

    @GetMapping("/metadata/entity-links")
    public List<XLink> listEntityLinks(@RequestParam("entityApiKey") String entityApiKey) {
        List<EntityLink> links = metadataMergeReadService.listMergedByEntityApiKey(MetamodelApiKey.ENTITY_LINK, entityApiKey);
        return MetaRepoConverter.toXLinkList(links);
    }

    @GetMapping("/metadata/check-rules")
    public List<XCheckRule> listCheckRules(@RequestParam("entityApiKey") String entityApiKey) {
        List<CheckRule> rules = metadataMergeReadService.listMergedByEntityApiKey(MetamodelApiKey.CHECK_RULE, entityApiKey);
        return MetaRepoConverter.toXCheckRuleList(rules);
    }

    /** 通用查询：按 metamodelApiKey 自动判断模型类型 */
    @GetMapping("/metadata")
    public List<?> listMergedAuto(@RequestParam("metamodelApiKey") String metamodelApiKey) {
        return metadataMergeReadService.listMergedAuto(metamodelApiKey);
    }

    /** 返回 ItemTypeEnum 完整映射（供前端展示字段类型信息） */
    @GetMapping("/item-type-mapping")
    public List<Map<String, Object>> getItemTypeMapping() {
        List<Map<String, Object>> result = new ArrayList<>();
        for (ItemTypeEnum e : ItemTypeEnum.values()) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("code", e.getCode());
            item.put("name", e.name());
            item.put("description", e.getDescription());
            item.put("dbColumnPrefix", e.getDbColumnPrefix());
            item.put("minIndex", e.getMinIndex());
            item.put("maxIndex", e.getMaxIndex());
            item.put("columnCapacity", e.getColumnCapacity());
            result.add(item);
        }
        return result;
    }
}
