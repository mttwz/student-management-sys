package com.radnoti.studentmanagementsystem.exception.form;

public class NullFormValueException extends RuntimeException{
    public NullFormValueException() {
        super("One or more fields are null");
    }
}
