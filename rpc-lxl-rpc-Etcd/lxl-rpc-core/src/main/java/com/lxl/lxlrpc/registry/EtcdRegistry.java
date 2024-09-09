package com.lxl.lxlrpc.registry;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ConcurrentHashSet;
import cn.hutool.core.lang.Console;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import cn.hutool.json.JSONUtil;
import com.lxl.lxlrpc.config.RegistryConfig;
import com.lxl.lxlrpc.model.ServiceMetaInfo;
import io.etcd.jetcd.*;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.options.PutOption;
import io.etcd.jetcd.watch.WatchEvent;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;


public class EtcdRegistry implements Registry{

    private Client client;

    private KV kcClient;

    private static final String ETCD_ROOT_PATH = "/rpc/";

    /**
     * 本地缓存已注册节点的key信息
     */
    private final Set<String> localRegisterNodeKeySet = new HashSet<>();

    /**
     * 正在监听的key集合
     */
    private final Set<String> watchingKeySet = new ConcurrentHashSet<>();

    /**
     * 注册中心服务缓存
     */
    private final RegistryServiceCache registryServiceCache = new RegistryServiceCache();

    @Override
    public void init(RegistryConfig registerConfig) {
        //设置
        client = Client.builder().endpoints(registerConfig.getAddress())
                .connectTimeout(Duration.ofMillis(registerConfig.getTimeout())).build();
        kcClient = client.getKVClient();
        //心跳检测
        heartBeat();
    }

    @Override
    public void register(ServiceMetaInfo serviceMetaInfo) throws Exception {
        Lease leaseClient = client.getLeaseClient();
        //设置30s的租约
        long leaseId = leaseClient.grant(30).get().getID();

        //获取key值
        String registerKey = ETCD_ROOT_PATH + serviceMetaInfo.getServiceNodeKey();
        ByteSequence key = ByteSequence.from(registerKey, StandardCharsets.UTF_8);
        ByteSequence value = ByteSequence.from(JSONUtil.toJsonStr(serviceMetaInfo), StandardCharsets.UTF_8);

        //将键值与租约关联起来，并设置过期时间
        PutOption putOption = PutOption.builder().withLeaseId(leaseId).build();
        kcClient.put(key,value,putOption).get();

        //本地加入缓存信息
        localRegisterNodeKeySet.add(registerKey);
    }

    @Override
    public void unRegister(ServiceMetaInfo serviceMetaInfo) {
        String registerKey = ETCD_ROOT_PATH + serviceMetaInfo.getServiceNodeKey();
        //删除某个键
        kcClient.delete(ByteSequence.from(registerKey,StandardCharsets.UTF_8));

        //删除本地的节点
        localRegisterNodeKeySet.remove(registerKey);
    }

    /**
     * 消费者服务发现
     * @param serviceKey
     * @return
     */
    @Override
    public List<ServiceMetaInfo> serviceDiscovery(String serviceKey) {
        List<ServiceMetaInfo> cachedServiceMetaInfo = registryServiceCache.readCache();
        if (cachedServiceMetaInfo != null){
            return cachedServiceMetaInfo;
        }
        //前缀搜索
        String searchPrefix = ETCD_ROOT_PATH + serviceKey + "/";
        GetOption getOption = GetOption.builder().isPrefix(true).build();
        try {
            List<KeyValue> keyValues = kcClient.get(ByteSequence.from(searchPrefix, StandardCharsets.UTF_8), getOption)
                    .get().getKvs();
            //解析返回服务信息
            List<ServiceMetaInfo> serviceMetaInfos = keyValues.stream().map(keyValue -> {
                String value = keyValue.getValue()
                        .toString(StandardCharsets.UTF_8);
                return JSONUtil.toBean(value, ServiceMetaInfo.class);
            }).collect(Collectors.toList());
            registryServiceCache.writeCache(serviceMetaInfos);
            return serviceMetaInfos;
        } catch (Exception e) {
            throw new RuntimeException("获取服务列表失败",e);
        }
    }

    @Override
    public void destroy() {
        System.out.println("当前节点下线");
        for (String key : localRegisterNodeKeySet){
            try {
                kcClient.delete(ByteSequence.from(key,StandardCharsets.UTF_8)).get();
            } catch (Exception e) {
                throw new RuntimeException(key + "节点下线失败");
            }
        }
        if (kcClient != null) {
            kcClient.close();
        }
        if (client != null){
            client.close();
        }
    }

    /**
     * 心跳检测
     */
    @Override
    public void heartBeat() {
        //10s执行一次检测
        CronUtil.schedule("*/10 * * * * *", new Task() {
            @Override
            public void execute() {
                //遍历节点
                for (String key : localRegisterNodeKeySet){
                    //获得该key所有的values值
                    try {
                        List<KeyValue> keyValues
                                = kcClient.get(ByteSequence.from(key, StandardCharsets.UTF_8)).get().getKvs();
                        //该节点已过期
                        if (CollUtil.isEmpty(keyValues)){
                            continue;
                        }
                        KeyValue keyValue = keyValues.get(0);
                        //重新注册
                        String value = keyValue.getValue().toString(StandardCharsets.UTF_8);
                        ServiceMetaInfo serviceMetaInfo = JSONUtil.toBean(value, ServiceMetaInfo.class);
                        register(serviceMetaInfo);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

    // 支持秒级别定时任务
        CronUtil.setMatchSecond(true);
        CronUtil.start();

    }

    /**
     * 消费端监听
     * @param serviceNodeKey
     */
    @Override
    public void watch(String serviceNodeKey) {
        Watch watchClient = client.getWatchClient();
        boolean newWatch = watchingKeySet.add(serviceNodeKey);
        //新节点
        if (newWatch){
            watchClient.watch(ByteSequence.from(serviceNodeKey,StandardCharsets.UTF_8),response -> {
                for (WatchEvent watchEvent: response.getEvents()){
                    switch (watchEvent.getEventType()){
                        case DELETE:
                            registryServiceCache.clearCache();
                            break;
                        case PUT:
                        default:
                            break;
                    }
                }
            });

        }
    }


//    public static void main(String[] args) throws ExecutionException, InterruptedException {
//
//        // create client using endpoints
//        Client client = Client.builder().endpoints("http://localhost:2379").build();
//
//        KV kvClient = client.getKVClient();
//        ByteSequence key = ByteSequence.from("test_key".getBytes());
//        ByteSequence value = ByteSequence.from("test_value".getBytes());
//
//    // put the key-value
//        kvClient.put(key, value).get();
//
//    // get the CompletableFuture
//        CompletableFuture<GetResponse> getFuture = kvClient.get(key);
//
//    // get the value from CompletableFuture
//        GetResponse response = getFuture.get();
//
//        System.out.println(response);
//    // delete the key
////        kvClient.delete(key).get();
//    }
}
