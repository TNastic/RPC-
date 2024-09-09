package com.lxl.lxlrpc.proxy;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.lxl.lxlrpc.model.RpcRequest;
import com.lxl.lxlrpc.model.RpcResponse;
import com.lxl.lxlrpc.serializer.JdkSerializer;
import com.lxl.lxlrpc.serializer.Serializer;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ServiceProxy implements InvocationHandler {

    //proxy被代理的对象
//    @Override
//    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//        Serializer serializer = new JdkSerializer();
//        //构建请求对象
//        RpcRequest rpcRequest = RpcRequest.builder()
//                .serviceName(method.getDeclaringClass().getName())
//                .methodName(method.getName())
//                .parametersTypes(method.getParameterTypes())
//                .args(args)
//                .build();
//        //序列化为字节数组便于网络传输
//        byte[] bodyBytes = serializer.serializer(rpcRequest);
//        try (HttpResponse httpResponse = HttpRequest.post("http://localhost:8080").body(bodyBytes).execute()){
//            byte[] result = httpResponse.bodyBytes();
//            RpcResponse response = serializer.deserializer(result, RpcResponse.class);
//            return response.getData();
//        }catch (IOException e){
//            e.printStackTrace();
//        }
//        return null;
//    }

    /**
     * 调用代理
     *
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 指定序列化器
        Serializer serializer = new JdkSerializer();

        // 构造请求
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parametersTypes(method.getParameterTypes())
                .args(args)
                .build();
        try {
            // 序列化
            byte[] bodyBytes = serializer.serialize(rpcRequest);
            // 发送请求
            // todo 注意，这里地址被硬编码了（需要使用注册中心和服务发现机制解决）
            try (HttpResponse httpResponse = HttpRequest.post("http://localhost:8080")
                    .body(bodyBytes)
                    .execute()) {
                byte[] result = httpResponse.bodyBytes();
                // 反序列化
                RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
                return rpcResponse.getData();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


}
