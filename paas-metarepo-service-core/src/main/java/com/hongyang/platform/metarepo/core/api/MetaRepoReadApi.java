package com.hongyang.platform.metarepo.core.api;

import com.hongyang.platform.metarepo.core.model.metamodel.XCheckRule;
import com.hongyang.platform.metarepo.core.model.metamodel.XEntity;
import com.hongyang.platform.metarepo.core.model.metamodel.XItem;
import com.hongyang.platform.metarepo.core.model.metamodel.XLink;
import com.hongyang.platform.metarepo.core.model.metamodel.XPickOption;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * MetaRepo 读接口定义（API 契约）
 * tenantId 由 TenantInterceptor 从 GlobalContext 自动注入，无需传递。
 */
@RequestMapping("/metarepo")
public interface MetaRepoReadApi {

    @GetMapping("/read/entities")
    List<XEntity> listEntities();

    @GetMapping("/read/entity")
    XEntity getEntity(@RequestParam("apiKey") String apiKey);

    @GetMapping("/read/items")
    List<XItem> listItems(@RequestParam("entityApiKey") String entityApiKey);

    @GetMapping("/read/pick-options")
    List<XPickOption> listPickOptions(@RequestParam("itemApiKey") String itemApiKey);

    @GetMapping("/read/check-rules")
    List<XCheckRule> listCheckRules(@RequestParam("entityApiKey") String entityApiKey);

    @GetMapping("/read/entity-links")
    List<XLink> listEntityLinks(@RequestParam("entityApiKey") String entityApiKey);
}
