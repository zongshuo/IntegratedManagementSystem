package com.zongshuo.security;

import com.zongshuo.Contains;
import com.zongshuo.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-6-15
 * @Time: 18:01
 * @Description:
 * 用于验证jwt的token并将权限对象放置到security的manager中
 */
public class JwtRequestFilter extends OncePerRequestFilter {
    private UserDetailsService userDetailsService;
    public JwtRequestFilter(UserDetailsService service){
        userDetailsService = service;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = JwtUtil.getRequestToken(request);
        if (token != null){
            Claims claims = JwtUtil.parseToken(token, Contains.JWT_SLOT);
            String username = claims.getSubject();
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            //放置处理过程中token失效，如果过期时间小于约定时间，重新生成token
            if ((claims.getExpiration().getTime() - Instant.now().toEpochMilli()) / 1000 / 60 < Contains.TOKEN_WILL_EXPIRE){
                token  = JwtUtil.buildToken(username, Contains.JWT_SLOT, Contains.EFFECTIVE_TIME_USER_TOKEN);
                response.setHeader(JwtUtil.TOKEN_HEADER_NAME, token);
            }
        }
        filterChain.doFilter(request, response);
    }
}
