package creditbank.gateway.controller;


import creditbank.gateway.dto.FinishRegistrationRequestDto;
import creditbank.gateway.dto.LoanOfferDto;
import creditbank.gateway.dto.LoanStatementRequestDto;
import creditbank.gateway.dto.entity.Statement;
import creditbank.gateway.exception.DefaultException;
import creditbank.gateway.interfaces.Gateway;
import creditbank.gateway.service.GatewayService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Основной контроллер Gateway API.
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Шлюз", description = "Инкапсулирует логику микросервисов приложения.")
public class GatewayController implements Gateway {

    private final GatewayService gatewayService;

    @PostMapping("/statement")
    public List<LoanOfferDto> calculateLoanOffers(LoanStatementRequestDto loanStatementRequestDto)
            throws DefaultException {
        log.debug("Запрос на расчёт возможных условий кредита: {}", loanStatementRequestDto.toString());

        List<LoanOfferDto> offers = gatewayService.calculateLoanOffers(loanStatementRequestDto);

        log.debug("Ответ после расчёта возможных условий кредита: {}", offers.toString());
        return offers;
    }

    @PostMapping("/statement/select")
    public void selectOffer(LoanOfferDto loanOfferDto) throws DefaultException {
        log.debug("Выбор кредитного предложения: {}", loanOfferDto.toString());

        gatewayService.selectOffer(loanOfferDto);
    }

    @PostMapping("/statement/registration/{statementId}")
    public void finishRegistration(FinishRegistrationRequestDto finishRequest,
                                   @PathVariable String statementId) throws DefaultException {
        log.debug("Запрос на расчёт кредитного предложения по заявке {}: {}",
                statementId, finishRequest.toString());

        gatewayService.finishRegistration(finishRequest, statementId);
    }

    @PostMapping("/document/{statementId}")
    public void sendDocuments(@PathVariable String statementId) throws DefaultException {
        log.debug("Запрос на формирование и отправку документов по заявке {}", statementId);

        gatewayService.sendDocuments(statementId);
    }

    @PostMapping("/document/{statementId}/sign")
    public void signDocuments(@RequestParam("decision") Boolean isAccepted,
                              @PathVariable String statementId) throws DefaultException {
        log.debug("Запрос на подписание документов по заявке {}. Принято: {}", statementId, isAccepted);

        gatewayService.signDocuments(isAccepted, statementId);
    }

    @PostMapping("/document/{statementId}/sign/code")
    public void sendCodeVerification(@RequestParam("code") String code,
                                     @PathVariable String statementId) throws DefaultException {
        log.debug("Запрос на подтверждение кода для подписания документов по заявке {}. Полученный код: {}", statementId, code);

        gatewayService.sendCodeVerification(code, statementId);
    }

    @GetMapping("/admin/statement/{statementId}")
    public Statement getStatementById(@PathVariable String statementId) throws DefaultException {
        log.debug("Запрос администратора на получение заявки {}", statementId);

        return gatewayService.getStatementById(statementId);
    }

    @GetMapping("/admin/statement")
    public List<Statement> getAllStatements() throws DefaultException {
        log.debug("Запрос администратора на получение всех заявок");

        return gatewayService.getAllStatements();
    }
}