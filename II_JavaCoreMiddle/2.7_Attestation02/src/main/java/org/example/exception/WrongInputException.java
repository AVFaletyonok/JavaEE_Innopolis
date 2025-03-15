package org.example.exception;

public class WrongInputException extends RuntimeException {

    public WrongInputException(String message) {
        super("Wrong input: " + message);
    }

    public WrongInputException(String message, Throwable cause) {
        super("Wrong input: " + message, cause);
    }
}
