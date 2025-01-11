package creditbank.deal.mapper;

import creditbank.deal.dto.FinishRegistrationRequestDto;
import creditbank.deal.dto.LoanStatementRequestDto;
import creditbank.deal.model.entity.Client;
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
