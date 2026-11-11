package com.eams.system.config;

import com.eams.system.interceptor.JwtInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web 配置：注册 JWT 拦截器，并配置哪些路径需要登录、哪些放行
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final JwtInterceptor jwtInterceptor;

    public WebMvcConfig(JwtInterceptor jwtInterceptor) {
        this.jwtInterceptor = jwtInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**")           // 拦所有请求
                .excludePathPatterns(             // 但这些路径放行（免登录）
                        "/system/user/login",     // 登录接口本身（否则没法登录）
                        "/doc.html",              // Knife4j 文档页面
                        "/webjars/**",            // Knife4j 静态资源
                        "/v3/api-docs/**",        // 接口文档 JSON
                        "/swagger-ui/**",         // swagger-ui 页面
                        "/favicon.ico",
                        "/error"
                );
    }
}
