package creditbank.dossier;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.PropertySource;

@SpringBootTest
@EnableFeignClients
@PropertySource("classpath:dossier.properties")
class DossierApplicationTests {

    @Test
    void contextLoads() {
    }

}
