package com.eams.course.controller;

import com.eams.common.api.Result;
import com.eams.course.dto.CourseAddDTO;
import com.eams.course.entity.EduCourse;
import com.eams.course.service.EduCourseService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

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

    //新增课程
    @PostMapping
    public Result<Long> add(@Valid @RequestBody CourseAddDTO courseAddDTO){
        //拿到新课程ID
        Long courseId = eduCourseService.addCourse(courseAddDTO);
        return Result.success(courseId);
    }

    @GetMapping("/{id}")
    public Result<EduCourse> detail(@PathVariable("id") Long id){
        EduCourse course = eduCourseService.getCourseDetail(id);
        return Result.success(course);
    }
}
