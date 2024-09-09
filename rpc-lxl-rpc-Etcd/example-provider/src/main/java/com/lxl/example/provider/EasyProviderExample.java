package com.lxl.example.provider;

import com.lxl.example.common.service.UserService;
import com.lxl.example.provider.service.impl.UserServiceImpl;
import com.lxl.lxlrpc.application.RpcApplication;
import com.lxl.lxlrpc.config.RegistryConfig;
import com.lxl.lxlrpc.config.RpcConfig;
import com.lxl.lxlrpc.constant.RpcConstant;
import com.lxl.lxlrpc.model.ServiceMetaInfo;
import com.lxl.lxlrpc.registry.LocalRegistry;
import com.lxl.lxlrpc.registry.RegisterFactory;
import com.lxl.lxlrpc.registry.Registry;
import com.lxl.lxlrpc.serializer.Serializer;
import com.lxl.lxlrpc.server.HttpServer;
import com.lxl.lxlrpc.server.VertxHttpServer;
import com.lxl.lxlrpc.spi.SpiLoader;

public class EasyProviderExample {
    //启动消费者
    public static void main(String[] args) {

        //注册服务
//        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);

        //初始化
        RpcApplication.init();
        //注册服务
        //根据源信息注册服务
        String serviceName = UserService.class.getName();
        LocalRegistry.register(serviceName, UserServiceImpl.class);
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegisterFactory.getInstance(registryConfig.getRegister());
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(serviceName);
        serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
        serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
        serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
        try {
            registry.register(serviceMetaInfo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //加载序列化器
        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(8081);
    }
}
