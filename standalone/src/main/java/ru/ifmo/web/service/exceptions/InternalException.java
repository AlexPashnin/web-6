package ru.ifmo.web.service.exceptions;

import lombok.Getter;

public class InternalException extends RuntimeException {
    @Getter
    private final String reason;

    public InternalException(String reason) {
        this.reason = reason;
    }
}
