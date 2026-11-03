package com.eams.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.eams.common.api.PageResult;
import com.eams.system.entity.SysUser;

/**
 * 用户服务接口 继承IService基础增删改查
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 分页查询用户
     */
    PageResult<SysUser> pageQuery(Long pageNum, Long pageSize);
}
