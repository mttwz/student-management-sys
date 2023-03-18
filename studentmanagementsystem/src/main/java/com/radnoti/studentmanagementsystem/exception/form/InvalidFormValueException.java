package com.radnoti.studentmanagementsystem.exception.form;

public class InvalidFormValueException extends RuntimeException{
    public InvalidFormValueException() {
        super("One or more values are invalid");
    }
}
