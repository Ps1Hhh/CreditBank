package creditbank.gateway.interfaces;


import creditbank.gateway.config.FeignConfig;
import creditbank.gateway.dto.FinishRegistrationRequestDto;
import creditbank.gateway.dto.entity.Statement;
import creditbank.gateway.exception.CustomErrorDecoder;
import creditbank.gateway.exception.DefaultException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "deal", configuration = {FeignConfig.class, CustomErrorDecoder.class})
@Component
public interface DealClient {

    @PostMapping("/calculate/{statementId}")
    void finishRegistration(FinishRegistrationRequestDto finishRequest,
                            @PathVariable String statementId) throws DefaultException;

    @PostMapping("/document/{statementId}/send")
    void sendDocuments(@PathVariable String statementId) throws DefaultException;

    @PostMapping("/document/{statementId}/sign")
    void signDocuments(@RequestParam("decision") Boolean isAccepted,
                       @PathVariable String statementId) throws DefaultException;

    @PostMapping("/document/{statementId}/code")
    void sendCodeVerification(@RequestParam("code") String code, @PathVariable String statementId)
            throws DefaultException;

    @GetMapping("/admin/statement/{statementId}")
    Statement getStatementById(@PathVariable String statementId) throws DefaultException;

    @GetMapping("/admin/statement")
    List<Statement> getAllStatements() throws DefaultException;

    @PostMapping("/document/{statementId}/status")
    void changeStatementStatusOnDocumentsCreation(@PathVariable("statementId") String statementId) throws DefaultException;

}