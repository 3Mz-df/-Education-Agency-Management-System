package com.eams.system.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Knife4j接口测试文档配置
 */
@Configuration
public class Knife4jConfig {

    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("EAMS 教培管理系统 - 系统服务接口文档")
                        .description("sys_user 用户管理等系统模块接口")
                        .version("1.0.0"));
    }
}
