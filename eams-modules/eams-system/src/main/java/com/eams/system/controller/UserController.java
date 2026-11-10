package com.eams.system.controller;

import com.eams.common.api.PageResult;
import com.eams.common.api.Result;
import com.eams.system.dto.LoginDTO;
import com.eams.system.entity.SysUser;
import com.eams.system.service.SysUserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;


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
    // 查单个用户：id 写在 URL 里，比如 GET /system/user/3
    @GetMapping("/{id}")
    public Result<SysUser> get(@PathVariable Long id){
        return Result.success(sysUserService.getById(id));
    }

    //新增用户
    @PostMapping
    public Result<Long> add(@Valid @RequestBody SysUser user){
        sysUserService.save(user);
        return Result.success(user.getId());
    }

    //修改用户
    @PutMapping
    public Result<Void> update(@Valid @RequestBody SysUser user){
        sysUserService.updateById(user);
        return Result.success();
    }

    //删除用户
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id){
        sysUserService.removeById(id);
        return Result.success();
    }

    // 登录
    @PostMapping("/login")
    public Result<String> login(@Valid @RequestBody LoginDTO loginDTO) {
        return Result.success(sysUserService.login(loginDTO));
    }
}
