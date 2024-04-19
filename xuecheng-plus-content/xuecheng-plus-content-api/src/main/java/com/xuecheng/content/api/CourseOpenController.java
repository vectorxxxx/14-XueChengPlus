package com.xuecheng.content.api;

import com.xuecheng.content.model.dto.CoursePreviewDto;
import com.xuecheng.content.service.CoursePublishService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author VectorX
 * @version V1.0
 * @description
 * @date 2024-04-15 22:56:31
 */
@Api(value = "课程公开查询接口",
     tags = "课程公开查询接口")
@RestController
@RequestMapping("/open")
public class CourseOpenController
{
    @Autowired
    private CoursePublishService coursePublishService;

    @GetMapping("/course/whole/{courseId}")
    public CoursePreviewDto getPreviewInfo(
            @PathVariable("courseId")
                    Long courseId) {
        // 获取课程预览信息
        return coursePublishService.getCoursePreviewInfo(courseId);
    }
}
