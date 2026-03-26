package com.hongyang.platform.metarepo.service.config;

import com.hongyang.framework.feign.interceptor.GlobalContextAssignInterceptorAdapter;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new GlobalContextAssignInterceptorAdapter()).addPathPatterns("/**");
    }
}
