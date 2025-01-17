package creditbank.statement.mapper;

import creditbank.statement.dto.FinishRegistrationRequestDto;
import creditbank.statement.dto.LoanStatementRequestDto;
import creditbank.statement.model.entity.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    Client dtoToClient(LoanStatementRequestDto loanStatementRequestDto);

    @Mapping(source = "finishRequest.passportIssueDate", target = "client.passport.issueDate")
    @Mapping(source = "finishRequest.passportIssueBranch", target = "client.passport.issueBranch")
    @Mapping(source = "finishRequest.employmentDto", target = "client.employment")
    void updateAtFinish(FinishRegistrationRequestDto finishRequest, @MappingTarget Client client);
}
