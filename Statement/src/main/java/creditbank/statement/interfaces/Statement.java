package creditbank.statement.interfaces;

import creditbank.statement.dto.LoanOfferDto;
import creditbank.statement.dto.LoanStatementRequestDto;
import creditbank.statement.exception.DefaultException;
import creditbank.statement.exception.LaterBirthdateException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;

import java.util.List;

public interface Statement {

    @Operation(
            summary = "Прескоринг и расчёт возможных условий кредита",
            description = "Осуществляет прескоринг и расчёт 4-х вариантов возможных условий кредита через Deal API"
                    + " в зависимости от наличия страховки кредита и наличия у пользователя статуса зарплатного клиента."
    )
    List<LoanOfferDto> calculateLoanOffers(
            @Valid @RequestBody @Parameter(description = "Заявка на кредит", required = true)
            LoanStatementRequestDto loanStatementRequestDto)
            throws LaterBirthdateException, DefaultException;

    @Operation(
            summary = "Выбор кредитного предложения",
            description = "Осуществляет выбор кредитного предложения через Deal API."
    )
    void selectOffer(
            @Valid @RequestBody @Parameter(description = "Выбранное кредитное предложение", required = true)
            LoanOfferDto loanOfferDto) throws DefaultException;
}