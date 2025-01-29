package creditbank.deal.service;

import creditbank.deal.dto.*;
import creditbank.deal.dto.enums.ApplicationStatus;
import creditbank.deal.dto.enums.ChangeType;
import creditbank.deal.dto.enums.CreditStatus;
import creditbank.deal.dto.enums.Theme;
import creditbank.deal.exception.DefaultException;
import creditbank.deal.exception.ScoringDeniedException;
import creditbank.deal.interfaces.CalculatorClient;
import creditbank.deal.mapper.ClientMapperImpl;
import creditbank.deal.mapper.CreditMapperImpl;
import creditbank.deal.mapper.ScoringDataMapperImpl;
import creditbank.deal.model.attribute.Passport;
import creditbank.deal.model.entity.Client;
import creditbank.deal.model.entity.Credit;
import creditbank.deal.model.entity.Statement;
import creditbank.deal.repository.ClientRepository;
import creditbank.deal.repository.CreditRepository;
import creditbank.deal.repository.StatementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
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
    private final KafkaTemplate<String, EmailMessage> emailKafkaTemplate;

    public List<LoanOfferDto> createStatement(LoanStatementRequestDto statementRequest)
            throws DefaultException {

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

        sendEmailMessage(statement, Theme.FINISH_REGISTRATION);

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

            sendEmailMessage(statement, Theme.CREATE_DOCUMENTS);

        } catch (ScoringDeniedException deniedException) {
            statement.setStatusAndHistoryEntry(ApplicationStatus.CC_DENIED, ChangeType.AUTOMATIC);
            statementRepository.save(statement);

            sendEmailMessage(statement, Theme.STATEMENT_DENIED);
            throw deniedException;
        }

    }

    private void sendEmailMessage(Statement statement, Theme theme) {
        EmailMessage message = EmailMessage.builder()
                .address(statement.getClient().getEmail())
                .theme(theme)
                .statementId(statement.getStatementId())
                .build();
        emailKafkaTemplate.send(theme.getConnectedTopic(), message);

        log.info("Отправлено сообщение в МС-Dossier через Kafka по теме {}: {}",
                theme.getConnectedTopic(), message.toString());
    }
}