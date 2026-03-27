package com.hongyang.platform.metarepo.service.api.metamodel;

import com.hongyang.framework.common.constants.paas.MetamodelApiKey;
import com.hongyang.platform.metarepo.core.api.MetaRepoWriteApi;
import com.hongyang.platform.metarepo.core.model.metamodel.XCheckRule;
import com.hongyang.platform.metarepo.core.model.metamodel.XEntity;
import com.hongyang.platform.metarepo.core.model.metamodel.XEntityItem;
import com.hongyang.platform.metarepo.core.model.metamodel.XLink;
import com.hongyang.platform.metarepo.core.model.request.CreateCheckRuleRequest;
import com.hongyang.platform.metarepo.core.model.request.CreateEntityRequest;
import com.hongyang.platform.metarepo.core.model.request.CreateItemRequest;
import com.hongyang.platform.metarepo.core.model.request.CreateLinkRequest;
import com.hongyang.platform.metarepo.core.model.request.SavePickOptionRequest;
import com.hongyang.platform.metarepo.core.model.request.UpdateCheckRuleRequest;
import com.hongyang.platform.metarepo.core.model.request.UpdateEntityRequest;
import com.hongyang.platform.metarepo.core.model.request.UpdateItemRequest;
import com.hongyang.platform.metarepo.service.common.converter.MetaRepoConverter;
import com.hongyang.platform.metarepo.service.entity.metadata.Entity;
import com.hongyang.platform.metarepo.service.service.metadata.IMetadataMergeReadService;
import com.hongyang.framework.base.exception.BaseKnownException;
import com.hongyang.framework.common.enums.paas.MetaRepoErrorCodeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/metarepo")
@RequiredArgsConstructor
public class MetaRepoWriteApiService implements MetaRepoWriteApi {

    private final IMetadataMergeReadService metadataMergeReadService;

    @Override
    @PostMapping("/write/entity")
    public XEntity createEntity(@RequestBody CreateEntityRequest request) {
        Entity existing = metadataMergeReadService.getByApiKeyMerged(
                MetamodelApiKey.ENTITY, request.getApiKey(), Entity.class);
        if (existing != null) {
            throw new BaseKnownException(MetaRepoErrorCodeEnum.META_APIKEY_DUPLICATE, request.getApiKey());
        }
        // TODO: 写入 p_tenant_metadata
        throw new UnsupportedOperationException("createEntity 写入逻辑待实现");
    }

    @Override
    @PutMapping("/write/entity")
    public XEntity updateEntity(@RequestBody UpdateEntityRequest request) {
        // TODO: 实现更新
        throw new UnsupportedOperationException("updateEntity 待实现");
    }

    @Override
    @DeleteMapping("/write/entity")
    public void deleteEntity(@RequestParam("apiKey") String apiKey) {
        // TODO: 实现删除
        throw new UnsupportedOperationException("deleteEntity 待实现");
    }

    @Override
    @PostMapping("/write/item")
    public XEntityItem createItem(@RequestBody CreateItemRequest request) {
        // TODO: 实现创建
        throw new UnsupportedOperationException("createItem 待实现");
    }

    @Override
    @PutMapping("/write/item")
    public XEntityItem updateItem(@RequestBody UpdateItemRequest request) {
        throw new UnsupportedOperationException("updateItem 待实现");
    }

    @Override
    @DeleteMapping("/write/item")
    public void deleteItem(@RequestParam("apiKey") String apiKey) {
        throw new UnsupportedOperationException("deleteItem 待实现");
    }

    @Override
    @PostMapping("/write/pick-options")
    public void savePickOptions(@RequestBody SavePickOptionRequest request) {
        throw new UnsupportedOperationException("savePickOptions 待实现");
    }

    @Override
    @PostMapping("/write/entity-link")
    public XLink createEntityLink(@RequestBody CreateLinkRequest request) {
        throw new UnsupportedOperationException("createEntityLink 待实现");
    }

    @Override
    @DeleteMapping("/write/entity-link")
    public void deleteEntityLink(@RequestParam("apiKey") String apiKey) {
        throw new UnsupportedOperationException("deleteEntityLink 待实现");
    }

    @Override
    @PostMapping("/write/check-rule")
    public XCheckRule createCheckRule(@RequestBody CreateCheckRuleRequest request) {
        throw new UnsupportedOperationException("createCheckRule 待实现");
    }

    @Override
    @PutMapping("/write/check-rule")
    public XCheckRule updateCheckRule(@RequestBody UpdateCheckRuleRequest request) {
        throw new UnsupportedOperationException("updateCheckRule 待实现");
    }
}
