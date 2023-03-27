package com.radnoti.studentmanagementsystem.exception.form;

import com.radnoti.studentmanagementsystem.exception.ErrorResponse;
import com.radnoti.studentmanagementsystem.exception.card.CardNotCreatedException;
import com.radnoti.studentmanagementsystem.exception.card.CardNotExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.PrintWriter;
import java.io.StringWriter;

@ControllerAdvice
public class CustomFormAdvice {

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

    @ExceptionHandler(EmptyFormValueException.class)
    public ResponseEntity<ErrorResponse> handleException(EmptyFormValueException e) {
        setErrBody(e);
        return new ResponseEntity<>(new ErrorResponse(status, e.getMessage(), stackTrace, "FORM_001_ERR"), status);
    }

    @ExceptionHandler(InvalidIdException.class)
    public ResponseEntity<ErrorResponse> handleException(InvalidIdException e) {
        setErrBody(e);
        return new ResponseEntity<>(new ErrorResponse(status, e.getMessage(), stackTrace, "FORM_002_ERR"), status);
    }

    @ExceptionHandler(NullFormValueException.class)
    public ResponseEntity<ErrorResponse> handleException(NullFormValueException e) {
        setErrBody(e);
        return new ResponseEntity<>(new ErrorResponse(status, e.getMessage(), stackTrace, "FORM_003_ERR"), status);
    }

    @ExceptionHandler(InvalidFormValueException.class)
    public ResponseEntity<ErrorResponse> handleException(InvalidFormValueException e) {
        setErrBody(e);
        return new ResponseEntity<>(new ErrorResponse(status, e.getMessage(), stackTrace, "FORM_004_ERR"), status);
    }
}
