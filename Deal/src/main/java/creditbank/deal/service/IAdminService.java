package creditbank.deal.service;

import creditbank.deal.model.entity.Statement;

import java.util.List;

public interface IAdminService{
    /**
     * Получение конкретного заявления по его идентификатору.
     *
     * @param statementId идентификатор заявления
     * @return объект Statement
     */
    Statement getStatementById(String statementId);

    /**
     * Получение списка всех заявлений.
     *
     * @return список объектов Statement
     */
    List<Statement> getAllStatements();
}

