package com.xuecheng.content.feignclient;

import com.xuecheng.content.po.CourseIndex;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author VectorX
 * @version V1.0
 * @description 搜索服务远程接口
 * @date 2024-04-23 16:37:01
 */
@FeignClient(value = "search",
             fallbackFactory = SearchServiceClientFallbackFactory.class)
public interface SearchServiceClient
{
    @PostMapping("/search/index/course")
    Boolean add(
            @RequestBody
                    CourseIndex courseIndex);
}
