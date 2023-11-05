package com.apptive.marico.controller;

import com.apptive.marico.dto.error.ErrorDto;
import com.apptive.marico.dto.error.UnknownErrorDto;
import com.apptive.marico.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.apptive.marico.exception.ErrorCode.INTERNAL_SERVER_ERROR;

@RestControllerAdvice
public class ExceptionHandlers {
    @ExceptionHandler({ CustomException.class })
    protected ResponseEntity handleCustomException(CustomException exception) {
        return new ResponseEntity(new ErrorDto(exception.getErrorCode().getStatus(), exception.getErrorCode().getMessage()), HttpStatus.valueOf(exception.getErrorCode().getStatus()));
    }

    @ExceptionHandler({ Exception.class })
    protected ResponseEntity handleServerException(Exception ex) {
        System.out.println(ex.getMessage());
        ex.printStackTrace();
        return new ResponseEntity(new UnknownErrorDto(INTERNAL_SERVER_ERROR.getStatus(), INTERNAL_SERVER_ERROR.getMessage(), ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
