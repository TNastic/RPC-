import com.lxl.example.common.service.StudentService;
import com.lxl.lxlrpc.proxy.MockServiceProxy;
import com.lxl.lxlrpc.proxy.MockServiceProxyFactory;
import com.lxl.lxlrpc.serializer.Serializer;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

public class MockProxyTest {

    @Test
    public void mockTest(){
        //获取代理对象
        //jdk动态代理的实现是基于接口的
        StudentService studentService = MockServiceProxyFactory.getMockProxy(StudentService.class);
        System.out.println(studentService.getStudentAge());
    }


    @Test
    public void spiTest(){
        Serializer serializer = null;
        ServiceLoader<Serializer> serviceLoader = ServiceLoader.load(Serializer.class);
        for (Serializer service : serviceLoader){
            serializer = service;
        }
        System.out.println(serializer);
    }

    @Test
    public void mapTest(){
        Map<Integer,Integer> map = new HashMap<Integer,Integer>(){{
           put(0,0);
           put(1,1);
            put(2,2);
            put(3,3);
            put(4,4);
            put(5,5);
            put(0,6);
            put(0,7);
        }};

        System.out.println(map.get(0));
    }

    @Test
    public boolean containsNearbyDuplicate(int[] nums, int k){
        Map<Integer,Integer> map = new HashMap<>();
        //记录每个值和索引位置
        for (int i = 0; i < nums.length; i++){
            map.put(nums[i],i);
        }
        for (int i = 0; i < nums.length; i++){
            //如果包含
            if (map.containsKey(nums[i])){
                int j = Math.abs(map.get(nums[i])-i);
                if (j <= k){
                    return true;
                }
            }
        }
        return false;
    }
}
