package edu.mod3.crud.romario.crudrestfulapi.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
@ResponseBody
public class ExceptionsHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseError> handleValidationError(MethodArgumentNotValidException exc) {
        log.warn("MethodArgumentNotValidException was thrown. Most likely there were invalid values in the request");

        BindingResult result = exc.getBindingResult();
        List<FieldError> errors = result.getFieldErrors();

        List<String> messages = errors.stream()
                .map(err -> err.getField() + " - " + err.getDefaultMessage())
                .collect(Collectors.toList());

        return new ResponseEntity<>(
                new ResponseError(messages),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(TodoTaskNotFoundException.class)
    public ResponseEntity<ResponseError> handleTodoTaskNotFoundException(TodoTaskNotFoundException exc) {
        log.warn("TodoTaskNotFoundException was thrown");

        return new ResponseEntity<>(
                new ResponseError(List.of(exc.getMessage())),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ResponseError> handleDataAccessException(DataAccessException exc) {
        log.warn("DataAccessException was thrown");

        return new ResponseEntity<>(
                new ResponseError(List.of(exc.getMessage())),
                HttpStatus.BAD_REQUEST
        );
    }
}