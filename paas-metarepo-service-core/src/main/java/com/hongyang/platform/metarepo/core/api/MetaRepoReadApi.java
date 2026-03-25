package com.hongyang.platform.metarepo.core.api;

import com.hongyang.platform.metarepo.core.model.Result;
import com.hongyang.platform.metarepo.core.model.dto.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * MetaRepo 读接口定义（API 契约，保留在 core 中）
 */
public interface MetaRepoReadApi {

    @GetMapping("/rpc/metarepo/read/metamodel")
    Result<MetaModelDTO> getMetaModel(@RequestParam("tenantId") Long tenantId,
                                       @RequestParam("objectApiKey") String objectApiKey);

    @PostMapping("/rpc/metarepo/read/metamodel/batch")
    Result<List<MetaModelDTO>> batchGetMetaModel(@RequestParam("tenantId") Long tenantId,
                                                  @RequestBody List<String> apiKeys);

    @GetMapping("/rpc/metarepo/read/metamodel/list")
    Result<List<MetaModelDTO>> listMetaModels(@RequestParam("tenantId") Long tenantId);

    @GetMapping("/rpc/metarepo/read/items")
    Result<List<ItemDTO>> listItems(@RequestParam("tenantId") Long tenantId,
                                     @RequestParam("entityId") Long entityId);

    @GetMapping("/rpc/metarepo/read/pick-options")
    Result<List<PickOptionDTO>> listPickOptions(@RequestParam("tenantId") Long tenantId,
                                                 @RequestParam("itemId") Long itemId);

    @GetMapping("/rpc/metarepo/read/check-rules")
    Result<List<CheckRuleDTO>> listCheckRules(@RequestParam("tenantId") Long tenantId,
                                               @RequestParam("entityId") Long entityId);

    @GetMapping("/rpc/metarepo/read/entity-links")
    Result<List<LinkDTO>> listEntityLinks(@RequestParam("tenantId") Long tenantId,
                                           @RequestParam("entityId") Long entityId);
}
