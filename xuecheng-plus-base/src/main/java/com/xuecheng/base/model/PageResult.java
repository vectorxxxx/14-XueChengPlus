package com.xuecheng.base.model;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @author VectorX
 * @version V1.0
 * @description 分页查询结果模型类
 * @date 2024-04-07 17:31:01
 */
@Data
@ToString
public class PageResult<T> implements Serializable
{

    private static final long serialVersionUID = 8623468181983035254L;
    
    // 数据列表
    private List<T> items;

    //总记录数
    private long counts;

    //当前页码
    private long page;

    //每页记录数
    private long pageSize;

    public PageResult(List<T> items, long counts, long page, long pageSize) {
        this.items = items;
        this.counts = counts;
        this.page = page;
        this.pageSize = pageSize;
    }
}
