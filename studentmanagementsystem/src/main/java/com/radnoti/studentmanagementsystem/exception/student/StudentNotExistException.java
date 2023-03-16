package com.radnoti.studentmanagementsystem.exception.student;

public class StudentNotExistException extends RuntimeException{
    public StudentNotExistException() {
        super("Student does not exist");
    }
}
