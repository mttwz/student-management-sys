package com.radnoti.studentmanagementsystem.exception.workgroupSchedule;

import com.radnoti.studentmanagementsystem.exception.ErrorResponse;
import com.radnoti.studentmanagementsystem.exception.workgroup.UserNotAddedToWorkgroupException;
import com.radnoti.studentmanagementsystem.exception.workgroup.WorkgroupNotCreatedException;
import com.radnoti.studentmanagementsystem.exception.workgroup.WorkgroupNotExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.PrintWriter;
import java.io.StringWriter;

@ControllerAdvice
public class CustomWorkgroupScheduleControllerAdvice {

    private HttpStatus status;
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

    @ExceptionHandler(WorkgroupScheduleNotCreatedException.class)
    public ResponseEntity<ErrorResponse> handleException(WorkgroupScheduleNotCreatedException e) {
        setErrBody(e);
        return new ResponseEntity<>(new ErrorResponse(status, e.getMessage(), stackTrace, "workgroupSchedule01"), status);
    }

    @ExceptionHandler(WorkgroupScheduleNotDeletedException.class)
    public ResponseEntity<ErrorResponse> handleException(WorkgroupScheduleNotDeletedException e) {
        setErrBody(e);
        return new ResponseEntity<>(new ErrorResponse(status, e.getMessage(), stackTrace, "workgroupSchedule02"), status);
    }

    @ExceptionHandler(WorkgroupScheduleNotExistException.class)
    public ResponseEntity<ErrorResponse> handleException(WorkgroupScheduleNotExistException e) {
        setErrBody(e);
        return new ResponseEntity<>(new ErrorResponse(status, e.getMessage(), stackTrace, "workgroupSchedule03"), status);
    }

}
