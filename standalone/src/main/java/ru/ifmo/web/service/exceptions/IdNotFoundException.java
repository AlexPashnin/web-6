package ru.ifmo.web.service.exceptions;

import lombok.Getter;

public class IdNotFoundException extends RuntimeException {
    @Getter
    private final String reason;

    public IdNotFoundException(String message) {
        this.reason = message;
    }
}
