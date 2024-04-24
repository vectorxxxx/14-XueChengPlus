package com.xuecheng.gateway.config;

import java.io.Serializable;

/**
 * @author VectorX
 * @version 1.0.0
 * @description 错误响应参数包装
 * @date 2024/04/24
 * @see Serializable
 */
public class RestErrorResponse implements Serializable
{

    private static final long serialVersionUID = 8726493275129574854L;

    private String errMessage;

    public RestErrorResponse(String errMessage) {
        this.errMessage = errMessage;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }
}
