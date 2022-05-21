package com.enesoral.bookretail.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class InsufficientStockException extends BookRetailException {

    public InsufficientStockException(String bookId, Long available, Long desired) {
        super(HttpStatus.OK, String.format("Insufficient stock for book id: %s, available: %s, desired: %s",
                bookId, available, desired));
    }
}
