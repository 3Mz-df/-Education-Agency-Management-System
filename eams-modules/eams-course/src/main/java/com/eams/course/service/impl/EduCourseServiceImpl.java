package com.eams.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.eams.common.api.PageResult;
import com.eams.common.enums.ResultCode;
import com.eams.common.exception.BizException;
import com.eams.course.dto.CourseAddDTO;
import com.eams.course.dto.CourseQueryDTO;
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

    //分页查询课程
    @Override
    public PageResult<EduCourse> pageQuery(CourseQueryDTO queryDTO){
        //创建分页对象 设置页码和每页条数
        Page<EduCourse> page1 = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());

        //创建查询条件清单
        LambdaQueryWrapper<EduCourse> wrapper = new LambdaQueryWrapper<>();
        //传入关键词时根据课程名称模糊查询
        String keyword = queryDTO.getKeyword();
        if(keyword != null && !keyword.isBlank()){
            wrapper.like(EduCourse::getCourseName, keyword.trim());
        }
        //传入状态时按状态精确筛选
        if (queryDTO.getStatus() != null){
            wrapper.eq(EduCourse::getStatus, queryDTO.getStatus());
        }
        //按课程id倒叙排列（新课程在前面）
        wrapper.orderByDesc(EduCourse::getId);

        //执行分页查询
        this.page(page1, wrapper);

        return PageResult.of(
                page1.getCurrent(),
                page1.getSize(),
                page1.getTotal(),
                page1.getRecords()
        );
    }
}
