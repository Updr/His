package com.ysu.his.utils;

import com.ysu.his.entity.User;
import com.ysu.his.exception.MyException;
import com.ysu.his.exception.UserNoLoginException;
import com.ysu.his.service.IUserService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;

import javax.annotation.Resource;

@Component
public class JwtUtil {

    private static IUserService userService;

    @Resource
    public void setUserService(IUserService userService) {
        JwtUtil.userService = userService;
    }
     /**
     * 过期时间为1小时
     */
    private static final long EXPIRE_TIME = 1*60*60*1000;
    
    
    /**
     * token私钥
     */
    public static final String KEY = "antrain_user_his";

    
    
    public static String create(User user) {

        HashMap<String, Object> header = new HashMap<>(2);
        header.put("typ", "JWT");
        header.put("alg", "HS256");

        return JWT.create()
                .withHeader(header)
                .withClaim("id", user.getId())
                .withClaim("username",user.getUsername())
                .withClaim("password",user.getPassword())
                .withClaim("deptId", user.getDeptId())
                .withClaim("timestamp",Date.from(user.getLastlogin().atZone(ZoneId.systemDefault()).toInstant()))
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRE_TIME))
                .sign(Algorithm.HMAC256(KEY));
    }

    public static void isExpire(String token){
        if(System.currentTimeMillis() - JWT.decode(token).getExpiresAt().getTime() > 0 ){
            throw new MyException("登录已过期，请重新登录");
        }
    }

    /**
     * 通过token串 获取id信息，然后从数据库中获取信息
     * @param token
     * @return
     */
    public static User getUserById(String token){
        Integer userId = JWT.decode(token).getClaim("id").asInt();
        System.out.println(userService+":"+userId);
        User user = userService.getById(userId);
        if (user == null){
            throw new UserNoLoginException();
        } 
        return user;
    }

    public static User getUser(String token){
        try {
            User user = new User();
            user.setId(JWT.decode(token).getClaim("id").asInt());
            user.setUsername(JWT.decode(token).getClaim("username").asString());
            user.setPassword(JWT.decode(token).getClaim("password").asString());
            user.setDeptId(JWT.decode(token).getClaim("deptId").asInt());
            Date date = JWT.decode(token).getClaim("timestamp").asDate();
            user.setLastlogin(Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime());
            return user;
        }catch (Exception e) {
            e.printStackTrace();
            throw new MyException(e.getMessage());
        }
    }

    public static void main(String[] args){
        User user = new User();
        user.setId(1);
        user.setUsername("admin");
        user.setPassword("admin");
        user.setLastlogin(LocalDateTime.now());
        System.out.println(user);
        String token = create(user);
        System.out.println("-------------------token----------------");
        System.out.println(getUser(token));
    }
}