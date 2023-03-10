package com.ysu.his.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSONObject;
import com.ysu.his.annotation.UserLoginToken;
import com.ysu.his.entity.InspectApply;
import com.ysu.his.service.IInspectApplyService;
import com.ysu.his.utils.Constants;
import com.ysu.his.utils.InitUtil;
import com.ysu.his.utils.Result;
import com.ysu.his.utils.ResultGenerator;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
@RequestMapping("/inspectApplys")
public class InspectApplyController {
  
  private Logger log = LoggerFactory.getLogger(InspectApplyController.class);

  @Resource
  private IInspectApplyService inspectApplyService;

  @GetMapping
  public Result getlist(@RequestParam Map<String, Object> param) {
    InitUtil.initPage(param);
    int num = Integer.parseInt(param.get("page").toString());
    int limit = Integer.parseInt(param.get("limit").toString());
    QueryWrapper<InspectApply> wrapper = new QueryWrapper<>();
    InitUtil.initLike(param, wrapper, "itemName");
    InitUtil.initEq(param, wrapper, "active");
    IPage<InspectApply> page = new Page<>(num, limit);
    return ResultGenerator.getSuccessResult(inspectApplyService.page(page, wrapper));
  }

  @GetMapping("/list/{id}")
  public Result getCaselist(@RequestParam Map<String, Object> param,@PathVariable int id) {
    log.debug("list/"+id+"; param="+param);
    QueryWrapper<InspectApply> wrapper = new QueryWrapper<>();
    wrapper.eq("active", 1).eq("register_id", id);
    InitUtil.initEq(param, wrapper, "status");
    return ResultGenerator.getSuccessResult(inspectApplyService.list(wrapper));
  }

  @GetMapping("/all")
  public Result getAll() {
    QueryWrapper<InspectApply> wrapper = new QueryWrapper<>();
    wrapper.eq("active", 1);
    JSONObject jsonObject = new JSONObject();
    List<InspectApply> list = inspectApplyService.list(wrapper);
    for (InspectApply i : list) {
      jsonObject.put(i.getId().toString(), Map.of("name",i.getItemName()));
    }
    return ResultGenerator.getSuccessResult(jsonObject);
  }

  @GetMapping("/{id}")
  public Result getInspectApply(@PathVariable int id) {
    InspectApply inspectApply = inspectApplyService.getById(id);
    if (inspectApply == null)
      return ResultGenerator.getFailResult("", "?????????????????????");
    return ResultGenerator.getSuccessResult(inspectApply);
  }

  @PostMapping()
  @UserLoginToken
  public Result save(@RequestBody InspectApply InspectApply) {
    // System.out.println(InspectApply);
    if (inspectApplyService.save(InspectApply))
      return ResultGenerator.getSuccessResult("", "????????????");
    return ResultGenerator.getFailResult("", "????????????");
  }

  @PutMapping("/{id}")
  @UserLoginToken
  public Result update(@RequestBody InspectApply inspectApply, @PathVariable int id) {
    inspectApply.setId(id);
    // System.out.println(InspectApply);
    if (inspectApplyService.updateById(inspectApply))
      return ResultGenerator.getSuccessResult("", "????????????");
    return ResultGenerator.getFailResult("", "????????????");
  }

  @PutMapping("/{id}/state/{active}")
  @UserLoginToken
  public Result changeActive(@PathVariable int id, @PathVariable int active) {
    InspectApply inspectApply = new InspectApply();
    inspectApply.setActive(active);
    inspectApply.setId(id);
    if (inspectApplyService.updateById(inspectApply))
      return ResultGenerator.getSuccessResult("", "????????????????????????");
    return ResultGenerator.getFailResult("", "????????????????????????");
  }

    /**
   * ??????
   */
  @PutMapping("/fee")
  @UserLoginToken
  public Result recevieFee(@RequestBody Map<String, Object> param) {
    String[] idList = param.get("ids").toString().split(",");
    InspectApply inspectApply = new InspectApply();
    inspectApply.setStatus(Constants.CHECK_APPLY_STATUS_2);
    for (String id : idList) {
      inspectApply.setId(Integer.parseInt(id));
      if(!inspectApplyService.updateById(inspectApply)){
        return ResultGenerator.getFailResult("", "????????????????????????????????????");
      }
    }
    return ResultGenerator.getSuccessResult("", "????????????");
  }

   /**
   * ??????
   */
  @PutMapping("/refund")
  @UserLoginToken
  public Result refund(@RequestBody Map<String, Object> param) {
    String[] idList = param.get("ids").toString().split(",");
    InspectApply inspectApply = new InspectApply();
    inspectApply.setStatus(Constants.CHECK_APPLY_STATUS_4);
    for (String id : idList) {
      inspectApply.setId(Integer.parseInt(id));
      if(!inspectApplyService.updateById(inspectApply)){
        return ResultGenerator.getFailResult("", "????????????????????????????????????");
      }
    }
    return ResultGenerator.getSuccessResult("", "????????????");
  }

  /**
   * ????????????
   */
  @PutMapping("/check")
  @UserLoginToken
  public Result check(@RequestBody Map<String, Object> param) {
    String[] idList = param.get("ids").toString().split(",");
    InspectApply inspectApply = new InspectApply();
    inspectApply.setStatus(Constants.CHECK_APPLY_STATUS_3);
    for (String id : idList) {
      inspectApply.setId(Integer.parseInt(id));
      if(!inspectApplyService.updateById(inspectApply)){
        return ResultGenerator.getFailResult("", "????????????????????????????????????");
      }
    }
    return ResultGenerator.getSuccessResult("", "????????????");
  }

   /**
   * ????????????
   */
  @PutMapping("/check/{id}")
  @UserLoginToken
  public Result checks(@PathVariable int id) {
    InspectApply inspectApply = new InspectApply();
    inspectApply.setStatus(Constants.CHECK_APPLY_STATUS_3);
    inspectApply.setId(id);
    if(!inspectApplyService.updateById(inspectApply)){
      return ResultGenerator.getFailResult("", "????????????????????????????????????");
    }
    return ResultGenerator.getSuccessResult("", "????????????");
  }

  @DeleteMapping("/{id}")
  @UserLoginToken
  public Result del(@PathVariable int id) {
    if (inspectApplyService.removeById(id))
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
    if (inspectApplyService.removeByIds(list))
      return ResultGenerator.getSuccessResult("", "????????????");
    return ResultGenerator.getFailResult("", "????????????");
  }
}
