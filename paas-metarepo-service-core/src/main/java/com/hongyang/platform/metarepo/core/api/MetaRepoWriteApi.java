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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * MetaRepo 写接口定义（API 契约）
 */
public interface MetaRepoWriteApi {

    // ==================== Entity ====================
    @PostMapping("/metarepo/write/entity")
    XEntity createEntity(@RequestBody CreateEntityRequest request);

    @PutMapping("/metarepo/write/entity/{entityId}")
    XEntity updateEntity(@PathVariable("entityId") Long entityId, @RequestBody UpdateEntityRequest request);

    @DeleteMapping("/metarepo/write/entity")
    void deleteEntity(@RequestParam("tenantId") Long tenantId, @RequestParam("entityId") Long entityId);

    // ==================== Item ====================
    @PostMapping("/metarepo/write/item")
    XItem createItem(@RequestBody CreateItemRequest request);

    @PutMapping("/metarepo/write/item/{itemId}")
    XItem updateItem(@PathVariable("itemId") Long itemId, @RequestBody UpdateItemRequest request);

    @DeleteMapping("/metarepo/write/item")
    void deleteItem(@RequestParam("tenantId") Long tenantId, @RequestParam("itemId") Long itemId);

    // ==================== PickOption ====================
    @PostMapping("/metarepo/write/pick-options")
    void savePickOptions(@RequestBody SavePickOptionRequest request);

    // ==================== EntityLink ====================
    @PostMapping("/metarepo/write/entity-link")
    XLink createEntityLink(@RequestBody CreateLinkRequest request);

    @DeleteMapping("/metarepo/write/entity-link")
    void deleteEntityLink(@RequestParam("tenantId") Long tenantId, @RequestParam("linkId") Long linkId);

    // ==================== CheckRule ====================
    @PostMapping("/metarepo/write/check-rule")
    XCheckRule createCheckRule(@RequestBody CreateCheckRuleRequest request);

    @PutMapping("/metarepo/write/check-rule/{ruleId}")
    XCheckRule updateCheckRule(@PathVariable("ruleId") Long ruleId, @RequestBody UpdateCheckRuleRequest request);
}
