package com.lxl.lxlrpc.registry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LocalRegistry {

    /**
     * 存储提供者服务
     */
    private static final Map<String,Class<?>> map = new ConcurrentHashMap<>();

    public static void register(String serviceName,Class<?> implClass){
        map.put(serviceName,implClass);
    }

    public static Class<?> get(String serviceName){
        return map.get(serviceName);
    }

    public static void remove(String serviceName){
        map.remove(serviceName);
    }

}
