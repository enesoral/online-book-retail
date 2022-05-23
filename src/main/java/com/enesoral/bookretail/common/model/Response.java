package com.enesoral.bookretail.common.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Response<T> {

    private boolean error;

    private String errorMessage;

    private T data;

    public Response(boolean error, String errorMessage) {
        this.error = error;
        this.errorMessage = errorMessage;
    }

    public Response(T data) {
        this.data = data;
    }
}
