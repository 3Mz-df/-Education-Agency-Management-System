package com.eams.course;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.eams")
@MapperScan("com.eams.course.mapper")
public class EamsCourseApplication {

    public static void main(String[] args){
        //启动课程服务
        SpringApplication.run(EamsCourseApplication.class, args);
    }
}
