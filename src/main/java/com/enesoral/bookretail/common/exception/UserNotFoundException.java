package com.enesoral.bookretail.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UserNotFoundException extends BookRetailException {

    public UserNotFoundException(String userId) {
        super(HttpStatus.NOT_FOUND, String.format("User not found with id: %s", userId));
    }
}
