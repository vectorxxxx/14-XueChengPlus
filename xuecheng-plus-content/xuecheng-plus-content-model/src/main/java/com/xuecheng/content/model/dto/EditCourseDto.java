package com.xuecheng.content.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author VectorX
 * @version V1.0
 * @description
 * @date 2024-04-09 16:31:33
 */
@Data
@ApiModel(value = "EditCourseDto",
          description = "修改课程基本信息")
public class EditCourseDto extends AddCourseDto
{
    @ApiModelProperty(value = "课程id",
                      required = true)
    private Long id;
}
