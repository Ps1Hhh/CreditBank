package creditbank.gateway.dto;

import creditbank.gateway.dto.attribute.StatementStatusHistoryDto;
import creditbank.gateway.dto.enums.ApplicationStatus;
import creditbank.gateway.dto.enums.ChangeType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatementDto {




    private ClientDto clientDto;

    private CreditDto credit;

    private ApplicationStatus status;

    private LocalDateTime creationDate;

    private LoanOfferDto appliedOffer;

    private LocalDateTime signDate;

    private String sesCode;

    private List<StatementStatusHistoryDto> statusHistory;

    public void setStatusAndHistoryEntry(ApplicationStatus applicationStatus, ChangeType changeType) {
        if (statusHistory == null) {
            statusHistory = new ArrayList<>();
        }

        status = applicationStatus;

        StatementStatusHistoryDto statusHistoryEntry = StatementStatusHistoryDto.builder()
                .status(applicationStatus)
                .time(LocalDateTime.now())
                .changeType(changeType)
                .build();
        statusHistory.add(statusHistoryEntry);
    }

}