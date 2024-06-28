package org.example.exceptions;

public class ClientProcessingException extends RuntimeException {
    public static final String DEFAULT_MESSAGE = "Exception during processing of a client";

    public ClientProcessingException(Throwable throwable) {
        super(DEFAULT_MESSAGE, throwable);
    }
}