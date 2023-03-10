package com.ysu.his.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSONObject;
import com.ysu.his.annotation.UserLoginToken;
import com.ysu.his.entity.Register;
import com.ysu.his.service.IRegisterService;
import com.ysu.his.utils.Constants;
import com.ysu.his.utils.InitUtil;
import com.ysu.his.utils.Result;
import com.ysu.his.utils.ResultGenerator;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/registers")
public class RegisterController {
  @Resource
  private IRegisterService registerService;

  // private Register find(int id){
  //   Register register = registerService.getById(id);
  //   if (register == null){
  //     throw new NotExistException();
  //   }
  //   return register;
  // }

  @GetMapping
  @UserLoginToken
  public Result getlist(@RequestParam Map<String, Object> param) {
    InitUtil.initPage(param);
    int num = Integer.parseInt(param.get("page").toString());
    int limit = Integer.parseInt(param.get("limit").toString());
    QueryWrapper<Register> wrapper = new QueryWrapper<>();
    Object name = param.get("name");
    if (!StringUtils.isEmpty(name)){
        wrapper.like("name", name).or().like("id", name);
    }
    InitUtil.initEq(param, wrapper, "active","status","doctor_id","dept_id");
    IPage<Register> page = new Page<>(num, limit);
    return ResultGenerator.getSuccessResult(registerService.page(page, wrapper));
  }

  @GetMapping("/all")
  public Result getAll() {
    QueryWrapper<Register> wrapper = new QueryWrapper<>();
    wrapper.eq("active", 1);
    JSONObject jsonObject = new JSONObject();
    List<Register> list = registerService.list(wrapper);
    for (Register i : list) {
      jsonObject.put(i.getId().toString(),Map.of("name",i.getName()));
    }
    return ResultGenerator.getSuccessResult(jsonObject);
  }

  @GetMapping("/all1")
  public Result getAll1() {
    QueryWrapper<Register> wrapper = new QueryWrapper<>();
    wrapper.eq("active", 1);
    wrapper.eq("status", 2);
    JSONObject jsonObject = new JSONObject();
    List<Register> list = registerService.list(wrapper);
    for (Register i : list) {
      jsonObject.put(i.getId().toString(), Map.of("name",i.getName()));
    }
    return ResultGenerator.getSuccessResult(jsonObject);
  }

  @GetMapping("/all2")
  public Result getAll2() {
    QueryWrapper<Register> wrapper = new QueryWrapper<>();
    wrapper.eq("active", 1);
    wrapper.eq("status", 3);
    JSONObject jsonObject = new JSONObject();
    List<Register> list = registerService.list(wrapper);
    for (Register i : list) {
      jsonObject.put(i.getId().toString(), Map.of("name",i.getName()));
    }
    return ResultGenerator.getSuccessResult(jsonObject);
  }

  @GetMapping("/{id}")
  public Result getRegister(@PathVariable int id) {
    Register register = registerService.getById(id);
    if (register == null)
      return ResultGenerator.getFailResult("", "?????????????????????");
    return ResultGenerator.getSuccessResult(register);
  }

  @PostMapping()
  @UserLoginToken
  public Result save(@RequestBody Register register) {
    // System.out.println(register);
    register.setStatus(Constants.REGISTER_REGIST); //???????????????????????????????????????
    if (registerService.save(register))
      return ResultGenerator.getSuccessResult("", "????????????");
    return ResultGenerator.getFailResult("", "????????????");
  }

  @PutMapping("/{id}")
  @UserLoginToken
  public Result update(@RequestBody Register register, @PathVariable int id) {
    register.setId(id);
    // System.out.println(register);
    if (registerService.updateById(register))
      return ResultGenerator.getSuccessResult("", "????????????");
    return ResultGenerator.getFailResult("", "????????????");
  }

  @PutMapping("/{id}/state/{active}")
  @UserLoginToken
  public Result changeActive(@PathVariable int id, @PathVariable int active) {
    Register register = new Register();
    register.setActive(active);
    register.setId(id);
    if (registerService.updateById(register))
      return ResultGenerator.getSuccessResult("", "????????????????????????");
    return ResultGenerator.getFailResult("", "????????????????????????");
  }

  /**
   * ??????
   */
  @PutMapping("/receive/{id}")
  @UserLoginToken
  public Result receive(@PathVariable int id) {
    Register register = new Register();
    register.setId(id);
    register.setStatus(Constants.REGISTER_DEAL);
    if (registerService.updateById(register))
      return ResultGenerator.getSuccessResult("", "????????????");
    return ResultGenerator.getFailResult("", "????????????");
  }

  /**
   * ??????
   */
  @PutMapping("/num/{id}")
  @UserLoginToken
  public Result returnNum(@PathVariable int id) {
    Register register = new Register();
    register.setId(id);
    register.setStatus(Constants.REGISTER_RETUEN);
    if (registerService.updateById(register))
      return ResultGenerator.getSuccessResult("", "????????????");
    return ResultGenerator.getFailResult("", "????????????");
  }

  

  @DeleteMapping("/{id}")
  @UserLoginToken
  public Result del(@PathVariable int id) {
    if (registerService.removeById(id))
      return ResultGenerator.getSuccessResult("", "????????????");
    return ResultGenerator.getFailResult("", "????????????");
  }

  @DeleteMapping("/batchdel")
  @UserLoginToken
  public Result batchDel(@RequestParam String ids) {
    String[] idList = ids.split(",");
    List<Integer> list = new ArrayList<>(idList.length);
    for (String id : idList) {
      list.add(Integer.parseInt(id));
    }
    if (registerService.removeByIds(list))
      return ResultGenerator.getSuccessResult("", "????????????");
    return ResultGenerator.getFailResult("", "????????????");
  }
}
