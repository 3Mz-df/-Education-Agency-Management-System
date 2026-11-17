package com.eams.course.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.eams.course.entity.EduCourse;
import com.eams.course.mapper.EduCourseMapper;
import com.eams.course.service.EduCourseService;
import org.springframework.stereotype.Service;

/**
 * 课程业务实现
 */
@Service
public class EduCourseServiceImpl
        extends ServiceImpl<EduCourseMapper, EduCourse>
        implements EduCourseService{
}
