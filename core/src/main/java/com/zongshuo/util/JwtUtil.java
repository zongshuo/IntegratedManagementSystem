package com.zongshuo.util;

import io.jsonwebtoken.Jwts;

import java.security.Key;
import java.util.Date;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-5-31
 * @Time: 18:29
 * @Description:
 */
public final class JwtUtil {
    public static String buildToken(String subject, Key key, Date expireTime){
        return Jwts.builder().setSubject(subject).signWith(key).setExpiration(expireTime).compact();
    }

}
