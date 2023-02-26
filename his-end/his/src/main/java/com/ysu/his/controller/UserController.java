package com.ysu.his.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSONObject;
import com.ysu.his.annotation.UserLoginToken;
import com.ysu.his.entity.User;
import com.ysu.his.service.IUserService;
import com.ysu.his.utils.InitUtil;
import com.ysu.his.utils.Result;
import com.ysu.his.utils.ResultGenerator;
import com.ysu.his.utils.ShaUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;


/**
 * <p>
 * 用户 前端控制器
 * </p>
 *
 * @author antrain
 * @since 2020-08-26
 */
@RestController
@RequestMapping("/users")
public class UserController {

    @Resource
    private IUserService userService;

    @GetMapping
    public Result getList(@RequestParam Map<String, Object> param) {
        InitUtil.initPage(param);
        int num = Integer.parseInt(param.get("page").toString());
        int limit = Integer.parseInt(param.get("limit").toString());
        QueryWrapper<User> wrapper = new QueryWrapper<User>();
        Object name = param.get("name");
        if (!StringUtils.isEmpty(name)){
            wrapper.like("username", name).or().like("realname", name);
        }
        InitUtil.initEq(param, wrapper, "active");
        IPage<User> page = new Page<>(num, limit);// 参数一是当前页，参数二是每页个数
        return ResultGenerator.getSuccessResult(userService.page(page, wrapper));
    }

    @GetMapping("/{id}")
    public Result getUser(@PathVariable int id) {
        User user = userService.getById(id);
        if (user == null) return ResultGenerator.getFailResult("","无用户记录");
        return ResultGenerator.getSuccessResult(user);
    }

    @GetMapping("/check")
    public Result checkUserName(@RequestParam String name) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", name);
        if (userService.getOne(wrapper) != null)
            return ResultGenerator.getFailResult("", "该用户名已存在");
        return ResultGenerator.getSuccessResult();
    }

    @GetMapping("/list")
    public Result getListBy(@RequestParam Map<String, Object> param) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        InitUtil.initEq(param, wrapper, "user_type","dept_id","active");
        JSONObject jsonObject = new JSONObject();
        List<User> list = userService.list(wrapper);
        for (User i : list) {
        jsonObject.put(i.getId().toString(),Map.of("name", i.getRealname()));
        }
        return ResultGenerator.getSuccessResult(jsonObject);
    }

    @PostMapping()
    @UserLoginToken
    public Result save(@RequestBody User user){
        //System.out.println(user);
        user.setPassword(ShaUtil.getSHA256(user.getPassword()));
        if(userService.save(user)) return ResultGenerator.getSuccessResult("","添加成功");
        return ResultGenerator.getFailResult("","添加失败");
    }

    @PutMapping("/{id}")
    @UserLoginToken
    public Result update(@RequestBody User user,@PathVariable int id){
        user.setId(id);
        user.setPassword(ShaUtil.getSHA256(user.getPassword()));
        //System.out.println(user);
        if(userService.updateById(user)) return ResultGenerator.getSuccessResult("","更新成功");
        return ResultGenerator.getFailResult("","更新失败");
    }

    /**
     * 修改用户的状态，相当于修改用户的激活状态
     */
    @PutMapping("/{id}/state/{active}")
    @UserLoginToken
    public Result changeActive(@PathVariable int id, @PathVariable int active) {
      User user = new User();
      user.setActive(active);
      user.setId(id);
      if (userService.updateById(user))
        return ResultGenerator.getSuccessResult("", "激活状态修改成功");
      return ResultGenerator.getFailResult("", "激活状态修改失败");
    }

    @DeleteMapping("/{id}")
    @UserLoginToken
    public Result del(@PathVariable int id) {
        if(userService.removeById(id)) return ResultGenerator.getSuccessResult("","删除成功");
        return ResultGenerator.getFailResult("","删除失败");
    }

    @DeleteMapping("/batchdel")
    @UserLoginToken
    public Result batchDel(@RequestParam String ids) {
        String[] idList = ids.split(",");
        List<Integer> list = new ArrayList<>(idList.length);
        for (String id : idList) {
            list.add(Integer.parseInt(id));
        }
        if (userService.removeByIds(list))
            return ResultGenerator.getSuccessResult("", "删除成功");
        return ResultGenerator.getFailResult("", "删除失败");
    }
}
