package com.eams.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 统一响应码枚举
 * <p>200 段：成功；4xx 段：客户端错误；5xx 段：系统错误；6xx 段：业务错误</p>
 */
@Getter
@AllArgsConstructor
public enum ResultCode {

    /** 操作成功 */
    SUCCESS(200, "操作成功"),

    /** 请求参数校验失败 */
    VALIDATE_FAILED(400, "参数校验失败"),

    /** 未认证：未登录或凭证已失效 */
    UNAUTHORIZED(401, "未登录或登录已过期"),

    /** 无权限访问 */
    FORBIDDEN(403, "没有相关操作权限"),

    /** 请求的资源不存在 */
    NOT_FOUND(404, "请求的资源不存在"),

    /** 系统内部错误 */
    SYSTEM_ERROR(500, "系统繁忙，请稍后重试"),

    /** 通用业务异常（具体信息由异常本身携带） */
    BUSINESS_ERROR(600, "业务处理失败");

    /** 响应码 */
    private final int code;

    /** 默认提示信息 */
    private final String message;
}
