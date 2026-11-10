package com.eams.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.eams.common.api.PageResult;
import com.eams.common.enums.ResultCode;
import com.eams.common.exception.BizException;
import com.eams.system.dto.LoginDTO;
import com.eams.system.entity.SysUser;
import com.eams.system.mapper.SysUserMapper;
import com.eams.system.service.SysUserService;
import com.eams.system.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 用户服务实现
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    private final PasswordEncoder passwordEncoder;

    //构造器注入 让Spring传入 BCryptPasswordEncoder
    public SysUserServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public PageResult<SysUser> pageQuery(Long pageNum, Long pageSize) {
        //构建分页对象
        Page<SysUser> page = new Page<>(pageNum, pageSize);
        //执行分页查询
        this.page(page);
        //返回分页结果
        return PageResult.of(page.getCurrent(), page.getSize(), page.getTotal(), page.getRecords());
    }

    @Override
    public String login(LoginDTO loginDTO) {
        //根据用户名查用户
        SysUser user = this.getOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, loginDTO.getUsername()), false);
        //用户不存在
        if (user == null || !passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new BizException(ResultCode.UNAUTHORIZED, "用户名或密码错误");
         }

        //登陆成功
        return JwtUtil.generateToken(user.getId(), user.getUsername());
        }
}
