package edu.mod7.confirmationcodesdeliveryservice.exceptions;

public class ConversionToDtoException extends RuntimeException {
    public static final String DEFAULT_MESSAGE = "Error converting to dto";

    public ConversionToDtoException() {
        super(DEFAULT_MESSAGE);
    }

    public ConversionToDtoException(Throwable throwable) {
        super(DEFAULT_MESSAGE, throwable);
    }
}