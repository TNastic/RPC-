package com.lxl.lxlrpc.registry;

import com.lxl.lxlrpc.serializer.JdkSerializer;
import com.lxl.lxlrpc.serializer.Serializer;
import com.lxl.lxlrpc.spi.SpiLoader;

public class RegisterFactory {

    static {
        SpiLoader.load(Registry.class);
    }

    public static final Registry DEFAULT_Registry = new EtcdRegistry();

    /**
     * 获取对应的实例
     * @param key
     * @return
     */
    public static Registry getInstance(String key){
        return SpiLoader.getInstance(Registry.class,key);
    }
}
