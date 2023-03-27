package com.radnoti.studentmanagementsystem.exception.form;

public class FormValueNullException extends RuntimeException{
    public FormValueNullException() {
        super("One or more fields are null");
    }
}
