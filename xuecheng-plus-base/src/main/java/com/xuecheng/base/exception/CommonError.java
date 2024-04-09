package com.xuecheng.base.exception;

import lombok.Getter;

/**
 * @author VectorX
 * @version 1.0.0
 * @description 通用错误信息
 * @date 2024/04/08
 * @see Enum
 */
@Getter
public enum CommonError
{
    UNKOWN_ERROR("执行过程异常，请重试。"),
    PARAMS_ERROR("非法参数"),
    OBJECT_NULL("对象为空"),
    QUERY_NULL("查询结果为空"),
    REQUEST_NULL("请求参数为空");

    private String errMessage;

    CommonError(String errMessage) {
        this.errMessage = errMessage;
    }
}
