package com.eams.common.exception;

import com.eams.common.enums.ResultCode;
import lombok.Getter;

/**
 * 业务异常：业务规则不满足时手动抛出 用于后续造异常对象 构造方法如下：
 */
@Getter
public class BizException extends RuntimeException{

    //编号
    private final int code;
    //消息消息
    private final String message;

    //直传一句话
    public BizException(String message){
        this(600,message); //this.()调用下面那个构造方法
    }

    //指定编号+指定内容
    public BizException(int code, String message){
        super(message); //把message交给父类RuntimeException
        this.code = code; //编号
        this.message = message; //消息内容
    }

    //指定枚举
    public BizException(ResultCode resultCode){
        this(resultCode.getCode(), resultCode.getMessage()); //this.()调用上面的构造方法
    }

    //用枚举编号+指定内容
    public BizException(ResultCode resultCode, String message){
        this(resultCode.getCode(), message); //一样
    }
}


