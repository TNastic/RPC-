package com.lxl.lxlrpc.server;


import com.lxl.lxlrpc.model.RpcRequest;
import com.lxl.lxlrpc.model.RpcResponse;
import com.lxl.lxlrpc.registry.LocalRegistry;
import com.lxl.lxlrpc.serializer.JdkSerializer;
import com.lxl.lxlrpc.serializer.Serializer;
import com.lxl.lxlrpc.serializer.SerializerFactory;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;

import java.io.IOException;
import java.lang.reflect.Method;


public class HttpServerHandler implements Handler<HttpServerRequest> {

    @Override
    public void handle(HttpServerRequest request) {

        //序列化器
        Serializer serializer = new JdkSerializer();


        System.out.println("Receive Request: "+request.method()+" "+request.uri());

        request.bodyHandler( body ->{
            byte[] bytes = body.getBytes();
            RpcRequest rpcRequest = null;
            try {
                //1.序列化为Java对象
                rpcRequest = serializer.deserialize(bytes, RpcRequest.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            //响应，如果响应为空则直接返回
            RpcResponse rpcResponse = new RpcResponse();
            if ( rpcRequest == null){
                rpcResponse.setMessage("rpcRequest is null");
                doResponse(request,rpcResponse,serializer);
                return;
            }

            //根据反射和传来的参数来调用对应的方法
            try {
                Class<?> implClass = LocalRegistry.get(rpcRequest.getServiceName());
                Method method = implClass.getMethod(rpcRequest.getMethodName(), rpcRequest.getParametersTypes());
                Object result = method.invoke(implClass.newInstance(), rpcRequest.getArgs());
                //设置返回的对象
                rpcResponse.setData(result);
                rpcResponse.setDataType(method.getReturnType());
                rpcResponse.setMessage("ok");
            } catch (Exception e) {
                e.printStackTrace();
                rpcResponse.setMessage(e.getMessage());
                rpcResponse.setException(e);
            }
            //响应
            doResponse(request,rpcResponse,serializer);
        });

    }

    void doResponse(HttpServerRequest request,RpcResponse rpcResponse,Serializer serializer){
        HttpServerResponse httpServerResponse = request.response()
                .putHeader("content-type","application/json");
        try {
            byte[] serialized = serializer.serialize(rpcResponse);
            httpServerResponse.end(Buffer.buffer(serialized));
        } catch (IOException e) {
            e.printStackTrace();
            httpServerResponse.end(Buffer.buffer());
        }
    }

}
