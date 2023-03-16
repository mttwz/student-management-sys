package com.radnoti.studentmanagementsystem.exception.user;

public class InvalidCredentialsException extends RuntimeException{
    public InvalidCredentialsException() {
        super("Invalid username or password");
    }
}
