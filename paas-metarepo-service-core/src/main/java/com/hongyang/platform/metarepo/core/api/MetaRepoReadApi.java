package com.hongyang.platform.metarepo.core.api;

import com.hongyang.platform.metarepo.core.model.metamodel.XCheckRule;
import com.hongyang.platform.metarepo.core.model.metamodel.XItem;
import com.hongyang.platform.metarepo.core.model.metamodel.XLink;
import com.hongyang.platform.metarepo.core.model.metamodel.XMetaModel;
import com.hongyang.platform.metarepo.core.model.metamodel.XPickOption;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

/**
 * MetaRepo 读接口定义（API 契约）
 */
public interface MetaRepoReadApi {

    @GetMapping("/metarepo/read/metamodel")
    XMetaModel getMetaModel(@RequestParam("tenantId") Long tenantId,
                             @RequestParam("objectApiKey") String objectApiKey);

    @PostMapping("/metarepo/read/metamodel/batch")
    List<XMetaModel> batchGetMetaModel(@RequestParam("tenantId") Long tenantId,
                                        @RequestBody List<String> apiKeys);

    @GetMapping("/metarepo/read/metamodel/list")
    List<XMetaModel> listMetaModels(@RequestParam("tenantId") Long tenantId);

    @GetMapping("/metarepo/read/items")
    List<XItem> listItems(@RequestParam("tenantId") Long tenantId,
                           @RequestParam("entityId") Long entityId);

    @GetMapping("/metarepo/read/field-meta")
    List<XItem> getFieldMeta(@RequestParam("tenantId") Long tenantId,
                              @RequestParam("entityId") Long entityId);

    @GetMapping("/metarepo/read/pick-options")
    List<XPickOption> listPickOptions(@RequestParam("tenantId") Long tenantId,
                                       @RequestParam("itemId") Long itemId);

    @GetMapping("/metarepo/read/check-rules")
    List<XCheckRule> listCheckRules(@RequestParam("tenantId") Long tenantId,
                                     @RequestParam("entityId") Long entityId);

    @GetMapping("/metarepo/read/entity-links")
    List<XLink> listEntityLinks(@RequestParam("tenantId") Long tenantId,
                                 @RequestParam("entityId") Long entityId);
}
