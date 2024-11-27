package creditbank.calculator.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * Возникает в случае, если пользователь несовершеннолетний.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LaterBirthdateException extends Exception {

    private String message;
    private Date timestamp;

}