package com.xuecheng.content.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuecheng.content.model.dto.CourseTeacherDto;
import com.xuecheng.content.model.po.CourseTeacher;

import java.util.List;

/**
 * @author VectorX
 * @version V1.0
 * @description 教师Service
 * @date 2024-04-10 15:15:09
 */
public interface CourseTeacherService extends IService<CourseTeacher>
{
    /**
     * 查询课程下的教师列表
     *
     * @param courseId 课程ID
     * @return {@link List}<{@link CourseTeacher}>
     */
    List<CourseTeacherDto> queryList(String courseId);

    /**
     * 添加教师
     *
     * @param courseTeacherDto
     */
    CourseTeacherDto saveTeacher(CourseTeacherDto courseTeacherDto);

    /**
     * 删除教师
     *
     * @param courseId 课程ID
     * @param teachId  教师ID
     */
    void delTeacher(String courseId, String teachId);
}
