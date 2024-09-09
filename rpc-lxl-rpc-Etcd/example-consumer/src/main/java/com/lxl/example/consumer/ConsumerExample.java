package com.lxl.example.consumer;

import com.lxl.lxlrpc.config.RpcConfig;
import com.lxl.lxlrpc.utils.ConfigUtils;

public class ConsumerExample {

    public static void main(String[] args) {
        RpcConfig rpc = ConfigUtils.loadConfig(RpcConfig.class, "rpc");
        System.out.println(rpc);
    }
}
