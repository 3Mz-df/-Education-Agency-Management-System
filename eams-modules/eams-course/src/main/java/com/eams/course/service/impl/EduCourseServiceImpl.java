package com.eams.course.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.eams.common.enums.ResultCode;
import com.eams.common.exception.BizException;
import com.eams.course.dto.CourseAddDTO;
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
    // 添加课程
    @Override
    public Long addCourse(CourseAddDTO courseAddDTO) {
        //创建课程对象 保存到数据库
        EduCourse course = new EduCourse();
        //请求参数对象的数据复制到实体对象 逻辑搞清楚
        course.setCourseName(courseAddDTO.getCourseName());
        course.setDescription(courseAddDTO.getDescription());
        //新课程默认保存为草稿
        course.setStatus(0);
        //新增方法
        save(course);
        //数据库自动生成的id
        return course.getId();
    }

    // 查询获取课程详情
    @Override
    public EduCourse getCourseDetail(Long id){
        EduCourse course = getById(id);
        if (course == null){
            throw new BizException(ResultCode.NOT_FOUND,"课程不存在");
        }
        else {
            return course;
        }
    }
}
