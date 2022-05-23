package com.enesoral.bookretail.common.exception;

public class RefreshTokenNotFoundException extends NotFoundException {

    public RefreshTokenNotFoundException(String token) {
        super(String.format("Refresh token not found: %s", token));;
    }
}
