package com.xuecheng.content;

import com.alibaba.fastjson.JSON;
import com.xuecheng.content.model.dto.CourseCategoryTreeDto;
import com.xuecheng.content.service.CourseCategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author VectorX
 * @version V1.0
 * @description
 * @date 2024-04-08 16:44:31
 */
@SpringBootTest
class CourseCategoryServiceTests
{

    @Autowired
    private CourseCategoryService courseCategoryService;

    @Test
    void testQueryTreeNodes() {
        List<CourseCategoryTreeDto> categoryTreeDtos = courseCategoryService.queryTreeNodes("1");
        System.out.println(JSON.toJSONString(categoryTreeDtos));
    }

}

