package com.radnoti.studentmanagementsystem.exception.user;

public class UserAlreadyExistException extends RuntimeException{
    public UserAlreadyExistException() {
        super("Username already taken");
    }
}
