package com.hongyang.platform.metarepo.service.api.metamodel;

import com.hongyang.platform.metarepo.core.api.MetaRepoReadApi;
import com.hongyang.platform.metarepo.core.model.Result;
import com.hongyang.platform.metarepo.core.model.dto.*;
import com.hongyang.platform.metarepo.service.common.converter.MetaRepoConverter;
import com.hongyang.platform.metarepo.service.entity.CustomCheckRuleEntity;
import com.hongyang.platform.metarepo.service.entity.CustomEntityLinkEntity;
import com.hongyang.platform.metarepo.service.entity.CustomItemEntity;
import com.hongyang.platform.metarepo.service.entity.CustomPickOptionEntity;
import com.hongyang.platform.metarepo.service.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * MetaRepo 读接口实现
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class MetaRepoReadApiService implements MetaRepoReadApi {

    private final IMetaModelQueryService metaModelQueryService;
    private final ICustomItemService customItemService;
    private final ICustomPickOptionService customPickOptionService;
    private final ICustomCheckRuleService customCheckRuleService;
    private final ICustomEntityLinkService customEntityLinkService;

    @Override
    public Result<MetaModelDTO> getMetaModel(Long tenantId, String objectApiKey) {
        MetaModelDTO dto = metaModelQueryService.getMetaModel(tenantId, objectApiKey);
        if (dto == null) {
            return Result.fail(404, "对象不存在: " + objectApiKey);
        }
        return Result.success(dto);
    }

    @Override
    public Result<List<MetaModelDTO>> batchGetMetaModel(Long tenantId, List<String> apiKeys) {
        return Result.success(metaModelQueryService.batchGetMetaModel(tenantId, apiKeys));
    }

    @Override
    public Result<List<MetaModelDTO>> listMetaModels(Long tenantId) {
        return Result.success(metaModelQueryService.listMetaModels(tenantId));
    }

    @Override
    public Result<List<ItemDTO>> listItems(Long tenantId, Long entityId) {
        List<CustomItemEntity> items = customItemService.listByEntityId(tenantId, entityId);
        return Result.success(MetaRepoConverter.toItemDTOList(items));
    }

    @Override
    public Result<List<PickOptionDTO>> listPickOptions(Long tenantId, Long itemId) {
        List<CustomPickOptionEntity> options = customPickOptionService.listByItemId(tenantId, itemId);
        return Result.success(MetaRepoConverter.toPickOptionDTOList(options));
    }

    @Override
    public Result<List<CheckRuleDTO>> listCheckRules(Long tenantId, Long entityId) {
        List<CustomCheckRuleEntity> rules = customCheckRuleService.listByObjectId(tenantId, entityId);
        return Result.success(MetaRepoConverter.toCheckRuleDTOList(rules));
    }

    @Override
    public Result<List<LinkDTO>> listEntityLinks(Long tenantId, Long entityId) {
        List<CustomEntityLinkEntity> links = customEntityLinkService.listByEntityId(tenantId, entityId);
        return Result.success(MetaRepoConverter.toLinkDTOList(links));
    }
}
