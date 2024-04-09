package com.xuecheng.content.service;

import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.model.dto.AddCourseDto;
import com.xuecheng.content.model.dto.CourseBaseInfoDto;
import com.xuecheng.content.model.dto.EditCourseDto;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;

/**
 * @author VectorX
 * @version V1.0
 * @description 课程基本信息管理业务接口
 * @date 2024-04-08 10:17:50
 */
public interface CourseBaseInfoService
{

    /**
     * 修改课程信息
     *
     * @param companyId
     * @param dto
     * @return {@link CourseBaseInfoDto}
     */
    CourseBaseInfoDto updateCourseBase(Long companyId, EditCourseDto dto);

    /**
     * 添加课程基本信息
     *
     * @param companyId    教学机构id
     * @param addCourseDto 课程基本信息
     * @return com.xuecheng.content.model.dto.CourseBaseInfoDto
     */
    CourseBaseInfoDto createCourseBase(Long companyId, AddCourseDto addCourseDto);

    /**
     * 根据课程id查询课程基本信息，包括基本信息和营销信息
     *
     * @param courseId 课程id
     * @return {@link CourseBaseInfoDto}
     */
    CourseBaseInfoDto getCourseBaseInfo(long courseId);

    /**
     * 课程查询接口
     *
     * @param pageParams           分页条件
     * @param queryCourseParamsDto 查询条件
     * @return com.xuecheng.base.model.PageResult<com.xuecheng.content.model.po.CourseBase>
     */
    PageResult<CourseBase> queryCourseBaseList(PageParams pageParams, QueryCourseParamsDto queryCourseParamsDto);
}
