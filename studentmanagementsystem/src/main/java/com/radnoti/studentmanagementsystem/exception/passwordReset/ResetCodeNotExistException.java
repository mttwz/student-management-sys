package com.radnoti.studentmanagementsystem.exception.passwordReset;

public class ResetCodeNotExistException extends RuntimeException{
    public ResetCodeNotExistException() {
        super("User does not has any valid reset code.");
    }
}
