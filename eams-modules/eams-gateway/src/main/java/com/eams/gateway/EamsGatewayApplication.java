package com.eams.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 网关服务启动类 由这转发给下游服务
 */
@SpringBootApplication
public class EamsGatewayApplication {
    public static void main(String[] args){
        SpringApplication.run(EamsGatewayApplication.class, args);
    }
}
