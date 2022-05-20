package com.enesoral.bookretail.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class EmailAlreadyTakenException extends BookRetailException {

    public EmailAlreadyTakenException(String email) {
        super(HttpStatus.OK, String.format("Email already taken: %s", email));
    }
}
