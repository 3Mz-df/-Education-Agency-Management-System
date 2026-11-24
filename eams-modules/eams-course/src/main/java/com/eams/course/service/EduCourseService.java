package com.eams.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.eams.course.dto.CourseAddDTO;
import com.eams.course.entity.EduCourse;

/**
 * 课程业务接口
 */
public interface EduCourseService extends IService<EduCourse> {

    //新增课程
    Long addCourse(CourseAddDTO courseAddDTO);

    //根据id查询课程详情
    EduCourse getCourseDetail(Long id);
}

