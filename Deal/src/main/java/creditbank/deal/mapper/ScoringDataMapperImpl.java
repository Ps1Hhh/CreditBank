package creditbank.deal.mapper;

import creditbank.deal.dto.LoanOfferDto;
import creditbank.deal.dto.ScoringDataDto;
import creditbank.deal.model.attribute.Passport;
import creditbank.deal.model.entity.Client;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public class ScoringDataMapperImpl implements ScoringDataMapper {

    @Override
    public ScoringDataDto clientDataToDto(Client client, Passport passport, LoanOfferDto offer) {
        if (client == null || passport == null || offer == null) {
            return null;
        }

        ScoringDataDto scoringData = new ScoringDataDto();
        scoringData.setAmount(offer.getRequestedAmount());
        scoringData.setPassportSeries(passport.getSeries());
        scoringData.setPassportNumber(passport.getNumber());
        scoringData.setPassportIssueBranch(passport.getIssueBranch());
        scoringData.setPassportIssueDate(passport.getIssueDate());
        scoringData.setEmploymentDto(client.getEmployment());
        scoringData.setFirstName(client.getFirstName());
        scoringData.setLastName(client.getLastName());
        scoringData.setMiddleName(client.getMiddleName());
        scoringData.setBirthdate(client.getBirthdate());
        scoringData.setGender(client.getGender());
        return scoringData;
    }
}