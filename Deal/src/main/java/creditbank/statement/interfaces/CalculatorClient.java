package creditbank.statement.interfaces;

import creditbank.statement.config.FeignConfig;
import creditbank.statement.dto.CreditDto;
import creditbank.statement.dto.LoanOfferDto;
import creditbank.statement.dto.LoanStatementRequestDto;
import creditbank.statement.dto.ScoringDataDto;
import creditbank.statement.exception.CustomErrorDecoder;
import creditbank.statement.exception.DefaultException;
import creditbank.statement.exception.ScoringDeniedException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(value = "calculator", configuration = {FeignConfig.class, CustomErrorDecoder.class})
public interface CalculatorClient {

    @RequestMapping(method = RequestMethod.POST, value = "/offers", consumes = "application/json")
    List<LoanOfferDto> getLoanOffers(LoanStatementRequestDto loanStatementRequestDto) throws DefaultException;

    @RequestMapping(method = RequestMethod.POST, value = "/calc", consumes = "application/json")
    CreditDto getCredit(ScoringDataDto scoringData) throws ScoringDeniedException, DefaultException;
}