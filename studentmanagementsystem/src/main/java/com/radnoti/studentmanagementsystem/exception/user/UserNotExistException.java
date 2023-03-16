package com.radnoti.studentmanagementsystem.exception.user;

public class UserNotExistException extends RuntimeException{
    public UserNotExistException() {
        super("User does not exist");
    }
}
