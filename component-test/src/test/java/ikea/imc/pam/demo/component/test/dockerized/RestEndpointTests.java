package ikea.imc.pam.demo.component.test.dockerized;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class RestEndpointTests extends AbstractBaseTest{
    @Autowired RestClient restClient;

    @Test
    void testMe(){
        restClient.getStatus();
    }
}
