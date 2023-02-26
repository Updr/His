package com.ysu.his.service.impl;

import com.ysu.his.entity.UserRole;
import com.ysu.his.mapper.UserRoleMapper;
import com.ysu.his.service.IUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {

}
