package creditbank.calculator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatementStatusHistoryDto {

    private StatementStatus status;
    private LocalDateTime time;
    private ChangeType changeType;

}