package com.enesoral.bookretail.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public abstract class BookRetailException extends ResponseStatusException {

    public BookRetailException(HttpStatus status, String message) {
        super(status, message);
    }
}
