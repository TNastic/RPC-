package com.lxl.lxlrpc.registry;

import com.lxl.lxlrpc.config.RegistryConfig;
import com.lxl.lxlrpc.model.ServiceMetaInfo;

import java.util.List;

/**
 * 注册
 */
public interface Registry {

    /**
     * 初始化
     * @param registerConfig
     */
    void init(RegistryConfig registerConfig);

    /**
     * 注册服务
     * @param serviceMetaInfo
     */
    void register(ServiceMetaInfo serviceMetaInfo) throws Exception;

    /**
     * 注销服务
     * @param serviceMetaInfo
     */
    void unRegister(ServiceMetaInfo serviceMetaInfo);

    /**
     * 发现服务
     * @param serviceKey
     * @return
     */
    List<ServiceMetaInfo> serviceDiscovery(String serviceKey);

    /**
     * 服务销毁
     */
    void destroy();

    /**
     * 心跳检测
     */
    void heartBeat();

    /**
     * 监听
     * @param serviceNodeKey
     */
    void watch(String serviceNodeKey);
}
