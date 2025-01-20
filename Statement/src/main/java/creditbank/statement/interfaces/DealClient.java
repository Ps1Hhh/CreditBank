package creditbank.statement.interfaces;

import creditbank.statement.config.FeignConfig;
import creditbank.statement.dto.LoanOfferDto;
import creditbank.statement.dto.LoanStatementRequestDto;
import creditbank.statement.exception.CustomErrorDecoder;
import creditbank.statement.exception.DefaultException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(value = "deal", configuration = {FeignConfig.class, CustomErrorDecoder.class})
@Component
public interface DealClient {

    @RequestMapping(method = RequestMethod.POST, value = "/statement", consumes = "application/json")
    List<LoanOfferDto> getLoanOffers(LoanStatementRequestDto loanStatementRequestDto) throws DefaultException;

    @RequestMapping(method = RequestMethod.POST, value = "/offer/select", consumes = "application/json")
    void selectOffer(LoanOfferDto loanOfferDto) throws DefaultException;
}