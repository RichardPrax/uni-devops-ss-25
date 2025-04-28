package de.koerperschmiede.backend.exceptions;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> handleNoSuchElementException(NoSuchElementException e) {
        return handleException(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(value = AccessException.class)
    public ResponseEntity<ErrorResponse> handleAccessException(AccessException e) {
        return handleException(HttpStatus.FORBIDDEN, e.getMessage());
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException e) {
        return handleException(HttpStatus.FORBIDDEN, e.getMessage());
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        return handleException(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(value = AlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyExistsException(AlreadyExistsException e) {
        return handleException(HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<String> errorMessages = e.getBindingResult().getAllErrors().stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .collect(Collectors.toList());
        String allErrors = String.join(", ", errorMessages);
        return handleException(HttpStatus.BAD_REQUEST, allErrors);
    }

    private ResponseEntity<ErrorResponse> handleException(HttpStatus status, String message) {
        return ResponseEntity
            .status(status)
            .body(new ErrorResponse(message));
    }
}
