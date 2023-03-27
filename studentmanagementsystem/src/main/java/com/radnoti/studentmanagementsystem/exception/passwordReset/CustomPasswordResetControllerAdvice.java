package com.radnoti.studentmanagementsystem.exception.passwordReset;

import com.radnoti.studentmanagementsystem.exception.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.PrintWriter;
import java.io.StringWriter;

@ControllerAdvice
public class CustomPasswordResetControllerAdvice {

    HttpStatus status;
    private StringWriter stringWriter;

    private PrintWriter printWriter;

    private String stackTrace;

    public void setErrBody(Exception e) {
        status = HttpStatus.CONFLICT;
        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        stackTrace = stringWriter.toString();
    }

    @ExceptionHandler(ResetCodeNotExistException.class)
    public ResponseEntity<ErrorResponse> handleException(ResetCodeNotExistException e) {
        setErrBody(e);
        return new ResponseEntity<>(new ErrorResponse(status, e.getMessage(), stackTrace, "PWR_001_ERR"), status);
    }

    @ExceptionHandler(ResetCodeExpiredException.class)
    public ResponseEntity<ErrorResponse> handleException(ResetCodeExpiredException e) {
        setErrBody(e);
        return new ResponseEntity<>(new ErrorResponse(status, e.getMessage(), stackTrace, "PWR_002_ERR"), status);
    }



}
