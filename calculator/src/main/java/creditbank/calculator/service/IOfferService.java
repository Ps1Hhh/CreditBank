package creditbank.calculator.service;

import creditbank.calculator.dto.LoanOfferDto;
import creditbank.calculator.dto.LoanStatementRequestDto;
import creditbank.calculator.exception.LaterBirthdateException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface IOfferService {

    /**
     * Проверяет совершеннолетие пользователя.
     *
     * @param date Дата рождения пользователя
     * @throws LaterBirthdateException Ошибка - пользователь несовершеннолетний.
     */
    void isDateLate(LocalDate date) throws LaterBirthdateException;

    /**
     * Рассчитывает список из 4-х возможных предложений по кредиту.
     *
     * @param request Запрос на кредит
     * @return Список из 4-х возможных предложений по кредиту
     */
    List<LoanOfferDto> getOfferList(LoanStatementRequestDto request);

    /**
     * Рассчитывает ежемесячный аннуитетный платёж по кредиту.
     *
     * @param offerRate Ставка кредитного предложения
     * @param offerRequestedAmount Запрошенная сумма кредита
     * @param term Срок кредита
     * @return Ежемесячный аннуитетный платёж
     */
    BigDecimal calculateMonthlyPayment(BigDecimal offerRate, BigDecimal offerRequestedAmount, int term);

    /**
     * Рассчитывает полную стоимость кредита.
     *
     * @param requestedAmount Запрошенная сумма кредита
     * @param monthlyPayment Ежемесячный платёж
     * @param rate Процентная ставка
     * @return Полная стоимость кредита
     */
    BigDecimal calculatePsk(BigDecimal requestedAmount, BigDecimal monthlyPayment, BigDecimal rate);
}
