package creditbank.calculator.dto;

import creditbank.calculator.dto.enums.Gender;
import creditbank.calculator.dto.enums.MaritalStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@Data
@Schema(description = "Данные, необходимые для скоринга")
public class ScoringDataDto {

    @NotNull
    @DecimalMax(value = "30000000", message = "Сумма кредита не может превышать 30 млн. рублей.")
    @DecimalMin(value = "30000", message = "Сумма кредита не может быть ниже 30 тыс. рублей.")
    @Schema(description = "Сумма кредита", required = true, example = "100000.00")
    private BigDecimal amount;

    @NotNull
    @Max(value = 84, message = "Максимальный срок кредита - 7 лет.")
    @Min(value = 6, message = "Минимальный срок кредита - 6 месяцев.")
    @Schema(description = "Срок кредита (мес.)", required = true, example = "24")
    private Integer term;

    @NotNull
    @Size(min = 2, max = 30, message = "Длина имени: от 2 до 30 знаков.")
    @Schema(description = "Имя клиента", required = true, example = "Сергей")
    private String firstName;

    @NotNull
    @Size(min = 2, max = 30, message = "Длина фамилии: от 2 до 30 знаков.")
    @Schema(description = "Фамилия клиента", required = true, example = "Есипов")
    private String lastName;

    @Schema(description = "Отчество клиента")
    private String middleName;

    @NotNull
    @Schema(description = "Пол клиента", required = true)
    private Gender gender;

    @NotNull
    @Past(message = "Дата рождения указана некорректно.")
    @Schema(description = "Дата рождения клиента", required = true, example = "2001-05-07")
    private LocalDate birthdate;

    @NotNull
    @Pattern(regexp = "\\d{4}", message = "Серия паспорта должна содержать 4 цифры.")
    @Schema(description = "Серия паспорта клиента", required = true, example = "2024")
    private String passportSeries;

    @NotNull
    @Pattern(regexp = "\\d{6}", message = "Номер паспорта должен содержать 6 цифр.")
    @Schema(description = "Номер паспорта клиента", required = true, example = "000001")
    private String passportNumber;

    @NotNull
    @PastOrPresent
    @Schema(description = "Дата выдачи паспорта клиента", required = true, example = "2000-09-20")
    private LocalDate passportIssueDate;

    @NotNull
    @Pattern(regexp = "\\d{3}-\\d{3}",
            message = "Код подразделения состоит из 6 цифр в формате XXX-XXX.")
    @Schema(description = "Код подразделения, где был выдан паспорт клиента", required = true,
            example = "560-400")
    private String passportIssueBranch;

    @NotNull
    @Schema(description = "Семейное положение клиента", required = true)
    private MaritalStatus maritalStatus;

    @NotNull
    @Schema(description = "Количество иждивенцев на попечении клиента", required = true, example = "1")
    private Integer dependentAmount;

    @NotNull
    @Valid
    @Schema(description = "Данные о работе клиента", required = true)
    private EmploymentDto employmentDto;

    @NotNull
    @Size(min = 20, max = 20, message = "Номер счёта состоит из 20 цифр.")
    @Schema(description = "Номер счёта клиента", required = true, example = "40802810064580000000")
    private String accountNumber;

    @NotNull
    @Schema(description = "Наличие страховки по кредиту", required = true, example = "true")
    private Boolean isInsuranceEnabled;

    @NotNull
    @Schema(description = "Наличие статуса зарплатного клиента", required = true, example = "false")
    private Boolean isSalaryClient;
}