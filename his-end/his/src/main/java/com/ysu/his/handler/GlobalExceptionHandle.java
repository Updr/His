package com.ysu.his.handler;

import javax.servlet.http.HttpServletRequest;

import com.ysu.his.exception.MyException;
import com.ysu.his.utils.Result;
import com.ysu.his.utils.ResultGenerator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandle {
  private static Logger log = LoggerFactory.getLogger(GlobalExceptionHandle.class);

  // 捕获全局异常，处理所有不可知的异常 这个注解是捕获所有异常
  @ExceptionHandler(value = MyException.class)
  public Result handleMyException(MyException e, HttpServletRequest request) {
    log.error("msg:{}, url:{}", e.getMessage(), request.getRequestURL());
    e.printStackTrace();
    return ResultGenerator.getFailResult("", e.getMessage(), e.getCode());
  }
}