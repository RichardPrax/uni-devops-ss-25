package de.koerperschmiede.backend.exceptions;

public class AccessException extends RuntimeException {
    public AccessException(String message) {
        super(message);
    }
}
