package com.ozdemirkan.store.product.model;

import lombok.*;

@Getter
public class GenericResponse<T> {
    private final T data;
    private final String errorCode;
    private final String errorDescription;
    public GenericResponse(T data){
        this.data = data;
        this.errorCode=null;
        this.errorDescription=null;
    }
    public GenericResponse(String errorCode, String errorDescription){
        this.data = null;
        this.errorCode=errorCode;
        this.errorDescription=errorDescription;
    }

}
