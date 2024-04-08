package com.xuecheng.content.model.dto;

import com.xuecheng.content.model.po.CourseCategory;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author VectorX
 * @version 1.0.0
 * @description 课程分类树型结点dto
 * @date 2024/04/08
 * @see CourseCategory
 * @see java.io.Serializable
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CourseCategoryTreeDto extends CourseCategory implements java.io.Serializable
{
    private static final long serialVersionUID = 2950235607890841126L;

    //子节点
    List<CourseCategoryTreeDto> childrenTreeNodes;
}
