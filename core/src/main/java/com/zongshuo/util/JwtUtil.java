package com.zongshuo.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import sun.misc.BASE64Decoder;

import java.security.Key;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-5-31
 * @Time: 18:29
 * @Description:
 */
public final class JwtUtil {
    //token认证类型
    public static final String TOKEN_TYPE = "Bearer";
    //token在header中字段名称
    public static final String TOKEN_HEADER_NAME = "Authorization";
    //token在参数中的字段名称
    public static final String TOKEN_PARAM_NAME = "access_token";

    /**
     * 构建jwt token
     * @param userCount 用户账号
     * @param slot 构建token签证使用的盐
     * @param effectiveTime token的有效时间
     * @return
     */
    public static String buildToken(String userCount, String slot, long effectiveTime){
        Date expireDate = new Date(Instant.now().toEpochMilli() + effectiveTime);
        Key key = getKey(slot);
        return buildToken(userCount, key, expireDate);
    }
    /**
     * 创建token
     * @param subject 用户名
     * @param key
     * @param expireTime
     * @return
     */
    public static String buildToken(String subject, Key key, Date expireTime){
        return Jwts.builder().setSubject(subject).signWith(key).setExpiration(expireTime).compact();
    }

    /**
     * 将盐封装成key
     * @param slot
     * @return
     */
    public static Key getKey(String slot){
        if (slot == null || slot.trim().isEmpty()) return null;
        //该方法要求盐的盐的长度大于等于256
        return Keys.hmacShaKeyFor(Base64.getDecoder().decode(slot));
    }
}
