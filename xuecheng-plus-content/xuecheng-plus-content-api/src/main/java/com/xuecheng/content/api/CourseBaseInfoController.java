package com.xuecheng.content.api;

import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;
import com.xuecheng.content.service.CourseBaseInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
    @Autowired
    private CourseBaseInfoService courseBaseInfoService;

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
