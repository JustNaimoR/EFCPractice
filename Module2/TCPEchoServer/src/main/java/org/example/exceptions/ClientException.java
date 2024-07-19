package org.example.exceptions;

public class ClientException extends RuntimeException {
    public static final String DEFAULT_MESSAGE = "Got exception during work of the client";

    public ClientException(Throwable throwable) {
        super(DEFAULT_MESSAGE, throwable);
    }
}