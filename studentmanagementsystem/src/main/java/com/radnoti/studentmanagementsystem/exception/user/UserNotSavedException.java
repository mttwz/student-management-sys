package com.radnoti.studentmanagementsystem.exception.user;

public class UserNotSavedException extends RuntimeException{
    public UserNotSavedException() {
        super("User does not saved");
    }
}
