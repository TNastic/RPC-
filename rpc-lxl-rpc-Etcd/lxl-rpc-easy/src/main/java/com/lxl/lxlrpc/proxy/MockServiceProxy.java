package com.lxl.lxlrpc.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * mock对象的静态代理
 */
public class MockServiceProxy implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //返回某个默认的值
        Class<?> returnType = method.getReturnType();
        System.out.println("生成mock代理对象");
        return getDefaultReturnTypeValue(returnType);
    }

    private Object getDefaultReturnTypeValue(Class<?> returnType) {
        //原始数据类型
        if (returnType.isPrimitive()){
            if (returnType == int.class){
                return 0;
            }else if (returnType == boolean.class){
                return false;
            }else if (returnType == char.class){
                return 'A';
            }else if (returnType == double.class){
                return 1.1d;
            }
        }
        return null;
    }


}
