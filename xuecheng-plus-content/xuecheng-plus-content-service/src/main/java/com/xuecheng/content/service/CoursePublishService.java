package com.xuecheng.content.service;

import com.xuecheng.content.model.dto.CoursePreviewDto;

/**
 * @author VectorX
 * @version V1.0
 * @description 课程预览、发布接口
 * @date 2024-04-15 20:06:35
 */
public interface CoursePublishService
{
    /**
     * 提交审核
     *
     * @param companyId
     * @param courseId  课程id
     */
    void commitAudit(Long companyId, Long courseId);

    /**
     * 获取课程预览信息
     *
     * @param courseId 课程id
     * @return com.xuecheng.content.model.dto.CoursePreviewDto
     */
    CoursePreviewDto getCoursePreviewInfo(Long courseId);
}
