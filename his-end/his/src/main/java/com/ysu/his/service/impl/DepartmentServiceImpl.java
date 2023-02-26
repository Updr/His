package com.ysu.his.service.impl;

import com.ysu.his.entity.Department;
import com.ysu.his.mapper.DepartmentMapper;
import com.ysu.his.service.IDepartmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements IDepartmentService {

}
