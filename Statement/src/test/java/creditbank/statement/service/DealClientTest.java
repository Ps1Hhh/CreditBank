package creditbank.statement.service;

import creditbank.statement.dto.LoanOfferDto;
import creditbank.statement.dto.LoanStatementRequestDto;
import creditbank.statement.interfaces.DealClient;

import java.math.BigDecimal;
import java.util.List;

public class DealClientTest implements DealClient {

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

    @Override
    public List<LoanOfferDto> getLoanOffers(LoanStatementRequestDto loanStatementRequestDto) {
        return getLoanOffers();
    }

    @Override
    public void selectOffer(LoanOfferDto loanOfferDto) {}
}