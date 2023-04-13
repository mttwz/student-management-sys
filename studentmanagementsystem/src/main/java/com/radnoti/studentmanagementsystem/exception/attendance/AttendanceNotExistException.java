package com.radnoti.studentmanagementsystem.exception.attendance;

public class AttendanceNotExistException extends RuntimeException{
    public AttendanceNotExistException() {
        super("Attendance does not exist");
    }
}
