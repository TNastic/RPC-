package com.lxl.example.common.service;

import com.lxl.example.common.model.User;

public interface UserService {
    /**
     * 获取用户
     * @param user
     * @return
     */
    User getUser(User user);

    /**
     * 返回名字
     */
    String getName(User user);
}
