package com.radnoti.studentmanagementsystem.exception.attendance;

import com.radnoti.studentmanagementsystem.exception.ErrorResponse;
import com.radnoti.studentmanagementsystem.exception.form.FormValueInvalidException;
import com.radnoti.studentmanagementsystem.exception.form.FormValueNullException;
import com.radnoti.studentmanagementsystem.exception.form.InvalidIdException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.PrintWriter;
import java.io.StringWriter;

@ControllerAdvice
public class CustomAttendanceAdvice {

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

    @ExceptionHandler(AttendanceNotExistException.class)
    public ResponseEntity<ErrorResponse> handleException(AttendanceNotExistException e) {
        setErrBody(e);
        return new ResponseEntity<>(new ErrorResponse(status, e.getMessage(), stackTrace, "ATT_001_ERR"), status);
    }


}
