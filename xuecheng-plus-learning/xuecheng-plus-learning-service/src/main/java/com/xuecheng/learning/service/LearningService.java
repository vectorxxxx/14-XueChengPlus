package com.xuecheng.learning.service;

import com.xuecheng.base.model.RestResponse;

/**
 * @author VectorX
 * @version V1.0
 * @description 学习过程管理service接口
 * @date 2024-04-30 16:23:31
 */
public interface LearningService
{
    /**
     * 获取教学视频
     *
     * @param courseId    课程id
     * @param teachplanId 课程计划id
     * @param mediaId     视频文件id
     * @return com.xuecheng.base.model.RestResponse<java.lang.String>
     */
    RestResponse<String> getVideo(String userId, Long courseId, Long teachplanId, String mediaId);
}
