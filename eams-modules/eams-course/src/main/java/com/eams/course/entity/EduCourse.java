package com.eams.course.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 课程实体
 */
@Data
// 生成 set get 方法
@TableName("edu_course")
public class EduCourse {

    //课程ID
    @TableId(type = IdType.AUTO)
    private Long id;

    //课程名称
    private String courseName; //course.getCourseName();

    //课程简介
    private String description; //course.getDescription();

    //课程状态 0草稿 1上架 2下架
    private Integer status; //course.getStatus();

    //创建时间
    private LocalDateTime createTime; //course.getCreateTime();

    //更新时间
    private LocalDateTime updateTime; //course.getUpdateTime();

}
