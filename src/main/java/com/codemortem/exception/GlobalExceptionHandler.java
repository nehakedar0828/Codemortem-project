package com.codemortem.exception;

import ch.qos.logback.core.model.processor.ModelHandlerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //handle custom exception
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFound(
            ResourceNotFoundException ex){

        return new ResponseEntity<>(
                ex.getMessage(),
                HttpStatus.NOT_FOUND);
    }

    //handle validation exception
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> handleValidationErrors(
            MethodArgumentNotValidException ex
    ){

        Map<String,String> errors = new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        ));

        return new ResponseEntity<>(
                errors,
                HttpStatus.BAD_REQUEST);
    }

}
