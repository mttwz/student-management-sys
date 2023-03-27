package com.radnoti.studentmanagementsystem.exception.passwordReset;

public class ResetCodeExpiredException extends RuntimeException{
    public ResetCodeExpiredException() {
        super("User's reset code has been expired.");
    }
}
