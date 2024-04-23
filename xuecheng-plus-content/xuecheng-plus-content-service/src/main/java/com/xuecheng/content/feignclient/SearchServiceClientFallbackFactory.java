package com.xuecheng.content.feignclient;

import com.xuecheng.content.po.CourseIndex;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author VectorX
 * @version V1.0
 * @description
 * @date 2024-04-23 16:37:38
 */
@Slf4j
@Component
public class SearchServiceClientFallbackFactory implements FallbackFactory<SearchServiceClient>
{
    @Override
    public SearchServiceClient create(Throwable throwable) {
        return new SearchServiceClient()
        {
            /**
             * 发生熔断上传服务调用此方法执行降级逻辑
             *
             * @param courseIndex
             * @return {@link Boolean}
             */
            @Override
            public Boolean add(CourseIndex courseIndex) {
                log.debug("调用搜索发生熔断走降级方法,熔断异常:{}", throwable.getMessage());
                return false;
            }
        };
    }
}
