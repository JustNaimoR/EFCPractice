package edu.mod6.linkabbreviationsservice.exceptions;

public class TempLinkExpiredException extends RuntimeException {
    public static final String DEFAULT_MESSAGE = "Link is expired";

    public TempLinkExpiredException(String message) {
        super(message);
    }

    public TempLinkExpiredException() {
        super(DEFAULT_MESSAGE);
    }
}