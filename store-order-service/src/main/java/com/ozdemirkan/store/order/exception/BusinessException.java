package com.ozdemirkan.store.order.exception;

import lombok.Data;

@Data
public class BusinessException extends Exception{
    private final ErrorType errorType;
}
