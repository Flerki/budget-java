package com.amairovi.exception;

import lombok.Getter;

@Getter
public class ServiceException extends RuntimeException {
    private final Error error;

    public ServiceException(final Error error, final String message,
                            final Throwable e) {
        super(message, e);
        this.error = error;
    }
}
