package com.lxl.lxlrpc.spi;

import cn.hutool.core.io.resource.ResourceUtil;
import com.lxl.lxlrpc.serializer.Serializer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 加载配置中配置的类对象
 */
public class SpiLoader {

    /**
     * 存储已经加载的类 (接口名 => (key => 实现类))
     */
    private static Map<String,Map<String,Class<?>>> loaderMap = new ConcurrentHashMap<>();

    /**
     * 存储加载的对象实例
     */
    private static Map<String,Object> instanceCache = new ConcurrentHashMap<>();


    /**
     * 系统的SPI目录
     */
    private static final String RPC_SYSTEM_SPI_DIR = "META-INF/rpc/system/";


    /**
     * 用户的SPI目录
     */
    private static final String RPC_CUSTOM_SPI_DIR = "META-INF/rpc/custom/";

    /**
     * 扫描路径
     */
    private static final String[] SCAN_DIRS = new String[]{RPC_SYSTEM_SPI_DIR,RPC_CUSTOM_SPI_DIR};


    /**
     * 动态加载的类列表
     */
    private static final List<Class<?>> LOAD_CLASS_LIST = Arrays.asList(Serializer.class);

    /**
     * 加载所有类型
     */
    public static void loadAll() {
        for (Class<?> aClass : LOAD_CLASS_LIST) {
            load(aClass);
        }
    }

    /**
     * 加载某个类型
     * @param loadClass
     * @return
     */
    public static Map<String,Class<?>> load(Class<?> loadClass){
        System.out.println(String.format("加载类型为%s的SPI",loadClass.getName()));
        Map<String,Class<?>> keyClassMap = new HashMap<>();
        for (String scanDir : SCAN_DIRS){
            List<URL> resources = ResourceUtil.getResources(scanDir+loadClass.getName());
            //读取每个资源文件
            for (URL resource : resources){
                try {
                    InputStreamReader inputStreamReader = new InputStreamReader(resource.openStream());
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        String[] strArray = line.split("=");
                        if (strArray.length > 1) {
                            String key = strArray[0];
                            String className = strArray[1];
                            keyClassMap.put(key, Class.forName(className));
                        }
                    }
                } catch (Exception e) {
                    System.out.println("spi load error");
                    throw new RuntimeException(e);
                }
            }
        }
        loaderMap.put(loadClass.getName(),keyClassMap);
        return keyClassMap;
    }


    /**
     * 获取某个接口的某个实例
     * @param tClass
     * @param key
     * @return
     * @param <T>
     */
    public static <T> T getInstance(Class<T> tClass,String key){
        //1.看loadMap中有没有
        String tClassName = tClass.getName();
        Map<String, Class<?>> keyClassMap = loaderMap.get(tClassName);
        if (keyClassMap == null){
            throw new RuntimeException(String.format("未加载 %s 类型",tClassName));
        }
        //有该类型，但是没有key对应的对象
        if (!keyClassMap.containsKey(key)){
            throw new RuntimeException(String.format("未加载key=%s的%s对象",key,tClassName));
        }
        //2.从instanceCache中取出对应的对象
        Class<?> implClass = keyClassMap.get(key);
        String implClassName = implClass.getName();
        //缓存中没有
        if (!instanceCache.containsKey(implClassName)){
            try {
                //加入
                instanceCache.put(implClassName,implClass.newInstance());
            } catch (Exception e) {
                String errorMsg = String.format("类%s实例化失败", implClassName);
                throw new RuntimeException(errorMsg);
            }
        }
        return (T) instanceCache.get(implClassName);
    }




}
