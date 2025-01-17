package creditbank.statement.mapper;

import creditbank.statement.dto.CreditDto;
import creditbank.statement.model.entity.Credit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CreditMapper {

    @Mapping(source = "isInsuranceEnabled", target = "insuranceEnabled")
    @Mapping(source = "isSalaryClient", target = "salaryClient")
    Credit dtoToCredit(CreditDto creditDto);
}
