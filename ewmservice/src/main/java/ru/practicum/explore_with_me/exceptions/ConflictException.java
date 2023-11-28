package ru.practicum.explore_with_me.exceptions;

public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
    public ConflictException(String message, Throwable cause) {
        super(message, cause);
    }
}
