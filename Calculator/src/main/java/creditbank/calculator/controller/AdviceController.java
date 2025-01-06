package creditbank.calculator.controller;

import creditbank.calculator.exception.ErrorResponse;
import creditbank.calculator.exception.LaterBirthdateException;
import creditbank.calculator.exception.ScoringDeniedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class AdviceController {

    @ExceptionHandler(LaterBirthdateException.class)
    public ResponseEntity<ErrorResponse> onLaterBirthdateException(LaterBirthdateException e,
                                                                   WebRequest request) {
        String message = e.getMessage();
        log.error(message);
        return new ResponseEntity<>(new ErrorResponse(
                e.getTimestamp(),
                "minor_user",
                message,
                request.getDescription(false)),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ScoringDeniedException.class)
    public ResponseEntity<ErrorResponse> onDeniedException(ScoringDeniedException e, WebRequest request) {
        return new ResponseEntity<>(new ErrorResponse(
                e.getTimestamp(),
                "cc_denied",
                e.getMessage(),
                request.getDescription(false)),
                HttpStatus.BAD_REQUEST);
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