package de.koerperschmiede.backend.exceptions;

import java.time.LocalDateTime;

public record ErrorResponse(String reason, LocalDateTime timeStamp) {
    public ErrorResponse(String reason) {
        this(reason, LocalDateTime.now());
    }
}


