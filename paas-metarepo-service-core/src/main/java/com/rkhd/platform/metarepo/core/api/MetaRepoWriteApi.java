package com.rkhd.platform.metarepo.core.api;

import com.rkhd.platform.metarepo.core.model.Result;
import com.rkhd.platform.metarepo.core.model.dto.*;
import com.rkhd.platform.metarepo.core.model.req.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * MetaRepo 写接口定义（API 契约，保留在 core 中）
 */
public interface MetaRepoWriteApi {

    @PostMapping("/rpc/metarepo/write/entity")
    Result<EntityDTO> createEntity(@RequestBody CreateEntityReq req);

    @PutMapping("/rpc/metarepo/write/entity/{entityId}")
    Result<EntityDTO> updateEntity(@PathVariable("entityId") Long entityId, @RequestBody CreateEntityReq req);

    @DeleteMapping("/rpc/metarepo/write/entity")
    Result<Void> deleteEntity(@RequestParam("tenantId") Long tenantId, @RequestParam("entityId") Long entityId);

    @PostMapping("/rpc/metarepo/write/item")
    Result<ItemDTO> createItem(@RequestBody CreateItemReq req);

    @DeleteMapping("/rpc/metarepo/write/item")
    Result<Void> deleteItem(@RequestParam("tenantId") Long tenantId, @RequestParam("itemId") Long itemId);

    @PostMapping("/rpc/metarepo/write/entity-link")
    Result<LinkDTO> createEntityLink(@RequestBody CreateLinkReq req);

    @DeleteMapping("/rpc/metarepo/write/entity-link")
    Result<Void> deleteEntityLink(@RequestParam("tenantId") Long tenantId, @RequestParam("linkId") Long linkId);
}
