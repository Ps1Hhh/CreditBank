package creditbank.statement.service;

import creditbank.statement.dto.LoanOfferDto;
import creditbank.statement.dto.LoanStatementRequestDto;
import creditbank.statement.exception.DefaultException;
import creditbank.statement.exception.LaterBirthdateException;
import creditbank.statement.interfaces.DealClient;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class StatementServiceTest implements IStatementService{

    private final DealClient dealClient = new DealClientTest();

    private final StatementService statementService = new StatementService(dealClient);

    private LoanStatementRequestDto getLoanStatementRequest() {
        return LoanStatementRequestDto.builder()
                .amount(new BigDecimal("100000"))
                .term(20)
                .firstName("Иван")
                .lastName("Иванов")
                .middleName("Иванович")
                .email("ivanov@yandex.ru")
                .birthdate(LocalDate.now())
                .passportSeries("0000")
                .passportNumber("000000")
                .build();
    }

    private List<LoanOfferDto> getLoanOffers() {
        LoanOfferDto example = LoanOfferDto.builder()
                .requestedAmount(new BigDecimal("100000"))
                .totalAmount(new BigDecimal("113636.81"))
                .term(20)
                .monthlyPayment(new BigDecimal("5682.04"))
                .rate(new BigDecimal("0.15"))
                .isInsuranceEnabled(false)
                .isSalaryClient(false)
                .build();
        return List.of(example, example, example, example);
    }

    @Test
    public void getOffers() throws LaterBirthdateException, DefaultException {
        LoanStatementRequestDto request = getLoanStatementRequest();
        LocalDate date = request.getBirthdate();

        assertThrows(LaterBirthdateException.class,
                () -> statementService.getOffers(request));

        request.setBirthdate(date.plusDays(1));
        assertThrows(LaterBirthdateException.class,
                () -> statementService.getOffers(request));

        request.setBirthdate(date.minusYears(18));
        assertDoesNotThrow(() -> statementService.getOffers(request));

        request.setBirthdate(date.minusYears(40));
        assertDoesNotThrow(() -> statementService.getOffers(request));

        assertEquals(getLoanOffers(), statementService.getOffers(request));

    }

    @Override
    public List<LoanOfferDto> getOffers(LoanStatementRequestDto loanStatementRequestDto) throws LaterBirthdateException, DefaultException {
        return List.of();
    }

    @Override
    public void selectOffer(LoanOfferDto loanOfferDto) throws DefaultException {

    }
}
