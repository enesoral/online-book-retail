package com.enesoral.bookretail.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RefreshTokenNotFoundException extends BookRetailException {

    public RefreshTokenNotFoundException(String token) {
        super(HttpStatus.NOT_FOUND, String.format("Refresh token not found: %s", token));;
    }
}
