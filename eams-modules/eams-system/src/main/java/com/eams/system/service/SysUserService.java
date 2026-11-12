package com.eams.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.eams.common.api.PageResult;
import com.eams.system.dto.LoginDTO;
import com.eams.system.entity.SysUser;

/**
 * 用户服务接口 继承IService基础增删改查
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 分页查询用户
     */
    PageResult<SysUser> pageQuery(Long pageNum, Long pageSize, String keyword, Integer userTpye, Integer status);

    /**
     * 登录：校验用户名密码，成功返回 token
     */
    String login(LoginDTO loginDTO);

    /**
     * 新增用户（密码加密后入库）
     */
    void addUser(SysUser user);

    /**
     * 更新用户（更新的密码也加密后入库）
     */
    void updateUser(SysUser user);
}
