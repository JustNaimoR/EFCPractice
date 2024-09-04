package demo.exceptions;

public class WriteIOException extends RuntimeException {
    public static final String DEFAULT_MESSAGE = "Exception when writing to a file";

    public WriteIOException() {
        super(DEFAULT_MESSAGE);
    }

    public WriteIOException(String message) {
        super(message);
    }
}