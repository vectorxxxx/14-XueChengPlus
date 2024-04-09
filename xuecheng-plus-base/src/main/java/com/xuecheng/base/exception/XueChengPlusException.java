package com.xuecheng.base.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author VectorX
 * @version 1.0.0
 * @description 自定义异常类型
 * @date 2024/04/08
 * @see RuntimeException
 */
@Getter
@Setter
@NoArgsConstructor
public class XueChengPlusException extends RuntimeException
{
    private static final long serialVersionUID = 7740257875239077728L;

    private String errMessage;

    public XueChengPlusException(String message) {
        super(message);
        this.errMessage = message;
    }

    public static void cast(String message) {
        throw new XueChengPlusException(message);
    }

    public static void cast(CommonError error) {
        throw new XueChengPlusException(error.getErrMessage());
    }

}
