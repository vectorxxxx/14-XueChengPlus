package com.xuecheng.content.api;

import com.xuecheng.content.model.dto.CourseTeacherDto;
import com.xuecheng.content.service.CourseTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author VectorX
 * @version V1.0
 * @description 教师编辑接口
 * @date 2024-04-10 15:07:22
 */
@Api(value = "教师编辑接口",
     tags = "教师编辑接口")
@RestController
@Slf4j
public class CourseTeacherController
{
    @Autowired
    private CourseTeacherService courseTeacherService;

    @ApiOperation("删除教师")
    @DeleteMapping("/courseTeacher/course/{courseId}/{teachId}")
    public void delTeacher(
            @ApiParam("课程id")
            @PathVariable
                    String courseId,

            @ApiParam("教师id")
            @PathVariable
                    String teachId) {
        courseTeacherService.delTeacher(courseId, teachId);
    }

    @ApiOperation("添加/修改教师")
    @PostMapping("/courseTeacher")
    public CourseTeacherDto saveTeacher(
            @ApiParam("教师Dto")
            @RequestBody
                    CourseTeacherDto courseTeacher) {
        return courseTeacherService.saveTeacher(courseTeacher);
    }

    @ApiOperation("查询教师")
    @GetMapping("/courseTeacher/list/{courseId}")
    public List<CourseTeacherDto> getTeacherList(
            @ApiParam("课程id")
            @PathVariable
                    String courseId) {
        return courseTeacherService.queryList(courseId);
    }
}
