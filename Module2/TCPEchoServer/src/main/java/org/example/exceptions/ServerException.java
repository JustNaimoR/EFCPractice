package org.example.exceptions;

public class ServerException extends RuntimeException {
    public final static String DEFAULT_MESSAGE = "Got an exception during work with server";

    public ServerException(Throwable throwable) {
        super(DEFAULT_MESSAGE, throwable);
    }
}