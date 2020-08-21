package com.tyq.service;

import com.tyq.entity.User;

public interface UserService {

    //用户注册
    void register(User user) throws Exception;

    //用户登录
    User login(User user);
}
