package creditbank.gateway.interfaces;


import creditbank.gateway.config.FeignConfig;
import creditbank.gateway.dto.FinishRegistrationRequestDto;
import creditbank.gateway.dto.StatementDto;
import creditbank.gateway.exception.CustomErrorDecoder;
import creditbank.gateway.exception.DefaultException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "deal", url = "${spring.cloud.openfeign.client.config.deal.url}",
        configuration = {FeignConfig.class, CustomErrorDecoder.class})
public interface DealClient {

    @RequestMapping(method = RequestMethod.POST, value = "/calculate/{statementId}", consumes = "application/json")
    void finishRegistration(FinishRegistrationRequestDto finishRequest,
                            @PathVariable("statementId") String statementId) throws DefaultException;

    @RequestMapping(method = RequestMethod.POST, value = "/document/{statementId}/send", consumes = "application/json")
    void sendDocuments(@PathVariable("statementId") String statementId) throws DefaultException;

    @RequestMapping(method = RequestMethod.POST, value = "/document/{statementId}/sign", consumes = "application/json")
    void signDocuments(@RequestParam("decision") Boolean isAccepted,
                       @PathVariable("statementId") String statementId) throws DefaultException;

    @RequestMapping(method = RequestMethod.POST, value = "/document/{statementId}/code", consumes = "application/json")
    void sendCodeVerification(@RequestParam("code") String code, @PathVariable("statementId") String statementId)
            throws DefaultException;

    @RequestMapping(method = RequestMethod.GET, value = "/admin/statement/{statementId}", consumes = "application/json")
    StatementDto getStatementById(@PathVariable("statementId") String statementId) throws DefaultException;

    @RequestMapping(method = RequestMethod.GET, value = "/admin/statement", consumes = "application/json")
    List<StatementDto> getAllStatements() throws DefaultException;

    @RequestMapping(method = RequestMethod.POST, value = "/document/{statementId}/status", consumes = "application/json")
    void changeStatementStatusOnDocumentsCreation(@PathVariable("statementId") String statementId) throws DefaultException;

}