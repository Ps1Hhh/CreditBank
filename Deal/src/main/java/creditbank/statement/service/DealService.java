package creditbank.statement.service;

import creditbank.statement.dto.*;
import creditbank.statement.dto.enums.ApplicationStatus;
import creditbank.statement.dto.enums.ChangeType;
import creditbank.statement.dto.enums.CreditStatus;
import creditbank.statement.exception.DefaultException;
import creditbank.statement.exception.ScoringDeniedException;
import creditbank.statement.interfaces.CalculatorClient;
import creditbank.statement.ClientMapperImpl;
import creditbank.statement.CreditMapperImpl;
import creditbank.statement.ScoringDataMapperImpl;
import creditbank.statement.model.attribute.Passport;
import creditbank.statement.model.entity.Client;
import creditbank.statement.model.entity.Credit;
import creditbank.statement.model.entity.Statement;
import creditbank.statement.repository.ClientRepository;
import creditbank.statement.repository.CreditRepository;
import creditbank.statement.repository.StatementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class DealService implements IDealService{

    private final ClientRepository clientRepository;
    private final StatementRepository statementRepository;
    private final CreditRepository creditRepository;
    private final CalculatorClient calculatorClient;
    private final ClientMapperImpl clientMapper;
    private final ScoringDataMapperImpl scoringDataMapper;
    private final CreditMapperImpl creditMapper;

    public List<LoanOfferDto> createStatement(LoanStatementRequestDto statementRequest) throws DefaultException {

        List<LoanOfferDto> offers = calculatorClient.getLoanOffers(statementRequest);
        log.info("Инициировано создание заявки на кредит: {}", statementRequest);

        Client client = clientMapper.dtoToClient(statementRequest);
        Passport passport = Passport.builder()
                .series(statementRequest.getPassportSeries())
                .number(statementRequest.getPassportNumber())
                .build();
        client.setPassport(passport);
        Client savedClient = clientRepository.save(client);
        log.info("Сохранена сущность клиента: {}", savedClient);

        Statement statement = Statement.builder()
                .client(savedClient)
                .creationDate(LocalDateTime.now())
                .build();
        statement.setStatusAndHistoryEntry(ApplicationStatus.PREAPPROVAL, ChangeType.AUTOMATIC);
        Statement savedStatement = statementRepository.save(statement);
        log.info("Сохранена сущность заявки: {}", savedStatement);

        UUID statementId = savedStatement.getStatementId();
        for (LoanOfferDto offer : offers) {
            offer.setStatementId(statementId);
        }

        return offers;
    }

    public void selectOffer(LoanOfferDto appliedOffer) {
        Statement statement = statementRepository.getByStatementId(appliedOffer.getStatementId());
        statement.setStatusAndHistoryEntry(ApplicationStatus.APPROVED, ChangeType.MANUAL);
        statement.setAppliedOffer(appliedOffer);

        statementRepository.save(statement);
        log.info("Сохранено выбранное кредитное предложение: {}", statement);

    }

    public void createCredit(FinishRegistrationRequestDto finishRequest, String statementId)
            throws ScoringDeniedException, DefaultException {

        Statement statement = statementRepository.getByStatementId(UUID.fromString(statementId));
        Client updatedClient = statement.getClient();
        clientMapper.updateAtFinish(finishRequest, updatedClient);
        clientRepository.save(updatedClient);
        log.info("Обновлены данные о клиенте: {}", updatedClient);

        ScoringDataDto scoringData = scoringDataMapper.clientDataToDto(updatedClient,
                updatedClient.getPassport(), statement.getAppliedOffer());
        try {
            CreditDto creditDto = calculatorClient.getCredit(scoringData);

            Credit credit = creditMapper.dtoToCredit(creditDto);
            credit.setCreditStatus(CreditStatus.CALCULATED);
            Credit savedCredit = creditRepository.save(credit);
            log.info("Сохранена сущность кредита: {}", savedCredit);

            statement.setCredit(savedCredit);
            statement.setStatusAndHistoryEntry(ApplicationStatus.CC_APPROVED, ChangeType.AUTOMATIC);
            statementRepository.save(statement);
            log.info("Вычислена и сохранена заявка: {}", statement);
        }
        catch(ScoringDeniedException deniedException) {
            statement.setStatusAndHistoryEntry(ApplicationStatus.CC_DENIED, ChangeType.AUTOMATIC);
            statementRepository.save(statement);
            throw deniedException;
        }

    }
}