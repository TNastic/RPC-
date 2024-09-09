package com.lxl.lxlrpc.server;

import io.vertx.core.Vertx;

public class VertxHttpServer implements HttpServer{


    @Override
    public void doStart(int port) {

        Vertx vertx = Vertx.vertx();

        //创建http服务器
        io.vertx.core.http.HttpServer httpServer = vertx.createHttpServer();

        httpServer.requestHandler(new HttpServerHandler());



        httpServer.listen(port, result -> {
            if (result.succeeded()){
                System.out.println("Server is listening on port: "+port);
            }else {
                System.out.println("Server failed "+result.cause());
            }
        } );
    }
}
