package com.eams.system.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.eams.common.api.PageResult;
import com.eams.system.entity.SysUser;
import com.eams.system.mapper.SysUserMapper;
import com.eams.system.service.SysUserService;
import org.springframework.stereotype.Service;

/**
 * 用户服务实现
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    @Override
    public PageResult<SysUser> pageQuery(Long pageNum, Long pageSize){
        //构建分页对象
        Page<SysUser> page = new Page<>(pageNum, pageSize);
        //执行分页查询
        this.page(page);
        //返回分页结果
        return PageResult.of(page.getCurrent(), page.getSize(), page.getTotal(), page.getRecords());
    }
}
