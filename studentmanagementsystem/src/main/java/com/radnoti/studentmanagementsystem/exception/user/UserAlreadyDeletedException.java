package com.radnoti.studentmanagementsystem.exception.user;

public class UserAlreadyDeletedException extends RuntimeException{
    public UserAlreadyDeletedException() {
        super("User is already deleted");
    }
}
