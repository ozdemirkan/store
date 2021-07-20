package com.ozdemirkan.store.order.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorType {
    UNPROCCESSABLE_ENTITY(null, HttpStatus.UNPROCESSABLE_ENTITY),
    NOT_FOUND(null, HttpStatus.NOT_FOUND),
    NON_EXISTING_PRODUCT("Request contains an invalid product id.", HttpStatus.OK),
    UNSUPPORTED_CURRENCY("Request contains an product price at a currency other than Euro.", HttpStatus.OK),
    UNEXPECTED_ERROR("Unexpected error occured, please try again later.", HttpStatus.OK);
    private String code;
    private HttpStatus httpStatus;
    ErrorType(String code, HttpStatus httpStatus){
        this.code = code;
        this.httpStatus = httpStatus;
    }
}
