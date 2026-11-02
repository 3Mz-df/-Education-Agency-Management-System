package com.eams.system.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//测试接口 验证网关路由转发
@RestController
@RequestMapping("/system")
public class HelloController {

    @GetMapping("/hello")
    public String hello(){
        return "hello form eams-system";
    }
}
