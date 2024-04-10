package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.content.mapper.CourseBaseMapper;
import com.xuecheng.content.mapper.CourseTeacherMapper;
import com.xuecheng.content.model.dto.CourseTeacherDto;
import com.xuecheng.content.model.po.CourseTeacher;
import com.xuecheng.content.service.CourseTeacherService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author VectorX
 * @version V1.0
 * @description 教师Service实现
 * @date 2024-04-10 15:15:36
 */
@Service
public class CourseTeacherServiceImpl extends ServiceImpl<CourseTeacherMapper, CourseTeacher> implements CourseTeacherService
{
    @Autowired
    private CourseBaseMapper courseBaseMapper;

    /**
     * 查询课程下的教师列表
     *
     * @param courseId 课程ID
     * @return {@link List}<{@link CourseTeacher}>
     */
    @Override
    public List<CourseTeacherDto> queryList(String courseId) {
        return baseMapper
                .selectList(new LambdaQueryWrapper<CourseTeacher>().eq(CourseTeacher::getCourseId, courseId))
                .stream()
                .map(courseTeacher -> {
                    final CourseTeacherDto courseTeacherDto = new CourseTeacherDto();
                    BeanUtils.copyProperties(courseTeacher, courseTeacherDto);
                    return courseTeacherDto;
                })
                .collect(Collectors.toList());
    }

    /**
     * 添加教师
     *
     * @param courseTeacherDto
     */
    @Override
    public CourseTeacherDto saveTeacher(CourseTeacherDto courseTeacherDto) {
        // 只允许向机构自己的课程中添加老师、删除老师。
        // 机构id统一使用：1232141425L
        final Long companyId = courseBaseMapper
                .selectById(courseTeacherDto.getCourseId())
                .getCompanyId();
        if (companyId != 1232141425L) {
            XueChengPlusException.cast("该课程不是本机构的课程，不允许添加/修改老师");
        }

        // 修改
        if (courseTeacherDto.getId() != null) {
            final CourseTeacher courseTeacher = new CourseTeacher();
            BeanUtils.copyProperties(courseTeacherDto, courseTeacher);
            baseMapper.updateById(courseTeacher);
            return getCourseTeacher(courseTeacherDto.getId());
        }
        // 新增
        else {
            final CourseTeacher courseTeacher = new CourseTeacher();
            BeanUtils.copyProperties(courseTeacherDto, courseTeacher);
            courseTeacher.setCreateDate(LocalDateTime.now());
            baseMapper.insert(courseTeacher);
            return getCourseTeacher(courseTeacher.getId());
        }
    }

    private CourseTeacherDto getCourseTeacher(Long id) {
        final CourseTeacher courseTeacher = baseMapper.selectById(id);
        final CourseTeacherDto courseTeacherDto = new CourseTeacherDto();
        BeanUtils.copyProperties(courseTeacher, courseTeacherDto);
        return courseTeacherDto;
    }

    @Override
    public void delTeacher(String courseId, String teachId) {
        // 只允许向机构自己的课程中添加老师、删除老师。
        // 机构id统一使用：1232141425L
        final Long companyId = courseBaseMapper
                .selectById(courseId)
                .getCompanyId();
        if (companyId != 1232141425L) {
            XueChengPlusException.cast("该课程不是本机构的课程，不允许删除老师");
        }

        // 删除教师
        baseMapper.delete(new LambdaQueryWrapper<CourseTeacher>()
                .eq(CourseTeacher::getCourseId, courseId)
                .eq(CourseTeacher::getId, teachId));
    }
}
