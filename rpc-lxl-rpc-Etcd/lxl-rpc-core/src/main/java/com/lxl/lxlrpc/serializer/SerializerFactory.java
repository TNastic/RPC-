package com.lxl.lxlrpc.serializer;

import com.lxl.lxlrpc.spi.SpiLoader;

import java.util.HashMap;
import java.util.Map;

public class SerializerFactory {

//    /**
//     * 序列化器map
//     */
//    private static final Map<String,Serializer> KEY_SERIALIZER_MAP = new HashMap<String, Serializer>(){{
//            put(SerializerKeys.JDK,new JdkSerializer());
//            put(SerializerKeys.JSON,new JsonSerializer());
//            put(SerializerKeys.HESSIAN,new HessianSerializer());
//            put(SerializerKeys.KRYO,new KryoSerializer());
//    }};
//
//    /**
//     * 默认序列化器
//     */
//    private static final Serializer DEFAULT_SERIALIZER = KEY_SERIALIZER_MAP.get(SerializerKeys.JDK);
//
//
//    public static Serializer getInstance(String key){
//        return KEY_SERIALIZER_MAP.getOrDefault(key,DEFAULT_SERIALIZER);
//    }

    static {
        SpiLoader.load(Serializer.class);
    }


    public static final Serializer DEFAULT_SERIALIZER = new JdkSerializer();

    /**
     * 获取对应的实例
     * @param key
     * @return
     */
    public static Serializer getInstance(String key){
        return SpiLoader.getInstance(Serializer.class,key);
    }


}
