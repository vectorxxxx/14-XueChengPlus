package com.xuecheng.base.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author VectorX
 * @version 1.0.0
 * @description 和前端约定返回的异常信息模型
 * @date 2024/04/08
 * @see Serializable
 */
@Getter
@Setter
@AllArgsConstructor
public class RestErrorResponse implements Serializable
{
    private static final long serialVersionUID = 9026504397012666687L;
    
    private String errMessage;
}
