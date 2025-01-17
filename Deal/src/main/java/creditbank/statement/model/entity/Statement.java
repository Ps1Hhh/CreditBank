package creditbank.statement.model.entity;

import creditbank.statement.dto.LoanOfferDto;
import creditbank.statement.dto.enums.ApplicationStatus;
import creditbank.statement.dto.enums.ChangeType;
import creditbank.statement.model.attribute.StatementStatusHistoryDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@ToString
@Builder
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Statement statement = (Statement) o;
        return Objects.equals(statementId, statement.statementId) && Objects.equals(client, statement.client) && Objects.equals(credit, statement.credit) && status == statement.status && Objects.equals(creationDate, statement.creationDate) && Objects.equals(appliedOffer, statement.appliedOffer) && Objects.equals(signDate, statement.signDate) && Objects.equals(sesCode, statement.sesCode) && Objects.equals(statusHistory, statement.statusHistory);
    }

}