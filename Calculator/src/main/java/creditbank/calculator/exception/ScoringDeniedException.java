package creditbank.calculator.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Возникает в случае отказа после скоринга.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ScoringDeniedException extends Exception {

    private String message;
    private LocalDateTime timestamp;
    private String details;

}
