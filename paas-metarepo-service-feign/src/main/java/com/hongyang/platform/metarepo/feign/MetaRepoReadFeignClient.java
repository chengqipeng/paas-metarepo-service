package com.hongyang.platform.metarepo.feign;

import com.hongyang.platform.metarepo.core.api.MetaRepoReadApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * MetaRepo 读接口 FeignClient
 * 继承 core 中的 API 契约，由 Spring Cloud OpenFeign 自动代理
 */
@FeignClient(name = "paas-metarepo-service", contextId = "metaRepoRead")
public interface MetaRepoReadFeignClient extends MetaRepoReadApi {
}
