package creditbank.deal.service;

import creditbank.deal.dto.FinishRegistrationRequestDto;
import creditbank.deal.dto.LoanOfferDto;
import creditbank.deal.dto.LoanStatementRequestDto;
import creditbank.deal.exception.DefaultException;
import creditbank.deal.exception.ScoringDeniedException;

import java.util.List;

public interface IDealService {

    /**
     * Создает заявку на кредит.
     *
     * @param statementRequest запрос на создание заявки.
     * @return список кредитных предложений.
     * @throws DefaultException в случае ошибки.
     */
    List<LoanOfferDto> createStatement(LoanStatementRequestDto statementRequest) throws DefaultException;

    /**
     * Выбирает предложение из списка кредитных предложений.
     *
     * @param appliedOffer выбранное предложение.
     */
    void selectOffer(LoanOfferDto appliedOffer);

    /**
     * Создает кредит на основе завершённой регистрации.
     *
     * @param finishRequest запрос с данными для завершения регистрации.
     * @param statementId идентификатор заявки.
     * @throws ScoringDeniedException в случае отказа в скоринге.
     * @throws DefaultException в случае ошибки.
     */
    void createCredit(FinishRegistrationRequestDto finishRequest, String statementId)
            throws ScoringDeniedException, DefaultException;
}