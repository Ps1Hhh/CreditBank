package creditbank.calculator.controller;

import creditbank.calculator.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Глобальный контроллер для перехвата ошибок.
 */
@RestControllerAdvice
@Slf4j
public class AdviceController {

    @ExceptionHandler(LaterBirthdateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage onLaterBirthdateException(LaterBirthdateException e) {
        String message = e.getMessage();
        log.error(message);
        return new ErrorMessage(message);
    }

    @ExceptionHandler(DeniedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage onDeniedException(DeniedException e) {
        return new ErrorMessage(e.getMessage());
    }
}