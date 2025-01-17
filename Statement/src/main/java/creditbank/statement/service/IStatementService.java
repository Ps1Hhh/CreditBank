package creditbank.statement.service;

import creditbank.statement.dto.LoanOfferDto;
import creditbank.statement.dto.LoanStatementRequestDto;
import creditbank.statement.exception.DefaultException;
import creditbank.statement.exception.LaterBirthdateException;

import java.util.List;

/**
 * Интерфейс для сервиса StatementService.
 */
public interface IStatementService {

    /**
     * Получение списка кредитных предложений.
     *
     * @param loanStatementRequestDto запрос на получение кредитных предложений
     * @return список кредитных предложений
     * @throws LaterBirthdateException если пользователь несовершеннолетний
     * @throws DefaultException если возникает ошибка при взаимодействии с внешними системами
     */
    List<LoanOfferDto> getOffers(LoanStatementRequestDto loanStatementRequestDto)
            throws LaterBirthdateException, DefaultException;

    /**
     * Выбор конкретного кредитного предложения.
     *
     * @param loanOfferDto данные выбранного кредитного предложения
     * @throws DefaultException если возникает ошибка при взаимодействии с внешними системами
     */
    void selectOffer(LoanOfferDto loanOfferDto) throws DefaultException;
}
