package com.lxl.lxlrpc.proxy;


import java.lang.reflect.Proxy;

public class MockServiceProxyFactory {

    public static <T> T getMockProxy(Class<T> tClass){
        return (T) Proxy.newProxyInstance(
                tClass.getClassLoader(),
                new Class[]{tClass},
                new MockServiceProxy()
        );
    }
}
