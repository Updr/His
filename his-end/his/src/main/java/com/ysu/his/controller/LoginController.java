package com.ysu.his.controller;

import java.time.LocalDateTime;
import java.util.Map;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSONObject;
import com.ysu.his.entity.User;
import com.ysu.his.service.IUserService;
import com.ysu.his.utils.JwtUtil;
import com.ysu.his.utils.Result;
import com.ysu.his.utils.ResultGenerator;
import com.ysu.his.utils.ShaUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
  
  @Resource
  private IUserService userService;

  @PostMapping("/login")
  public Result login(@RequestBody Map<String,Object> param){
    System.out.println(param);
    QueryWrapper<User> wrapper = new QueryWrapper<>();
    wrapper.eq("telephone", param.get("telephone")).eq("password", ShaUtil.getSHA256(param.get("password").toString()));
    User user =  userService.getOne(wrapper);
    if (user == null){
      return ResultGenerator.getFailResult(null,"手机号或密码错误");
    }
    if (user.getActive() == 0) {
      return ResultGenerator.getFailResult(null,"请联系管理员帮你激活");
    }
    user.setLastlogin(LocalDateTime.now());
    updateLoginTime(user.getId());
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("token", JwtUtil.create(user));
    jsonObject.put("id",user.getId());
    jsonObject.put("deptId",user.getDeptId());
    return ResultGenerator.getSuccessResult(jsonObject,"登录成功");
  }

  private void updateLoginTime(int id){
    User user = new User();
    user.setId(id);
    user.setLastlogin(LocalDateTime.now());
    userService.updateById(user);
  }

}