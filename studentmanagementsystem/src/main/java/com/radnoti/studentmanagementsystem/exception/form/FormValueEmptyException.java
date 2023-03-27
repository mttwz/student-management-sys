package com.radnoti.studentmanagementsystem.exception.form;

public class FormValueEmptyException extends RuntimeException{
    public FormValueEmptyException() {
        super("One of the fields is empty");
    }
}
