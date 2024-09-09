package com.lxl.example.provider.service.impl;

import com.lxl.example.common.model.User;
import com.lxl.example.common.service.UserService;

public class UserServiceImpl implements UserService {

    @Override
    public User getUser(User user) {
        System.out.println(user.getName());
        return user;
    }

    @Override
    public String getName(User user) {
        return user.getName();
    }
}
