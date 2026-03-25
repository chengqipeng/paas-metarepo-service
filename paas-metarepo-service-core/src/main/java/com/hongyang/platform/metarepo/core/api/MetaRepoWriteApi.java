package com.hongyang.platform.metarepo.core.api;

import com.hongyang.platform.metarepo.core.model.metamodel.*;
import com.hongyang.platform.metarepo.core.model.request.*;
import org.springframework.web.bind.annotation.*;

/**
 * MetaRepo 写接口定义（API 契约）
 */
public interface MetaRepoWriteApi {

    // ==================== Entity ====================
    @PostMapping("/rpc/metarepo/write/entity")
    XEntity createEntity(@RequestBody CreateEntityRequest request);

    @PutMapping("/rpc/metarepo/write/entity/{entityId}")
    XEntity updateEntity(@PathVariable("entityId") Long entityId, @RequestBody UpdateEntityRequest request);

    @DeleteMapping("/rpc/metarepo/write/entity")
    void deleteEntity(@RequestParam("tenantId") Long tenantId, @RequestParam("entityId") Long entityId);

    // ==================== Item ====================
    @PostMapping("/rpc/metarepo/write/item")
    XItem createItem(@RequestBody CreateItemRequest request);

    @PutMapping("/rpc/metarepo/write/item/{itemId}")
    XItem updateItem(@PathVariable("itemId") Long itemId, @RequestBody UpdateItemRequest request);

    @DeleteMapping("/rpc/metarepo/write/item")
    void deleteItem(@RequestParam("tenantId") Long tenantId, @RequestParam("itemId") Long itemId);

    // ==================== PickOption ====================
    @PostMapping("/rpc/metarepo/write/pick-options")
    void savePickOptions(@RequestBody SavePickOptionRequest request);

    // ==================== EntityLink ====================
    @PostMapping("/rpc/metarepo/write/entity-link")
    XLink createEntityLink(@RequestBody CreateLinkRequest request);

    @DeleteMapping("/rpc/metarepo/write/entity-link")
    void deleteEntityLink(@RequestParam("tenantId") Long tenantId, @RequestParam("linkId") Long linkId);

    // ==================== CheckRule ====================
    @PostMapping("/rpc/metarepo/write/check-rule")
    XCheckRule createCheckRule(@RequestBody CreateCheckRuleRequest request);

    @PutMapping("/rpc/metarepo/write/check-rule/{ruleId}")
    XCheckRule updateCheckRule(@PathVariable("ruleId") Long ruleId, @RequestBody UpdateCheckRuleRequest request);
}
