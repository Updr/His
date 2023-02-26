package com.ysu.his.service.impl;

import com.ysu.his.entity.User;
import com.ysu.his.mapper.UserMapper;
import com.ysu.his.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
