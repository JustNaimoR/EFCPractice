package edu.mod6.linkabbreviationsservice.exceptions;

public class LinksPairNotFoundException extends RuntimeException {
    public static final String DEFAULT_MESSAGE = "Pair of links wasn't found!";

    public LinksPairNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public LinksPairNotFoundException(String message) {
        super(message);
    }
}