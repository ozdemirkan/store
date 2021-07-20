package com.ozdemirkan.store.product.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorType {
    UNPROCCESSABLE_ENTITY(null, HttpStatus.UNPROCESSABLE_ENTITY),
    NOT_FOUND(null, HttpStatus.NOT_FOUND);
    private String code;
    private HttpStatus httpStatus;
    ErrorType(String code, HttpStatus httpStatus){
        this.code = code;
        this.httpStatus = httpStatus;
    }
}
