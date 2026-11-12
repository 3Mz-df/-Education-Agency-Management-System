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
    public PageResult<SysUser> pageQuery(Long pageNum, Long pageSize, String keyword, Integer userType, Integer status) {
        //构建分页对象
        Page<SysUser> page = new Page<>(pageNum, pageSize);
        //构建条件构造器
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        //关键词模糊匹配（手机号，用户名）
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(SysUser::getUsername, keyword)
                    .or().like(SysUser::getPhone, keyword));
        }
        //(用户类型) 传了才筛选
        if (userType != null){
            wrapper.eq(SysUser::getUserType, userType);
        }
        //(状态) 传了才筛选
        if (status != null){
            wrapper.eq(SysUser::getStatus, status);
        }
        //执行分页查询 条件清单一并上交
        this.page(page, wrapper);
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

    @Override
    public void addUser(SysUser user) {
        //密码不能为空
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new BizException("密码不能为空");
        }
        //把明文密码加密成密文，再入库
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        this.save(user);
    }

    @Override
    public void updateUser(SysUser user) {
        // 传了密码才加密；没传（null 或空串），让 updateById 自动跳过，保持原密码不变
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            user.setPassword(null);
        }
        this.updateById(user);
    }
}
