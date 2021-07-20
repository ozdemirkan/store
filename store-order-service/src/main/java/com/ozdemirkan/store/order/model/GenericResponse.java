package com.ozdemirkan.store.order.model;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GenericResponse<T> {
    private T data;
    private String errorCode;
    private String errorDescription;
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
