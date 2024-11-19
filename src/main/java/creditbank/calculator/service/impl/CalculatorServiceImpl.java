package creditbank.calculator.service.impl;

import creditbank.calculator.dto.*;
import creditbank.calculator.service.CalculatorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class CalculatorServiceImpl implements CalculatorService {

    private static final BigDecimal BASE_RATE = new BigDecimal("10.0"); // Базовая ставка 10%
    private static final BigDecimal INSURANCE_DISCOUNT = new BigDecimal("3.0"); // Скидка за страховку
    private static final BigDecimal SALARY_CLIENT_DISCOUNT = new BigDecimal("1.0"); // Скидка для зарплатных клиентов
    private static final BigDecimal INSURANCE_COST = new BigDecimal("100000"); // Стоимость страховки

    @Override
    public List<LoanOfferDto> generateLoanOffers(LoanStatementRequestDto request) {
        log.info("Generating loan offers for request: {}", request);

        List<LoanOfferDto> offers = new ArrayList<>();
        for (boolean isInsuranceEnabled : new boolean[]{false, true}) {
            for (boolean isSalaryClient : new boolean[]{false, true}) {
                BigDecimal rate = BASE_RATE
                        .subtract(isInsuranceEnabled ? INSURANCE_DISCOUNT : BigDecimal.ZERO)
                        .subtract(isSalaryClient ? SALARY_CLIENT_DISCOUNT : BigDecimal.ZERO);

                BigDecimal totalAmount = request.getAmount();
                if (isInsuranceEnabled) {
                    totalAmount = totalAmount.add(INSURANCE_COST);
                }

                BigDecimal monthlyPayment = calculateMonthlyPayment(totalAmount, rate, request.getTerm());

                LoanOfferDto offer = new LoanOfferDto(
                        UUID.randomUUID(),
                        request.getAmount(),
                        totalAmount,
                        request.getTerm(),
                        monthlyPayment,
                        rate,
                        isInsuranceEnabled,
                        isSalaryClient
                );
                offers.add(offer);
            }
        }

        // Сортируем от худшего к лучшему предложению
        offers.sort((o1, o2) -> o1.getRate().compareTo(o2.getRate()));
        return offers;
    }

    @Override
    public CreditDto calculateLoan(ScoringDataDto scoringData) {
        log.info("Calculating loan for scoring data: {}", scoringData);

        BigDecimal rate = BASE_RATE
                .subtract(scoringData.getIsInsuranceEnabled() ? INSURANCE_DISCOUNT : BigDecimal.ZERO)
                .subtract(scoringData.getIsSalaryClient() ? SALARY_CLIENT_DISCOUNT : BigDecimal.ZERO);

        BigDecimal totalAmount = scoringData.getAmount();
        if (scoringData.getIsInsuranceEnabled()) {
            totalAmount = totalAmount.add(INSURANCE_COST);
        }

        BigDecimal monthlyPayment = calculateMonthlyPayment(totalAmount, rate, scoringData.getTerm());
        BigDecimal psk = calculatePsk(totalAmount, rate, scoringData.getTerm());
        List<PaymentScheduleElementDto> schedule = generatePaymentSchedule(totalAmount, rate, scoringData.getTerm());

        return new CreditDto(totalAmount, scoringData.getTerm(), monthlyPayment, rate, psk,
                scoringData.getIsInsuranceEnabled(), scoringData.getIsSalaryClient(), schedule);
    }

    private BigDecimal calculateMonthlyPayment(BigDecimal amount, BigDecimal rate, int term) {
        BigDecimal monthlyRate = rate.divide(BigDecimal.valueOf(12 * 100), BigDecimal.ROUND_HALF_UP);
        return amount.multiply(monthlyRate)
                .divide(BigDecimal.ONE.subtract(BigDecimal.ONE.divide(
                        BigDecimal.ONE.add(monthlyRate).pow(term), BigDecimal.ROUND_HALF_UP)), BigDecimal.ROUND_HALF_UP);
    }

    private BigDecimal calculatePsk(BigDecimal amount, BigDecimal rate, int term) {
        // Полная стоимость кредита, например, сумма всех платежей
        BigDecimal totalPayment = calculateMonthlyPayment(amount, rate, term).multiply(BigDecimal.valueOf(term));
        return totalPayment.subtract(amount);
    }

    private List<PaymentScheduleElementDto> generatePaymentSchedule(BigDecimal amount, BigDecimal rate, int term) {
        List<PaymentScheduleElementDto> schedule = new ArrayList<>();
        BigDecimal remainingDebt = amount;
        BigDecimal monthlyRate = rate.divide(BigDecimal.valueOf(12 * 100), BigDecimal.ROUND_HALF_UP);
        BigDecimal monthlyPayment = calculateMonthlyPayment(amount, rate, term);

        for (int i = 1; i <= term; i++) {
            BigDecimal interestPayment = remainingDebt.multiply(monthlyRate);
            BigDecimal debtPayment = monthlyPayment.subtract(interestPayment);
            remainingDebt = remainingDebt.subtract(debtPayment);

            schedule.add(new PaymentScheduleElementDto(i, LocalDate.now().plusMonths(i),
                    monthlyPayment, interestPayment, debtPayment, remainingDebt));
        }

        return schedule;
    }
}