package com.ozdemirkan.store.product.exception;

import com.ozdemirkan.store.product.model.GenericResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;



@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("Exception occured.", ex);
        return new ResponseEntity<>( HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Object> handleExceptions(BusinessException exception, WebRequest webRequest) {
        log.error("Exception occured.", exception);

        GenericResponse<Void> response = new GenericResponse<>(exception.getErrorType().getCode(),exception.getErrorType().name());

        HttpStatus status = null;
        ErrorType errorType = exception.getErrorType();

        if(errorType.getHttpStatus()!=null){
            status = errorType.getHttpStatus();
        } else{
            status=HttpStatus.OK;
        }

        if(status==HttpStatus.OK){
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        return new ResponseEntity<>(status);

    }
}
