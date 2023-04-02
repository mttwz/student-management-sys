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
        return new ResponseEntity<>(new ErrorResponse(status, e.getMessage(), stackTrace, "CARD_001_ERR"), status);
    }

    @ExceptionHandler(CardNotExistException.class)
    public ResponseEntity<ErrorResponse> handleException(CardNotExistException e) {
        setErrBody(e);
        return new ResponseEntity<>(new ErrorResponse(status, e.getMessage(), stackTrace, "CARD_002_ERR"), status);
    }

    @ExceptionHandler(CardNotAssignedException.class)
    public ResponseEntity<ErrorResponse> handleException(CardNotAssignedException e) {
        setErrBody(e);
        return new ResponseEntity<>(new ErrorResponse(status, e.getMessage(), stackTrace, "CARD_003_ERR"), status);
    }

    @ExceptionHandler(CardAlreadyDeletedException.class)
    public ResponseEntity<ErrorResponse> handleException(CardAlreadyDeletedException e) {
        setErrBody(e);
        return new ResponseEntity<>(new ErrorResponse(status, e.getMessage(), stackTrace, "CARD_004_ERR"), status);
    }

    @ExceptionHandler(CardAlreadyAssignedException.class)
    public ResponseEntity<ErrorResponse> handleException(CardAlreadyAssignedException e) {
        setErrBody(e);
        return new ResponseEntity<>(new ErrorResponse(status, e.getMessage(), stackTrace, "CARD_005_ERR"), status);
    }

    @ExceptionHandler(CardNotDeletedException.class)
    public ResponseEntity<ErrorResponse> handleException(CardNotDeletedException e) {
        setErrBody(e);
        return new ResponseEntity<>(new ErrorResponse(status, e.getMessage(), stackTrace, "CARD_006_ERR"), status);
    }

    @ExceptionHandler(AnotherCardAlreadyAssignedException.class)
    public ResponseEntity<ErrorResponse> handleException(AnotherCardAlreadyAssignedException e) {
        setErrBody(e);
        return new ResponseEntity<>(new ErrorResponse(status, e.getMessage(), stackTrace, "CARD_006_ERR"), status);
    }

    @ExceptionHandler(CardMismatchException.class)
    public ResponseEntity<ErrorResponse> handleException(CardMismatchException e) {
        setErrBody(e);
        return new ResponseEntity<>(new ErrorResponse(status, e.getMessage(), stackTrace, "CARD_007_ERR"), status);
    }
}
