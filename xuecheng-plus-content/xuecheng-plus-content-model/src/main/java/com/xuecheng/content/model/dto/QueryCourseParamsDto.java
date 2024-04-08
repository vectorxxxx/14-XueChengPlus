package com.xuecheng.content.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * @author VectorX
 * @version V1.0
 * @description 课程查询参数Dto
 * @date 2024-04-07 17:30:14
 */
@ApiModel(value = "课程查询参数")
@Data
@ToString
public class QueryCourseParamsDto
{
    @ApiModelProperty("审核状态")
    private String auditStatus;

    @ApiModelProperty("课程名称")
    private String courseName;

    @ApiModelProperty("发布状态")
    private String publishStatus;
}
