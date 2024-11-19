package creditbank.calculator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailMessage {
    private String address; // Email адрес получателя
    private EmailTheme theme; // Тема письма
    private Long statementId; // ID заявки
    private String text;      // Текст письма
}