package org.example;

public class RingBufferIsEmptyException extends RuntimeException {
    public static final String DEFAULT_MESSAGE = "Buffer is empty";

    public RingBufferIsEmptyException() {
        super(DEFAULT_MESSAGE);
    }

    public RingBufferIsEmptyException(Throwable throwable) {
        super(DEFAULT_MESSAGE, throwable);
    }
}