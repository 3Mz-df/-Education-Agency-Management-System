package com.eams.common.exception;

import com.eams.common.api.Result;
import com.eams.common.enums.ResultCode;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器 接所有异常 转成Result结构返回
 */
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET) //Web专用
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    //业务异常（BizException）
    @ExceptionHandler(BizException.class)
    public Result<Void> handleBizException(BizException e){
        log.warn("业务异常:{}", e.getMessage());
        return Result.failed(e.getCode(), e.getMessage());
    }

    //@RequestBody参数的@Valid校验失败
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleMethodArgumentNotValid(MethodArgumentNotValidException e){
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .findFirst()
                .orElse(ResultCode.VALIDATE_FAILED.getMessage());
        log.warn("参数校验失败:{}", message);
        return Result.failed(ResultCode.VALIDATE_FAILED.getCode(), message);
    }

    //表单绑定@ModelAttribute校验失败
    @ExceptionHandler(BindException.class)
    public Result<Void> handleBindException(BindException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .findFirst()
                .orElse(ResultCode.VALIDATE_FAILED.getMessage());
        log.warn("参数校验失败:{}", message);
        return Result.failed(ResultCode.VALIDATE_FAILED.getCode(), message);
    }

    //方法参数上@Validated校验失败 controller类级校验
    @ExceptionHandler(ConstraintViolationException.class)
    public Result<Void> handleConstraintViolation(ConstraintViolationException e) {
        String message = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .findFirst()
                .orElse(ResultCode.VALIDATE_FAILED.getMessage());
        log.warn("参数校验失败:{}", message);
        return Result.failed(ResultCode.VALIDATE_FAILED.getCode(), message);
    }

    //其他异常
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e){
        log.error("其他异常:{}", e);
        return Result.failed(ResultCode.SYSTEM_ERROR);
    }
}
