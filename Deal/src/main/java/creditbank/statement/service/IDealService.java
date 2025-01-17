package creditbank.statement.service;

import creditbank.statement.dto.FinishRegistrationRequestDto;
import creditbank.statement.dto.LoanOfferDto;
import creditbank.statement.dto.LoanStatementRequestDto;
import creditbank.statement.exception.DefaultException;
import creditbank.statement.exception.ScoringDeniedException;

import java.util.List;

public interface IDealService {

    /**
     * Создаёт новую заявку на кредит на основе данных клиента и возвращает список предложений.
     *
     * @param statementRequest запрос с данными клиента для расчёта предложений кредита.
     * @return список предложений кредита (от худшего к лучшему).
     */
    List<LoanOfferDto> createStatement(LoanStatementRequestDto statementRequest) throws DefaultException;

    /**
     * Обрабатывает выбор кредитного предложения клиентом и обновляет статус заявки.
     *
     * @param appliedOffer выбранное кредитное предложение.
     */
    void selectOffer(LoanOfferDto appliedOffer);

    /**
     * Завершает процесс регистрации, отправляет данные для скоринга и создаёт сущность кредита.
     *
     * @param finishRequest объект с завершённой регистрацией клиента.
     * @param statementId идентификатор заявки.
     * @throws ScoringDeniedException если скоринг отклонён.
     */
    void createCredit(FinishRegistrationRequestDto finishRequest, String statementId) throws ScoringDeniedException, DefaultException;
}