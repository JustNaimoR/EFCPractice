package edu.mod7.authenticationservice.exceptions;

public class ClientNotFoundException extends RuntimeException {
    public static final String DEFAULT_MESSAGE = "Client wasn't found";

    public ClientNotFoundException() {
        super(DEFAULT_MESSAGE);
    }
}