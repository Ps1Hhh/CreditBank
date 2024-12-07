package creditbank.calculator.controller;

import creditbank.calculator.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

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

    @ExceptionHandler(ScoringDeniedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage onDeniedException(ScoringDeniedException e) {
        return new ErrorMessage(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        log.error("Ошибки валидации: {}", errors);
        return ResponseEntity.badRequest().body(errors);
    }
}