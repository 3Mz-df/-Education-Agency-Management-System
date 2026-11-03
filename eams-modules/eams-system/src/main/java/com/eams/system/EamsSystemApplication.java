package com.eams.system;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.eams.system.mapper")
public class EamsSystemApplication {

    public static void main(String[] args){
        SpringApplication.run(EamsSystemApplication.class, args);
    }
}
