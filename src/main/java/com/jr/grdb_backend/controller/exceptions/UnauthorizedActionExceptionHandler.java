package com.jr.grdb_backend.controller.exceptions;


import com.jr.grdb_backend.model.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
public class UnauthorizedActionExceptionHandler {

    @ExceptionHandler(UnauthorizedActionException.class)
    public ResponseEntity handleException(UnauthorizedActionException exception){
        ApiError apiError = new ApiError(exception.getErrorMessage(), HttpStatus.UNAUTHORIZED.value() );

        return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiError);
    }
}
