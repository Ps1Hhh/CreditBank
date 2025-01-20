package creditbank.deal.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

/**
 * Класс конфигурации Swagger.
 */
@OpenAPIDefinition(
        info = @Info(
                title = "Deal API",
                description = "API для обработки заявки на кредит. Сохраняет в БД данные о клиенте и его кредитах.", version = "1.0.0",
                contact = @Contact(
                        name = "Sergey Esipov",
                        email = "sergeyesipov7@gmail.com"
                )
        )
)
public class OpenApiConfig {

}