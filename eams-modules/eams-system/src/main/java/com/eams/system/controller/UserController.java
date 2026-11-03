package com.eams.system.controller;

import com.eams.common.api.PageResult;
import com.eams.common.api.Result;
import com.eams.system.entity.SysUser;
import com.eams.system.mapper.SysUserMapper;
import com.eams.system.service.SysUserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

///**
// * 检验数据库连通
// */
//@RestController
//@RequestMapping("/system/user")
//public class UserController {
//
//    private final SysUserMapper sysUserMapper;
//
//    public UserController(SysUserMapper sysUserMapper){
//        this.sysUserMapper = sysUserMapper;
//    }
//
//    @GetMapping("/list")
//    public List<SysUser> list(){
//        return sysUserMapper.selectList(null);
//    }
//}

/**
 * 用户接口
 */
@RestController
@RequestMapping("/system/user")
public class UserController{

    private final SysUserService sysUserService;

    public UserController(SysUserService sysUserService){
        this.sysUserService = sysUserService;
    }

    @GetMapping("/list")
    public Result<PageResult<SysUser>> list(
            @RequestParam(defaultValue = "1") Long pageNum,
            @RequestParam(defaultValue = "10") Long pageSize){
        return Result.success(sysUserService.pageQuery(pageNum, pageSize));
    }

    //新增用户
    @PostMapping
    public Result<Void> add(@RequestBody SysUser user){
        sysUserService.save(user);
        return Result.success();
    }

    //修改用户
    @PutMapping
    public Result<Void> update(@RequestBody SysUser user){
        sysUserService.updateById(user);
        return Result.success();
    }

    //删除用户
    @DeleteMapping
    public Result<Void> delete(@PathVariable Long id){
        sysUserService.removeById(id);
        return Result.success();
    }
}
