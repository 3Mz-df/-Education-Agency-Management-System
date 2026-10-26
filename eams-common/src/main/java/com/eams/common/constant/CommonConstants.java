package com.eams.common.constant;

public final class CommonConstants {
    //常量类
    private CommonConstants() {} //私有构造

    //请求头 token
    public static final String AUTH_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer";

    //网关往业务服务透传用的请求头
    public static final String HEADER_USER_ID = "X-User-Id";
    public static final String HEADER_USERNAME = "X-Username";
    public static final String HEADER_USER_TYPE = "X-User-Type";
    public static final String HEADER_INSTITUTION_ID = "X-Institution-Id";

    //用户类型（数字常量）
    public static final int USER_TYPE_ADMIN = 1; //平台管理员
    public static final int USER_TYPE_INSTITUTION = 2; //机构管理员
    public static final int USER_TYPE_STAFF = 3; //机构员工
    public static final int USER_TYPE_MEMBER = 4; //C端用户
}
