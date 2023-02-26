package com.ysu.his.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSONObject;
import com.ysu.his.annotation.UserLoginToken;
import com.ysu.his.entity.Department;
import com.ysu.his.service.IDepartmentService;
import com.ysu.his.utils.InitUtil;
import com.ysu.his.utils.Result;
import com.ysu.his.utils.ResultGenerator;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/departments")
public class DepartmentController {
    @Resource
    private IDepartmentService departmentService;

    @GetMapping
    public Result getlist(@RequestParam Map<String, Object> param) {
        InitUtil.initPage(param);
        int num = Integer.parseInt(param.get("page").toString());
        int limit = Integer.parseInt(param.get("limit").toString());
        QueryWrapper<Department> wrapper = new QueryWrapper<>();
        InitUtil.initLike(param, wrapper, "name");
        InitUtil.initEq(param, wrapper, "active");
        IPage<Department> page = new Page<>(num, limit);
        return ResultGenerator.getSuccessResult(departmentService.page(page, wrapper));
    }

    @GetMapping("/{id}")
    public Result getDepartment(@PathVariable int id) {
        Department department = departmentService.getById(id);
        if (department == null)
            return ResultGenerator.getFailResult("", "无科室记录");
        return ResultGenerator.getSuccessResult(department);
    }

    @GetMapping("/check")
    public Result checkName(@RequestParam String name) {
        QueryWrapper<Department> wrapper = new QueryWrapper<>();
        wrapper.eq("name", name);
        if (departmentService.getOne(wrapper) != null)
            return ResultGenerator.getFailResult("", "科室名称已存在");
        return ResultGenerator.getSuccessResult();
    }

    @GetMapping("/all")
    public Result getAll() {
      QueryWrapper<Department> wrapper = new QueryWrapper<>();
      wrapper.eq("active", 1);
      JSONObject jsonObject = new JSONObject();
      List<Department> list = departmentService.list(wrapper);
      for (Department i : list) {
        jsonObject.put(i.getId().toString(), Map.of("name",i.getName()));
      }
      return ResultGenerator.getSuccessResult(jsonObject);
    }

    @PostMapping()
    @UserLoginToken
    public Result save(@RequestBody Department department) {
        // System.out.println(department);
        if (departmentService.save(department))
            return ResultGenerator.getSuccessResult("", "添加成功");
        return ResultGenerator.getFailResult("", "添加失败");
    }

    @PutMapping("/{id}")
    @UserLoginToken
    public Result update(@RequestBody Department department, @PathVariable int id) {
        department.setId(id);
        // System.out.println(department);
        if (departmentService.updateById(department))
            return ResultGenerator.getSuccessResult("", "更新成功");
        return ResultGenerator.getFailResult("", "更新失败");
    }

    @PutMapping("/{id}/state/{active}")
    @UserLoginToken
    public Result changeActive(@PathVariable int id, @PathVariable int active) {
        Department department = new Department();
        department.setActive(active);
        department.setId(id);
        if (departmentService.updateById(department))
            return ResultGenerator.getSuccessResult("", "激活状态修改成功");
        return ResultGenerator.getFailResult("", "激活状态修改失败");
    }

    @DeleteMapping("/{id}")
    @UserLoginToken
    public Result del(@PathVariable int id) {
        if (departmentService.removeById(id))
            return ResultGenerator.getSuccessResult("", "删除成功");
        return ResultGenerator.getFailResult("", "删除失败");
    }

    @DeleteMapping("/batchdel")
    @UserLoginToken
    public Result batchDel(@RequestParam String ids) {
        String[] idList = ids.split(",");
        List<Integer> list = new ArrayList<>(idList.length);
        for (String id : idList) {
            list.add(Integer.parseInt(id));
        }
        if (departmentService.removeByIds(list))
            return ResultGenerator.getSuccessResult("", "删除成功");
        return ResultGenerator.getFailResult("", "删除失败");
    }
}
