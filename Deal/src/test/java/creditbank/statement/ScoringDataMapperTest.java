package creditbank.statement;

import creditbank.statement.dto.EmploymentDto;
import creditbank.statement.dto.LoanOfferDto;
import creditbank.statement.dto.ScoringDataDto;
import creditbank.statement.dto.enums.EmploymentStatus;
import creditbank.statement.dto.enums.Gender;
import creditbank.statement.dto.enums.MaritalStatus;
import creditbank.statement.dto.enums.Position;
import creditbank.statement.model.attribute.Passport;
import creditbank.statement.model.entity.Client;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
public class ScoringDataMapperTest {

    private final ScoringDataMapperImpl scoringDataMapper = new ScoringDataMapperImpl();

    private Client getClient() {
        EmploymentDto employment = EmploymentDto.builder()
                .employmentStatus(EmploymentStatus.EMPLOYER)
                .employerINN("1024555125")
                .salary(new BigDecimal("25000"))
                .position(Position.ORDINARY)
                .workExperienceTotal(20)
                .workExperienceCurrent(4)
                .build();
        Passport passport = Passport.builder()
                .series("0000")
                .number("000000")
                .issueDate(LocalDate.of(2000, 1, 1))
                .issueBranch("560-400")
                .build();

        return Client.builder()
                .lastName("Иванов")
                .firstName("Иван")
                .middleName("Иванович")
                .birthdate(LocalDate.of(2000, 10, 24))
                .email("ivanov@yandex.ru")
                .gender(Gender.MALE)
                .maritalStatus(MaritalStatus.MARRIED)
                .dependentAmount(0)
                .accountNumber("40802810064580000000")
                .employment(employment)
                .passport(passport)
                .build();
    }

    private LoanOfferDto getOffer() {
        return LoanOfferDto.builder()
                .requestedAmount(new BigDecimal("100000"))
                .totalAmount(new BigDecimal("113636.81"))
                .term(20)
                .monthlyPayment(new BigDecimal("5682.04"))
                .rate(new BigDecimal("0.15"))
                .isInsuranceEnabled(false)
                .isSalaryClient(false)
                .build();
    }

    private EmploymentDto getEmployment() {
        return EmploymentDto.builder()
                .employmentStatus(EmploymentStatus.EMPLOYER)
                .employerINN("1024555125")
                .salary(new BigDecimal("25000"))
                .position(Position.ORDINARY)
                .workExperienceTotal(20)
                .workExperienceCurrent(4)
                .build();
    }
    private ScoringDataDto getExpectedScoringDataDto() {

        EmploymentDto employment = getEmployment();
        LoanOfferDto offer = getOffer();

        return ScoringDataDto.builder()
                .lastName("Иванов")
                .firstName("Иван")
                .middleName("Иванович")
                .birthdate(LocalDate.of(2000, 10, 24))
                .gender(Gender.MALE)
                .maritalStatus(MaritalStatus.MARRIED)
                .dependentAmount(0)
                .accountNumber("40802810064580000000")
                .employmentDto(employment)
                .passportSeries("0000")
                .passportNumber("000000")
                .passportIssueDate(LocalDate.of(2000, 1, 1))
                .passportIssueBranch("560-400")
                .amount(offer.getRequestedAmount())
                .term(offer.getTerm())
                .isInsuranceEnabled(offer.getIsInsuranceEnabled())
                .isSalaryClient(offer.getIsSalaryClient())
                .build();
    }
    @Test
    public void testScoringDataMapper() {

        ScoringDataDto expectedScoringDataDto = getExpectedScoringDataDto();
        Client client = getClient();
        ScoringDataDto actualScoringDataDto = scoringDataMapper.clientDataToDto(client, client.getPassport(), getOffer());

        assertEquals(expectedScoringDataDto, actualScoringDataDto);
    }
}