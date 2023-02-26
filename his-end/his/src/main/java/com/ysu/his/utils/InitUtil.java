package com.ysu.his.utils;

import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import org.springframework.util.StringUtils;

@SuppressWarnings("all")
public class InitUtil {

    /**
     * 初始化分页信息
     */
    public static void initPage(Map<String, Object> param) {
        if (StringUtils.isEmpty(param.get("page"))) {
            param.put("page", 1);
        }
        if (StringUtils.isEmpty(param.get("limit"))) {
            param.put("limit", 3);
        }
    }

    public static void initLike(Map<String, Object> param,QueryWrapper wrapper, String ...str){
        for(String s:str){
            if(!StringUtils.isEmpty(param.get(s))){
                wrapper.like(s, param.get(s));
            }
        }
    }

    public static void initEq(Map<String, Object> param,QueryWrapper wrapper, String ...str){
        for(String s:str){
            if(!StringUtils.isEmpty(param.get(s))){
                wrapper.eq(s, param.get(s));
            }
        }
    }
}