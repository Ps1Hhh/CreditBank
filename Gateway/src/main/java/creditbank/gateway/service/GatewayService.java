package creditbank.gateway.service;

import creditbank.gateway.dto.FinishRegistrationRequestDto;
import creditbank.gateway.dto.LoanOfferDto;
import creditbank.gateway.dto.LoanStatementRequestDto;
import creditbank.gateway.dto.entity.Statement;
import creditbank.gateway.exception.DefaultException;
import creditbank.gateway.interfaces.DealClient;
import creditbank.gateway.interfaces.StatementClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class GatewayService {

    private final DealClient dealClient;
    private final StatementClient statementClient;

    public List<LoanOfferDto> calculateLoanOffers(LoanStatementRequestDto loanStatementRequestDto)
            throws DefaultException {
        return statementClient.createLoanOffers(loanStatementRequestDto);
    }

    public void selectOffer(LoanOfferDto loanOfferDto) throws DefaultException {
        statementClient.selectOffer(loanOfferDto);
    }

    public void finishRegistration(FinishRegistrationRequestDto finishRegistrationRequestDto,
                                   String statementId) throws DefaultException {
        dealClient.finishRegistration(finishRegistrationRequestDto, statementId);
    }

    public void sendDocuments(String statementId) throws DefaultException {
        dealClient.sendDocuments(statementId);
    }

    public void signDocuments(Boolean isAccepted, String statementId) throws DefaultException {
        dealClient.signDocuments(isAccepted, statementId);
    }

    public void sendCodeVerification(String code, String statementId) throws DefaultException {
        dealClient.sendCodeVerification(code, statementId);
    }

    public Statement getStatementById(String statementId) throws DefaultException {
        return dealClient.getStatementById(statementId);
    }

    public List<Statement> getAllStatements() throws DefaultException {
        return dealClient.getAllStatements();
    }

    public void changeStatementStatusOnDocumentsCreation(String statementId) throws DefaultException {
        dealClient.changeStatementStatusOnDocumentsCreation(statementId);
    }
}