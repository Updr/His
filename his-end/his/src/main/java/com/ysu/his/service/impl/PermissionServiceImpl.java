package com.ysu.his.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.ysu.his.entity.Permission;
import com.ysu.his.entity.RolePermission;
import com.ysu.his.mapper.PermissionMapper;
import com.ysu.his.service.IPermissionService;
import com.ysu.his.service.IRolePermissionService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements IPermissionService {

  @Resource
  private IRolePermissionService roles;

  @Override
  public Object listByTree() {
    QueryWrapper<Permission> wrapper = new QueryWrapper<>();
    wrapper.eq("parent_id", 0);
    List<Permission> list = this.list(wrapper);
    for (Permission i : list) {
      getChildren(i);
    }
    return list;
  }

  /**
   * 参数为父权限对象 通过父权限获取子权限, 这里假设为2级
   *
   */
  private void getChildren(Permission permission) {
    QueryWrapper<Permission> wrapper = new QueryWrapper<>();
    wrapper.eq("parent_id", permission.getId());
    // List<Permission> list =this.list(wrapper) ;
    // for (Permission i : list) {
    //   if (permission.getType() != 0) {
    //     getChildren(permission);
    //   }
    // }
    permission.setChildren(this.list(wrapper));
  }

  @Override
  public Object userPermissionList(int id) {
    QueryWrapper<RolePermission> wrapper = new QueryWrapper<>();
    wrapper.eq("role_id", id);
    RolePermission rop = roles.getOne(wrapper,false);
    if (rop == null) return null;
    QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("parent_id", 0).inSql("id", rop.getPermissioinId());
    List<Permission> list = this.list(queryWrapper);
    for (Permission i : list) {
      queryWrapper.clear();
      queryWrapper.eq("parent_id",i.getId()).inSql("id", rop.getPermissioinId());
      i.setChildren(this.list(queryWrapper));
    }
    return list;
  }

}
