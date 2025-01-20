package creditbank.statement.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Возникает в случае, если пользователь несовершеннолетний.
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LaterBirthdateException extends Exception {

    private String message;
    private LocalDateTime timestamp;
    private String details;
}