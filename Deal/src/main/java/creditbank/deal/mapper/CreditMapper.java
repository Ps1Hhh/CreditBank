package creditbank.deal.mapper;

import creditbank.deal.dto.CreditDto;
import creditbank.deal.model.entity.Credit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CreditMapper {

    @Mapping(source = "isInsuranceEnabled", target = "insuranceEnabled")
    @Mapping(source = "isSalaryClient", target = "salaryClient")
    Credit dtoToCredit(CreditDto creditDto);
}
