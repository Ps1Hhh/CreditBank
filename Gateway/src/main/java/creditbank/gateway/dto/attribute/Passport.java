package creditbank.gateway.dto.attribute;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Passport {

    private String series;
    private String number;
    private String issueBranch;
    private LocalDate issueDate;

}