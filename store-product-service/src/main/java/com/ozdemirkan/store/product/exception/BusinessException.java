package com.ozdemirkan.store.product.exception;

import lombok.Data;

@Data
public class BusinessException extends Exception{
    private final ErrorType errorType;
}
