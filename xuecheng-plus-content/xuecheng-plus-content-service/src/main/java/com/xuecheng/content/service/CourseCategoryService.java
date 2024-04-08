package com.xuecheng.content.service;

import com.xuecheng.content.model.dto.CourseCategoryTreeDto;

import java.util.List;

/**
 * @author VectorX
 * @version V1.0
 * @description
 * @date 2024-04-08 16:34:40
 */
public interface CourseCategoryService
{
    /**
     * 课程分类树形结构查询
     *
     * @return
     */
    List<CourseCategoryTreeDto> queryTreeNodes(String id);
}
