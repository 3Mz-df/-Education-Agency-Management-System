package com.eams.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
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

    //真实姓名
    @Size(max = 50, message = "真实姓名长度不得超过50个字符")
    private String realName;

    //用户类型
    private Integer userType;

    //所属机构
    private Long institutionId;

    //手机号
    @Size(max = 20, message = "手机号格式错误")
    private String phone;

    //邮箱
    @Email(message = "邮箱格式错误")
    @Size(max = 100, message = "邮箱字符长度错误")
    private String email;

    //头像地址
    private String avatar;

    //状态（1-启用 0-禁用）
    private Integer status;

    //创建时间
    private LocalDateTime createTime;

    //密码：只进不出（前端能传进来，但不返回给前端）
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
}
