package com.ysu.his.service;

import com.ysu.his.entity.Permission;
import com.baomidou.mybatisplus.extension.service.IService;


public interface IPermissionService extends IService<Permission> {
  Object listByTree();
  Object userPermissionList(int id);
}
