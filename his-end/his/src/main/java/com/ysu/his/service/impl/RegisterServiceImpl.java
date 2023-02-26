package com.ysu.his.service.impl;

import com.ysu.his.entity.Register;
import com.ysu.his.mapper.RegisterMapper;
import com.ysu.his.service.IRegisterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service
public class RegisterServiceImpl extends ServiceImpl<RegisterMapper, Register> implements IRegisterService {

}
