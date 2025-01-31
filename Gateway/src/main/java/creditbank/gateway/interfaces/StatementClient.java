package creditbank.gateway.interfaces;


import creditbank.gateway.config.FeignConfig;
import creditbank.gateway.dto.LoanOfferDto;
import creditbank.gateway.dto.LoanStatementRequestDto;
import creditbank.gateway.exception.CustomErrorDecoder;
import creditbank.gateway.exception.DefaultException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(name = "statement", url = "${spring.cloud.openfeign.client.config.statement.url}",
        configuration = {FeignConfig.class, CustomErrorDecoder.class})
public interface StatementClient {

    @PostMapping("")
    List<LoanOfferDto> createLoanOffers(LoanStatementRequestDto loanStatementRequestDto)
                                                throws DefaultException;

    @PostMapping("/offer")
    void selectOffer(LoanOfferDto loanOfferDto) throws DefaultException;

}