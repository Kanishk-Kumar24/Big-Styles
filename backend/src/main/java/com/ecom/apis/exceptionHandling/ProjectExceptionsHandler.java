package com.ecom.apis.exceptionHandling;

import com.ecom.apis.exceptionHandling.exceptions.NotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class ProjectExceptionsHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public Map<String,String> handleInvalidArgument(MethodArgumentNotValidException ex){
        Map<String,String> errorMap=new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(fieldError -> {errorMap.put(fieldError.getField(),fieldError.getDefaultMessage());});
        return errorMap;
    }
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(NotFoundException.class)
    public Map<String,String> handleUserNotFound(NotFoundException ex){
        Map<String,String> errorMap=new HashMap<>();
        errorMap.put("error", ex.getMessage());
        return errorMap;
    }
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(NoSuchElementException.class)
    public Map<String,String> handleNoData(NoSuchElementException ex){
        Map<String,String> errorMap=new HashMap<>();
        errorMap.put("error", ex.getMessage());
        return errorMap;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ConstraintViolationException.class})
    public Map<String,String> handleInvalidArgument(ConstraintViolationException ex){
        Map<String,String> errorMap=new HashMap<>();
        ex.getConstraintViolations().forEach(constraintViolation -> {errorMap.put(constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage());});
        return errorMap;
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler({SQLIntegrityConstraintViolationException.class})
    public Map<String,String> duplicateValueEntry(SQLIntegrityConstraintViolationException ex){
        Map<String,String> errorMap=new HashMap<>();
        ex.spliterator().forEachRemaining(throwable -> {errorMap.put("error",throwable.getMessage());});
        return errorMap;
    }

}
