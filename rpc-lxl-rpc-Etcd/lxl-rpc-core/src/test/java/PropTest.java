import cn.hutool.setting.dialect.Props;
import com.lxl.lxlrpc.config.RpcConfig;
import com.lxl.lxlrpc.serializer.Serializer;
import com.lxl.lxlrpc.serializer.SerializerFactory;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;


public class PropTest {

    @Test
    public void propTest(){

        Props props = new Props("application.properties");

        RpcConfig rpcConfig = props.toBean(RpcConfig.class);

        System.out.println(rpcConfig);

        System.out.println(props);

    }


//    @Test
//    public void mapTest(int[] nums){
//        HashMap<Integer,Integer> hashMap = new HashMap<>();
//        for (int i = 0;i < nums.length; i++){
//            if (hashMap.containsKey(nums[i])){
//                //如果存在就加一
//                Integer num = hashMap.get(nums[i]);
//                hashMap.replace(nums[i],num+1);
//            }else {
//                hashMap.put(nums[i],1);
//            }
//        }
//        //取出每个key以及对应的值
//        for (int i = 0;i< nums.length;i++){
//            if (hashMap.get(nums[i]) >= nums.length/2){
//                return nums[i];
//            }
//        }
//        return 0;
//    }
//
//
//    @Test
//    public void mapTestTwo(int[] nums){
//        int max = nums.length/2;
//        int count = 0;
//        for (int i = 0; i < nums.length; i++){
//            int number = nums[i];
//            count = 1;
//            for (int j = 1; j< nums.length; j++){
//                if (nums[j] == number){
//                    count++;
//                    if (count > max){
//                        return number;
//                    }
//                }
//            }
//        }
//        return 0;
//    }

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
    public void reflectTest(){
        System.out.println(Serializer.class.getName());
    }


    @Test
    public void spiTest(){
        //加载JDKSerializer
        Serializer serializer = SerializerFactory.getInstance("hession");
        System.out.println(serializer);
    }



}
