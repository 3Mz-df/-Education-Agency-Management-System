package com.eams.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 系统用户实体
 */
@Data
@TableName("sys_user")
public class SysUser {

    @TableId(type = IdType.AUTO)
    private Long id;

    //用户名限制
    @NotBlank(message = "用户名不能为空")
    @Size(min = 2, max = 20, message = "用户名长度需在2~20个字符之间")
    private String username;

    //昵称限制
    @Size(max = 30, message = "昵称长度不得超过30个字符")
    private String nickname;

    private LocalDateTime createTime;

    //密码 隔离 不返回前端
    @JsonIgnore
    private String password;
}
