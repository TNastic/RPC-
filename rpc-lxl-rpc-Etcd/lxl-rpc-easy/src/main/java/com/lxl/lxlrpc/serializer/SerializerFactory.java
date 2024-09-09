package com.lxl.lxlrpc.serializer;

import java.util.HashMap;
import java.util.Map;

public class SerializerFactory {

    /**
     * 序列化器map
     */
    private static final Map<String,Serializer> KEY_SERIALIZER_MAP = new HashMap<String, Serializer>(){{
            put(SerializerKeys.JDK,new JdkSerializer());
            put(SerializerKeys.JSON,new JsonSerializer());
            put(SerializerKeys.HESSIAN,new HessianSerializer());
            put(SerializerKeys.KRYO,new KryoSerializer());
    }};

    /**
     * 默认序列化器
     */
    private static final Serializer DEFAULT_SERIALIZER = KEY_SERIALIZER_MAP.get(SerializerKeys.JDK);


    public static Serializer getInstance(String key){
        return KEY_SERIALIZER_MAP.getOrDefault(key,DEFAULT_SERIALIZER);
    }


}
