package com.radnoti.studentmanagementsystem.exception.student;

public class StudentNotSavedException extends RuntimeException{
    public StudentNotSavedException() {
        super("Student does not saved");
    }
}
