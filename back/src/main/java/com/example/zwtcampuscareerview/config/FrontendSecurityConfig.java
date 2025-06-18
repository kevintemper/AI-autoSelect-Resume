package com.example.zwtcampuscareerview.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class FrontendSecurityConfig {

    @Bean
    public SecurityFilterChain frontendSecurityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable() // 禁用 CSRF，开发环境使用
                .cors(cors -> cors.configurationSource(request -> {
                    var config = new org.springframework.web.cors.CorsConfiguration();
                    config.addAllowedOrigin("http://localhost:8080"); // 允许来自前端的请求
                    config.addAllowedMethod("*"); // 允许所有HTTP方法
                    config.addAllowedHeader("*"); // 允许所有请求头
                    config.setAllowCredentials(true); // 允许跨域发送Cookie
                    return config;
                }))
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // 针对所有请求放行
                );
        return http.build();
    }
}

