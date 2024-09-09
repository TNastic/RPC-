package com.lxl.lxlrpc.model;

import cn.hutool.core.util.StrUtil;
import lombok.Data;

/**
 * 注册服务信息（服务元信息）
 */
@Data
public class ServiceMetaInfo {

    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 服务版本号
     */
    private String serviceVersion;


    /**
     * 服务域名
     */
    private String serviceHost;

    /**
     * 服务端口号
     */
    private Integer servicePort;


    /**
     * 服务地址
     */
    private String serviceGroup;

    private String serviceAddress;

    /**
     * 获取键名
     * @return
     */
    public String getServiceKey(){
        return String.format("%s:%s",serviceName,serviceVersion);
    }

    //  /rpc/UserService:1.0/localhost:8080
    public String getServiceNodeKey(){
        return String.format("%s/%s:%s", getServiceKey(), serviceHost, servicePort);
    }


    /**
     * 获取完整服务地址
     *
     * @return
     */
    public String getServiceAddress() {
        if (!StrUtil.contains(serviceHost, "http")) {
            return String.format("http://%s:%s", serviceHost, servicePort);
        }
        return String.format("%s:%s", serviceHost, servicePort);
    }
}
