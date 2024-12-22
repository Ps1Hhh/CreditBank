package creditbank.deal.mapper;

import creditbank.deal.dto.CreditDto;
import creditbank.deal.model.entity.Credit;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public class CreditMapperImpl implements CreditMapper {

    @Override
    public Credit dtoToCredit(CreditDto creditDto) {
        if (creditDto == null) {
            return null;
        }

        Credit credit = new Credit();
        credit.setAmount(creditDto.getAmount());
        credit.setRate(creditDto.getRate());
        credit.setTerm(creditDto.getTerm());
        credit.setInsuranceEnabled(creditDto.getIsInsuranceEnabled());
        credit.setSalaryClient(creditDto.getIsSalaryClient());
        return credit;
    }
}