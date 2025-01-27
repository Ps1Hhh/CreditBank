package creditbank.deal.controller;

import creditbank.deal.dto.FinishRegistrationRequestDto;
import creditbank.deal.dto.LoanOfferDto;
import creditbank.deal.dto.LoanStatementRequestDto;
import creditbank.deal.dto.enums.ApplicationStatus;
import creditbank.deal.dto.enums.ChangeType;
import creditbank.deal.exception.DefaultException;
import creditbank.deal.exception.ScoringDeniedException;
import creditbank.deal.interfaces.Deal;
import creditbank.deal.service.DealService;
import creditbank.deal.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Основной контроллер Deal API. Рассчитывает возможные и полные условия кредита.
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/deal")
public class DealController implements Deal {

    private final DealService dealService;
    private final EmailService emailService;

    @Value("${topics.send-documents}")
    private String sendDocumentsTopic;

    @Value("${topics.send-ses}")
    private String sendSesTopic;

    @Value("${topics.credit-issued}")
    private String creditIssuedTopic;

    @PostMapping("/statement")
    public List<LoanOfferDto> createLoanOffers(@RequestBody LoanStatementRequestDto statementRequest)
            throws DefaultException {
        log.info("Запрос на обработку кредитной заявки: {}", statementRequest.toString());

        List<LoanOfferDto> result = dealService.createStatement(statementRequest);

        log.info("Ответ после обработки кредитной заявки: {}", result.toString());
        return result;


    }

    @PostMapping("/offer/select")
    public void selectOffer(@RequestBody LoanOfferDto loanOfferDto) {
        log.info("Выбор кредитного предложения: {}", loanOfferDto.toString());

        dealService.selectOffer(loanOfferDto);
    }

    @PostMapping("/calculate/{statementId}")
    public void finishRegistration(@RequestBody FinishRegistrationRequestDto finishRequest,
                                   @PathVariable String statementId) throws ScoringDeniedException, DefaultException {
        log.info("Запрос на расчёт кредитного предложения по заявке {}: {}",
                statementId, finishRequest.toString());

        dealService.createCredit(finishRequest, statementId);
    }

    @PostMapping("/document/{statementId}/send")
    public void sendDocuments(@PathVariable String statementId) {
        log.info("Запрос на формирование и отправку документов по заявке {}", statementId);

        emailService.sendDocuments(sendDocumentsTopic, statementId, ApplicationStatus.PREPARE_DOCUMENTS);
    }

    @PostMapping("/document/{statementId}/sign")
    public void signDocuments(@RequestParam("decision") Boolean isAccepted,
                              @PathVariable String statementId) {
        log.info("Запрос на подписание документов по заявке {}. Принято: {}", statementId, isAccepted);

        if (isAccepted) {
            emailService.sendCode(sendSesTopic, statementId);
        } else {
            log.info("Изменение статуса заявки {} на 'CLIENT_DENIED'", statementId);

            emailService.changeStatementStatus(
                    statementId,
                    ApplicationStatus.CLIENT_DENIED, ChangeType.MANUAL);
        }
    }

    @PostMapping("/document/{statementId}/code")
    public void sendCodeVerification(@RequestParam("code") String code, @PathVariable String statementId) {
        log.info("Запрос на подтверждение кода для подписания документов по заявке {}. Полученный код: {}", statementId, code);

        emailService.sendCreditIssuedMessage(creditIssuedTopic, statementId, code);
    }
}