package com.radnoti.studentmanagementsystem.exception.passwordReset;

public class ResetCodeInvalidException extends RuntimeException{
    public ResetCodeInvalidException() {
        super("Invalid reset code.");
    }
}
