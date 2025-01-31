package creditbank.gateway.service;


import creditbank.gateway.dto.FinishRegistrationRequestDto;
import creditbank.gateway.dto.LoanOfferDto;
import creditbank.gateway.dto.LoanStatementRequestDto;
import creditbank.gateway.dto.StatementDto;
import creditbank.gateway.exception.DefaultException;

import java.util.List;

/**
 * Интерфейс для сервиса StatementService.
 */
public interface IGatewayService {
    List<LoanOfferDto> calculateLoanOffers(LoanStatementRequestDto loanStatementRequestDto) throws DefaultException;

    void selectOffer(LoanOfferDto loanOfferDto) throws DefaultException;

    void finishRegistration(FinishRegistrationRequestDto finishRegistrationRequestDto, String statementId) throws DefaultException;

    void sendDocuments(String statementId) throws DefaultException;

    void signDocuments(Boolean isAccepted, String statementId) throws DefaultException;

    void sendCodeVerification(String code, String statementId) throws DefaultException;

    StatementDto getStatementById(String statementId) throws DefaultException;

    List<StatementDto> getAllStatements() throws DefaultException;

    void changeStatementStatusOnDocumentsCreation(String statementId) throws DefaultException;
}
