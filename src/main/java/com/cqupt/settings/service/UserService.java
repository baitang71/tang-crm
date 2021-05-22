package com.cqupt.settings.service;

import com.cqupt.exception.LoginException;
import com.cqupt.settings.domain.User;

import java.util.List;

public interface UserService {
    User login(String loginAct,String loginPwd,String ip) throws LoginException;
    List<User> selectAll();
}
