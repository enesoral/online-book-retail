package com.enesoral.bookretail.common.exception;

public class UserNotFoundException extends NotFoundException {

    public UserNotFoundException(String userId) {
        super(String.format("User not found with id: %s", userId));
    }
}
