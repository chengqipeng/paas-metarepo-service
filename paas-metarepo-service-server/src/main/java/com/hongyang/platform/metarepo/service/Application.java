package com.hongyang.platform.metarepo.service;

import com.hongyang.framework.dao.config.DaoAutoConfiguration;
import com.hongyang.framework.base.utils.AppStartupHelper;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisReactiveAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;

@SpringBootApplication(exclude = {RedisAutoConfiguration.class, RedisReactiveAutoConfiguration.class})
@EnableDiscoveryClient
@EnableFeignClients
@Import(DaoAutoConfiguration.class)
public class Application {
    public static void main(String[] args) {
        AppStartupHelper.run(Application.class, args);
    }
}
