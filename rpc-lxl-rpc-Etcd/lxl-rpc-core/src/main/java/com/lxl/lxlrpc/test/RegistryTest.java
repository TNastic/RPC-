package com.lxl.lxlrpc.test;

import com.lxl.lxlrpc.config.RegistryConfig;
import com.lxl.lxlrpc.model.ServiceMetaInfo;
import com.lxl.lxlrpc.registry.EtcdRegistry;
import com.lxl.lxlrpc.registry.Registry;

public class RegistryTest {

    static final Registry registry = new EtcdRegistry();

    public static void main(String[] args) throws Exception {
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress("http://localhost:2379");
        //开启心跳检测
        registry.init(registryConfig);
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        //设置注册键的信息
        serviceMetaInfo.setServiceName("myService");
        serviceMetaInfo.setServiceHost("localhost");
        serviceMetaInfo.setServiceVersion("1.0");
        serviceMetaInfo.setServicePort(1234);
        registry.register(serviceMetaInfo);

        serviceMetaInfo.setServiceName("myService");

    }


}
