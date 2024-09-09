package com.lxl.lxlrpc.serializer;

import java.io.IOException;

/**
 * 序列化器
 */
public interface Serializer {

    //序列化，将java对象转换为可传输的字节数组
    <T> byte[] serialize(T object) throws IOException;

    //反序列化，将Java对象还原
    <T> T deserialize(byte[] bytes,Class<T> type) throws IOException;

}
