package com.radnoti.studentmanagementsystem.exception.form;

public class PasswordTooShortException extends RuntimeException{
    public PasswordTooShortException() {
        super("Password is too short");
    }
}
