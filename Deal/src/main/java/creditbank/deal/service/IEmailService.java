package creditbank.deal.service;

import creditbank.deal.dto.enums.ApplicationStatus;
import creditbank.deal.dto.enums.ChangeType;

public interface IEmailService {

    /**
     * Отправляет документы в указанный Kafka-топик.
     * @param statementId идентификатор заявки
     * @param status      статус заявки
     */
    void sendDocuments(String statementId, ApplicationStatus status);

    /**
     * Отправляет код подтверждения в указанный Kafka-топик.
     * @param statementId идентификатор заявки
     */
    void sendCode(String statementId);

    /**
     * Отправляет сообщение о выдаче кредита в указанный Kafka-топик.
     * @param statementId идентификатор заявки
     * @param code        код подтверждения
     */
    void sendCreditIssuedMessage(String statementId, String code);

    /**
     * Изменяет статус заявки.
     * @param statementId идентификатор заявки
     * @param status      новый статус заявки
     * @param changeType  тип изменения
     */
    void changeStatementStatus(String statementId, ApplicationStatus status, ChangeType changeType);

}
