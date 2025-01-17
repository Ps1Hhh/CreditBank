package creditbank.statement;

import static org.junit.jupiter.api.Assertions.assertEquals;

import creditbank.statement.dto.EmploymentDto;
import creditbank.statement.dto.FinishRegistrationRequestDto;
import creditbank.statement.dto.LoanStatementRequestDto;
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

@ActiveProfiles("test")
public class ClientMapperTest {

    private final ClientMapperImpl clientMapper = new ClientMapperImpl();

    private LoanStatementRequestDto getLoanStatementRequest() {
        return LoanStatementRequestDto.builder()
                .amount(new BigDecimal("100000"))
                .term(20)
                .firstName("Иван")
                .lastName("Иванов")
                .middleName("Иванович")
                .email("ivanov@yandex.ru")
                .birthdate(LocalDate.of(2000, 10, 24))
                .passportSeries("0000")
                .passportNumber("000000")
                .build();
    }

    private FinishRegistrationRequestDto getFinishRegistrationRequestDto() {
        EmploymentDto employment = EmploymentDto.builder()
                .employmentStatus(EmploymentStatus.EMPLOYER)
                .employerINN("1024555125")
                .salary(new BigDecimal("25000"))
                .position(Position.ORDINARY)
                .workExperienceTotal(20)
                .workExperienceCurrent(4)
                .build();
        return FinishRegistrationRequestDto.builder()
                .gender(Gender.MALE)
                .maritalStatus(MaritalStatus.MARRIED)
                .dependentAmount(0)
                .passportIssueDate(LocalDate.of(2000, 1, 1))
                .passportIssueBranch("560-400")
                .accountNumber("40802810064580000000")
                .employmentDto(employment)
                .build();
    }

    private Client getExpectedClient() {

        return Client.builder()
                .lastName("Иванов")
                .firstName("Иван")
                .middleName("Иванович")
                .birthdate(LocalDate.of(2000, 10, 24))
                .email("ivanov@yandex.ru")
                .build();
    }

    private Client getClientForUpdate() {
        Passport forUpdatePassport = Passport.builder()
                .issueDate(LocalDate.of(2000, 1, 1))
                .issueBranch("560-400")
                .build();

        return Client.builder()
                .lastName("Иванов")
                .firstName("Иван")
                .middleName("Иванович")
                .birthdate(LocalDate.of(2000, 10, 24))
                .email("ivanov@yandex.ru")
                .passport(forUpdatePassport)
                .build();

    }
    private Client getUpdatedExpectedClient() {
        EmploymentDto employment = EmploymentDto.builder()
                .employmentStatus(EmploymentStatus.EMPLOYER)
                .employerINN("1024555125")
                .salary(new BigDecimal("25000"))
                .position(Position.ORDINARY)
                .workExperienceTotal(20)
                .workExperienceCurrent(4)
                .build();
        Passport passport = Passport.builder()
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
    @Test
    public void testClientMapper() {

        Client actualClient = clientMapper.dtoToClient(getLoanStatementRequest());
        Client expectedClient = getExpectedClient();

        assertEquals(expectedClient, actualClient);

        Client updatedExpectedClient = getUpdatedExpectedClient();
        Client updatedActualClient = getClientForUpdate();
        clientMapper.updateAtFinish(getFinishRegistrationRequestDto(), updatedActualClient);

        assertEquals(updatedExpectedClient, updatedActualClient);

    }
}