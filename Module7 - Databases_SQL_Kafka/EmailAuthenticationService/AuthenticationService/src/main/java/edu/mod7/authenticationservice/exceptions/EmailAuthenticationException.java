package edu.mod7.authenticationservice.exceptions;

public class EmailAuthenticationException extends RuntimeException {

    public EmailAuthenticationException(String message, Throwable throwable) {
        super(message, throwable);
    }

}