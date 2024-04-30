package com.xuecheng.learning.feignclient;

import com.xuecheng.content.model.po.CoursePublish;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author VectorX
 * @version 1.0.0
 * @description 内容管理远程接口
 * @date 2024/04/28
 */
@FeignClient(value = "content-api",
             fallbackFactory = ContentServiceClientFallbackFactory.class)
public interface ContentServiceClient
{

    @ResponseBody
    @GetMapping("/content/r/coursepublish/{courseId}")
    CoursePublish getCoursepublish(
            @PathVariable("courseId")
                    Long courseId);

}
