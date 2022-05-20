package com.enesoral.bookretail.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class BookNotFoundException extends BookRetailException {

    public BookNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
