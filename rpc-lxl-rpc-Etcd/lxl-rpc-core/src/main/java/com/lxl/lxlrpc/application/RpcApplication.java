package com.lxl.lxlrpc.application;

import com.lxl.lxlrpc.config.RegistryConfig;
import com.lxl.lxlrpc.config.RpcConfig;
import com.lxl.lxlrpc.registry.RegisterFactory;
import com.lxl.lxlrpc.registry.Registry;
import com.lxl.lxlrpc.utils.ConfigUtils;

//单例类，获取全局维持的一个配置类
public class RpcApplication {

    private static volatile RpcConfig rpcConfig;


    public static void init(RpcConfig newRpcConfig){
        rpcConfig = newRpcConfig;
        System.out.println("初始化配置"+rpcConfig.toString());
        //初始化注册中心
        RegistryConfig registerConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegisterFactory.getInstance(registerConfig.getRegister());
        registry.init(registerConfig);
        System.out.println("加载注册中心配置"+registerConfig);

        //注册下线服务,jvm的下线服务
        Runtime.getRuntime().addShutdownHook(new Thread(registry::destroy));
    }

    public static void init(){

        RpcConfig rpc = ConfigUtils.loadConfig(RpcConfig.class, "rpc");

        init(rpc);
    }


    public static RpcConfig getRpcConfig(){
        if (rpcConfig == null){
            synchronized (RpcApplication.class){
                if (rpcConfig == null){
                    init();
                }
            }
        }
        return rpcConfig;
    }

}
