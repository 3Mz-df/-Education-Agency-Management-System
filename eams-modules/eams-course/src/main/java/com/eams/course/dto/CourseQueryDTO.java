package com.eams.course.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

//课程分页查询参数
@Data
public class CourseQueryDTO {

    //页码 默认第一页
    @NotNull(message = "页码不存在")
    @Min(value = 1 , message = "页码不能小于一")
    private Long pageNum = 1L;

    //每页条数
    @NotNull(message = "每页条数不能为空")
    @Min(value = 1, message = "每页条数不能小于1")
    @Max(value = 100, message = "每页条数不得超过100")
    private Long pageSize = 10L;

    //课程名称关键词 不传就默认按名称筛选
    @Size(max = 100, message = "关键词不得超过100个字符")
    private String keyword;

    //沿用当前课程状态，0草稿 1上架 2下架
    @Min(value = 0, message = "课程状态不确定")
    @Max(value = 2, message = "课程状态不确定")
    private Integer status;
}
