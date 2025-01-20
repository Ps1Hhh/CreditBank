package creditbank.calculator.controller;

import creditbank.calculator.exception.ErrorResponse;
import creditbank.calculator.exception.ScoringDeniedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class AdviceController {

    @ExceptionHandler(ScoringDeniedException.class)
    public ResponseEntity<ErrorResponse> onDeniedException(ScoringDeniedException e, WebRequest request) {
        return new ResponseEntity<>(new ErrorResponse(
                e.getTimestamp(),
                "cc_denied",
                e.getMessage(),
                request.getDescription(false)),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> onNativeDefaultException(Exception e, WebRequest request) {
        log.error(e.getMessage());
        return new ResponseEntity<>(new ErrorResponse(
                LocalDateTime.now(),
                "default",
                e.getMessage(),
                request.getDescription(false)),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}