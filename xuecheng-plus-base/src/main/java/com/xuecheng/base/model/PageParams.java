package com.xuecheng.base.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * @author VectorX
 * @version V1.0
 * @description 分页查询通用参数
 * @date 2024-04-07 17:28:35
 */
@ApiModel(value = "分页查询通用参数")
@Data
@ToString
public class PageParams
{
    @ApiModelProperty("当前页码")
    private Long pageNo = 1L;

    @ApiModelProperty("每页记录数默认值")
    private Long pageSize = 10L;

    public PageParams() {

    }

    public PageParams(long pageNo, long pageSize) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

}
