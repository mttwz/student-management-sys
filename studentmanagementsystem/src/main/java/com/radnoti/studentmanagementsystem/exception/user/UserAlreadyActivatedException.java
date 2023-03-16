package com.radnoti.studentmanagementsystem.exception.user;

public class UserAlreadyActivatedException extends RuntimeException{
    public UserAlreadyActivatedException() {
        super("User is already activated");
    }
}
