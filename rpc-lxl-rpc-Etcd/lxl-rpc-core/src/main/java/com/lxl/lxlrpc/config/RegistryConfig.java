package com.lxl.lxlrpc.config;

import lombok.Data;

@Data
public class RegistryConfig {

    /**
     * 注册中心类别
     */
    private String register = "etcd";

    /**
     * 注册中心地址
     */
    private String address = "http://localhost:2380";


    private String username;


    private String password;

    /**
     * 超时时间
     */
    private Long timeout = 10000L;
}
