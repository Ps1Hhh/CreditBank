package creditbank.calculator.interfaces;

import creditbank.calculator.dto.*;
import creditbank.calculator.exception.ScoringDeniedException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;

import java.util.List;

public interface Calculate {

    @Operation(
            summary = "Расчёт возможных условий кредита",
            description = "Рассчитывает 4 варианта возможных условий кредита в зависимости "
                    + "от наличия страховки кредита и наличия у пользователя статуса зарплатного клиента."
    )
    List<LoanOfferDto> calculateLoanOffers(
            @Valid @RequestBody @Parameter(description = "Заявка на кредит", required = true)
            LoanStatementRequestDto loanStatementRequestDto);

    @Operation(
            summary = "Расчёт полных условий кредита",
            description = "Производит скоринг пользователя и рассчитывает полные условия кредита. "
    )
    CreditDto calculateCreditOffer(
            @Valid @RequestBody @Parameter(description = "Данные, необходимые для скоринга", required = true)
            ScoringDataDto scoringDataDto) throws ScoringDeniedException;
}