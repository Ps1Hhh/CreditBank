package creditbank.statement.controller;


import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import creditbank.statement.exception.DefaultException;
import creditbank.statement.exception.ErrorResponse;
import creditbank.statement.exception.LaterBirthdateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Objects;

/**
 * Глобальный контроллер для перехвата ошибок.
 */
@RestControllerAdvice
@Slf4j
public class AdviceController {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> onHttpMessageNotReadableException(HttpMessageNotReadableException e,
                                                                           WebRequest request) {
        log.error(e.getMessage());
        var cause = e.getCause();

        if (cause instanceof InvalidFormatException) {
            cause = cause.getCause();

            if (cause instanceof DateTimeParseException) {
                return new ResponseEntity<>(new ErrorResponse(
                        LocalDateTime.now(),
                        "birthdate",
                        "Некорректный ввод даты: '" + ((DateTimeParseException) cause).getParsedString()
                                + "'. Корректный пример: 2001-07-05.",
                        request.getDescription(false)),
                        HttpStatus.BAD_REQUEST);
            }
        }

        return new ResponseEntity<>(new ErrorResponse(
                LocalDateTime.now(),
                "default",
                e.getMessage(),
                request.getDescription(false)),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> onMethodArgumentNotValidException(
            MethodArgumentNotValidException e, WebRequest request) {
        log.error(e.getMessage());
        return new ResponseEntity<>(new ErrorResponse(
                LocalDateTime.now(),
                Objects.requireNonNull(e.getFieldError()).getField(),
                e.getFieldError().getDefaultMessage(),
                request.getDescription(false)),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DefaultException.class)
    public ResponseEntity<ErrorResponse> onDefaultException(DefaultException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(new ErrorResponse(
                e.getTimestamp(),
                e.getCode(),
                e.getMessage(),
                e.getDetails()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(LaterBirthdateException.class)
    public ResponseEntity<ErrorResponse> onLaterBirthdateException(LaterBirthdateException e,
            WebRequest request) {
        String message = e.getMessage();
        log.error(message);
        return new ResponseEntity<>(new ErrorResponse(
                e.getTimestamp(),
                "birthdate",
                message,
                request.getDescription(false)),
                HttpStatus.BAD_REQUEST);
    }
}
