package creditbank.gateway.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DefaultException extends Exception {

    private LocalDateTime timestamp;
    private String code;
    private String message;
    private String details;

}