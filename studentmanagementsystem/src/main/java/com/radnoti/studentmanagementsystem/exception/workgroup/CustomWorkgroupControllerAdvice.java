package com.radnoti.studentmanagementsystem.exception.workgroup;

import com.radnoti.studentmanagementsystem.exception.ErrorResponse;
import com.radnoti.studentmanagementsystem.exception.user.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.PrintWriter;
import java.io.StringWriter;

@ControllerAdvice
public class CustomWorkgroupControllerAdvice {

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

    @ExceptionHandler(UserNotAddedToWorkgroupException.class)
    public ResponseEntity<ErrorResponse> handleException(UserNotAddedToWorkgroupException e) {
        setErrBody(e);
        return new ResponseEntity<>(new ErrorResponse(status, e.getMessage(), stackTrace, "workgroup01"), status);
    }

    @ExceptionHandler(WorkgroupNotCreatedException.class)
    public ResponseEntity<ErrorResponse> handleException(WorkgroupNotCreatedException e) {
        setErrBody(e);
        return new ResponseEntity<>(new ErrorResponse(status, e.getMessage(), stackTrace, "workgroup02"), status);
    }

    @ExceptionHandler(WorkgroupNotExistException.class)
    public ResponseEntity<ErrorResponse> handleException(WorkgroupNotExistException e) {
        setErrBody(e);
        return new ResponseEntity<>(new ErrorResponse(status, e.getMessage(), stackTrace, "workgroup03"), status);
    }

}
