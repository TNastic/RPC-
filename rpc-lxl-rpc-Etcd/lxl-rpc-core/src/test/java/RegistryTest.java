import com.lxl.lxlrpc.config.RegistryConfig;
import com.lxl.lxlrpc.model.ServiceMetaInfo;
import com.lxl.lxlrpc.registry.EtcdRegistry;
import com.lxl.lxlrpc.registry.Registry;
import org.junit.Before;
import org.junit.Test;

public class RegistryTest {

    final Registry registry = new EtcdRegistry();

    @Before
    public void init(){
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress("http://localhost:2379");
        registry.init(registryConfig);
    }

    @Test
    public void register() throws Exception {
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName("myService");
        serviceMetaInfo.setServiceHost("localhost");
        serviceMetaInfo.setServiceVersion("1.0");
        serviceMetaInfo.setServicePort(1234);
        registry.register(serviceMetaInfo);
    }
}
