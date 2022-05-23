package com.enesoral.bookretail.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public abstract class NotFoundException extends BookRetailException {

    public NotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
