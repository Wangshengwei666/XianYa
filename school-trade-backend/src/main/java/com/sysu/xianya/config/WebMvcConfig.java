package com.sysu.xianya.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 对所有后端接口路径生效
                .allowedOrigins("http://localhost:8081") // 【关键】明确允许来自前端开发服务器的请求
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允许所有常用方法
                .allowCredentials(true) // 允许前端携带cookie
                .allowedHeaders("*") // 允许所有请求头
                .maxAge(3600); // 预检请求的有效时间
    }
}