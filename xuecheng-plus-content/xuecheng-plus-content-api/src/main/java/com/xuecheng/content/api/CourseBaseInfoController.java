package com.xuecheng.content.api;

import com.xuecheng.base.exception.ValidationGroups;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.model.dto.AddCourseDto;
import com.xuecheng.content.model.dto.CourseBaseInfoDto;
import com.xuecheng.content.model.dto.EditCourseDto;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;
import com.xuecheng.content.service.CourseBaseInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author VectorX
 * @version V1.0
 * @description 课程信息编辑接口
 * @date 2024-04-07 17:34:01
 */
@Api(value = "课程信息编辑接口",
     tags = "课程信息编辑接口")
@RestController
public class CourseBaseInfoController
{
    /**
     * 机构id，由于认证系统没有上线暂时硬编码
     */
    private static final long COMPANY_ID = 1232141425L;
    @Autowired
    private CourseBaseInfoService courseBaseInfoService;

    @ApiOperation("修改课程基础信息")
    @PutMapping("/course")
    public CourseBaseInfoDto modifyCourseBase(
            @RequestBody
            @Validated
                    EditCourseDto editCourseDto) {
        return courseBaseInfoService.updateCourseBase(COMPANY_ID, editCourseDto);
    }

    @ApiOperation("根据课程id查询课程基础信息")
    @GetMapping("/course/{courseId}")
    public CourseBaseInfoDto getCourseBaseById(
            @PathVariable
                    Long courseId) {
        return courseBaseInfoService.getCourseBaseInfo(courseId);
    }

    @ApiOperation("新增课程基础信息")
    @PostMapping("/course")
    public CourseBaseInfoDto createCourseBase(
            @RequestBody
            @Validated({ValidationGroups.Insert.class})
                    AddCourseDto addCourseDto) {
        return courseBaseInfoService.createCourseBase(COMPANY_ID, addCourseDto);
    }

    @ApiOperation("课程查询接口")
    @PostMapping("/course/list")
    public PageResult<CourseBase> list(
            @ApiParam(value = "分页条件",
                      required = true)
                    PageParams pageParams,

            @ApiParam(value = "查询条件",
                      required = false)
            @RequestBody(required = false)
                    QueryCourseParamsDto queryCourseParams) {
        return courseBaseInfoService.queryCourseBaseList(pageParams, queryCourseParams);
    }
}
