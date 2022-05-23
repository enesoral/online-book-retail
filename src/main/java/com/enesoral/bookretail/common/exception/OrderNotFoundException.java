package com.enesoral.bookretail.common.exception;

public class OrderNotFoundException extends NotFoundException {

    public OrderNotFoundException(String id) {
        super(String.format("Order not found with id: %s", id));
    }
}
