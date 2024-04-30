package com.xuecheng.content.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuecheng.content.model.dto.CoursePreviewDto;
import com.xuecheng.content.model.po.CoursePublish;

import java.io.File;
import java.io.IOException;

/**
 * @author VectorX
 * @version V1.0
 * @description 课程预览、发布接口
 * @date 2024-04-15 2:06:35
 */
public interface CoursePublishService extends IService<CoursePublish>
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

    /**
     * 课程发布接口
     *
     * @param companyId 机构id
     * @param courseId  课程id
     * @return void
     */
    void publish(Long companyId, Long courseId);

    /**
     * @param courseId 课程id
     * @return File 静态化文件
     * @description 课程静态化
     * @author Mr.M
     * @date 2022/9/23 16:59
     */
    File generateCourseHtml(Long courseId);

    /**
     * @param file 静态化文件
     * @return void
     * @description 上传课程静态化页面
     * @author Mr.M
     * @date 2022/9/23 16:59
     */
    void uploadCourseHtml(Long courseId, File file) throws IOException;

    /**
     * 获取课程发布信息
     *
     * @param courseId
     * @return {@link CoursePublish}
     */
    CoursePublish getCoursePublish(Long courseId);
}
