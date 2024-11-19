package creditbank.calculator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentScheduleElementDto {
    private Integer number;          // Номер платежа
    private LocalDate date;          // Дата платежа
    private BigDecimal totalPayment; // Общая сумма платежа
    private BigDecimal interestPayment; // Сумма процентов
    private BigDecimal debtPayment;  // Основной долг
    private BigDecimal remainingDebt; // Остаток долга
}