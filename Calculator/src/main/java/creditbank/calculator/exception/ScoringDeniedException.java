package creditbank.calculator.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * Возникает в случае отказа после скоринга.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ScoringDeniedException extends Exception {

    private String message;
    private Date timestamp;

}
