package com.radnoti.studentmanagementsystem.exception.card;

import com.radnoti.studentmanagementsystem.exception.ErrorResponse;
import com.radnoti.studentmanagementsystem.exception.user.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.PrintWriter;
import java.io.StringWriter;

@ControllerAdvice
public class CustomCardControllerAdvice {

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

    @ExceptionHandler(CardNotCreatedException.class)
    public ResponseEntity<ErrorResponse> handleException(CardNotCreatedException e) {
        setErrBody(e);
        return new ResponseEntity<>(new ErrorResponse(status, e.getMessage(), stackTrace, "card01"), status);
    }

    @ExceptionHandler(CardNotExistException.class)
    public ResponseEntity<ErrorResponse> handleException(CardNotExistException e) {
        setErrBody(e);
        return new ResponseEntity<>(new ErrorResponse(status, e.getMessage(), stackTrace, "card02"), status);
    }
}
