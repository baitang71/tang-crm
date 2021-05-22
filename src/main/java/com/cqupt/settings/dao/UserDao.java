package com.cqupt.settings.dao;

import com.cqupt.settings.domain.User;

import java.util.List;
import java.util.Map;

public interface UserDao {
    User login(Map<String,String> loginData);

    List<User> selectAll();
}
