package com.ysu.his.service.impl;

import com.ysu.his.entity.Role;
import com.ysu.his.mapper.RoleMapper;
import com.ysu.his.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

}
