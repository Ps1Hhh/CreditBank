package creditbank.calculator.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Loan Calculator API")
                        .version("1.0.0")
                        .description("API для расчета кредитов")
                        .contact(new Contact()
                                .name("Sergey Esipov")
                                .email("sergeyesipov7@gmail.com")));
    }
}