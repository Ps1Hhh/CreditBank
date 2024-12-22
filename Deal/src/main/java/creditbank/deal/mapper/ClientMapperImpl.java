package creditbank.deal.mapper;

import creditbank.deal.dto.FinishRegistrationRequestDto;
import creditbank.deal.dto.LoanStatementRequestDto;
import creditbank.deal.model.attribute.Passport;
import creditbank.deal.model.entity.Client;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public class ClientMapperImpl implements ClientMapper {

    @Override
    public Client dtoToClient(LoanStatementRequestDto loanStatementRequestDto) {
        if (loanStatementRequestDto == null) {
            return null;
        }

        Client client = new Client();
        client.setFirstName(loanStatementRequestDto.getFirstName());
        client.setLastName(loanStatementRequestDto.getLastName());
        client.setMiddleName(loanStatementRequestDto.getMiddleName());
        client.setEmail(loanStatementRequestDto.getEmail());
        client.setBirthdate(loanStatementRequestDto.getBirthdate());
        return client;
    }

    @Override
    public void updateAtFinish(FinishRegistrationRequestDto finishRequest, @MappingTarget Client client) {
        if (finishRequest == null || client == null) {
            return;
        }

        Passport passport = client.getPassport();
        if (passport == null) {
            passport = new Passport();
            client.setPassport(passport);
        }
        passport.setIssueDate(finishRequest.getPassportIssueDate());
        passport.setIssueBranch(finishRequest.getPassportIssueBranch());

        client.setEmployment(finishRequest.getEmploymentDto());
    }
}