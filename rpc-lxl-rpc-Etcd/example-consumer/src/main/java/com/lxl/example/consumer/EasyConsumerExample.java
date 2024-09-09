package com.lxl.example.consumer;

import com.lxl.example.common.model.User;
import com.lxl.example.common.service.UserService;
import com.lxl.lxlrpc.proxy.ServiceProxyFactory;


public class EasyConsumerExample {
    public static void main(String[] args) {
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("lxl");
//        User newUser = userService.getUser(user);
//        if (newUser != null){
//            System.out.println(user.getName());
//        }else {
//            System.out.println("user == null");
//        }
        for (int i = 0; i < 10; i++) {
            String name = userService.getName(user);
            System.out.println(name);
        }
    }
}
