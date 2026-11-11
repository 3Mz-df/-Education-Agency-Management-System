package com.eams.system.interceptor;

import com.eams.common.api.Result;
import com.eams.common.context.UserContext;
import com.eams.common.enums.ResultCode;
import com.eams.system.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * JWT 登录拦截器：校验请求头里的 token，不合法就拦截返回 401
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

    private final ObjectMapper objectMapper;

    // 注入 Spring 的 ObjectMapper（把 Result 转成 JSON 写回给前端）
    public JwtInterceptor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //从请求头里拿 token
        String token = request.getHeader("Authorization");

        //没带 token，或格式不对 拦截
        if (token == null || !token.startsWith("Bearer ")) {
            return reject(response);
        }

        //去掉 "Bearer " 前缀，拿到真正的 token
        token = token.substring(7);

        try {
            Claims claims = JwtUtil.parseToken(token);

            //从 token 里取出 userId 和 username
            //用 toString 统一转
            Object userIdObj = claims.get("userId");
            Long userId = userIdObj == null ? null : Long.valueOf(userIdObj.toString());
            String username = claims.getSubject();

            // 装进 UserInfo，挂到当前线程的抽屉里
            UserContext.UserInfo userInfo = new UserContext.UserInfo();
            userInfo.setUserId(userId);
            userInfo.setUsername(username);
            UserContext.set(userInfo);
        } catch (Exception e) {
            //解析失败  拦截
            return reject(response);
        }

        //token 合法 → 放行
        return true;
    }

    // 请求结束时调用：清空线程池
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        UserContext.remove();
    }

    // 拦截并返回 401（把错误信息写成 JSON 写回给前端）
    private boolean reject(HttpServletResponse response) throws Exception {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // HTTP 状态码 401
        response.setContentType("application/json;charset=UTF-8");
        Result<Void> result = Result.failed(ResultCode.UNAUTHORIZED);
        response.getWriter().write(objectMapper.writeValueAsString(result));
        return false; // 返回 false = 拦截
    }
}
