package com.ysu.his.handler;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ysu.his.annotation.UserLoginToken;
import com.ysu.his.exception.UserNoLoginException;
import com.ysu.his.utils.JwtUtil;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * @description ：拦截器去获取token并验证token
 */
public class AuthenticationInterceptor implements HandlerInterceptor {


  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object object) throws Exception {
    /** 地址过滤 */
    String uri = request.getRequestURI();
    if (uri.endsWith("/login")) {
      return true;
    }
    // System.out.println(request);
    String token = request.getHeader("Authorization");// 从 http 请求头中取出 token
    // 如果不是映射到方法直接通过
    if (!(object instanceof HandlerMethod)) {
      return true;
    }
    HandlerMethod handlerMethod = (HandlerMethod) object;
    Method method = handlerMethod.getMethod();

    // 检查有没有需要用户权限的注解
    if (method.isAnnotationPresent(UserLoginToken.class)) {
      UserLoginToken userLoginToken = method.getAnnotation(UserLoginToken.class);
      if (userLoginToken.required()) {
        // 执行认证
        if (token == null) {
          throw new UserNoLoginException();
        }
        JwtUtil.isExpire(token);
      }
    }
    return true;
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse httpServletResponse, Object o,
      ModelAndView modelAndView) throws Exception {

  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse httpServletResponse, Object o,
      Exception e) throws Exception {
  }
}