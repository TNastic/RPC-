package com.lxl.lxlrpc.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.dialect.Props;

/**
 * 加载配置文件工具
 */
public class ConfigUtils {

    //返回配置类对象
    public static <T> T loadConfig(Class<T> tClass,String prefix){
        return loadConfig(tClass,prefix,"");
    }

    /**
     * 加载配置类转换为配置对象
     * @param tClass
     * @param prefix
     * @param environment
     * @return
     * @param <T>
     */
    public static <T> T loadConfig(Class<T> tClass,String prefix,String environment){
        //拼接资源文件路径
        StringBuilder filePathBuilder = new StringBuilder("application");
        if (StrUtil.isNotBlank(environment)){
            filePathBuilder.append("-").append(environment);
        }
        //todo 支持加载多种配置文件，比如yml,yaml等文件
        filePathBuilder.append(".properties");
        //转换为配置对象
        Props props = new Props(filePathBuilder.toString());
        return props.toBean(tClass,prefix);

    }
}
