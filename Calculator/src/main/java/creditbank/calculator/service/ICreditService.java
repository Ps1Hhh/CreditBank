package creditbank.calculator.service;

import creditbank.calculator.dto.CreditDto;
import creditbank.calculator.dto.ScoringDataDto;
import creditbank.calculator.exception.ScoringDeniedException;

public interface ICreditService {

    /**
     * Рассчитать условия кредита на основе скоринговых данных.
     *
     * @param scoringDataDto данные для скоринга
     * @return объект CreditDto с результатами расчёта
     * @throws ScoringDeniedException если кредит не одобрен
     */
    CreditDto calculateCredit(ScoringDataDto scoringDataDto) throws ScoringDeniedException;
}