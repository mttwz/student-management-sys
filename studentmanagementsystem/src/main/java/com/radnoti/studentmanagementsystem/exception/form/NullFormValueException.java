package com.radnoti.studentmanagementsystem.exception.form;

public class NullFormValueException extends RuntimeException{
    public NullFormValueException() {
        super("One of the fields is null");
    }
}
