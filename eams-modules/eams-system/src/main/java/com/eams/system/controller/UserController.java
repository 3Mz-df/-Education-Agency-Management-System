package com.eams.system.controller;

import com.eams.system.entity.SysUser;
import com.eams.system.mapper.SysUserMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 检验数据库连通
 */
@RestController
@RequestMapping("/system/user")
public class UserController {
    
    private final SysUserMapper sysUserMapper;
    
    public UserController(SysUserMapper sysUserMapper){
        this.sysUserMapper = sysUserMapper;
    }
    
    @GetMapping("/list")
    public List<SysUser> list(){
        return sysUserMapper.selectList(null);
    }
}
