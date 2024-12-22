package creditbank.deal.model.entity;

import creditbank.deal.dto.LoanOfferDto;
import creditbank.deal.dto.enums.ApplicationStatus;
import creditbank.deal.dto.enums.ChangeType;
import creditbank.deal.model.attribute.StatementStatusHistoryDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Statement {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID statementId;

    @OneToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToOne
    @JoinColumn(name = "credit_id")
    private Credit credit;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    private LocalDateTime creationDate;

    @JdbcTypeCode(SqlTypes.JSON)
    private LoanOfferDto appliedOffer;

    private LocalDateTime signDate;

    private String sesCode;

    @JdbcTypeCode(SqlTypes.JSON)
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