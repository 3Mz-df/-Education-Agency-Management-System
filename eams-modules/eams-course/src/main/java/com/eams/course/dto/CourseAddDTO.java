package com.eams.course.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 新增课程的请求参数
 */
@Data
public class CourseAddDTO {
    //课程名称
    @NotBlank(message = "课程名称不能为空")
    @Size(max = 100, message = "课程名称不能超过100个字符")
    private String courseName;

    //课程简介
    @Size(max = 1000, message = "课程简介不能超过1000个字符")
    private String description;
}
