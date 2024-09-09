package com.lxl.lxlrpc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 封装返回的响应信息
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RpcResponse implements Serializable {


    //响应数据
    private Object data;

    //响应信息
    private String message;

    private Class<?> dataType;

    //异常
    private Exception exception;
}
