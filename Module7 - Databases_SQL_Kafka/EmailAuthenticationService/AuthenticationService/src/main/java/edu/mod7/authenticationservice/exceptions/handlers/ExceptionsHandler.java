package edu.mod7.authenticationservice.exceptions.handlers;

import edu.mod7.authenticationservice.exceptions.EmailAuthenticationException;
import edu.mod7.authenticationservice.exceptions.VerifiedCodeIsInvalidException;
import edu.mod7.authenticationservice.exceptions.VerifiedEmailNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(EmailAuthenticationException.class)
    public ResponseEntity<ErrorResponse> emailAuthenticationExceptionHandler(EmailAuthenticationException exc) {
        return new ResponseEntity<>(
                new ErrorResponse(exc.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(VerifiedCodeIsInvalidException.class)
    public ResponseEntity<ErrorResponse> verifiedCodeIsInvalidExceptionHandler(VerifiedCodeIsInvalidException exc) {
        return new ResponseEntity<>(
                new ErrorResponse(exc.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(VerifiedEmailNotFoundException.class)
    public ResponseEntity<ErrorResponse> verifiedEmailNotFoundException(VerifiedEmailNotFoundException exc) {
        return new ResponseEntity<>(
                new ErrorResponse(exc.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }
}