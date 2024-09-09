package com.lxl.lxlrpc.config;

import com.lxl.lxlrpc.serializer.SerializerKeys;
import lombok.Data;


/**
 * 配置类
 */

@Data
public class RpcConfig {

    private String name = "lxl-rpc";

    private String version = "1.0";

    private String serverHost = "localhost";

    private Integer serverPort = 8080;

    /**
     * 支持开启mock模式
     * 默认为false
     */
    private boolean mock = false;


    private String serializer = SerializerKeys.JDK;


    /**
     * 注册中心配置
     */
    private RegistryConfig registryConfig = new RegistryConfig();
}
