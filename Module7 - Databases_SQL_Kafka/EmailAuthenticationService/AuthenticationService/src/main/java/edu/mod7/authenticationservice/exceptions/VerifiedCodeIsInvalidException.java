package edu.mod7.authenticationservice.exceptions;

public class VerifiedCodeIsInvalidException extends RuntimeException {
    public static final String DEFAULT_MESSAGE = "Verified code is invalid";

    public VerifiedCodeIsInvalidException() {
        super(DEFAULT_MESSAGE);
    }
}