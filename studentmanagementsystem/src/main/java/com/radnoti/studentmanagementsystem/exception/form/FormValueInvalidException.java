package com.radnoti.studentmanagementsystem.exception.form;

public class FormValueInvalidException extends RuntimeException{
    public FormValueInvalidException() {
        super("One or more values are invalid");
    }
}
