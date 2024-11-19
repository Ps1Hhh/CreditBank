package creditbank.calculator.service;


import creditbank.calculator.dto.*;


import java.util.List;

public interface CalculatorService {
    List<LoanOfferDto> generateLoanOffers(LoanStatementRequestDto request);
    CreditDto calculateLoan(ScoringDataDto scoringData);
}