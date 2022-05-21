package com.enesoral.bookretail.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class OrderNotFoundException extends BookRetailException {

    public OrderNotFoundException(String id) {
        super(HttpStatus.NOT_FOUND,
                String.format("Order not found with id: %s", id));
    }
}
