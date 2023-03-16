package com.radnoti.studentmanagementsystem.exception.user;

public class UserDeletedException extends RuntimeException{
    public UserDeletedException() {
        super("User is deleted");
    }
}
