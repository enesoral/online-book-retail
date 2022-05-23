package com.enesoral.bookretail.common.exception.handler;

import com.enesoral.bookretail.common.exception.EmailAlreadyTakenException;
import com.enesoral.bookretail.common.exception.InsufficientStockException;
import com.enesoral.bookretail.common.exception.NotFoundException;
import com.enesoral.bookretail.common.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        log.info(ex.toString());
        String message = String.format("Missing or bad arguments passed! %s", ex.getAllErrors());
        return handleExceptionInternal(ex, new Response<String>(true, message),
                new HttpHeaders(), HttpStatus.OK, request);
    }

    @ExceptionHandler(value = NotFoundException.class)
    protected ResponseEntity<Object> handleNotFoundException(NotFoundException ex, WebRequest request) {
        log.info(ex.toString());
        String message = "Resource not found!";
        return handleExceptionInternal(ex, new Response<String>(true, message),
                new HttpHeaders(), HttpStatus.OK, request);
    }

    @ExceptionHandler(value = {EmailAlreadyTakenException.class, DataIntegrityViolationException.class})
    protected ResponseEntity<Object> handleDataIntegrityExceptions(RuntimeException ex, WebRequest request) {
        log.info(ex.toString());
        String message = "Given field(s) is already taken!";
        return handleExceptionInternal(ex, new Response<String>(true, message),
                new HttpHeaders(), HttpStatus.OK, request);
    }

    @ExceptionHandler(value = InsufficientStockException.class)
    protected ResponseEntity<Object> handleInsufficientException(InsufficientStockException ex, WebRequest request) {
        log.info(ex.toString());
        String message = "Insufficient stock!";
        return handleExceptionInternal(ex, new Response<String>(true, message),
                new HttpHeaders(), HttpStatus.OK, request);
    }
}
