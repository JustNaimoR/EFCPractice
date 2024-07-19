package edu.mod5.crud.romario.crudrestfulapi.exceptions;

public class TodoTaskNotFoundException extends RuntimeException {
    public static final String DEFAULT_MESSAGE = "todo task wasn't found";

    public TodoTaskNotFoundException() {
        super(DEFAULT_MESSAGE);
    }
}