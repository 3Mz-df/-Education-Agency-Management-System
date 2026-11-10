package com.eams.system.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

//JWT工具类
public class JwtUtil {
    //token
    private static final String SECRET = "eams-jwt-secret-key-please-change-me-2026";

    //token有效期7天
    private static final long EXPIRE = 604800000;

    //转换
    private static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET.getBytes());

    //生成token
    public static String generateToken(Long userId, String username) {
        return Jwts.builder()
                .subject(username) // 设置主题
                .claim("userId", userId) // 添加自定义声明
                .issuedAt(new Date()) // 设置签发时间
                .expiration(new Date(System.currentTimeMillis() + EXPIRE)) //过期时间
                .signWith(KEY)  //防伪
                .compact(); //打包
    }

    //解析token
    public static Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(KEY) // 验证
                .build()
                .parseSignedClaims(token) //解析校验
                .getPayload(); //取出payload
    }
}
