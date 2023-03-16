package com.radnoti.studentmanagementsystem.exception.form;

public class InvalidIdException extends RuntimeException{
    public InvalidIdException() {
        super("The ID is invalid");
    }
}
