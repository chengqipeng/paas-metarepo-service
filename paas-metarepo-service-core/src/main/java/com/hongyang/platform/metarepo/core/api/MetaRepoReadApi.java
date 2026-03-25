package com.hongyang.platform.metarepo.core.api;

import com.hongyang.platform.metarepo.core.model.metamodel.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * MetaRepo 读接口定义（API 契约，保留在 core 中）
 */
public interface MetaRepoReadApi {

    @GetMapping("/rpc/metarepo/read/metamodel")
    XMetaModel getMetaModel(@RequestParam("tenantId") Long tenantId,
                             @RequestParam("objectApiKey") String objectApiKey);

    @PostMapping("/rpc/metarepo/read/metamodel/batch")
    List<XMetaModel> batchGetMetaModel(@RequestParam("tenantId") Long tenantId,
                                        @RequestBody List<String> apiKeys);

    @GetMapping("/rpc/metarepo/read/metamodel/list")
    List<XMetaModel> listMetaModels(@RequestParam("tenantId") Long tenantId);

    @GetMapping("/rpc/metarepo/read/items")
    List<XItem> listItems(@RequestParam("tenantId") Long tenantId,
                           @RequestParam("entityId") Long entityId);

    @GetMapping("/rpc/metarepo/read/pick-options")
    List<XPickOption> listPickOptions(@RequestParam("tenantId") Long tenantId,
                                       @RequestParam("itemId") Long itemId);

    @GetMapping("/rpc/metarepo/read/check-rules")
    List<XCheckRule> listCheckRules(@RequestParam("tenantId") Long tenantId,
                                     @RequestParam("entityId") Long entityId);

    @GetMapping("/rpc/metarepo/read/entity-links")
    List<XLink> listEntityLinks(@RequestParam("tenantId") Long tenantId,
                                 @RequestParam("entityId") Long entityId);
}
