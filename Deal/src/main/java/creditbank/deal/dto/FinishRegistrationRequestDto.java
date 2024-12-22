package creditbank.deal.dto;


import creditbank.deal.dto.enums.Gender;
import creditbank.deal.dto.enums.MaritalStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Schema(description = "Заявка на завершение регистрации")
public class FinishRegistrationRequestDto {

    @NotNull
    @Schema(description = "Пол клиента", required = true)
    private Gender gender;

    @NotNull
    @Schema(description = "Семейное положение клиента", required = true)
    private MaritalStatus maritalStatus;

    @NotNull
    @Schema(description = "Количество иждивенцев на попечении клиента", required = true, example = "1")
    private Integer dependentAmount;

    @NotNull
    @PastOrPresent
    @Schema(description = "Дата выдачи паспорта клиента", required = true, example = "2015-05-07")
    private LocalDate passportIssueDate;

    @NotNull
    @Pattern(regexp = "\\d{3}-\\d{3}",
            message = "Код подразделения состоит из 6 цифр в формате XXX-XXX.")
    @Schema(description = "Код подразделения, где был выдан паспорт клиента", required = true,
            example = "560-400")
    private String passportIssueBranch;

    @NotNull
    @Valid
    @Schema(description = "Данные о работе клиента", required = true)
    private EmploymentDto employmentDto;

    @NotNull
    @Size(min = 20, max = 20, message = "Номер счёта состоит из 20 цифр.")
    @Schema(description = "Номер счёта клиента", required = true, example = "12345678123412341234")
    private String accountNumber;

}