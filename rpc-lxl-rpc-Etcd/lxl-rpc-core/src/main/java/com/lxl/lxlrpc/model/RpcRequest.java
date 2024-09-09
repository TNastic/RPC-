package com.lxl.lxlrpc.model;

import com.lxl.lxlrpc.constant.RpcConstant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 封装请求信息
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RpcRequest implements Serializable {

    //服务名称
    private String serviceName;

    //方法名称
    private String methodName;

    //参数类型列表
    private Class<?>[] parametersTypes;

    //参数列表
    private Object[] args;

    //版本
    private String serviceVersion = RpcConstant.DEFAULT_SERVICE_VERSION;
}
