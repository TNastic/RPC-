package com.lxl.lxlrpc.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.lxl.lxlrpc.application.RpcApplication;
import com.lxl.lxlrpc.config.RpcConfig;
import com.lxl.lxlrpc.constant.RpcConstant;
import com.lxl.lxlrpc.model.RpcRequest;
import com.lxl.lxlrpc.model.RpcResponse;
import com.lxl.lxlrpc.model.ServiceMetaInfo;
import com.lxl.lxlrpc.registry.RegisterFactory;
import com.lxl.lxlrpc.registry.Registry;
import com.lxl.lxlrpc.serializer.JdkSerializer;
import com.lxl.lxlrpc.serializer.Serializer;
import com.lxl.lxlrpc.serializer.SerializerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

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
        //利用spi机制完成序列化器的加载
        Serializer serializer = SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());
        // 指定序列化器
        String serviceName = method.getDeclaringClass().getName();
        // 构造请求
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(serviceName)
                .methodName(method.getName())
                .parametersTypes(method.getParameterTypes())
                .args(args)
                .build();
        try {
            // 序列化
            byte[] bodyBytes = serializer.serialize(rpcRequest);
            // 发送请求
            RpcConfig rpcConfig = RpcApplication.getRpcConfig();
            //
            Registry registry = RegisterFactory.getInstance(rpcConfig.getRegistryConfig().getRegister());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
            List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
            if (CollUtil.isEmpty(serviceMetaInfoList)){
                throw new RuntimeException("暂无服务地址");
            }
            ServiceMetaInfo selectedServiceInfo = serviceMetaInfoList.get(0);
            try (HttpResponse httpResponse = HttpRequest.post(selectedServiceInfo.getServiceAddress())
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
