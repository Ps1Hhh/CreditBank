package creditbank.deal.mapper;

import creditbank.deal.dto.LoanOfferDto;
import creditbank.deal.dto.ScoringDataDto;
import creditbank.deal.model.attribute.Passport;
import creditbank.deal.model.entity.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ScoringDataMapper {

    @Mapping(source = "offer.requestedAmount", target = "amount")
    @Mapping(source = "passport.series", target = "passportSeries")
    @Mapping(source = "passport.number", target = "passportNumber")
    @Mapping(source = "passport.issueBranch", target = "passportIssueBranch")
    @Mapping(source = "passport.issueDate", target = "passportIssueDate")
    @Mapping(source = "client.employment", target = "employmentDto")
    ScoringDataDto clientDataToDto(Client client, Passport passport, LoanOfferDto offer);

}
