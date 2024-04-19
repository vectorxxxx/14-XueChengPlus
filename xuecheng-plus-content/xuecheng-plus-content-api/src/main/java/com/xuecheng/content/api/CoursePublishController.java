package com.xuecheng.content.api;

import com.xuecheng.content.model.dto.CoursePreviewDto;
import com.xuecheng.content.service.CoursePublishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author VectorX
 * @version V1.0
 * @description 课程预览，发布
 * @date 2024-04-15 19:34:43
 */
@RestController
@Slf4j
public class CoursePublishController
{
    @Autowired
    private CoursePublishService coursePublishService;

    @ResponseBody
    @PostMapping("/courseaudit/commit/{courseId}")
    public void commitAudit(
            @PathVariable("courseId")
                    Long courseId) {
        Long companyId = 1232141425L;
        coursePublishService.commitAudit(companyId, courseId);
    }

    @GetMapping("/coursepreview/{courseId}")
    public ModelAndView preview(
            @PathVariable("courseId")
                    Long courseId) {

        // 获取课程预览信息
        CoursePreviewDto coursePreviewInfo = coursePublishService.getCoursePreviewInfo(courseId);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("model", coursePreviewInfo);
        modelAndView.setViewName("course_template");
        return modelAndView;
    }
}
