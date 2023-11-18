package com.apptive.marico.exception;

import com.apptive.marico.utils.ApiUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiUtils.ApiFail> handleCustomException(CustomException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        HttpStatus status = HttpStatus.resolve(errorCode.getStatus());
        if (status == null) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return createFailResponse(status, errorCode.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiUtils.ApiFail> handleGeneralException(Exception ex) {
        return createFailResponse(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SERVER_ERROR.getMessage());
    }

    private static ResponseEntity<ApiUtils.ApiFail> createFailResponse(HttpStatus httpStatus, String message) {
        ApiUtils.ApiFail apiFail = ApiUtils.fail(httpStatus.value(), message);
        return new ResponseEntity<>(apiFail, httpStatus);
    }
}
