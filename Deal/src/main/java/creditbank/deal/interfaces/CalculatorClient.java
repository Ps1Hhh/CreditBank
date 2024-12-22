package creditbank.deal.interfaces;

import creditbank.deal.config.FeignConfig;
import creditbank.deal.dto.CreditDto;
import creditbank.deal.dto.LoanOfferDto;
import creditbank.deal.dto.LoanStatementRequestDto;
import creditbank.deal.dto.ScoringDataDto;
import creditbank.deal.exception.CustomErrorDecoder;
import creditbank.deal.exception.ScoringDeniedException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(value = "calculator", configuration = {FeignConfig.class, CustomErrorDecoder.class})
public interface CalculatorClient {

    @RequestMapping(method = RequestMethod.POST, value = "/offers", consumes = "application/json")
    List<LoanOfferDto> getLoanOffers(LoanStatementRequestDto loanStatementRequestDto);

    @RequestMapping(method = RequestMethod.POST, value = "/calc", consumes = "application/json")
    CreditDto getCredit(ScoringDataDto scoringData) throws ScoringDeniedException;
}