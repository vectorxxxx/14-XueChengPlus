package com.xuecheng.content.model.dto;

import lombok.Data;
import lombok.ToString;

/**
 * <p>
 * 课程-教师关系表
 * </p>
 *
 * @author itcast
 */
@Data
@ToString
public class CourseTeacherDto
{

    private Long id;

    /**
     * 课程标识
     */
    private Long courseId;

    /**
     * 教师标识
     */
    private String teacherName;

    /**
     * 教师职位
     */
    private String position;

    /**
     * 教师简介
     */
    private String introduction;

    /**
     * 照片
     */
    private String photograph;
}
