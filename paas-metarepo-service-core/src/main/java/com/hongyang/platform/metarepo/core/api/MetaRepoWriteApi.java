package com.hongyang.platform.metarepo.core.api;

import com.hongyang.platform.metarepo.core.model.metamodel.*;
import com.hongyang.platform.metarepo.core.model.request.*;
import org.springframework.web.bind.annotation.*;

/**
 * MetaRepo 写接口定义（API 契约，保留在 core 中）
 */
public interface MetaRepoWriteApi {

    @PostMapping("/rpc/metarepo/write/entity")
    XEntity createEntity(@RequestBody CreateEntityRequest request);

    @PutMapping("/rpc/metarepo/write/entity/{entityId}")
    XEntity updateEntity(@PathVariable("entityId") Long entityId, @RequestBody CreateEntityRequest request);

    @DeleteMapping("/rpc/metarepo/write/entity")
    void deleteEntity(@RequestParam("tenantId") Long tenantId, @RequestParam("entityId") Long entityId);

    @PostMapping("/rpc/metarepo/write/item")
    XItem createItem(@RequestBody CreateItemRequest request);

    @DeleteMapping("/rpc/metarepo/write/item")
    void deleteItem(@RequestParam("tenantId") Long tenantId, @RequestParam("itemId") Long itemId);

    @PostMapping("/rpc/metarepo/write/entity-link")
    XLink createEntityLink(@RequestBody CreateLinkRequest request);

    @DeleteMapping("/rpc/metarepo/write/entity-link")
    void deleteEntityLink(@RequestParam("tenantId") Long tenantId, @RequestParam("linkId") Long linkId);
}
