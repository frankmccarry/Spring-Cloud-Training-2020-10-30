package integration.pa.spring.springcloud;

import com.pa.spring.springcloud.SpringCloudApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringCloudApplication.class)
public class SpringCloudApplicationTest {

    @SuppressWarnings("EmptyMethod")
    @Test
    public void springBoardApplication_whenLoaded_DoesNotError() {
        // N.B. this is an integration test to check that the Spring Context can load correctly
        // i.e. it checks whether the application is configured correctly
    }
}