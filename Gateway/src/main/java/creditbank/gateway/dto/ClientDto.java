package creditbank.gateway.dto;


import creditbank.gateway.dto.attribute.Passport;
import creditbank.gateway.dto.enums.Gender;
import creditbank.gateway.dto.enums.MaritalStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;


import java.time.LocalDate;
import java.util.UUID;


@Getter
@Setter
@RequiredArgsConstructor
@ToString
@AllArgsConstructor
@Builder
public class ClientDto {

    private UUID clientId;

    private String lastName;

    private String firstName;

    private String middleName;

    private LocalDate birthdate;

    private String email;

    private Gender gender;


    private MaritalStatus maritalStatus;

    private Integer dependentAmount;

    @JdbcTypeCode(SqlTypes.JSON)
    private Passport passport;

    @JdbcTypeCode(SqlTypes.JSON)
    private EmploymentDto employment;

    private String accountNumber;

}
