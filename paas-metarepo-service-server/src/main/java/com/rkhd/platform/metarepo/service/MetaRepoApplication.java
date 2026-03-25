package com.rkhd.platform.metarepo.service;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan("com.rkhd.platform.metarepo.service.mapper")
public class MetaRepoApplication {
    public static void main(String[] args) {
        SpringApplication.run(MetaRepoApplication.class, args);
    }
}
