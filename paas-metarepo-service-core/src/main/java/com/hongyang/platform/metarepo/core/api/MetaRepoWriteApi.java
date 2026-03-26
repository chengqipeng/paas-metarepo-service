package com.hongyang.platform.metarepo.core.api;

import com.hongyang.platform.metarepo.core.model.metamodel.XCheckRule;
import com.hongyang.platform.metarepo.core.model.metamodel.XEntity;
import com.hongyang.platform.metarepo.core.model.metamodel.XItem;
import com.hongyang.platform.metarepo.core.model.metamodel.XLink;
import com.hongyang.platform.metarepo.core.model.request.CreateCheckRuleRequest;
import com.hongyang.platform.metarepo.core.model.request.CreateEntityRequest;
import com.hongyang.platform.metarepo.core.model.request.CreateItemRequest;
import com.hongyang.platform.metarepo.core.model.request.CreateLinkRequest;
import com.hongyang.platform.metarepo.core.model.request.SavePickOptionRequest;
import com.hongyang.platform.metarepo.core.model.request.UpdateCheckRuleRequest;
import com.hongyang.platform.metarepo.core.model.request.UpdateEntityRequest;
import com.hongyang.platform.metarepo.core.model.request.UpdateItemRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * MetaRepo 写接口定义（API 契约）
 * tenantId 由 TenantInterceptor 从 GlobalContext 自动注入，无需传递。
 */
@RequestMapping("/metarepo")
public interface MetaRepoWriteApi {

    @PostMapping("/write/entity")
    XEntity createEntity(@RequestBody CreateEntityRequest request);

    @PutMapping("/write/entity")
    XEntity updateEntity(@RequestBody UpdateEntityRequest request);

    @DeleteMapping("/write/entity")
    void deleteEntity(@RequestParam("apiKey") String apiKey);

    @PostMapping("/write/item")
    XItem createItem(@RequestBody CreateItemRequest request);

    @PutMapping("/write/item")
    XItem updateItem(@RequestBody UpdateItemRequest request);

    @DeleteMapping("/write/item")
    void deleteItem(@RequestParam("apiKey") String apiKey);

    @PostMapping("/write/pick-options")
    void savePickOptions(@RequestBody SavePickOptionRequest request);

    @PostMapping("/write/entity-link")
    XLink createEntityLink(@RequestBody CreateLinkRequest request);

    @DeleteMapping("/write/entity-link")
    void deleteEntityLink(@RequestParam("apiKey") String apiKey);

    @PostMapping("/write/check-rule")
    XCheckRule createCheckRule(@RequestBody CreateCheckRuleRequest request);

    @PutMapping("/write/check-rule")
    XCheckRule updateCheckRule(@RequestBody UpdateCheckRuleRequest request);
}
