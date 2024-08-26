package edu.mod7.authenticationservice.exceptions;

public class VerifiedEmailNotFoundException extends RuntimeException {
    public static final String DEFAULT_MESSAGE = "Verified email wasn't found";

    public VerifiedEmailNotFoundException() {
        super(DEFAULT_MESSAGE);
    }
}