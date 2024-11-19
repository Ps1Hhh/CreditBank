package creditbank.calculator.controller;

import creditbank.calculator.dto.*;
import creditbank.calculator.service.CalculatorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/calculator")
@RequiredArgsConstructor
public class CalculatorController {

    private final CalculatorService calculatorService;


    @Operation(summary = "Получить предложения по кредиту")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список предложений по кредиту",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LoanOfferDto.class))),
            @ApiResponse(responseCode = "400", description = "Некорректный запрос",
                    content = @Content)
    })
    @PostMapping("/offers")
    public ResponseEntity<List<LoanOfferDto>> getLoanOffers(@RequestBody LoanStatementRequestDto request) {
        List<LoanOfferDto> offers = calculatorService.generateLoanOffers(request);
        return ResponseEntity.ok(offers);
    }

    @PostMapping("/calc")
    public ResponseEntity<CreditDto> calculateLoan(@RequestBody ScoringDataDto scoringData) {
        CreditDto credit = calculatorService.calculateLoan(scoringData);
        return ResponseEntity.ok(credit);
    }
}