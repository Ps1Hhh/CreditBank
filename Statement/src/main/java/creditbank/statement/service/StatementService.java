package creditbank.statement.service;

import creditbank.statement.dto.LoanOfferDto;
import creditbank.statement.dto.LoanStatementRequestDto;
import creditbank.statement.exception.DefaultException;
import creditbank.statement.exception.LaterBirthdateException;
import creditbank.statement.interfaces.DealClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Сервис для осуществления прескоринга и выбора кредитного предложения.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class StatementService implements IStatementService {

    private final DealClient dealClient;

    private void isDateLate(LocalDate date) throws LaterBirthdateException {
        LocalDate checkpoint = LocalDate.now().minusYears(18);
        if (date.isAfter(checkpoint)) {
            throw new LaterBirthdateException("Некорректно указана дата рождения: '" + date
                    + "'. Пользователь несовершеннолетний.",
                    LocalDateTime.now(), "");
        }
    }

    public List<LoanOfferDto> getOffers(LoanStatementRequestDto loanStatementRequestDto)
            throws LaterBirthdateException, DefaultException {
        isDateLate(loanStatementRequestDto.getBirthdate());

        return dealClient.getLoanOffers(loanStatementRequestDto);
    }

    public void selectOffer(LoanOfferDto loanOfferDto) throws DefaultException {
        dealClient.selectOffer(loanOfferDto);
    }

}