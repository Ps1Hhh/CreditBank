package creditbank.gateway.dto.entity;

import creditbank.gateway.dto.PaymentScheduleElementDto;
import creditbank.gateway.dto.enums.CreditStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@ToString
@Builder
@AllArgsConstructor
public class Credit {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID creditId;

    private BigDecimal amount;

    private Integer term;

    private BigDecimal monthlyPayment;

    private BigDecimal rate;

    private BigDecimal psk;

    @JdbcTypeCode(SqlTypes.JSON)
    private List<PaymentScheduleElementDto> paymentSchedule;

    private boolean insuranceEnabled;

    private boolean salaryClient;

    @Enumerated(EnumType.STRING)
    private CreditStatus creditStatus;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Credit credit = (Credit) o;
        return insuranceEnabled == credit.insuranceEnabled && salaryClient == credit.salaryClient && Objects.equals(creditId, credit.creditId) && Objects.equals(amount, credit.amount) && Objects.equals(term, credit.term) && Objects.equals(monthlyPayment, credit.monthlyPayment) && Objects.equals(rate, credit.rate) && Objects.equals(psk, credit.psk) && Objects.equals(paymentSchedule, credit.paymentSchedule) && creditStatus == credit.creditStatus;
    }

}