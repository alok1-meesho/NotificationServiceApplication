package com.example.notificationservice.services;


import com.example.notificationservice.entity.Exceptions.InvalidEntryException;
import com.example.notificationservice.entity.dto.ErrorDetail;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ErrorDetail errorDetail=new ErrorDetail(new ErrorDetail.ErrorInfo("Invalid_Request", ex.getMessage()));
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errorDetail);
    }

    @ExceptionHandler(InvalidEntryException.class)
    public ResponseEntity<Object> HandleInvalidException(InvalidEntryException invalidEntryException){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(invalidEntryException.getError());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> HandleRuntimeException(RuntimeException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("RuntimeException "+ ex.getMessage());
    }
}
