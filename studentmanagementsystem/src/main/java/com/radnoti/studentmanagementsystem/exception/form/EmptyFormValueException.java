package com.radnoti.studentmanagementsystem.exception.form;

public class EmptyFormValueException extends RuntimeException{
    public EmptyFormValueException() {
        super("One of the fields is empty");
    }
}
