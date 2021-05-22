package com.cqupt.settings.service.impl;

import com.cqupt.exception.LoginException;
import com.cqupt.settings.dao.UserDao;
import com.cqupt.settings.domain.User;
import com.cqupt.settings.service.UserService;
import com.cqupt.utils.DateTimeUtil;
import com.cqupt.utils.MD5Util;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;


    @Override
    public User login(String loginAct,String loginPwd,String ip) throws LoginException {
        Map<String,String> loginData=new HashMap<>();
        loginData.put("loginAct",loginAct);
        loginPwd=MD5Util.getMD5(loginPwd);
        loginData.put("loginPwd",loginPwd);
        User user=userDao.login(loginData);

        if(user==null){
            throw new LoginException("账号密码错误");
        }
        System.out.println(ip);

        String expireTime=user.getExpireTime();
        String nowTime=DateTimeUtil.getSysTime();
        String lockState=user.getLockState();
        String allowIps=user.getAllowIps();

        if("0".equals(lockState)){
            throw new LoginException("该账号已被锁定");
        }else if(nowTime.compareTo(expireTime)>=0){
            throw new LoginException("该账号使用期限已到");
        }else if(!allowIps.contains(ip)){
            throw new LoginException("非允许IP地址访问");
        }
        return user;
    }

    @Override
    public List<User> selectAll() {
        List<User> users=userDao.selectAll();
        return users;
    }
}
