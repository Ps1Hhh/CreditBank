package creditbank.deal.service;

import creditbank.deal.dto.EmailMessage;
import creditbank.deal.dto.EmploymentDto;
import creditbank.deal.dto.PaymentScheduleElementDto;
import creditbank.deal.dto.enums.ApplicationStatus;
import creditbank.deal.dto.enums.ChangeType;
import creditbank.deal.dto.enums.CreditStatus;
import creditbank.deal.dto.enums.Theme;
import creditbank.deal.model.attribute.Passport;
import creditbank.deal.model.entity.Client;
import creditbank.deal.model.entity.Credit;
import creditbank.deal.model.entity.Statement;
import creditbank.deal.repository.CreditRepository;
import creditbank.deal.repository.StatementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService implements IEmailService {

    private final StatementRepository statementRepository;
    private final CreditRepository creditRepository;
    private final KafkaTemplate<String, EmailMessage> emailKafkaTemplate;
    private final KafkaTemplate<String, EmailMessage> fileKafkaTemplate;

    @Value("${topics.send-documents}")
    private String sendDocumentsTopic;

    @Value("${topics.send-ses}")
    private String sendSesTopic;

    @Value("${topics.credit-issued}")
    private String creditIssuedTopic;

    public void sendDocuments(String statementId, ApplicationStatus status) {
        Statement statement = statementRepository.getByStatementId(UUID.fromString(statementId));

        statement.setStatusAndHistoryEntry(status, ChangeType.AUTOMATIC);
        statementRepository.save(statement);

        List<String> documents = getDocuments(statementId);
        EmailMessage message = EmailMessage.builder()
                .address(statement.getClient().getEmail())
                .theme(Theme.getThemeByTopic(sendDocumentsTopic))
                .statementId(statement.getStatementId())
                .documents(documents)
                .build();
        log.info("Сформирован текст документов по заявке {}", statementId);

        fileKafkaTemplate.send(sendDocumentsTopic, message);
        log.info("Отправлено сообщение в МС-Dossier через Kafka по теме {}: {}",
                sendDocumentsTopic, message.toString());
    }

    public void signDocuments(String statementId, Boolean isAccepted) {
        if (isAccepted) {
            sendCode(statementId);
        } else {
            log.info("Изменение статуса заявки {} на 'CLIENT_DENIED'", statementId);

            changeStatementStatus(statementId, ApplicationStatus.CLIENT_DENIED, ChangeType.MANUAL);
        }
    }

    public void sendCode(String statementId) {
        Statement statement = statementRepository.getByStatementId(UUID.fromString(statementId));

        Random rand = new Random();
        int codeNumber = rand.nextInt(0, 9999);
        String code = String.format("%04d", codeNumber);

        statement.setSesCode(code);
        statementRepository.save(statement);
        log.info("Сгенерирован код подтверждения {} для заявки {}.", code, statementId);

        EmailMessage message = EmailMessage.builder()
                .address(statement.getClient().getEmail())
                .theme(Theme.getThemeByTopic(sendSesTopic))
                .statementId(statement.getStatementId())
                .code(code)
                .build();

        emailKafkaTemplate.send(sendSesTopic, message);
        log.info("Отправлено сообщение в МС-Dossier через Kafka по теме {}: {}",
                sendSesTopic, message.toString());
    }

    public void sendCreditIssuedMessage(String statementId, String code) {
        Statement statement = statementRepository.getByStatementId(UUID.fromString(statementId));

        if (statement.getSesCode().equals(code)) {
            statement.setStatusAndHistoryEntry(ApplicationStatus.DOCUMENT_SIGNED, ChangeType.MANUAL);
            statement.setStatusAndHistoryEntry(ApplicationStatus.CREDIT_ISSUED, ChangeType.AUTOMATIC);
            statement.setSignDate(LocalDateTime.now());

            statementRepository.save(statement);

            Credit credit = statement.getCredit();
            credit.setCreditStatus(CreditStatus.ISSUED);

            creditRepository.save(credit);

            log.info("Полученный код подтверждения {} совпал с отправленным. Кредит по заявке {} выдан.",
                    code, statementId);

            EmailMessage message = EmailMessage.builder()
                    .address(statement.getClient().getEmail())
                    .theme(Theme.getThemeByTopic(creditIssuedTopic))
                    .statementId(statement.getStatementId())
                    .build();

            emailKafkaTemplate.send(creditIssuedTopic, message);
            log.info("Отправлено сообщение в МС-Dossier через Kafka по теме {}: {}",
                    creditIssuedTopic, message.toString());
        }
    }

    public void changeStatementStatus(String statementId, ApplicationStatus status,
                                      ChangeType changeType) {
        Statement statement = statementRepository.getByStatementId(UUID.fromString(statementId));

        statement.setStatusAndHistoryEntry(status, changeType);

        statementRepository.save(statement);
    }

    private List<String> getDocuments(String statementId) {
        Statement statement = statementRepository.getByStatementId(UUID.fromString(statementId));

        String loanContract = createLoanContract(statement);
        String paymentSchedule = createPaymentSchedule(statement.getCredit());
        String application = createApplication(statement);

        return List.of(loanContract, application, paymentSchedule);
    }

    private String createLoanContract(Statement statement) {
        Client client = statement.getClient();
        Credit credit = statement.getCredit();

        String loanContract = "";
        loanContract += "Кредитный договор №" + statement.getStatementId() + "\n";
        loanContract += getClientInfo(client);
        loanContract += getCreditInfo(credit);

        return loanContract;
    }

    private String getClientInfo(Client client) {
        String clientInfo = "1. Информация о заёмщике.\n";

        clientInfo += "Номер счёта: " + client.getAccountNumber() + "\n";
        clientInfo += "ФИО: "
                + client.getLastName() + " "
                + client.getFirstName() + " "
                + client.getMiddleName() + "\n";
        clientInfo +=
                "Дата рождения: " + client.getBirthdate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                        + "\n";
        clientInfo += "Адрес эл. почты: " + client.getEmail() + "\n";
        clientInfo += "Пол: " + client.getGender().getDocName() + "\n";
        clientInfo += "Семейное положение: " + client.getMaritalStatus().getDocName() + "\n";
        clientInfo += "Количество иждивенцев: " + client.getDependentAmount() + "\n";

        clientInfo += "Паспортные данные.\n";
        Passport passport = client.getPassport();
        clientInfo += "Номер: " + passport.getNumber() + "\n";
        clientInfo += "Серия: " + passport.getSeries() + "\n";
        clientInfo +=
                "Дата выдачи: " + passport.getIssueDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                        + "\n";
        clientInfo += "Код подразделения: " + passport.getIssueBranch() + "\n";

        clientInfo += "Информация о трудоустройстве.\n";
        EmploymentDto employment = client.getEmployment();
        clientInfo += "Заработная плата: " + employment.getSalary() + " руб." + "\n";
        clientInfo += "Характер должности: " + employment.getPosition().getDocName() + "\n";
        clientInfo += "ИНН работодателя: " + employment.getEmployerINN() + "\n";
        clientInfo += "Статус трудоустройства: " + employment.getEmploymentStatus().getDocName() + "\n";
        clientInfo += "Общий опыт работы: " + employment.getWorkExperienceTotal() + " мес.\n";
        clientInfo += "Текущий опыт работы: " + employment.getWorkExperienceCurrent() + " мес.\n";

        return clientInfo;
    }

    private String getCreditInfo(Credit credit) {
        String creditInfo = "2. Информация о кредите.\n";

        creditInfo += "Сумма кредита: " + credit.getAmount() + " руб.\n";
        creditInfo += "Срок: " + credit.getTerm() + " мес.\n";
        creditInfo += "Ежемесячный платёж: " + credit.getMonthlyPayment() + " руб.\n";
        creditInfo += "Ставка: " + credit.getRate().multiply(new BigDecimal(100)) + "%\n";
        creditInfo += "Полная сумма кредита: " + credit.getPsk() + " руб.\n";

        return creditInfo;
    }

    private String createPaymentSchedule(Credit credit) {
        String paymentSchedule = "График платежей.\n";
        List<PaymentScheduleElementDto> schedule = credit.getPaymentSchedule();

        for (PaymentScheduleElementDto element : schedule) {
            paymentSchedule += element.getNumber() + "-й платёж.\n";
            paymentSchedule +=
                    "Дата платежа: " + element.getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                            + "\n";
            paymentSchedule += "Общий размер платежа: " + element.getTotalPayment() + " руб.\n";
            paymentSchedule += "Платеж по телу кредита: " + element.getDebtPayment() + " руб.\n";
            paymentSchedule += "Платеж по процентам: " + element.getInterestPayment() + " руб.\n";
            paymentSchedule += "Остаток долга: " + element.getRemainingDebt() + " руб.\n\n";
        }

        return paymentSchedule;
    }

    private String createApplication(Statement statement) {
        String application = "Анкета.\n";

        Client client = statement.getClient();
        Credit credit = statement.getCredit();

        application += "ФИО: "
                + client.getLastName() + " "
                + client.getFirstName() + " "
                + client.getMiddleName() + "\n";
        application += "Адрес эл. почты: " + client.getEmail() + "\n";
        application +=
                "Дата рождения: " + client.getBirthdate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                        + "\n";

        Passport passport = client.getPassport();
        application += "Номер паспорта: " + passport.getNumber() + "\n";
        application += "Серия паспорта: " + passport.getSeries() + "\n";
        application += "Сумма кредита: " + credit.getAmount() + " руб.\n";
        application += "Срок по кредиту: " + credit.getTerm() + " мес.\n";

        return application;
    }
}