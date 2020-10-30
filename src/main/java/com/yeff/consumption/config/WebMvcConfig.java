package com.yeff.consumption.config;

import com.yeff.consumption.interceptor.ConsumptionInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private ConsumptionInterceptor consumptionInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        // 1.添加ugcContextInterceptor，初始化UGCContext;
        registry.addInterceptor(consumptionInterceptor)
                .addPathPatterns("/consumer/go");

    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/js/");

    }
}
