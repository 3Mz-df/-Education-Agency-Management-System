package com.eams.course.controller;

import com.eams.common.api.Result;
import com.eams.course.entity.EduCourse;
import com.eams.course.service.EduCourseService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 课程接口
 */
@RestController
@RequestMapping("/course")
public class CourseController {

    private final EduCourseService eduCourseService;

    //构造方法注入课程业务对象
    public CourseController(EduCourseService eduCourseService){
        this.eduCourseService = eduCourseService;
    }

    //查看课程列表
    @GetMapping("list")
    public Result<List<EduCourse>> list(){
        List<EduCourse> courses = eduCourseService.list();
        return Result.success(courses);
    }
}
