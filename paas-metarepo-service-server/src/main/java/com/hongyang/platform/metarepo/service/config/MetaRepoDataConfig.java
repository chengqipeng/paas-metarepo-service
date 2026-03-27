package com.hongyang.platform.metarepo.service.config;

import com.hongyang.framework.dao.dynamic.DynamicTableNameInterceptor;
import com.hongyang.framework.dao.tenant.TenantInterceptor;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * MetaRepo 数据路由配置。
 * <p>
 * aPaaS 元数据采用双库架构：
 * - Common 库（paas_metarepo_common）：平台级元模型定义（p_meta_*）和 Common 级业务元数据（p_common_*）
 * - Tenant 库（paas_metarepo）：租户级业务元数据（p_tenant_*）和运行时数据
 * <p>
 * 应用数据源连接 Tenant 库，Common 表通过 MySQL 跨库查询访问。
 * 本配置类在启动时向框架层注册：
 * 1. 表名路由规则：p_meta_* / p_common_* → commonDatabase.tableName
 * 2. 租户忽略规则：p_meta_* / p_common_* 表不加 tenant_id 条件
 */
@Slf4j
@Configuration
public class MetaRepoDataConfig {

    @Value("${metarepo.common-database:}")
    private String commonDatabase;

    @PostConstruct
    public void init() {
        // 注册租户忽略规则：Common 级表和元模型定义表不加 tenant_id
        TenantInterceptor.addIgnorePredicate(tableName ->
                tableName.startsWith("p_meta_") || tableName.startsWith("p_common_"));

        // 注册跨库路由：Common 级表自动加数据库前缀
        if (commonDatabase != null && !commonDatabase.isEmpty()) {
            DynamicTableNameInterceptor.registerGlobalMapper(tableName -> {
                if (tableName.startsWith("p_meta_") || tableName.startsWith("p_common_")) {
                    return commonDatabase + "." + tableName;
                }
                return tableName;
            });
            log.info("[MetaRepo] Common 库跨库路由已启用: {} → {}.tableName",
                    "p_meta_*/p_common_*", commonDatabase);
        } else {
            log.info("[MetaRepo] Common 库跨库路由未启用（单库模式）");
        }
    }
}
