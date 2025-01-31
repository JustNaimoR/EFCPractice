package edu.mod6.linkabbreviationsservice.exceptions.handlers;

import edu.mod6.linkabbreviationsservice.exceptions.LinksPairNotFoundException;
import edu.mod6.linkabbreviationsservice.exceptions.TempLinkExpiredException;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.List;

@ControllerAdvice
public class MainExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exc) {
        List<String> errors = exc.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();

        return new ResponseEntity<>(
                new ErrorResponse(
                        errors, Instant.now()
                ),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(LinksPairNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleLinksPairNotFoundException(LinksPairNotFoundException exc) {
        return new ResponseEntity<>(
                new ErrorResponse(
                        exc.getMessage(), Instant.now()
                ),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(TempLinkExpiredException.class)
    public ResponseEntity<ErrorResponse> handleTempLinkExpiredException(TempLinkExpiredException exc) {
        return new ResponseEntity<>(
                new ErrorResponse(
                        exc.getMessage(), Instant.now()
                ),
                HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(ValidationException exc) {
        return new ResponseEntity<>(
                new ErrorResponse(
                        exc.getMessage(), Instant.now()
                ),
                HttpStatus.BAD_REQUEST
        );
    }
}